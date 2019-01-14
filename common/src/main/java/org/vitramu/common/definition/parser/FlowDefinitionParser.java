package org.vitramu.common.definition.parser;

import org.vitramu.common.definition.element.FlowDefinition;

public interface FlowDefinitionParser<T> {

    FlowDefinition parse(T document);
}
