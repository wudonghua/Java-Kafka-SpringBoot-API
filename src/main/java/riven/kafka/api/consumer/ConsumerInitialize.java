package riven.kafka.api.consumer;

import org.apache.commons.lang3.StringUtils;
import org.apache.kafka.clients.consumer.ConsumerConfig;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.kafka.annotation.EnableKafka;
import org.springframework.kafka.config.ConcurrentKafkaListenerContainerFactory;
import org.springframework.kafka.config.KafkaListenerContainerFactory;
import org.springframework.kafka.core.ConsumerFactory;
import org.springframework.kafka.core.DefaultKafkaConsumerFactory;
import org.springframework.kafka.listener.ConcurrentMessageListenerContainer;
import riven.kafka.api.configuration.ConsumerConfiguration;

import java.util.HashMap;
import java.util.Map;

/**
 * @Author dw07-Riven770[wudonghua@gznb.com]
 * @Date 2017/12/1411:16
 * 配置Consumer选项
 * 初始化consumer_S
 */
@Configuration
@EnableKafka
@EnableConfigurationProperties(ConsumerConfiguration.class)
@ConditionalOnProperty(name = {"Riven.kafka.consumer.bootstrapServers", "Riven.kafka.consumer.groupId"}, matchIfMissing = false)
public class ConsumerInitialize {


    private Logger logger = LoggerFactory.getLogger(this.getClass());

    /**
     * 初始化参数
     *
     * @param config
     * @return
     */
    private Map<String, Object> assembleProducer(ConsumerConfiguration config) {
        Map<String, Object> propsMap = new HashMap<>();
        if (StringUtils.isBlank(config.getBootstrapServers()))
            throw new RuntimeException("缺失kafka集群列表,初始化失败");
        propsMap.put(ConsumerConfig.BOOTSTRAP_SERVERS_CONFIG, config.getBootstrapServers());

        propsMap.put(ConsumerConfig.ENABLE_AUTO_COMMIT_CONFIG, config.getEnableAutoCommit());
        //提交延迟毫秒数
        propsMap.put(ConsumerConfig.AUTO_COMMIT_INTERVAL_MS_CONFIG, config.getAutoCommitIntervalMs());
        //执行超时时间
        propsMap.put(ConsumerConfig.SESSION_TIMEOUT_MS_CONFIG, config.getSessionTimeoutMs());
        propsMap.put(ConsumerConfig.KEY_DESERIALIZER_CLASS_CONFIG, config.getKeySerializer());
        propsMap.put(ConsumerConfig.VALUE_DESERIALIZER_CLASS_CONFIG, config.getValueSerializer());
        propsMap.put(ConsumerConfig.FETCH_MIN_BYTES_CONFIG, config.getFetchMinBytes());
        propsMap.put(ConsumerConfig.MAX_POLL_RECORDS_CONFIG, config.getMaxPollRecords());
        //组ID
        if (StringUtils.isBlank(config.getGroupId()))
            throw new RuntimeException("缺失Consumer组信息,初始化失败");
        propsMap.put(ConsumerConfig.GROUP_ID_CONFIG, config.getGroupId());

        propsMap.put(ConsumerConfig.AUTO_OFFSET_RESET_CONFIG, config.getAutoOffseReset());
        return propsMap;
    }

    @Bean
    public KafkaListenerContainerFactory<ConcurrentMessageListenerContainer<String, String>> kafkaListenerContainerFactory
            (ConsumerConfiguration ver) {
        ConcurrentKafkaListenerContainerFactory<String, String> factory = new ConcurrentKafkaListenerContainerFactory<>();
        try {
            factory.setConsumerFactory(consumerFactory(ver));
            factory.setConcurrency(ver.getConsumerAmount());//启动的consumer个数
            factory.getContainerProperties().setPollTimeout(ver.getPollTimeout());//consumer;连接超时时间ms
            logger.info("初始化Consumer_S完成,共启动 {} 个Consumer", ver.getConsumerAmount());
        } catch (Exception e) {
            logger.info("初始化Consumer_S失败!");
            e.printStackTrace();
        }
        return factory;
    }

    @org.jetbrains.annotations.NotNull
    private ConsumerFactory<String, String> consumerFactory(ConsumerConfiguration ver) {
        return new DefaultKafkaConsumerFactory<>(assembleProducer(ver));
    }

}
