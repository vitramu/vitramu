package org.vitramu.common.definition.parser;

import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlType;

@XmlRootElement(name = "Model", namespace = Model.NS_UML)
@XmlType()
public class Model {

    static final String NS_UML = "http://www.eclipse.org/uml2/5.0.0/UML";
    static final String NS_XMI = "http://www.omg.org/spec/XMI/20131001";
    @XmlAttribute
    private String name;
    @XmlElement
    private PackagedElement packagedElement;

}
