package org.vitramu.engine.excution;

import lombok.extern.slf4j.Slf4j;
import org.junit.Before;
import org.junit.Ignore;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.amqp.rabbit.connection.ConnectionFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.redis.connection.jedis.JedisConnectionFactory;
import org.springframework.statemachine.StateMachine;
import org.springframework.statemachine.StateMachinePersist;
import org.springframework.statemachine.config.StateMachineBuilder;
import org.springframework.statemachine.persist.RepositoryStateMachinePersist;
import org.springframework.statemachine.redis.RedisStateMachineContextRepository;
import org.springframework.statemachine.redis.RedisStateMachinePersister;
import org.springframework.statemachine.state.State;
import org.springframework.test.context.junit4.SpringRunner;
import org.vitramu.common.constant.DefinitionState;
import org.vitramu.engine.excution.instance.FlowEngineEventListener;
import org.vitramu.engine.excution.instance.statemachine.FlowStateMachine;

import java.util.Arrays;

import static org.vitramu.engine.excution.instance.statemachine.FlowStateMachine.EVENT_INITIALIZED;


@Slf4j
@RunWith(SpringRunner.class)
@SpringBootTest
public class SpringStateMachinePocTest {


    @Autowired
    private ConnectionFactory connectionFactory;
    private StateMachine<String, String> osbsm;


    @Before
    public void setUp() {
        osbsm = FlowStateMachine.newStateMachineInstance("F1", connectionFactory);
    }

    @Test
    public void testStartAfterStop() {
        osbsm.sendEvent("INITIALIZED");
        osbsm.stop();
        osbsm.start();
    }

    @Test
    public void testStartInstance() {
        osbsm.start();
        osbsm.sendEvent(EVENT_INITIALIZED);
    }

    @Test
    public void testFork() {
        osbsm.start();
        osbsm.sendEvent("INITIALIZED");
        osbsm.sendEvent(DefinitionState.REQUEST_SAVING.getName());
    }

    @Test
    public void testJoinBlockingAfterOSB() {
        osbsm.start();
        osbsm.sendEvent("INITIALIZED");
        osbsm.sendEvent(DefinitionState.REQUEST_SAVING.getName());
        osbsm.sendEvent(DefinitionState.CREATE_OSB.getName());
    }

    @Test
    public void testJoinBlockingFinish() {
        osbsm.start();
        osbsm.sendEvent("INITIALIZED");
        osbsm.sendEvent(DefinitionState.REQUEST_SAVING.getName());
        osbsm.sendEvent(DefinitionState.CREATE_OSB.getName());
        osbsm.sendEvent(DefinitionState.CREATE_PUD.getName());
    }


    @Ignore
    @Test
    public void testChoice() {
        osbsm.start();
        String[] events = new String[]{"INITIALIZED", DefinitionState.REQUEST_SAVING.getName(), DefinitionState.CREATE_OSB.getName(), DefinitionState.CREATE_PUD.getName()/*, DefinitionState.CREATE_EW.getName() */};
        Arrays.stream(events)
                .forEach(event -> osbsm.sendEvent(event));
    }


    @Test
    public void testChoiceWithSpElGuard() {
        // TODO
    }

    private String stateToString(State<String, String> state) {
        return state.getIds().toString();
    }

    @Test
    public void testStateMachinePersistInMemory() throws Exception {
        FlowStateMachine.InMemoryStateMachinePersister persister = new FlowStateMachine.InMemoryStateMachinePersister();
        //        Deployment
        osbsm.start();
        // TODO use statemachine pooling technical
        persister.persist(osbsm, "PROTOTYPE");

        //        Transaction A
        osbsm.sendEvent("INITIALIZED");
        osbsm.sendEvent(DefinitionState.REQUEST_SAVING.getName());
        osbsm.sendEvent(DefinitionState.CREATE_OSB.getName());
        persister.persist(osbsm, "BLOCKING_AFTER_OSB");
        log.info("Before restore: {}", stateToString(osbsm.getState()));

        //        Transaction B
        persister.restore(osbsm, "PROTOTYPE");
        log.info("After restore: {}", stateToString(osbsm.getState()));
        osbsm.sendEvent("INITIALIZED");
        osbsm.sendEvent(DefinitionState.REQUEST_SAVING.getName());
        osbsm.sendEvent(DefinitionState.CREATE_OSB.getName());
        osbsm.sendEvent(DefinitionState.CREATE_PUD.getName());
        persister.persist(osbsm, "BLOCKING_FINISH");
        log.info("Before restore: {}", stateToString(osbsm.getState()));

        //        Transaction A
        persister.restore(osbsm, "BLOCKING_AFTER_OSB");
        log.info("After restore: {}", stateToString(osbsm.getState()));
        osbsm.sendEvent(DefinitionState.CREATE_PUD.getName());
//        osbsm.sendEvent(DefinitionState.CREATE_EW.getName());
    }

    public RedisStateMachinePersister<String, String> redisStateMachinePersister() {
        RedisStateMachineContextRepository<String, String> repository =
                new RedisStateMachineContextRepository<>(new JedisConnectionFactory());
        StateMachinePersist<String, String, String> stateMachinePersist = new RepositoryStateMachinePersist<>(repository);
        return new RedisStateMachinePersister<>(stateMachinePersist);
    }

