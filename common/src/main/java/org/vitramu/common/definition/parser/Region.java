package org.vitramu.common.definition.parser;

import javax.xml.bind.annotation.*;
import java.util.Collection;

@XmlType()
@XmlRootElement(name = "region", namespace = Model.NS_UML)
public class Region {
    @XmlAttribute
    private String name;

    @XmlAttribute(namespace = Model.NS_XMI)
    private String type;

    @XmlElements({
            @XmlElement(name = "transition")
    })
    private Collection<Transition> transitions;

    @XmlElements({
            @XmlElement(name = "subvertex")
    })
    private Collection<Subvertex> subvertices;

}
