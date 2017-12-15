package riven.kafka.api.producer;

import org.apache.kafka.clients.producer.RecordMetadata;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.kafka.support.ProducerListener;
import org.springframework.util.ObjectUtils;

/**
 * @Author dw07-Riven770[wudonghua@gznb.com]
 * @Date 2017/12/1411:05
 * simple implements interface {@link ProducerListener} to logging producer send result info
 * 做Producer发送消息给kafka之前和之后的一些记录
 */
public class SimpleProducerListener implements ProducerListener<String,String> {

    private static final Logger logger = LoggerFactory.getLogger(SimpleProducerListener.class);

    private int maxContentLogged = 500;

    /**
     * Invoked after the successful send of a message (that is, after it has been acknowledged by the broker).
     * @param topic the destination topic
     * @param partition the destination partition (could be null)
     * @param key the key of the outbound message
     * @param value the payload of the outbound message
     * @param recordMetadata the result of the successful send operation
     */
    @Override
    public void onSuccess(String topic, Integer partition, String key, String value, RecordMetadata recordMetadata) {
        StringBuilder logOutput = new StringBuilder();
        logOutput.append("消息发送成功! \n");
        logOutput.append(" with key=【").append(toDisplayString(ObjectUtils.nullSafeToString(key), this.maxContentLogged)).append("】");
        logOutput.append(" and value=【").append(toDisplayString(ObjectUtils.nullSafeToString(value), this.maxContentLogged)).append("】");
        logOutput.append(" to topic 【").append(topic).append("】");
        String[] resultArr = recordMetadata.toString().split("@");
        logOutput.append(" send result: topicPartition【").append(resultArr[0]).append("】 offset 【").append(resultArr[1]).append("】");
        logger.info(logOutput.toString());
    }

    /**
     * Invoked after an attempt to send a message has failed.
     * @param topic the destination topic
     * @param partition the destination partition (could be null)
     * @param key the key of the outbound message
     * @param value the payload of the outbound message
     * @param exception the exception thrown
     */
    @Override
    public void onError(String topic, Integer partition, String key, String value, Exception exception) {
        StringBuilder logOutput = new StringBuilder();
        logOutput.append("消息发送失败!\n");
        logOutput.append("Exception thrown when sending a message");
        logOutput.append(" with key=【").append(toDisplayString(ObjectUtils.nullSafeToString(key), this.maxContentLogged)).append("】");
        logOutput.append(" and value=【").append(toDisplayString(ObjectUtils.nullSafeToString(value), this.maxContentLogged)).append("】");
        logOutput.append(" to topic 【").append(topic).append("】");
        if (partition != null) {
            logOutput.append(" and partition 【" + partition + "】");
        }
        logOutput.append(":");
        logger.error(logOutput.toString(), exception);
    }

    /**
     * Return true if this listener is interested in success as well as failure.
     * @return true to express interest in successful sends.
     */
    @Override
    public boolean isInterestedInSuccess() {
        return true;
    }

    private String toDisplayString(String original, int maxCharacters) {
        if (original.length() <= maxCharacters) {
            return original;
        }
        return original.substring(0, maxCharacters) + "...";
    }
}
