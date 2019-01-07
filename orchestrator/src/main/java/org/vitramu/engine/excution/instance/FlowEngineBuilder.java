package org.vitramu.engine.excution.instance;

import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.rabbit.connection.ConnectionFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.statemachine.StateMachine;
import org.springframework.stereotype.Component;
import org.vitramu.engine.definition.Definition;
import org.vitramu.engine.definition.element.FlowDefinition;
import org.vitramu.engine.excution.instance.statemachine.FlowStateMachine;

/**
 * FlowEngineFactory提供根据flow definition id创建flow engine的能力。
 * 需要封装关于statemachine实例的获取过程。
 * TODO：
 * 1. 支持statemachine实例池
 * 2. 支持从持久化配置数据中动态创建statemachine实例
 */
@Slf4j
@Component
public class FlowEngineBuilder {


    public FlowEngineBuilder() {

    }

    @Autowired
    private ConnectionFactory connectionFactory;

    /**
     * every time build method is invoked, a new statemachine instance will be returned.
     *
     * @param definition
     * @return
     */
    public StateMachine<Definition, String> build(FlowDefinition definition) {
        //  TODO build a new Statemachine according definition
        StateMachine<Definition, String> sm = FlowStateMachine.newStateMachineInstance(connectionFactory);
        return sm;
    }
}
