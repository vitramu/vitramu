package org.vitramu.engine.excution;

import lombok.extern.slf4j.Slf4j;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.amqp.rabbit.connection.ConnectionFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.statemachine.StateMachine;
import org.springframework.statemachine.config.StateMachineFactory;
import org.springframework.statemachine.data.StateMachineRepository;
import org.springframework.statemachine.data.StateRepository;
import org.springframework.statemachine.data.TransitionRepository;
import org.springframework.statemachine.data.mongodb.MongoDbRepositoryState;
import org.springframework.statemachine.data.mongodb.MongoDbRepositoryStateMachine;
import org.springframework.statemachine.data.mongodb.MongoDbRepositoryTransition;
import org.springframework.statemachine.state.PseudoStateKind;
import org.springframework.test.context.junit4.SpringRunner;
import org.vitramu.engine.excution.instance.FlowEngineEventListener;

import java.util.Arrays;

import static org.vitramu.common.constant.DefinitionState.*;

@Slf4j
@RunWith(SpringRunner.class)
@SpringBootTest
public class SpringStateMachineMongoTest {

    static final String MACHINE_ID = "SM-MongoDB-1";
    @Autowired
    StateRepository<MongoDbRepositoryState> stateRepository;

    @Autowired
    TransitionRepository<MongoDbRepositoryTransition> transitionRepository;

    @Autowired
    StateMachineRepository<MongoDbRepositoryStateMachine> stateMachineRepository;


    @Autowired
    StateMachineFactory<String,String> stateMachineFactory;
    @Test
    public void saveStateMachineModel() {
        MongoDbRepositoryState STATE_REQUEST_ARRIVED = new MongoDbRepositoryState(REQUEST_ARRIVED.getName(), true);

        MongoDbRepositoryState STATE_REQUEST_SAVING = new MongoDbRepositoryState(REQUEST_SAVING.getName());

        MongoDbRepositoryState STATE_CREATE_PARALLEL = new MongoDbRepositoryState(CREATE_PARALLEL.getName());
        STATE_CREATE_PARALLEL.setKind(PseudoStateKind.FORK);

        MongoDbRepositoryState STATE_CREATE_BOOKING = new MongoDbRepositoryState(CREATE_BOOKING.getName());

        MongoDbRepositoryState STATE_CREATE_OSB = new MongoDbRepositoryState(CREATE_OSB.getName(), true);
        STATE_CREATE_OSB.setParentState(STATE_CREATE_BOOKING);
        STATE_CREATE_OSB.setRegion("OSB-Region");

        MongoDbRepositoryState STATE_CREATE_PUD = new MongoDbRepositoryState(CREATE_PUD.getName(), true);
        STATE_CREATE_PUD.setParentState(STATE_CREATE_BOOKING);
        STATE_CREATE_PUD.setRegion("PUD-Region");

        MongoDbRepositoryState STATE_END_OSB = new MongoDbRepositoryState(END_OSB.getName());
        STATE_END_OSB.setKind(PseudoStateKind.END);
        STATE_END_OSB.setParentState(STATE_CREATE_BOOKING);
        STATE_END_OSB.setRegion("OSB-Region");

        MongoDbRepositoryState STATE_END_PUD = new MongoDbRepositoryState(END_PUD.getName());
        STATE_END_PUD.setKind(PseudoStateKind.END);
        STATE_END_PUD.setParentState(STATE_CREATE_BOOKING);
        STATE_END_PUD.setRegion("PUD-Region");

        MongoDbRepositoryState STATE_CREATE_FINISH = new MongoDbRepositoryState(CREATE_FINISH.getName());
        STATE_CREATE_FINISH.setKind(PseudoStateKind.JOIN);

        MongoDbRepositoryState STATE_REFRESH_STATUS = new MongoDbRepositoryState(REFRESH_STATUS.getName());

        MongoDbRepositoryState STATE_END = new MongoDbRepositoryState(END.getName());
        STATE_END.setKind(PseudoStateKind.END);

        MongoDbRepositoryState[] states = new MongoDbRepositoryState[]{
                STATE_REQUEST_ARRIVED,
                STATE_REQUEST_SAVING,
                STATE_CREATE_PARALLEL,
                STATE_CREATE_BOOKING,
                STATE_CREATE_OSB,
                STATE_CREATE_PUD,
                STATE_END_OSB,
                STATE_END_PUD,
                STATE_CREATE_FINISH,
                STATE_REFRESH_STATUS,
                STATE_END
        };
        Arrays.stream(states)
                .forEach(s -> s.setMachineId(MACHINE_ID));


        MongoDbRepositoryTransition[] transitions = new MongoDbRepositoryTransition[]{
                new MongoDbRepositoryTransition(MACHINE_ID, STATE_REQUEST_ARRIVED, STATE_REQUEST_SAVING, "INITIALIZED"),
                new MongoDbRepositoryTransition(MACHINE_ID, STATE_REQUEST_SAVING, STATE_CREATE_PARALLEL, REQUEST_SAVING.getName()),
                new MongoDbRepositoryTransition(MACHINE_ID, STATE_CREATE_PARALLEL, STATE_CREATE_OSB, null),
                new MongoDbRepositoryTransition(MACHINE_ID, STATE_CREATE_PARALLEL, STATE_CREATE_PUD, null),
                new MongoDbRepositoryTransition(MACHINE_ID, STATE_CREATE_OSB, STATE_END_OSB, CREATE_OSB.getName()),
                new MongoDbRepositoryTransition(MACHINE_ID, STATE_CREATE_PUD, STATE_END_PUD, CREATE_PUD.getName()),
                new MongoDbRepositoryTransition(MACHINE_ID, STATE_END_OSB, STATE_CREATE_FINISH, null),
                new MongoDbRepositoryTransition(MACHINE_ID, STATE_END_PUD, STATE_CREATE_FINISH, null),
                new MongoDbRepositoryTransition(MACHINE_ID, STATE_CREATE_FINISH, STATE_REFRESH_STATUS, null),
                new MongoDbRepositoryTransition(MACHINE_ID, STATE_REFRESH_STATUS, STATE_END, REFRESH_STATUS.getName())
        };

        MongoDbRepositoryStateMachine sm = new MongoDbRepositoryStateMachine();
        sm.setMachineId(MACHINE_ID);
        stateMachineRepository.save(sm);
        stateRepository.saveAll(Arrays.asList(states));
        transitionRepository.saveAll(Arrays.asList(transitions));

    }

    @Autowired
    ConnectionFactory connectionFactory;

    @Test
    public void testLoadStateMachineFromMongo() throws Exception {
        StateMachine<String, String> sm1 = stateMachineFactory.getStateMachine(MACHINE_ID);
        String[] events = new String[]{
                "INITIALIZED",
                REQUEST_SAVING.getName(),
                CREATE_OSB.getName(),
                CREATE_PUD.getName(),
                REFRESH_STATUS.getName()
                /*, DefinitionState.CREATE_EW.getName() */};
        sm1.addStateListener(new FlowEngineEventListener());
        sm1.start();
        Arrays.stream(events)
                .forEach(event -> sm1.sendEvent(event));
    }

}
