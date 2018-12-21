package org.vitramu.engine.rest;

import java.io.File;

/**
 * FlowDefinitionController contains rest api like submitting flow definition, binding task to service command and deploying flow definition.
 * */
public class FlowDefinitionController {


    /**
     * @param definition {String} definition written in json
     * @return definition id assigned by master
     */
    public String createDefinition(String definition) {
        return null;
    }

    /**
     * @param definitionId {String} definition id assigned by master when creating definition
     * @param definition {String} definition described in json
     */
    public void updateDefinition(String definitionId, String definition) {

    }

    /**
     * @param definitionId {String} definition id in which the binding is being created
     * @param taskId {String} task definition id which is bound to
     * @param commandId {String} command definition id which is bound
     */
    public void bind(String definitionId, String taskId, String commandId) {

    }

    /**
     * @param definitionId {String} definition id which is being deployed
     */
    public void deploy(String definitionId) {

    }
}
