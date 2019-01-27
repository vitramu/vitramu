package org.vitramu.common.definition.xml.model;

import lombok.Getter;

import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlType;

@Getter
@XmlType()
@XmlRootElement(name = "transition", namespace = Model.NS_UML)
public class Transition extends Node {


    @XmlAttribute
    private String source;

    @XmlAttribute
    private String target;


}
