package org.vitramu.common.definition.parser;

import lombok.Cleanup;
import lombok.NonNull;
import org.springframework.core.io.InputStreamResource;
import org.springframework.core.io.Resource;
import org.springframework.statemachine.config.model.StateMachineModel;
import org.springframework.statemachine.uml.UmlStateMachineModelFactory;
import org.vitramu.common.definition.FlowDefinitionXmlDocument;
import org.vitramu.common.definition.element.FlowDefinition;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.nio.charset.StandardCharsets;

public class FlowDefinitionXmlParser implements FlowDefinitionParser<FlowDefinitionXmlDocument> {

    @Override
    public FlowDefinition parse(@NonNull FlowDefinitionXmlDocument document) throws IOException {
        @NonNull String xml = document.getXml();
        @Cleanup InputStream is = new ByteArrayInputStream(xml.getBytes(StandardCharsets.UTF_8));
        Resource resource = new InputStreamResource(is);
        UmlStateMachineModelFactory factory = new UmlStateMachineModelFactory(resource);
        StateMachineModel<String, String> model = factory.build();
        FlowDefinition definition = new FlowDefinition(document.getId(), document.getDefinitionId(), model);
        return definition;
    }
}
