package org.vitramu.common.definition;

import org.springframework.data.repository.CrudRepository;

public interface FlowDefinitionDocumentRepository<T extends FlowDefinitionDocument> extends CrudRepository<T, String> {
}
