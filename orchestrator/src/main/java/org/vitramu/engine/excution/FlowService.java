package org.vitramu.engine.excution;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.vitramu.engine.definition.FlowDefinitionRepository;
import org.vitramu.engine.excution.element.StartEvent;

/**
 * FlowService is used for rest controller or event listener and other application layer element to consume capability provided by Flow
 *
 * */
@Service
public class FlowService {

    @Autowired
    private FlowRepository flowRepository;

    private FlowDefinitionRepository flowDefinitionRepository;
    public void startFlowInstance(StartEvent starter) {
        if(flowRepository.isStarted(starter.getTransactionId())) {
            // flow instance with transactionId in starter as instance id already exists, do nothing

            return;
        }
        Flow flowInstance = Flow.builder()
                .starter(starter)
                .definition(flowDefinitionRepository.findFlowDefinitionById(starter.getFlowDefinitionId()))
                .build();
        flowInstance.start();
    }

    public void completeTask(String flowInstanceId, String taskId) {
        Flow flowInstance = flowRepository.findFlowInstanceById(flowInstanceId);
        flowInstance.completeTask(taskId);

    }

    public void abortTask(String flowInstanceId, String taskId) {
        Flow flowInstance = flowRepository.findFlowInstanceById(flowInstanceId);
        flowInstance.abortTask(taskId);
    }

    public void abortFlowInstance(String flowInstanceId) {
        Flow flowInstance = flowRepository.findFlowInstanceById(flowInstanceId);
        flowInstance.abort();
    }

}
