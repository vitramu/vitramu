package org.vitramu.common.definition.parser;

import org.vitramu.common.definition.element.FlowDefinition;

import java.io.IOException;

public interface FlowDefinitionParser<T> {

    FlowDefinition parse(T document) throws IOException;
}
