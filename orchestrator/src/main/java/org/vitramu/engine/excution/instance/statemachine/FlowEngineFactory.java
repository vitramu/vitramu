package org.vitramu.engine.excution.instance.statemachine;

import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.rabbit.connection.ConnectionFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.statemachine.StateMachine;
import org.springframework.statemachine.StateMachineContext;
import org.springframework.statemachine.StateMachinePersist;
import org.springframework.statemachine.persist.AbstractStateMachinePersister;
import org.springframework.stereotype.Component;
import org.vitramu.engine.definition.Definition;

import java.util.HashMap;

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

    public static class InMemoryStateMachinePersister<T> extends AbstractStateMachinePersister<T, String, String> {
        public InMemoryStateMachinePersister() {
            super(new InMemoryStateMachinePersist<T>());
        }
    }

    public static class InMemoryStateMachinePersist<T> implements StateMachinePersist<T, String, String> {

        private final HashMap<String, StateMachineContext<T, String>> contexts = new HashMap<>();

        @Override
        public void write(StateMachineContext<T, String> context, String contextObj) throws Exception {
            contexts.put(contextObj, context);
        }

        @Override
        public StateMachineContext<T, String> read(String contextObj) throws Exception {
            return contexts.get(contextObj);
        }
    }

    public static InMemoryStateMachinePersister<Definition> persister = new InMemoryStateMachinePersister<>();

    public FlowEngineFactory() {

    }

    @Autowired
    private ConnectionFactory connectionFactory;

    public StateMachine<Definition, String> build(String definitionId) {
        StateMachine<Definition, String> sm = FlowStateMachine.newStateMachineInstance(connectionFactory);
        sm.start();
        return sm;
    }
}
