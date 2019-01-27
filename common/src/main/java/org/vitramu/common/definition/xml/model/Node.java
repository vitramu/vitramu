package org.vitramu.common.definition.xml.model;

import lombok.Getter;
import lombok.ToString;

import javax.xml.bind.annotation.XmlAttribute;

import static org.vitramu.common.definition.xml.model.Model.NS_XMI;

@Getter
@ToString(callSuper = true)
public class Node extends Element {

    protected String type;

    @XmlAttribute(namespace = NS_XMI)
    public void setType(String type) {
        this.type = type;
    }
}
