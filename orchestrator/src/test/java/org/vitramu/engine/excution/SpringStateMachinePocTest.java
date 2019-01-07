package org.vitramu.engine.excution;

import lombok.extern.slf4j.Slf4j;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.amqp.rabbit.connection.ConnectionFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.redis.connection.jedis.JedisConnectionFactory;
import org.springframework.statemachine.StateMachine;
import org.springframework.statemachine.StateMachinePersist;
import org.springframework.statemachine.persist.RepositoryStateMachinePersist;
import org.springframework.statemachine.redis.RedisStateMachineContextRepository;
import org.springframework.statemachine.redis.RedisStateMachinePersister;
import org.springframework.statemachine.state.State;
import org.springframework.test.context.junit4.SpringRunner;
import org.vitramu.engine.constant.DefinitionState;
import org.vitramu.engine.definition.Definition;
import org.vitramu.engine.excution.instance.statemachine.FlowStateMachine;

import java.util.Arrays;

import static java.util.stream.Collectors.toList;


@Slf4j
@RunWith(SpringRunner.class)
@SpringBootTest
public class SpringStateMachinePocTest {


    @Autowired
    private ConnectionFactory connectionFactory;
    private StateMachine<Definition, String> osbsm;


    @Before
    public void setUp() {
        osbsm = FlowStateMachine.newStateMachineInstance(connectionFactory);
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
        osbsm.sendEvent(FlowStateMachine.EVENT_INITIALIZED);
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

    @Test
    public void testChoice() {
        osbsm.start();
        String[] events = new String[]{"INITIALIZED", DefinitionState.REQUEST_SAVING.getName(), DefinitionState.CREATE_OSB.getName(), DefinitionState.CREATE_PUD.getName(), DefinitionState.CREATE_EW.getName()};
        Arrays.stream(events)
                .forEach(event -> osbsm.sendEvent(event));
    }


    @Test
    public void testChoiceWithSpElGuard() {
        // TODO
    }

    private String stateToString(State<Definition, String> state) {
        return state.getIds().stream().map(Definition::getName).collect(toList()).toString();
    }

    @Test
    public void testStateMachinePersistInMemory() throws Exception {
        FlowStateMachine.InMemoryStateMachinePersister<Definition> persister = new FlowStateMachine.InMemoryStateMachinePersister<>();
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
        osbsm.sendEvent(DefinitionState.CREATE_EW.getName());
    }

    public RedisStateMachinePersister<Definition, String> redisStateMachinePersister() {
        RedisStateMachineContextRepository<Definition, String> repository =
                new RedisStateMachineContextRepository<>(new JedisConnectionFactory());
        StateMachinePersist<Definition, String, String> stateMachinePersist = new RepositoryStateMachinePersist<>(repository);
        return new RedisStateMachinePersister<>(stateMachinePersist);
    }

    @Test
    public void testStateMachinePersistWithRedis() throws Exception {
        // TODO
        RedisStateMachinePersister<Definition, String> persister = redisStateMachinePersister();

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
        persister.restore(osbsm, "BLOCKING_AFTER_OSB");
        log.info("After restore: {}", osbsm.getState().getIds());
        osbsm.sendEvent(DefinitionState.CREATE_PUD.getName());
        osbsm.sendEvent(DefinitionState.CREATE_EW.getName());
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
