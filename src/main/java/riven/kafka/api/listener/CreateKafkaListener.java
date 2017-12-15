package riven.kafka.api.listener;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * @Author dw07-Riven770[wudonghua@gznb.com]
 * @Date 2017/12/1416:04
 * 把KafkaConsumerListener注册到SpringIOC之中
 */
@Configuration
@ConditionalOnProperty(name = {"Riven.kafka.consumer.bootstrapServers","Riven.kafka.consumer.groupId"})
public class CreateKafkaListener {
    private Logger logger = LoggerFactory.getLogger(this.getClass());

    @Bean
    public KafkaListenerInitConfig init() {
        return new KafkaListenerInitConfig();
    }
}
