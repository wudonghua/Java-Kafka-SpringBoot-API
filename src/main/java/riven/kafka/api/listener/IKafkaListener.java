package riven.kafka.api.listener;

import org.apache.kafka.clients.consumer.ConsumerRecord;

/**
 * @Author dw07-Riven770[wudonghua@gznb.com]
 * @Date 2017/12/1414:52
 */
public interface IKafkaListener {
    void listener(ConsumerRecord<?, ?> record);
}
