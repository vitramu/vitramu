package org.vitramu.common.definition.xml;

import com.fasterxml.jackson.annotation.JsonIdentityInfo;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.ObjectIdGenerators;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;
import org.vitramu.common.definition.FlowDefinitionDocument;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonIdentityInfo(generator = ObjectIdGenerators.IntSequenceGenerator.class)
@Document(collection = FlowDefinitionXmlDocument.DEFINITION_XML_DOCUMENT_COLLECTION_NAME)
@ToString
@Getter
public class FlowDefinitionXmlDocument implements FlowDefinitionDocument {
    public static final String DEFINITION_XML_DOCUMENT_COLLECTION_NAME = "document";

    @Id
    private String id;
    @Setter
    private String definitionId;
    @Setter
    private String content;
    private String createdAt;

    public FlowDefinitionXmlDocument() {
        LocalDateTime createdAtDateTime = LocalDateTime.now();
        createdAt = createdAtDateTime.format(DateTimeFormatter.ISO_DATE_TIME);
    }

    public FlowDefinitionXmlDocument(String definitionId, String content) {
        this.definitionId = definitionId;
        this.content = content;
    }
}
