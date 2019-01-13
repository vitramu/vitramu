package org.vitramu.engine.excution.instance;

import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.rabbit.connection.ConnectionFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

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


    public FlowEngineFactory() {

    }

}
