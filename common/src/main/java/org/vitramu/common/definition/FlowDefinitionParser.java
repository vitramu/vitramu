package org.vitramu.common.definition;

import org.vitramu.common.definition.element.FlowDefinition;

import javax.xml.bind.JAXBException;
import java.io.IOException;

public interface FlowDefinitionParser<T> {

    FlowDefinition parse(T document) throws IOException, JAXBException;
}
