package org.vitramu.engine.excution.service;

import java.util.Map;

public interface FlowDriver {
    void onTaskMessage(String payload, Map<String, Object> headers);

    void onStartMessage(String payload, Map<String, Object> headers);
}
