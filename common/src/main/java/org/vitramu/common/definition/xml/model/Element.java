package org.vitramu.common.definition.xml.model;

import lombok.Getter;
import lombok.ToString;

import javax.xml.bind.annotation.XmlAttribute;

import static org.vitramu.common.definition.xml.model.Model.NS_XMI;

@Getter
@ToString
public class Element {
    protected String id;
    protected String name;

    @XmlAttribute(namespace = NS_XMI)
    public void setId(String id) {
        this.id = id;
    }

    @XmlAttribute
    public void setName(String name) {
        this.name = name;
    }
}

