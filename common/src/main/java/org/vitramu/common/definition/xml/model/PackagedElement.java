package org.vitramu.common.definition.xml.model;

import lombok.Getter;
import lombok.ToString;

import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlType;

@Getter
@ToString(callSuper = true)
@XmlType
@XmlRootElement(name = "packagedElement", namespace = Model.NS_UML)
public class PackagedElement extends Node{

    @XmlElement
    private Region region;

}
