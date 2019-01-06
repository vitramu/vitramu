package org.vitramu.engine.excution.instance.statemachine;

import lombok.extern.slf4j.Slf4j;
import org.springframework.statemachine.StateMachine;
import org.springframework.statemachine.StateMachineContext;
import org.springframework.statemachine.StateMachinePersist;
import org.springframework.statemachine.action.Action;
import org.springframework.statemachine.config.StateMachineBuilder;
import org.springframework.statemachine.guard.Guard;
import org.springframework.statemachine.persist.AbstractStateMachinePersister;
import org.springframework.stereotype.Component;
import org.vitramu.engine.definition.element.TaskDefinition;

import java.util.HashMap;

@Slf4j
@Component
public class FlowStateMachineFactory {
    // TEST BEGINS
    public static final TaskDefinition REQUEST_ARRIVED = new TaskDefinition("T1", "REQUEST_ARRIVED");
    public static final TaskDefinition REQUEST_SAVING = new TaskDefinition("T2", "REQUEST_SAVING");
    public static final TaskDefinition CREATE_PARALLEL = new TaskDefinition("T3", "CREATE_PARALLEL");
    public static final TaskDefinition CREATE_BOOKING = new TaskDefinition("T4", "CREATE_BOOKING");
    public static final TaskDefinition CREATE_OSB = new TaskDefinition("T5", "CREATE_OSB");
    public static final TaskDefinition END_OSB = new TaskDefinition("T6", "END_OSB");
    public static final TaskDefinition CREATE_PUD = new TaskDefinition("T7", "CREATE_PUD");
    public static final TaskDefinition END_PUD = new TaskDefinition("T8", "END_PUD");
    public static final TaskDefinition CREATE_FINISH = new TaskDefinition("T9", "CREATE_FINISH");
    public static final TaskDefinition CHOICE_EW = new TaskDefinition("T10", "CHOICE_EW");
    public static final TaskDefinition CREATE_EW = new TaskDefinition("T11", "CREATE_EW");
    public static final TaskDefinition REFRESH_STATUS = new TaskDefinition("T12", "REFRESH_STATUS");
    public static final TaskDefinition END = new TaskDefinition("T13", "END");

    // TEST ENDS
    private StateMachine<TaskDefinition, String> osbsm;

    public static class InMemoryStateMachinePersister<T> extends AbstractStateMachinePersister<T, String, String> {
        public InMemoryStateMachinePersister() {
            super(new InMemoryStateMachinePersist<T>());
        }
    }

    public static class InMemoryStateMachinePersist<T> implements StateMachinePersist<T, String, String> {

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
    public static InMemoryStateMachinePersister<TaskDefinition> persister = new InMemoryStateMachinePersister<>();

    public FlowStateMachineFactory() {
        try {
            StateMachineBuilder.Builder<TaskDefinition, String> builder = StateMachineBuilder.builder();
            builder.configureStates()
                    .withStates()
                    .initial(REQUEST_ARRIVED)
                    .state(REQUEST_SAVING, dispatchCommandAction())
                    .end(END)
                    .fork(CREATE_PARALLEL)
                    .join(CREATE_FINISH)
                    .choice(CHOICE_EW)
                    .state(CREATE_BOOKING)
                    .and()
                    .withStates()
                    .parent(CREATE_BOOKING).initial(CREATE_OSB, dispatchCommandAction()).end(END_OSB)
                    .and()
                    .withStates()
                    .parent(CREATE_BOOKING).initial(CREATE_PUD, dispatchCommandAction()).end(END_PUD)
                    .and()
                    .withStates().state(CREATE_EW, dispatchCommandAction()).state(REFRESH_STATUS, dispatchCommandAction());
            builder.configureTransitions()
                    .withExternal().source(REQUEST_ARRIVED).target(REQUEST_SAVING).event("INITIALIZED")
                    .and().withExternal().source(REQUEST_SAVING).target(CREATE_PARALLEL).event("REQUEST_SAVED")
                    .and().withFork().source(CREATE_PARALLEL).target(CREATE_OSB).target(CREATE_PUD)
                    .and().withExternal().source(CREATE_OSB).target(END_OSB).event("CREATE_OSB_FIN")
                    .and().withExternal().source(CREATE_PUD).target(END_PUD).event("CREATE_PUD_FIN")
                    .and().withJoin().source(END_OSB).source(END_PUD).target(CREATE_FINISH)
                    .and().withExternal().source(CREATE_FINISH).target(CHOICE_EW)
                    .and().withChoice().source(CHOICE_EW).first(CREATE_EW, createEwGuard()).last(REFRESH_STATUS)
                    .and().withExternal().source(CREATE_EW).target(REFRESH_STATUS).event("CREATE_EW_FIN")
                    .and().withExternal().source(REFRESH_STATUS).target(END);
            osbsm = builder.build();
            osbsm.addStateListener(new FlowStateMachineEventListener());
            osbsm.start();

            persister.persist(osbsm, "PROTOTYPE");
        } catch (Exception e) {
            log.error("factory build failed", e);
        }
    }

    private Action<TaskDefinition, String> dispatchCommandAction() {
        return context -> log.info("Dispatching command");
    }

    private Guard<TaskDefinition, String> createEwGuard() {
        return context -> {
            log.info("evaluate ew guard");
            return true;
        };
    }

    public StateMachine build(String definitionId) {
        return osbsm;
    }
}
