package org.vitramu.engine.excution.driver;

import org.springframework.amqp.core.Message;

public interface FlowDriver {
    void onMessage(Message message);
}
