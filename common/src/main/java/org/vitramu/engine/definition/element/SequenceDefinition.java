package org.vitramu.engine.definition.element;

import lombok.Data;
import org.vitramu.engine.definition.AbstractDefinition;

@Data
public class SequenceDefinition  extends AbstractDefinition {
    private String sourceId;
    private DefinitionType sourceType;
    private String targetId;
    private DefinitionType targetType;
    private String condition;


    public SequenceDefinition(String id, String name) {
        super(id,name,DefinitionType.SEQUENCE);
    }

}
