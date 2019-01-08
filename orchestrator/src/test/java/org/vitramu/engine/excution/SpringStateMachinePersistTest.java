package org.vitramu.engine.excution;

import lombok.extern.slf4j.Slf4j;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.statemachine.data.StateMachineRepository;
import org.springframework.statemachine.data.mongodb.MongoDbRepositoryStateMachine;
import org.springframework.test.context.junit4.SpringRunner;

@Slf4j
@RunWith(SpringRunner.class)
@SpringBootTest
public class SpringStateMachinePersistTest {

    @Autowired
    StateMachineRepository<MongoDbRepositoryStateMachine> stateMachineRepository;
    @Test
    public void saveStateMachineInMongo() {
    }
}
