package me.shouheng.common;

import com.rabbitmq.client.*;
import lombok.extern.slf4j.Slf4j;
import org.junit.Test;

import java.io.IOException;
import java.util.concurrent.TimeoutException;

/**
 * 测试 Rabbit MQ 生成和消费，还需要了解更多关于 Rabbit MQ 的细节
 * 从效果上面来看，Rabbit MQ 的作用是通过中间的消息管理将生产者和消费者独立开来
 * 这样生产者只负责生成，消费者只负责消费，而生产者和消费者可以根据需要进行拓展
 *
 * @author shouh, 2019/4/6-19:27
 */
@Slf4j
public class RabbitMQTest {

    private static final String QUEUE_NAME = "rabbitMQ.test";

    @Test
    public void testProducer() throws IOException, TimeoutException {
        // 创建连接工厂
        ConnectionFactory factory = new ConnectionFactory();
        // 设置 RabbitMQ 相关信息
        factory.setHost("localhost");
        // factory.setUsername("lp");
        // factory.setPassword("");
        // factory.setPort(2088);
        // 创建一个新的连接
        Connection connection = factory.newConnection();
        // 创建一个通道
        Channel channel = connection.createChannel();
        // 声明一个队列
        // channel.queueDeclare(QUEUE_NAME, false, false, false, null);
        String message = "Hello Rabbit MQ";
        // 发送消息到队列中
        channel.basicPublish("", QUEUE_NAME, null, message.getBytes("UTF-8"));
        log.debug("Producer Send +'{}'", message);
        // 关闭通道和连接
        channel.close();
        connection.close();
    }

    @Test
    public void testConsumer() throws IOException, TimeoutException, InterruptedException {
        // 创建连接工厂
        ConnectionFactory factory = new ConnectionFactory();
        // 设置RabbitMQ地址
        factory.setHost("localhost");
        // 创建一个新的连接
        Connection connection = factory.newConnection();
        // 创建一个通道
        Channel channel = connection.createChannel();
        // 声明要关注的队列
        channel.queueDeclare(QUEUE_NAME, false, false, true, null);
        log.debug("Customer Waiting Received messages");
        // DefaultConsumer类实现了Consumer接口，通过传入一个频道，
        // 告诉服务器我们需要那个频道的消息，如果频道中有消息，就会执行回调函数handleDelivery
        Consumer consumer = new DefaultConsumer(channel) {
            @Override
            public void handleDelivery(String consumerTag, Envelope envelope,
                                       AMQP.BasicProperties properties, byte[] body) throws IOException {
                String message = new String(body, "UTF-8");
                log.debug("Customer Received '{}'", message);
            }
        };
        // 自动回复队列应答 -- RabbitMQ中的消息确认机制
        channel.basicConsume(QUEUE_NAME, true, consumer);
        Thread.sleep(10000);
    }
}
