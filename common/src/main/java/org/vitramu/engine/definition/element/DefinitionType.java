package org.vitramu.engine.definition.element;

import org.vitramu.engine.definition.Type;

import java.util.Arrays;
import java.util.Map;
import java.util.function.Function;
import java.util.stream.Collectors;

public enum DefinitionType implements Type {

    FLOW("FLOW"),
    COMMAND("COMMAND"),

    START("START"),
    TASK("TASK"),
    FORK("FORK"),
    CHOICE("CHOICE"),
    JOIN("JOIN"),
    SEQUENCE("SEQUENCE"),
    END("END");
    private String name;

    private DefinitionType(String name) {
        this.name = name;
    }

    public DefinitionType from(String name) {
        DefinitionType[] types = DefinitionType.values();
        Map<String, DefinitionType> typeMap = Arrays.stream(types).collect(Collectors.toMap(DefinitionType::getTypeName, Function.identity()));
        return typeMap.get(name);
    }

    @Override
    public String getTypeName() {
        return name;
    }
}
