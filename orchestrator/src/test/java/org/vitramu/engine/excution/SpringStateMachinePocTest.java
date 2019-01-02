package org.vitramu.engine.excution;

import lombok.extern.slf4j.Slf4j;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.redis.connection.jedis.JedisConnectionFactory;
import org.springframework.statemachine.StateMachine;
import org.springframework.statemachine.StateMachineContext;
import org.springframework.statemachine.StateMachinePersist;
import org.springframework.statemachine.action.Action;
import org.springframework.statemachine.config.StateMachineBuilder;
import org.springframework.statemachine.guard.Guard;
import org.springframework.statemachine.listener.StateMachineListenerAdapter;
import org.springframework.statemachine.persist.AbstractStateMachinePersister;
import org.springframework.statemachine.persist.RepositoryStateMachinePersist;
import org.springframework.statemachine.redis.RedisStateMachineContextRepository;
import org.springframework.statemachine.redis.RedisStateMachinePersister;
import org.springframework.statemachine.state.State;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.Arrays;
import java.util.HashMap;


@Slf4j
@RunWith(SpringRunner.class)
@SpringBootTest
public class SpringStateMachinePocTest {

    public enum Task {
        CREATE_START,
        SAVE_REQUEST,
        CREATE_PARALLEL,
        CREATE_BOOKING,
        CREATE_OSB,
        END_OSB,
        CREATE_PUD,
        END_PUD,
        CREATE_FINISH,
        CHOICE_EW,
        CREATE_EW,
        REFRESH_STATUS,
        END
    }

    private StateMachine<Task, String> osbsm;

    public class StateMachineEventListener<T> extends StateMachineListenerAdapter<T, String> {
        @Override
        public void stateEntered(State<T, String> state) {
            log.info("Enter State: {}", state.getIds());
        }

        @Override
        public void stateExited(State<T, String> state) {
            log.info("Exit State: {}", state.getIds());
        }

    }

    public class InMemoryStateMachinePersister<T> extends AbstractStateMachinePersister<T, String, String> {
        public InMemoryStateMachinePersister() {
            super(new InMemoryStateMachinePersist<T>());
        }
    }

    public class InMemoryStateMachinePersist<T> implements StateMachinePersist<T, String, String> {

        private final HashMap<String, StateMachineContext<T, String>> contexts = new HashMap<>();

        @Override
        public void write(StateMachineContext<T, String> context, String contextObj) throws Exception {
            contexts.put(contextObj, context);
        }

        @Override
        public StateMachineContext<T, String> read(String contextObj) throws Exception {
            return contexts.get(contextObj);
        }
    }

    @Before
    public void setUp() throws Exception {
        StateMachineBuilder.Builder<Task, String> builder = StateMachineBuilder.builder();
        builder.configureStates()
                .withStates()
                .initial(Task.CREATE_START)
                .state(Task.SAVE_REQUEST, dispatchCommandAction())
                .end(Task.END)
                .fork(Task.CREATE_PARALLEL)
                .join(Task.CREATE_FINISH)
                .choice(Task.CHOICE_EW)
                .state(Task.CREATE_BOOKING)
                .and()
                .withStates()
                .parent(Task.CREATE_BOOKING).initial(Task.CREATE_OSB, dispatchCommandAction()).end(Task.END_OSB)
                .and()
                .withStates()
                .parent(Task.CREATE_BOOKING).initial(Task.CREATE_PUD, dispatchCommandAction()).end(Task.END_PUD)
                .and()
                .withStates().state(Task.CREATE_EW, dispatchCommandAction()).state(Task.REFRESH_STATUS, dispatchCommandAction());
        builder.configureTransitions()
                .withExternal().source(Task.CREATE_START).target(Task.SAVE_REQUEST).event("FLOW_STARTED")
                .and().withExternal().source(Task.SAVE_REQUEST).target(Task.CREATE_PARALLEL).event("REQUEST_SAVED")
                .and().withFork().source(Task.CREATE_PARALLEL).target(Task.CREATE_OSB).target(Task.CREATE_PUD)
                .and().withExternal().source(Task.CREATE_OSB).target(Task.END_OSB).event("CREATE_OSB_FIN")
                .and().withExternal().source(Task.CREATE_PUD).target(Task.END_PUD).event("CREATE_PUD_FIN")
                .and().withJoin().source(Task.END_OSB).source(Task.END_PUD).target(Task.CREATE_FINISH)
                .and().withExternal().source(Task.CREATE_FINISH).target(Task.CHOICE_EW)
                .and().withChoice().source(Task.CHOICE_EW).first(Task.CREATE_EW, createEwGuard()).last(Task.REFRESH_STATUS)
                .and().withExternal().source(Task.CREATE_EW).target(Task.REFRESH_STATUS).event("CREATE_EW_FIN")
                .and().withExternal().source(Task.REFRESH_STATUS).target(Task.END);
        osbsm = builder.build();
        osbsm.addStateListener(new StateMachineEventListener<Task>());
    }

    private Action<Task, String> responseAction() {
        return context -> log.info("Respond to trigger");
    }

