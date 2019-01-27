package org.vitramu.master.rest;

import lombok.Cleanup;
import lombok.NonNull;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import org.vitramu.common.definition.FlowDefinitionRepository;
import org.vitramu.common.definition.xml.FlowDefinitionXmlDocument;
import org.vitramu.common.definition.xml.FlowDefinitionXmlDocumentRepository;
import org.vitramu.common.definition.element.FlowDefinition;
import org.vitramu.common.definition.xml.FlowDefinitionXmlParser;
import org.vitramu.master.rest.exception.DocumentNotFoundException;
import org.vitramu.master.rest.vo.BindingVo;

import javax.xml.bind.JAXBException;
import java.io.*;
import java.util.Optional;

/**
 * FlowDefinitionController contains rest api like submitting flow definition, binding task to service command and deploying flow definition.
 */
@Slf4j
@RestController
@RequestMapping("/flow/definition")
public class FlowDefinitionController {

    @Autowired
    private FlowDefinitionXmlDocumentRepository xmlDocumentRepository;
    @Autowired
    private FlowDefinitionRepository definitionRepository;

    @GetMapping(value = {"/"})
    public String listDefinition() {
        return xmlDocumentRepository.findAllId().toString();
    }

    @GetMapping(value = ("/{id}"), produces = {"text/content"})
    public String getDefinition(@PathVariable("id") String id) throws Exception {
        Optional<FlowDefinitionXmlDocument> docOpt = xmlDocumentRepository.findById(id);
        docOpt.orElseThrow(() -> new DocumentNotFoundException("document with id=" + id + " not exist"));
        return docOpt.get().getContent();
    }

    /**
     * @param {String} definition written in json
     * @return definition id assigned by engine
     */
    @PostMapping(value = {"/"})
    public String createDefinition(@RequestBody FlowDefinitionXmlDocument document) {
        xmlDocumentRepository.save(document);
        return document.getId();
    }

    /**
     * @param id         {String} definition id assigned by engine when creating definition
     * @param definition {String} definition described in json
     */
    @PutMapping(value = {"{id}"})
    public void updateDefinition(@PathVariable("id") String id, @RequestParam("definition") MultipartFile definition) throws Exception {
        @NonNull String contentType = definition.getContentType();
        @Cleanup InputStreamReader reader = new InputStreamReader(definition.getInputStream());
        @Cleanup BufferedReader bufReader = new BufferedReader(reader);
        String xml = bufReader.lines().collect(StringBuilder::new, StringBuilder::append, StringBuilder::append).toString();
        Optional<FlowDefinitionXmlDocument> docOpt = xmlDocumentRepository.findById(id);
        docOpt.orElseThrow(() -> new DocumentNotFoundException("document with id=" + id + " not exist"));
        docOpt.ifPresent(doc -> {
            doc.setContent(xml);
            xmlDocumentRepository.save(doc);
        });
    }


    /**
     * @param definitionId {String} definition id in which the binding is being created
     * @param taskId       {String} task definition id which is bound to
     * @param commandId    {String} command definition id which is bound
     */
    public void bind(String definitionId, String taskId, String commandId) {

    }

    /**
     * @param id {String} definition id which is being deployed
     */
    @PostMapping("/{id}/deploy")
    public void deploy(@PathVariable("id") String id) throws DocumentNotFoundException {
        Optional<FlowDefinitionXmlDocument> docOpt = xmlDocumentRepository.findById(id);
        docOpt.orElseThrow(() -> new DocumentNotFoundException("Document with id=" + id + "not found"));
        docOpt.ifPresent(doc -> {
            FlowDefinitionXmlParser parser = new FlowDefinitionXmlParser();
            FlowDefinition definition = null;
            try {
                definition = parser.parse(doc);
                definitionRepository.save(definition);
            } catch (JAXBException e) {
                e.printStackTrace();
            }
        });
    }

    @PostMapping("/{id}/binding")
    public void binding(@PathVariable("id") String id, @RequestBody BindingVo binding) {

    }
}
