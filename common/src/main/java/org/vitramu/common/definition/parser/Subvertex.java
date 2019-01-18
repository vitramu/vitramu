package org.vitramu.common.definition.parser;

import javax.xml.bind.annotation.*;
import java.util.Collection;

@XmlType()
@XmlRootElement(name = "subvertex", namespace = Model.NS_UML)
public class Subvertex {
    @XmlAttribute
    private String name;

    @XmlAttribute(namespace = Model.NS_XMI)
    private String type;

    @XmlElements({
            @XmlElement(name = "region")
    })
    private Collection<Region> regions;

}
