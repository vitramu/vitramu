package org.vitramu.common.definition.model;

import org.springframework.statemachine.state.PseudoStateKind;

public interface Vertex extends Element {

    PseudoStateKind getKind();


}
