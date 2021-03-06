package org.vitramu.common.definition;


import org.vitramu.common.definition.element.DefinitionType;

public interface Definition {

    /**
     * Get id of current Definition Object. Id usually is an internal unique identifier like UUID
     * */
    String getId();
    /**
     * Get name of current Definition Object. Name usually is readable for hunman.
     * */
    String getName();
    /**
     * Get type of current Definition Object.
     * */
    DefinitionType getType();
}
