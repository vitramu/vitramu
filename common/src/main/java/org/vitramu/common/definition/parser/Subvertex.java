package org.vitramu.common.definition.parser;

import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlType;

@XmlType()
@XmlRootElement(name = "subvertex", namespace = Model.NS_UML)
public class Subvertex {
    @XmlAttribute
    private String name;

    @XmlAttribute(namespace = Model.NS_XMI)
    private String type;

}
