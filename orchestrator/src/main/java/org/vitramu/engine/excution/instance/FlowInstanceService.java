package org.vitramu.engine.excution.instance;


import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.vitramu.engine.definition.FlowDefinitionRepository;
import org.vitramu.engine.excution.instance.statemachine.FlowEngineFactory;
import org.vitramu.engine.excution.message.StartMessage;
import org.vitramu.engine.excution.message.TaskMessage;
import org.vitramu.engine.excution.service.EventService;

import java.util.Optional;

@Slf4j
@Service
public class FlowInstanceService {

    @Autowired
    private FlowDefinitionRepository definitionRepository;

    @Autowired
    private FlowInstanceRepository flowInstanceRepository;


    @Autowired
    private FlowEngineFactory flowFactory;

    @Autowired
    private FlowInstanceFactory flowInstanceFactory;

    @Autowired
    private EventService eventService;

    /**
     * flow对应的statemachine应该在应用启动后处于就绪状态，预定义的initial state是REQUEST_ARRIVED。
     * startFlowInstance向statemachine发送预定义的 INITIALIZED event，触发流程启动。
     */
    public void startFlowInstance(StartMessage start) {
//        find flow definition
//        generate new instanceId and create flow instance
//        build statemachine instance specified by definitionId
//        definitionId should be prototype statemachine id
        log.info("starting flow {} with instance id {}", start.getFlowDefinitionId(), start.getFlowInstanceId());
        FlowInstance instance = flowInstanceFactory.build(start.getFlowDefinitionId(), start.getFlowInstanceId());
        instance.setParentInstanceId(start.getParentFlowInstanceId());
        instance.setStartServiceName(start.getServiceName());
        instance.setStartServiceInstanceId(start.getServiceInstanceId());
        instance.start();

        instance.pause();
        flowInstanceRepository.save(instance);

    }

    public void completeTask(TaskMessage message) {
        // TODO use abstract class to encapsulate complete task message

        Optional<FlowInstance> instanceOpt = flowInstanceRepository.findById(message.getFlowInstanceId());
        // build event according taskId and task definition
        instanceOpt.ifPresent(instance -> {
            if (message.isAborted()) {
                log.info("abort task: " + message);
            } else {
                instance.completeTask(message.getTaskInstanceId(), message.getTaskName());
            }
            instance.pause();
        });
    }

}
