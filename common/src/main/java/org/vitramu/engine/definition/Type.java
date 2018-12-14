package org.vitramu.engine.definition;

import java.util.Arrays;
import java.util.Map;
import java.util.function.Function;
import java.util.stream.Collectors;

public enum Type {

    START("START"),
    TASK("TASK"),
    GATEWAY("GATEWAY"),
    SEQUENCE("SEQUENCE"),
    END("END");
    private String name;
    private Type(String name) {
        this.name = name;
    }

    public String getName() {
        return name;
    }
    public Type from(String name) {
        Type[] types = Type.values();
        Map<String, Type> typeMap = Arrays.stream(types).collect(Collectors.toMap(Type::getName, Function.identity()));
        return typeMap.get(name);
    }
}