    @Test
    public void testStateMachinePersistWithRedis() throws Exception {
        // TODO
        RedisStateMachinePersister<String, String> persister = redisStateMachinePersister();

        osbsm.start();
        // TODO use statemachine pooling technical
        persister.persist(osbsm, "PROTOTYPE");

        //        Transaction A
        osbsm.sendEvent("INITIALIZED");
        osbsm.sendEvent(DefinitionState.REQUEST_SAVING.getName());
        osbsm.sendEvent(DefinitionState.CREATE_OSB.getName());
        persister.persist(osbsm, "BLOCKING_AFTER_OSB");
        log.info("Before restore: {}", osbsm.getState().getIds());

        //        Transaction B
        persister.restore(osbsm, "PROTOTYPE");
        log.info("After restore: {}", osbsm.getState().getIds());
        osbsm.sendEvent("INITIALIZED");
        osbsm.sendEvent(DefinitionState.REQUEST_SAVING.getName());
        osbsm.sendEvent(DefinitionState.CREATE_OSB.getName());
        osbsm.sendEvent(DefinitionState.CREATE_PUD.getName());
        persister.persist(osbsm, "BLOCKING_FINISH");
        log.info("Before restore: {}", osbsm.getState().getIds());

        //        Transaction A
//        StateMachine<String, String> osbsm2 = FlowStateMachine.newStateMachineInstance(connectionFactory);
        persister.restore(osbsm, "BLOCKING_AFTER_OSB");
        log.info("After restore: {}", osbsm.getState().getIds());
        osbsm.sendEvent(DefinitionState.CREATE_PUD.getName());
//        osbsm.sendEvent(DefinitionState.CREATE_EW.getName());
    }

    @Test
    public void testStateMachineMultipleThread() {
        // TODO
    }

    @Test
    public void testStateMachinePerformance() {
        // TODO
    }

    @Test

    public void testParallelWithoutParent() {
        String machineId = "testParallelWithoutParent";

        try {
            StateMachineBuilder.Builder<String, String> builder = StateMachineBuilder.builder();
            builder.configureStates()
                    .withStates()
                    .initial(DefinitionState.REQUEST_ARRIVED.getName())
                    .state(DefinitionState.REQUEST_SAVING.getName() )
                    .end(DefinitionState.END.getName())
                    .fork(DefinitionState.CREATE_PARALLEL.getName())
                    .join(DefinitionState.CREATE_FINISH.getName())
//                    .choice(DefinitionState.CHOICE_EW.getName())
                    .state(DefinitionState.CREATE_BOOKING.getName())
                    .and()
                    .withStates()
//                    .parent(DefinitionState.CREATE_BOOKING.getName()).initial(DefinitionState.CREATE_OSB.getName())
                    .state(DefinitionState.CREATE_OSB.getName())
//                    .end(DefinitionState.END_OSB.getName())
                    .and()
                    .withStates()
//                    .parent(DefinitionState.CREATE_BOOKING.getName()).initial(DefinitionState.CREATE_PUD.getName())
                    .state(DefinitionState.CREATE_PUD.getName())
//                    .end(DefinitionState.END_PUD.getName())
                    .and()
                    .withStates().state(DefinitionState.REFRESH_STATUS.getName());
//                    .withStates().state(DefinitionState.CREATE_EW.getName(), entryAction, exitAction)

            builder.configureTransitions()
                    .withExternal().source(DefinitionState.REQUEST_ARRIVED.getName()).target(DefinitionState.REQUEST_SAVING.getName()).event(EVENT_INITIALIZED)
                    .and().withExternal().source(DefinitionState.REQUEST_SAVING.getName()).target(DefinitionState.CREATE_PARALLEL.getName()).event(DefinitionState.REQUEST_SAVING.getName())
                    .and().withFork().source(DefinitionState.CREATE_PARALLEL.getName()).target(DefinitionState.CREATE_OSB.getName()).target(DefinitionState.CREATE_PUD.getName())
                    .and().withExternal().source(DefinitionState.CREATE_OSB.getName()).target(DefinitionState.END_OSB.getName()).event(DefinitionState.CREATE_OSB.getName())
                    .and().withExternal().source(DefinitionState.CREATE_PUD.getName()).target(DefinitionState.END_PUD.getName()).event(DefinitionState.CREATE_PUD.getName())
                    .and().withJoin().source(DefinitionState.END_OSB.getName()).source(DefinitionState.END_PUD.getName()).target(DefinitionState.CREATE_FINISH.getName())
//                    .and().withExternal().source(DefinitionState.CREATE_FINISH.getName()).target(DefinitionState.CHOICE_EW.getName())
//                    .and().withChoice().source(DefinitionState.CHOICE_EW.getName()).first(DefinitionState.CREATE_EW.getName(), createEwGuard()).last(DefinitionState.REFRESH_STATUS.getName())
//                    .and().withExternal().source(DefinitionState.CREATE_EW.getName()).target(DefinitionState.REFRESH_STATUS.getName()).event(DefinitionState.CREATE_EW.getName())
                    .and().withExternal().source(DefinitionState.CREATE_FINISH.getName()).target(DefinitionState.REFRESH_STATUS.getName())
                    .and().withExternal().source(DefinitionState.REFRESH_STATUS.getName()).target(DefinitionState.END.getName()).event(DefinitionState.REFRESH_STATUS.getName());
            builder.configureConfiguration()
                    .withConfiguration().machineId(machineId);

            final StateMachine<String, String> sm = builder.build();
            sm.addStateListener(new FlowEngineEventListener());
            sm.start();
            String[] events = new String[]{"INITIALIZED", DefinitionState.REQUEST_SAVING.getName(), DefinitionState.CREATE_OSB.getName(), DefinitionState.CREATE_PUD.getName(), DefinitionState.REFRESH_STATUS.getName()/*, DefinitionState.CREATE_EW.getName() */};
            Arrays.stream(events)
                    .forEach(event -> sm.sendEvent(event));
        } catch (Exception e) {
            log.error("factory build failed", e);
        }


    }
}
