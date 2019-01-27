package org.vitramu.common.definition.xml;

import lombok.NonNull;
import org.vitramu.common.definition.xml.model.FlowDefinitionModel;
import org.vitramu.common.definition.element.FlowDefinition;
import org.vitramu.common.definition.FlowDefinitionParser;
import org.vitramu.common.definition.xml.model.Model;

import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBException;
import javax.xml.bind.Unmarshaller;
import java.io.IOException;
import java.io.StringReader;

public class FlowDefinitionXmlParser implements FlowDefinitionParser<FlowDefinitionXmlDocument> {

    @Override
    public FlowDefinition parse(@NonNull FlowDefinitionXmlDocument document) throws JAXBException {
        @NonNull String xml = document.getContent();
        JAXBContext context = JAXBContext.newInstance(Model.class);
        Unmarshaller unmarshaller = context.createUnmarshaller();
        StringReader reader = new StringReader(xml);
        Model model = (Model) unmarshaller.unmarshal(reader);
        String machineId = document.getDefinitionId();
        FlowDefinition definition = new FlowDefinition(document.getId(), machineId, model);
        return definition;
    }
}
