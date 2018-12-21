package org.vitramu.engine.definition.element;

import org.vitramu.engine.definition.Type;

import java.util.Arrays;
import java.util.Map;
import java.util.function.Function;
import java.util.stream.Collectors;

public enum GatewayType implements Type {
    PARALLEL("PARALLEL"),
    EXCLUSIVE("EXCLUSIVE"),
    INCLUSIVE("INCLUSIVE"),
    JOIN("JOIN")
    ;

    private final String name;
    private GatewayType(String name) {
        this.name = name;
    }
    @Override
    public String getTypeName() {
        return name;
    }

    public GatewayType from(String name) {
        GatewayType[] types = GatewayType.values();
        Map<String, GatewayType> typeMap = Arrays.stream(types).collect(Collectors.toMap(GatewayType::getTypeName, Function.identity()));
        return typeMap.get(name);
    }

}
