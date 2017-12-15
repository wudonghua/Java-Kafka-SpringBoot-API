package riven.kafka.api.producer;

import org.apache.commons.lang3.StringUtils;
import org.apache.kafka.clients.producer.ProducerConfig;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.autoconfigure.condition.ConditionalOnClass;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.kafka.annotation.EnableKafka;
import org.springframework.kafka.core.DefaultKafkaProducerFactory;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.kafka.core.ProducerFactory;
import riven.kafka.api.configuration.ProducerConfiguration;

import java.util.HashMap;
import java.util.Map;

/**
 * @Author dw07-Riven770[wudonghua@gznb.com]
 * @Date 2017/12/1314:21
 *
 */
@Configuration
@ConditionalOnClass({KafkaTemplate.class})
@ConditionalOnProperty(name = "Riven.kafka.producer.bootstrapServers", matchIfMissing = false)//某一个值存在着才初始化和个BEAN
@EnableConfigurationProperties(ProducerConfiguration.class)//检查ConfigurationProperties注解标记的配置类是否初始化
@EnableKafka
public class ProducerInitialize {
    private Logger logger = LoggerFactory.getLogger(this.getClass());

    /**
     * 初始化producer参数
     *
     * @param config 参数
     * @return 初始化map
     */
    private Map<String, Object> assembleProducer(ProducerConfiguration config) {
        Map<String, Object> props = new HashMap<>();
        if (StringUtils.isNoneBlank(config.getBootstrapServers()))
            props.put(ProducerConfig.BOOTSTRAP_SERVERS_CONFIG, config.getBootstrapServers());
        if (StringUtils.isNoneBlank(config.getAcks()))
            props.put(ProducerConfig.ACKS_CONFIG, config.getAcks());
        props.put(ProducerConfig.RETRIES_CONFIG, config.getRetries());
        props.put(ProducerConfig.BATCH_SIZE_CONFIG, config.getBatchSize());
        props.put(ProducerConfig.LINGER_MS_CONFIG, config.getLingerMs());
        props.put(ProducerConfig.BUFFER_MEMORY_CONFIG, config.getBufferMemory());
        props.put(ProducerConfig.KEY_SERIALIZER_CLASS_CONFIG, config.getKeySerializer());
        props.put(ProducerConfig.VALUE_SERIALIZER_CLASS_CONFIG, config.getKeySerializer());
        return props;
    }

    private ProducerFactory<String, String> producerFactory(ProducerConfiguration config) {
        return new DefaultKafkaProducerFactory<>(assembleProducer(config));
    }

    @Bean
    public KafkaTemplate<String, String> kafkaTemplate(ProducerConfiguration config) {
        KafkaTemplate<String, String> stringStringKafkaTemplate = new KafkaTemplate<>(producerFactory(config));
        stringStringKafkaTemplate.setProducerListener(new SimpleProducerListener());
        logger.info("kafka Producer 初始化完成");
        return stringStringKafkaTemplate;
    }
}
