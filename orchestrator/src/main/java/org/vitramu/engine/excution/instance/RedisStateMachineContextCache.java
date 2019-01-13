package org.vitramu.engine.excution.instance;

import com.esotericsoftware.kryo.Kryo;
import com.esotericsoftware.kryo.io.Input;
import com.esotericsoftware.kryo.io.Output;
import org.springframework.data.redis.core.RedisOperations;
import org.springframework.messaging.MessageHeaders;
import org.springframework.statemachine.StateMachineContext;
import org.springframework.statemachine.StateMachineContextRepository;
import org.springframework.statemachine.StateMachinePersist;
import org.springframework.statemachine.kryo.MessageHeadersSerializer;
import org.springframework.statemachine.kryo.StateMachineContextSerializer;
import org.springframework.statemachine.kryo.UUIDSerializer;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.util.UUID;

public class RedisStateMachineContextCache implements StateMachineContextRepository<String, String, StateMachineContext<String, String>>, StateMachinePersist<String,String, String> {
    private static final ThreadLocal<Kryo> kryoThreadLocal = ThreadLocal.withInitial(() -> {
        Kryo kryo = new Kryo();
        kryo.addDefaultSerializer(StateMachineContext.class, new StateMachineContextSerializer());
        kryo.addDefaultSerializer(MessageHeaders.class, new MessageHeadersSerializer());
        kryo.addDefaultSerializer(UUID.class, new UUIDSerializer());
        return kryo;
    });

    protected final RedisOperations<String, byte[]> redisOperations;

    /**
     * Instantiates a new redis state machine context repository.
     *
     */
    public RedisStateMachineContextCache(RedisOperations<String, byte[]> redisOperations) {
        this.redisOperations = redisOperations;
    }

    @Override
    public void save(StateMachineContext<String, String> context, String id) {
        redisOperations.opsForValue().set(id, serialize(context));
    }

    @Override
    public StateMachineContext<String, String> getContext(String id) {
        return deserialize(redisOperations.opsForValue().get(id));
    }

    public boolean hasKey(String id) {
        return redisOperations.hasKey(id);
    }


    protected byte[] serialize(StateMachineContext<String, String> context) {
        Kryo kryo = kryoThreadLocal.get();
        ByteArrayOutputStream out = new ByteArrayOutputStream();
        Output output = new Output(out);
        kryo.writeObject(output, context);
        output.close();
        return out.toByteArray();
    }

    @SuppressWarnings("unchecked")
    protected StateMachineContext<String, String> deserialize(byte[] data) {
        if (data == null || data.length == 0) {
            return null;
        }
        Kryo kryo = kryoThreadLocal.get();
        ByteArrayInputStream in = new ByteArrayInputStream(data);
        Input input = new Input(in);
        return kryo.readObject(input, StateMachineContext.class);
    }

    @Override
    public void write(StateMachineContext context, String contextObj) throws Exception {
        save(context,contextObj);
    }

    @Override
    public StateMachineContext<String, String> read(String contextObj) throws Exception {
        return getContext(contextObj);
    }
}
