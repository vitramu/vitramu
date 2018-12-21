package org.vitramu.engine.excution;

import org.vitramu.engine.definition.TaskDefinition;
/**
 * FlowService is used for rest controller or event listener and other application layer element to consume capability provided by Flow
 *
 * */
public class FlowService {

    private FlowRepository flowRepository;

    public void excuteTask(String flowInstanceId, TaskDefinition task) {
        // use FlowRepository to re-construct a flow instance

        // call completeTask on this instance

        // persist task state

        // create and emit MQ event according task definition, flowInstanceId and input parameters


    }
}
