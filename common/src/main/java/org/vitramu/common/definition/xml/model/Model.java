package org.vitramu.common.definition.xml.model;

import lombok.Getter;
import lombok.ToString;

import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlType;
import java.util.Optional;

@XmlRootElement(name = "Model", namespace = Model.NS_UML)
@XmlType
@Getter
@ToString(callSuper = true)
public class Model extends Element {

    public static final String NS_UML = "http://www.eclipse.org/uml2/5.0.0/UML";
    public static final String NS_XMI = "http://www.omg.org/spec/XMI/20131001";

    @XmlElement
    private PackagedElement packagedElement;

    public Region getRegion() {
        return Optional.ofNullable(packagedElement).map(PackagedElement::getRegion).orElse(null);
    }

}
