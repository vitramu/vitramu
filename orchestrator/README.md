# Orchestrator

# 执行引擎
执行引擎使用statemachine作为流程驱动核心。
需要解决如下问题：
- 根据新建的流程模型创建新的statemachine实例
- 事务请求分派到不同流程的statemachine实例进行处理
- 同一个statemachine实例处理同样的事务请求时进行复用
- 同一个statemachine实例对并发的事务请求处理进行隔离
- 已完成的事务请求的statemachine运行时信息的持久化
- 支持事务请求重放
- 执行引擎和下游服务直接的消息协调模型

# 流程模型定义

定义过程可以大致分为如下阶段：
- 流程设计人员设计流程模型，以json文件或xml文件的格式进行提交（初步支持json格式，方便rest接口化）
- 流程开发者将流程模型中的task和microservice实现的command进行绑定
- 流程开发者开发部署实现了task的microservice
- 流程开发者部署流程

## 设计阶段
这一阶段需要解决的问题：
- 一个良定义的json格式对模型进行描述（优先级高）
- 一个定义提交接口（优先级高）
- 一个UI设计器

## 绑定阶段
这一阶段需要建立流程模型和微服务的实现之间的映射关系，解决的问题：
- 微服务实现的复用

## 部署流程阶段
部署阶段需要完成的任务：
- 解析提交的流程模型定义，使用statemachine builder动态创建流程定义模型
- 使用statemachine持久化机制，将state，transition等信息存储在DB中
 
## 部署微服务阶段
这一阶段需要结合服务注册完成服务metadata的上报过程。
这些metadata需要用来解决如下问题：
- 协助orchestrator下发command到service
- service执行command后发送回复消息，包括执行成功的回复和执行失败的回复两种情况
- 协助orchestrator下发abort command到service
- 建立service instance和orchestrator之间的reply channel，协助orchestrator返回reply消息

# 执行过程定义
## 基于状态机的抽象
- state：流程运行阶段的抽象
- compsite state：子流程的抽象
- event：微服务执行命令结果的抽象
- transition：流程运行阶段流转过程的抽象
- fork pseudo state：并行任务split point的抽象
- join pseudo state：并行子流程结束的synchronize point的抽象
- choice pseduo state：分支流程decision point的抽象
- end：流程结束过程，事务返回的抽象

## 消息监听
执行引擎运行过程监听两种类型的消息：
- StartMessage：触发创建新的FlowInstance
- TaskMessage：重建运行中的FlowInstance，向下一个状态进行转移

### StartMessage
一个StartMessage由微服务组装，通过消息中间件发送到orchestrator，包含如下信息：
- flowDefinitionId：选择流程
- flowInstanceId：本次事务的全局transaction id
- parentFlowInstanceId：本次事务的父级全局transaction id，顶级事务的parentFlowInstanceId为null，否则一定时某个流程实例的某个中间状态的instanceId
- serviceName
- serviceInstanceId
- body：输入流程的数据（高级特性，TBD）

### TaskMessage
一个TaskMessage由微服务组装，通过消息中间件发送到orchestrator，包含如下信息：
- flowDefinitionId：选择流程
- flowInstanceId：本次事务的全局transaction id
- taskName：执行的task name，唯一性
- taskInstanceId：本次task执行的local transaction id
- aborted： 执行过程是否有异常，task执行异常会被abort
- body：执行结果

### FlowInstance实例化过程

- event listener接到StartMessage
- 提取flowDefinitionId，生成flowInstanceId
- 以flowDefinitionId为machineId获取statemachine实例
- 新获取statemachine实例处于初始状态
- 向新获取的statemachine实例发送start事件
- 以flowInstanceId为key，保存statemachine的context
- 还原statemachine到初始状态

### TaskMessage处理过程

- event listener接到TaskMessage
- 提取flowDefinitionId和flowInstanceId，生成taskInstanceId
- 以flowDefinitionId为machineId获取statemachine实例
- 恢复statemachine实例至flowInstanceId为key的context
- 根据TaskMessage构建event
- 向statemachine发送event
- 以flowInstanceId为key，保存statemachine的context
- 还原statemachine到初始状态

