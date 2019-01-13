package org.vitramu.engine.excution.instance;


import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.vitramu.engine.excution.message.StartMessage;
import org.vitramu.engine.excution.message.TaskMessage;
import org.vitramu.engine.excution.service.FlowEngineService;

@Slf4j
@Service
public class FlowInstanceService {
    private FlowInstanceFactory flowInstanceFactory;

    @Autowired
    private FlowEngineService engineService;

    public FlowInstanceService(FlowInstanceFactory flowInstanceFactory) {
        this.flowInstanceFactory = flowInstanceFactory;
    }

    /**
     * flow对应的statemachine应该在应用启动后处于就绪状态，预定义的initial state是REQUEST_ARRIVED。
     * startFlowInstance向statemachine发送预定义的 INITIALIZED event，触发流程启动。
     */
    public void startFlowInstance(StartMessage start) {
        log.info("starting flow {} with instance id {}", start.getFlowDefinitionId(), start.getFlowInstanceId());
        FlowInstance instance = flowInstanceFactory.build(start.getFlowDefinitionId(), start.getFlowInstanceId());

        instance.setParentInstanceId(start.getParentFlowInstanceId());
        instance.setStartServiceName(start.getServiceName());
        instance.setStartServiceInstanceId(start.getServiceInstanceId());
        instance.start();
        instance.stop();
    }


    public void completeTask(TaskMessage message) {
        FlowInstance instance = flowInstanceFactory.build(message.getFlowDefinitionId(), message.getFlowInstanceId());
        // build event according taskId and task definition
        if (message.isAborted()) {
            log.info("abort task: " + message);
        } else {
            try {
                engineService.restoreStateMachine(instance.getEngine(), instance.getInstanceId());
                instance.completeTask(message.getTaskInstanceId(), message.getTaskName());
                engineService.cacheInstance(instance.getEngine(), instance.getInstanceId());
            } catch (Exception e) {
                log.error("Complete task failed with message={}", message, e);
            }
        }
//        instance.stop();
    }

}
