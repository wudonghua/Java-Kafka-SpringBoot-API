java-kafka-SpringBoot-API


该工程是为了能够让使用者在最小程度的去了解kafka集群极其使用方式的情况下去使用kafka,配置和使用都十分简单
也许你只需要配置好集群地址即可.
# 依赖
```xml
<dependency>
			<groupId>org.apache.commons</groupId>
			<artifactId>commons-lang3</artifactId>
			<version>3.6</version>
</dependency>

<dependency>
			<groupId>javassist</groupId>
			<artifactId>javassist</artifactId>
			<version>3.11.0.GA</version>
</dependency>

<dependency>
			<groupId>org.springframework.kafka</groupId>
			<artifactId>spring-kafka</artifactId>
</dependency>

<dependency>
			<groupId>org.jetbrains</groupId>
			<artifactId>annotations</artifactId>
			<version>15.0</version>
</dependency>
		
```
#Producer模块

#配置yml文件
```java
Riven:
  kafka:
   producer:
    bootstrapServers: 服务器列表 #必填
    retries: 99
    acks: 接受策略
    batchSize: 99
    lingerMs: 99
    bufferMemory: 99
    keySerializer: org.apache.kafka.common.serialization.StringSerializer
    valueSerializer: org.apache.kafka.common.serialization.StringSerializer
```


# Usage
```java
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;

/**
 * @Author dw07-Riven770[wudonghua@gznb.com]
 * @Date 2017/12/1417:37
 */
@Component
@EnableScheduling
public class Producer {
    @Autowired
    private KafkaTemplate kafkaTemplate;

    @Scheduled(fixedDelay = 6000)
    private void sendMsg(){
        kafkaTemplate.send("topic-test", "hello"+ LocalDateTime.now().toString());
    }
}
    
```







# Consumer模块
## yml 配置
```xml
Riven:
  kafka:
    consumer:
    bootstrapServers: 服务器列表 #必填
    enableAutoCommit: true
    autoCommitIntervalMs: 99
    sessionTimeoutMs: 99
    fetchMinBytes: 99
    maxPollRecords: 99
    groupId: java #必填
    autoOffseReset: latest
    keySerializer: org.apache.kafka.common.serialization.StringDeserializer
    valueSerializer: org.apache.kafka.common.serialization.StringDeserializer
    consumerAmount: 99
    PollTimeout: 99
    topics[0]: test_group #必填
```
## User
```java
import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import riven.kafka.api.listener.IKafkaListener;

import java.util.Optional;

/**
 * @Author dw07-Riven770[wudonghua@gznb.com]
 * @Date 2017/12/1417:37
 */
@Component
public class Consumer implements IKafkaListener{

    @Autowired
    private TestBean testBean;

    @Override
    public void listener(ConsumerRecord<?, ?> record) {
        Optional<?> value = Optional.ofNullable(record.value());
        String s = (String) value.get();
        System.out.println("消费者"+s);
        testBean.test();
    }
}
```
# 建议和完善
问题、BUG可以在issue中提问，feature可以pull request。

