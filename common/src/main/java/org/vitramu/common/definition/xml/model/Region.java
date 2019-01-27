package org.vitramu.common.definition.xml.model;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import javax.xml.bind.annotation.*;
import java.util.Collection;

@Getter
@ToString(callSuper = true, exclude = {"parent"})
@XmlType
@XmlRootElement(name = "region", namespace = Model.NS_UML)
public class Region extends Node {

    @XmlElements({
            @XmlElement(name = "transition")
    })
    private Collection<Transition> transitions;

    @XmlElements({
            @XmlElement(name = "subvertex")
    })
    private Collection<Subvertex> subvertices;

    @Setter
    private Subvertex parent;

    public boolean hasParent() {
        return null != parent;
    }
}
