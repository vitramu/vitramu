package org.vitramu.engine.excution.element;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NonNull;
import org.vitramu.engine.definition.element.StartEventDefinition;
import org.vitramu.engine.excution.AbstractInstance;
import org.vitramu.engine.excution.Instance;


@Builder
@AllArgsConstructor
public class StartEvent  extends AbstractInstance<StartEventDefinition> implements Instance {

    @Getter
    private String flowDefinitionId;
    @Getter
    private String transactionId;
    @Getter
    private String serviceName;
    @Getter
    private String serviceInstanceId;
    @Getter
    private String parentFlowInstanceId;

    // TODO use typed json Object
    private String data;

    public StartEvent() {
    }

}
