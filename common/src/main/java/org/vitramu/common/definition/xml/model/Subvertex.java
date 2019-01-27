package org.vitramu.common.definition.xml.model;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import org.springframework.statemachine.state.PseudoStateKind;
import org.springframework.util.StringUtils;

import javax.xml.bind.annotation.*;
import java.util.*;

import static org.springframework.statemachine.state.PseudoStateKind.*;

@Getter
@ToString(callSuper = true, exclude = {"parent"})
@XmlType()
@XmlRootElement(name = "subvertex", namespace = Model.NS_UML)
public class Subvertex extends Node {
    public static Map<String, PseudoStateKind> KIND_MAP = new HashMap<>(PseudoStateKind.values().length * 2);

    static {
        KIND_MAP.put("fork", FORK);
        KIND_MAP.put("join", JOIN);
        KIND_MAP.put("choice", CHOICE);
        KIND_MAP.put("end", END);
    }

    @XmlElements({
            @XmlElement(name = "region")
    })
    private Collection<Region> regions;

    @Setter
    private Region parent;

    private String kind;


    @Setter
    private boolean zeroIngoing = false;

    @XmlAttribute
    public void setKind(String kind) {
        this.kind = kind;
    }

    public Optional<Subvertex> getParentSubvertex() {
        return Optional.ofNullable(parent).map(Region::getParent);
    }

    public static final Set<String> PSEUDO_STATE_TYPE = new HashSet<>(Arrays.asList("uml:FinalState", "uml:Pseudostate"));

    public PseudoStateKind getPseudoStateKind() {
        if (PSEUDO_STATE_TYPE.contains(type) && !StringUtils.isEmpty(kind)) {
            return KIND_MAP.get(kind);
        }
        return null;
    }

    public Optional<Collection<Region>> getRegions() {
        return Optional.ofNullable(regions);
    }
}
