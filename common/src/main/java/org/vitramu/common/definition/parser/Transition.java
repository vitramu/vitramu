package org.vitramu.common.definition.parser;

import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlType;

@XmlType()
@XmlRootElement(name = "transition", namespace = Model.NS_UML)
public class Transition {
    @XmlAttribute
    private String name;

    @XmlAttribute(namespace = Model.NS_XMI)
    private String type;

    @XmlAttribute
    private String source;

    @XmlAttribute
    private String target;
}
