package org.vitramu.engine.excution.message;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;
import org.springframework.lang.Nullable;

import java.util.Map;

@Data
@JsonInclude
public class Payload<T> {

    @Nullable
    @JsonProperty("meta")
    private Map<String,Object> meta;
    @Nullable
    @JsonProperty("data")
    private T data;
}
