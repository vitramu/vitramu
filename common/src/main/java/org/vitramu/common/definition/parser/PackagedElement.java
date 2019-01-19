package org.vitramu.common.definition.parser;

import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlType;

@XmlType()
@XmlRootElement(name = "packagedElement", namespace = Model.NS_UML)
public class PackagedElement {

    @XmlAttribute
    private String name;

    @XmlAttribute(namespace = Model.NS_XMI)
    private String type;


    @XmlElement
    private Region region;

}
