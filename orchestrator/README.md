# 执行引擎

## 消息监听
执行引擎运行过程监听两种类型的消息：
- StartEvent
- TaskEvent

StartEvent用来触发创建新的Flow Instance，然后交给执行引擎进行执行。  
TaskEvent用来触发重建运行中的Flow Instance，然后交给执行引擎进行执行。  
执行引擎执行一个Flow Instance的过程是按照Flow Definition以及Flow Instance执行上下文触发Flow Instance进行状态转移的过程。  


## 实例化过程

1. StartEventListener接到StartEvent，触发实例化过程
2. StartEvent.getFlowDefinitionId获取Flow Definition ID
3. FlowDefinitionRepository.findFlowDefinitionById从数据库中加载出完整的FlowDefinition对象
4. 一个FlowDefinition是一个Aggregate对象，拥有一个StartEvent Entity，一个TaskDefinition Entity List，一个Sequence Entity List，一个Gateway Entity List，一个EndEvent Entity
5. FlowInstanceBuilder根据FlowDefinition和StartEvent进行FlowInstance的实例化
6. StartEvent.getTransactionId获取本次请求的全局Transaction ID，并传递给FlowInstanceBuilder
7. FlowInstanceBuilde新建一个FlowInstance对象，以全局Transaction ID作为FlowInstance的instanceId
8. FlowInstanceBuilder把FlowDefinition作为新建的FlowInstance对象的definition属性
9. FlowInstanceBuilder返回FlowInstance对象，实例化过程结束

## 执行过程

### Start过程
1. StartEventListener接到StartEvent
2. 通过StartEvent.getTransactionId获取StartEvent的全局Transaction ID
3. FlowHistoryService.isFlowInstanceStarted判断Transaction ID对应的FlowInstance是否已经启动
3.a 若已经启动，直接返回启动
3.b 若没有启动，执行实例化过程
4. 实例化过程结束，获得FlowInstance对象
5. FlowInstance.findSequenceBySource(StartEvent)获取一个Sequence对象s
6. 获取s.targetType
    1. 若s.targetType是Task
        1. FlowInstance生成一个Task Instance ID
        2. 执行Task.excute(TaskInstanceId, Excution)
    2. 若s.targetType是Gateway gw
        1. FlowInstace.findSequenceBySource(Gateway)获取一个List<Sequence>对象
        2. 若gw.type是Parallel
            1. 遍历列表，每一个Sequence作为s执行STEP 6
        3. 若gw.type是Exclusive
            1. 遍历列表
            2. 以使FlowInstace.Excution.calculate(Sequence)计算结果为true的Sequence对象为s，执行STEP6
            3. 若没有使FlowInstace.Excution.calculate(Sequence)计算结果为true的Sequence对象，则以default值为true的Sequence为s，执行STEP6
            4. 进行一轮标记过程
        4. 若gw.type是Inclusive
            1. 遍历列表
            2. 以使FlowInstace.Excution.calculate(Sequence)计算结果为true的每一个Sequence对象为s，执行STEP6
            3. 若没有使FlowInstace.Excution.calculate(Sequence)计算结果为true的Sequence对象，则以default值为true的Sequence为s，执行STEP6
            4. 进行一轮标记过程
        5. 若gw.type是Join
            1. FlowInstance.findSequenceByTarget(Gateway)获取一个List<Sequence>对象
            2. 若所有Sequence均被标记为BLACK，gw标记为BLACK，FlowInstace.findSequenceBySource(Gateway)获取一个List<Sequence>的每一个Sequence也标记为BLACK
            3. 若存在Sequence被标记为RED或未标记，gw检查此类Sequence的excuted是否为true
            4. 若全部为true，gw继续进行
            5. 若存在false，gw等待执行或标记
            
### TaskEvent处理过程
       