    private Action<Task, String> dispatchCommandAction() {
        return context -> log.info("Dispatching command");
    }

    private Guard<Task, String> createEwGuard() {
        return context -> {
            log.info("evaluate ew guard");
            return true;
        };
    }

    @Test
    public void testStartInstance() {
        osbsm.start();
        osbsm.sendEvent("FLOW_STARTED");
    }

    @Test
    public void testFork() {
        osbsm.start();
        osbsm.sendEvent("FLOW_STARTED");
        osbsm.sendEvent("REQUEST_SAVED");
    }

    @Test
    public void testJoinBlockingAfterOSB() {
        osbsm.start();
        osbsm.sendEvent("FLOW_STARTED");
        osbsm.sendEvent("REQUEST_SAVED");
        osbsm.sendEvent("CREATE_OSB_FIN");
    }

    @Test
    public void testJoinBlockingFinish() {
        osbsm.start();
        osbsm.sendEvent("FLOW_STARTED");
        osbsm.sendEvent("REQUEST_SAVED");
        osbsm.sendEvent("CREATE_OSB_FIN");
        osbsm.sendEvent("CREATE_PUD_FIN");
    }

    @Test
    public void testChoice() {
        osbsm.start();
        String[] events = new String[]{"FLOW_STARTED", "REQUEST_SAVED", "CREATE_OSB_FIN", "CREATE_PUD_FIN", "CREATE_EW_FIN"};
        Arrays.stream(events)
                .forEach(event -> osbsm.sendEvent(event));
    }


    @Test
    public void testChoiceWithSpElGuard() {
        // TODO
    }

    @Test
    public void testStateMachinePersistInMemory() throws Exception {
        InMemoryStateMachinePersister<Task> persister = new InMemoryStateMachinePersister<>();
        //        Deployment
        osbsm.start();
        // TODO use statemachine pooling technical
        persister.persist(osbsm, "PROTOTYPE");

        //        Transaction A
        osbsm.sendEvent("FLOW_STARTED");
        osbsm.sendEvent("REQUEST_SAVED");
        osbsm.sendEvent("CREATE_OSB_FIN");
        persister.persist(osbsm, "BLOCKING_AFTER_OSB");
        log.info("Before restore: {}", osbsm.getState().getIds());

        //        Transaction B
        persister.restore(osbsm, "PROTOTYPE");
        log.info("After restore: {}", osbsm.getState().getIds());
        osbsm.sendEvent("FLOW_STARTED");
        osbsm.sendEvent("REQUEST_SAVED");
        osbsm.sendEvent("CREATE_OSB_FIN");
        osbsm.sendEvent("CREATE_PUD_FIN");
        persister.persist(osbsm, "BLOCKING_FINISH");
        log.info("Before restore: {}", osbsm.getState().getIds());

        //        Transaction A
        persister.restore(osbsm, "BLOCKING_AFTER_OSB");
        log.info("After restore: {}", osbsm.getState().getIds());
        osbsm.sendEvent("CREATE_PUD_FIN");
        osbsm.sendEvent("CREATE_EW_FIN");
    }

    public RedisStateMachinePersister<Task, String> redisStateMachinePersister() {
        RedisStateMachineContextRepository<Task, String> repository =
                new RedisStateMachineContextRepository<>(new JedisConnectionFactory());
        StateMachinePersist<Task, String, String> stateMachinePersist = new RepositoryStateMachinePersist<Task, String>(repository);
        return new RedisStateMachinePersister<>(stateMachinePersist);
    }

    @Test
    public void testStateMachinePersistWithRedis() throws Exception {
        // TODO
        RedisStateMachinePersister<Task, String> persister = redisStateMachinePersister();

        osbsm.start();
        // TODO use statemachine pooling technical
        persister.persist(osbsm, "PROTOTYPE");

        //        Transaction A
        osbsm.sendEvent("FLOW_STARTED");
        osbsm.sendEvent("REQUEST_SAVED");
        osbsm.sendEvent("CREATE_OSB_FIN");
        persister.persist(osbsm, "BLOCKING_AFTER_OSB");
        log.info("Before restore: {}", osbsm.getState().getIds());

        //        Transaction B
        persister.restore(osbsm, "PROTOTYPE");
        log.info("After restore: {}", osbsm.getState().getIds());
        osbsm.sendEvent("FLOW_STARTED");
        osbsm.sendEvent("REQUEST_SAVED");
        osbsm.sendEvent("CREATE_OSB_FIN");
        osbsm.sendEvent("CREATE_PUD_FIN");
        persister.persist(osbsm, "BLOCKING_FINISH");
        log.info("Before restore: {}", osbsm.getState().getIds());

        //        Transaction A
        persister.restore(osbsm, "BLOCKING_AFTER_OSB");
        log.info("After restore: {}", osbsm.getState().getIds());
        osbsm.sendEvent("CREATE_PUD_FIN");
        osbsm.sendEvent("CREATE_EW_FIN");
    }

    @Test
    public void testStateMachineMultipleThread() {
        // TODO
    }

    @Test
    public void testStateMachinePerformance() {
        // TODO
    }
}
