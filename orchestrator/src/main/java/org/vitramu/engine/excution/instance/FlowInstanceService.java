package org.vitramu.engine.excution.service;


import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.statemachine.StateMachine;
import org.springframework.statemachine.action.Action;
import org.springframework.statemachine.config.StateMachineBuilder;
import org.springframework.statemachine.guard.Guard;
import org.springframework.statemachine.listener.StateMachineListenerAdapter;
import org.springframework.statemachine.state.State;
import org.springframework.stereotype.Service;
import org.vitramu.engine.definition.FlowDefinitionRepository;
import org.vitramu.engine.definition.element.FlowDefinition;
import org.vitramu.engine.definition.element.TaskDefinition;
import org.vitramu.engine.excution.action.FlowStateEntryAction;
import org.vitramu.engine.excution.instance.FlowInstance;

@Slf4j
@Service
public class FlowStateMachineService {

    @Autowired
    private FlowDefinitionRepository definitionRepository;

    @Autowired
    private FlowInstanceRepository flowInstanceRepository;

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

    @Autowired
    private FlowStateEntryAction<Task, String> dispatchCommandAction;

    private FlowStateMachineService() throws Exception {
        StateMachineBuilder.Builder<Task, String> builder = StateMachineBuilder.builder();
        builder.configureStates()
                .withStates()
                .initial(Task.CREATE_START)
                // TODO 需要调查
                // 必须使用entry action下发command
                // 实验表明，state action没有执行，具体原因需要调查
                .state(Task.SAVE_REQUEST, dispatchCommandAction, null)
                .end(Task.END)
                .fork(Task.CREATE_PARALLEL)
                .join(Task.CREATE_FINISH)
                .choice(Task.CHOICE_EW)
                .state(Task.CREATE_BOOKING)
                .and()
                .withStates()
                .parent(Task.CREATE_BOOKING).initial(Task.CREATE_OSB, dispatchCommandAction).end(Task.END_OSB)
                .and()
                .withStates()
                .parent(Task.CREATE_BOOKING).initial(Task.CREATE_PUD, dispatchCommandAction).end(Task.END_PUD)
                .and()
                .withStates().state(Task.CREATE_EW, dispatchCommandAction).state(Task.REFRESH_STATUS, dispatchCommandAction);
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
        osbsm.start();
    }


    private Action<Task, String> responseAction() {
        return context ->
        {
            log.info("Respond to trigger");
        };
    }


    private Guard<Task, String> createEwGuard() {
        return context -> {
            log.info("evaluate ew guard");
            return true;
        };
    }

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

    @Autowired
    private FlowStateMachineFactory flowFactory;

    /**
     *
     */
    public void startFlowInstance(StartMessage start) {
//        find flow definition
//        generate new instanceId and create flow instance
//        build statemachine instance specified by definitionId
//        definitionId should be prototype statemachine id
        log.info("starting flow: {}", start.getFlowDefinitionId());
        FlowDefinition definition = definitionRepository.findFlowDefinitionById(start.getFlowDefinitionId());
        StateMachine<TaskDefinition, String> engine = flowFactory.build(start.getFlowDefinitionId());

        FlowInstance instance = new FlowInstance(definition, start.getFlowInstanceId(), start.getParentFlowInstanceId(), engine);
        instance.start();
        instance.pause();
    }

    public void completeTask(TaskMessage message) {
        // TODO use abstract class to encapsulate complete task message
        FlowInstance instance = flowInstanceRepository.findFlowInstanceById(message.getFlowInstanceId());
        // build event according taskId and task definition
        instance.completeTask(message.getTaskName(), message.getTaskInstanceId());

        instance.pause();
    }

    public void completeTask(String eventType) {
        log.info("completing task: {}", eventType);
        osbsm.sendEvent(eventType);
    }
}
