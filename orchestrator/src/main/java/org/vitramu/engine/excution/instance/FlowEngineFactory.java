package org.vitramu.engine.excution.instance;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.statemachine.StateMachine;
import org.springframework.statemachine.config.StateMachineFactory;
import org.springframework.stereotype.Component;

import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

/**
 * FlowEngineFactory提供根据flow definition id创建flow engine的能力。
 * 需要封装关于statemachine实例的获取过程。
 * TODO：
 * 1. 支持statemachine实例池
 * 2. 支持从持久化配置数据中动态创建statemachine实例
 */
@Slf4j
@Component
public class FlowEngineFactory {
    private static final int MA_PER_ENGINE = 10;
    private Map<String, List<FlowEngine>> engines = new HashMap<>();
    private StateMachineFactory<String, String> stateMachineFactory;

    @Autowired
    public FlowEngineFactory(StateMachineFactory<String, String> stateMachineFactory) {
        this.stateMachineFactory = stateMachineFactory;
    }

    public FlowEngine getFlowEngine(String definitionId) {
        FlowEngine engine = null;
        List<FlowEngine> flowEngines = engines.get(definitionId);
        if (null != flowEngines && !flowEngines.isEmpty()) {
            engine = flowEngines.remove(0);
        } else {
            StateMachine<String, String> stateMachine = stateMachineFactory.getStateMachine(definitionId);
            stateMachine.addStateListener(new FlowEngineEventListener());
            engine = new FlowEngine(definitionId, stateMachine);
        }
        engine.getStateMachine().start();
        return engine;
    }

    public void destory(FlowEngine engine) {
        synchronized (engines) {
            List<FlowEngine> flowEngines = engines.get(engine.getDefinitionId());
            if (null == flowEngines) {
                flowEngines = new LinkedList<>();
            }
            engine.getStateMachine().stop();
            if (flowEngines.size() < MA_PER_ENGINE) {
                flowEngines.add(engine);
            }
            engines.put(engine.getDefinitionId(), flowEngines);
        }
    }


}
