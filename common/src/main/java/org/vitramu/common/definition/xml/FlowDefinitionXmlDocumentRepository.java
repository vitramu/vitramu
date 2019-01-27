package org.vitramu.common.definition.xml;

import com.mongodb.MongoClient;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;
import org.vitramu.common.definition.FlowDefinitionDocumentRepository;
import org.vitramu.common.definition.xml.FlowDefinitionXmlDocument;

import java.util.ArrayList;
import java.util.Optional;

@Repository
public class FlowDefinitionXmlDocumentRepository implements FlowDefinitionDocumentRepository<FlowDefinitionXmlDocument> {

    public static final String FLOW_DEFINITION_DB_NAME = "flow_definition";
    private MongoTemplate mongoTemplate;

    public FlowDefinitionXmlDocumentRepository(MongoClient mongo) {
        mongoTemplate = new MongoTemplate(mongo, FLOW_DEFINITION_DB_NAME);
    }

    @Override
    public <S extends FlowDefinitionXmlDocument> S save(S document) {
        mongoTemplate.save(document);
        return document;
    }

    @Override
    public <S extends FlowDefinitionXmlDocument> Iterable<S> saveAll(Iterable<S> documents) {
        documents.forEach(mongoTemplate::save);
        return documents;
    }

    @Override
    public Optional<FlowDefinitionXmlDocument> findById(String id) {
        return Optional.ofNullable(mongoTemplate.findById(id, FlowDefinitionXmlDocument.class));
    }

    @Override
    public boolean existsById(String definitionId) {
        return mongoTemplate.exists(new Query(Criteria.where("definitionId").is(definitionId)), FlowDefinitionXmlDocument.class);
    }

    public Iterable<String> findAllId() {
        Query query = new Query();
        query.fields().include("id");
        return mongoTemplate.find(query, FlowDefinitionXmlDocument.class)
                .stream()
                .collect(
                        ArrayList::new,
                        (list, doc) -> list.add(doc.getId()),
                        (list, ids) -> list.addAll(ids)
                );
    }

    @Override
    public Iterable<FlowDefinitionXmlDocument> findAll() {
        return mongoTemplate.findAll(FlowDefinitionXmlDocument.class);
    }

    @Override
    public Iterable<FlowDefinitionXmlDocument> findAllById(Iterable<String> definitionIds) {
        return mongoTemplate.find(new Query(Criteria.where("definitionId").in(definitionIds)), FlowDefinitionXmlDocument.class);
    }

    @Override
    public long count() {
        return 0;
    }

    @Override
    public void deleteById(String s) {

    }

    @Override
    public void delete(FlowDefinitionXmlDocument entity) {

    }

    @Override
    public void deleteAll(Iterable<? extends FlowDefinitionXmlDocument> entities) {

    }

    @Override
    public void deleteAll() {

    }
}
