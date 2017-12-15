package riven.kafka.api.configuration;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.apache.kafka.common.serialization.StringDeserializer;
import java.util.List;

/**
 * @Author dw07-Riven770[wudonghua@gznb.com]
 * @Date 2017/12/1315:58
 */
@ConfigurationProperties(prefix = "Riven.kafka.consumer")
public class ConsumerConfiguration {
    //kafka服务器列表
    private String bootstrapServers;

    /**
     * 如果设置成true,偏移量由auto.commit.interval.ms控制自动提交的频率。
     * <p>
     * 如果设置成false,不需要定时的提交offset，可以自己控制offset，当消息认为已消费过了，这个时候再去提交它们的偏移量。
     * 这个很有用的，当消费的消息结合了一些处理逻辑，这个消息就不应该认为是已经消费的，直到它完成了整个处理。
     */
    private Boolean enableAutoCommit = false;

    /**
     * 提交延迟毫秒数
     */
    private int autoCommitIntervalMs = 100;

    /**
     * 执行超时时间
     */
    private int sessionTimeoutMs = 15000;

    /**
     * 每次最少拉取多少数据
     */
    private int fetchMinBytes = 1;

    /**
     * 在单次调用中的最大返回
     */
    private int maxPollRecords = 300;

    /**
     * 该Consumer属于的组
     */
    private String groupId ;

    /**
     * 在consumter端配置文件中(或者是ConsumerConfig类参数)有个"autooffset.reset"(在kafka 0.8版本中为auto.offset.reset),
     * 有2个合法的值"largest"/"smallest",默认为"largest",此配置参数表示当此groupId下的消费者,在ZK中没有offset值时(比如新的groupId,或者是zk数据被清空),
     * consumer应该从哪个offset开始消费.largest表示接受接收最大的offset(即最新消息),smallest表示最小offset,即从topic的开始位置消费所有消息.
     */
    private String autoOffseReset = "latest";

    /**
     * 同一个组下 启动几个consumer来获取kafka的消息
     */
    private int consumerAmount = 3;

    /**
     * 设置启动的consumer多久超时
     */
    private int pollTimeout = 5000;

    private List<String> topics;

    private String keySerializer = StringDeserializer.class.getName();
    private String valueSerializer = StringDeserializer.class.getName();

    public String getBootstrapServers() {
        return bootstrapServers;
    }

    public void setBootstrapServers(String bootstrapServers) {
        this.bootstrapServers = bootstrapServers;
    }

    public Boolean getEnableAutoCommit() {
        return enableAutoCommit;
    }

    public void setEnableAutoCommit(Boolean enableAutoCommit) {
        this.enableAutoCommit = enableAutoCommit;
    }

    public int getAutoCommitIntervalMs() {
        return autoCommitIntervalMs;
    }

    public void setAutoCommitIntervalMs(int autoCommitIntervalMs) {
        this.autoCommitIntervalMs = autoCommitIntervalMs;
    }

    public int getSessionTimeoutMs() {
        return sessionTimeoutMs;
    }

    public void setSessionTimeoutMs(int sessionTimeoutMs) {
        this.sessionTimeoutMs = sessionTimeoutMs;
    }

    public int getFetchMinBytes() {
        return fetchMinBytes;
    }

    public void setFetchMinBytes(int fetchMinBytes) {
        this.fetchMinBytes = fetchMinBytes;
    }

    public int getMaxPollRecords() {
        return maxPollRecords;
    }

    public void setMaxPollRecords(int maxPollRecords) {
        this.maxPollRecords = maxPollRecords;
    }

    public String getGroupId() {
        return groupId;
    }

    public void setGroupId(String groupId) {
        this.groupId = groupId;
    }

    public String getAutoOffseReset() {
        return autoOffseReset;
    }

    public void setAutoOffseReset(String autoOffseReset) {
        this.autoOffseReset = autoOffseReset;
    }

    public int getConsumerAmount() {
        return consumerAmount;
    }

    public void setConsumerAmount(int consumerAmount) {
        this.consumerAmount = consumerAmount;
    }

    public int getPollTimeout() {
        return pollTimeout;
    }

    public void setPollTimeout(int pollTimeout) {
        this.pollTimeout = pollTimeout;
    }

    public List<String> getTopics() {
        return topics;
    }

    public void setTopics(List<String> topics) {
        this.topics = topics;
    }

    public String getKeySerializer() {
        return keySerializer;
    }

    public void setKeySerializer(String keySerializer) {
        this.keySerializer = keySerializer;
    }

    public String getValueSerializer() {
        return valueSerializer;
    }

    public void setValueSerializer(String valueSerializer) {
        this.valueSerializer = valueSerializer;
    }

    @Override
    public String toString() {
        return "ConsumerConfiguration{" +
                "bootstrapServers='" + bootstrapServers + '\'' +
                ", enableAutoCommit=" + enableAutoCommit +
                ", autoCommitIntervalMs=" + autoCommitIntervalMs +
                ", sessionTimeoutMs=" + sessionTimeoutMs +
                ", fetchMinBytes=" + fetchMinBytes +
                ", maxPollRecords=" + maxPollRecords +
                ", groupId='" + groupId + '\'' +
                ", autoOffseReset='" + autoOffseReset + '\'' +
                ", consumerAmount=" + consumerAmount +
                ", pollTimeout=" + pollTimeout +
                ", topics=" + topics +
                ", keySerializer='" + keySerializer + '\'' +
                ", valueSerializer='" + valueSerializer + '\'' +
                '}';
    }
}
