package org.vitramu.engine.excution.service;

import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;
import org.vitramu.engine.definition.FlowDefinitionRepository;
import org.vitramu.engine.excution.element.StartEvent;
import org.vitramu.engine.excution.instance.Flow;

/**
 * FlowService is used for rest controller or event listener and other application layer element to consume capability provided by Flow
 *
 * */
@AllArgsConstructor
@Service
public class FlowService {

    private FlowRepository flowRepository;
    private FlowDefinitionRepository flowDefinitionRepository;


    public Flow startFlowInstance(StartEvent starter) {
        if(flowRepository.isStarted(starter.getTransactionId())) {
            // flow instance with transactionId in starter as instance id already exists, do nothing

            return null;
        }
        Flow flowInstance = Flow.builder()
                .starter(starter)
                .definition(flowDefinitionRepository.findFlowDefinitionById(starter.getFlowDefinitionId()))
                .build();
        flowInstance.start();
        return flowInstance;
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
