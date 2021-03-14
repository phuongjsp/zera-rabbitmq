package sapo.phuoghv.rabbitmq1.configuration;

import org.springframework.amqp.core.*;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.amqp.support.converter.Jackson2JsonMessageConverter;
import org.springframework.amqp.support.converter.MessageConverter;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.amqp.rabbit.connection.ConnectionFactory;

import java.util.HashMap;
import java.util.Map;

@Configuration
public class BeanConfig {

    @Value("${sapo.rabbitmq.queue.ghtk}")
    private String GHTK_QUEUE;
    @Value("${sapo.rabbitmq.queue.se}")
    private String SE_QUEUE;
    @Value("${sapo.rabbitmq.exchange.create-order}")
    private String createOrderExchange;
    @Bean
    public Queue queueGHTK() {
        Map<String, Object> args = new HashMap<String, Object>();
        args.put("x-max-priority", 1);
        return new Queue(GHTK_QUEUE,true,false,false, args);
    }
    @Bean
    public Queue queueGHTKLog() {
        Map<String, Object> args = new HashMap<String, Object>();
        args.put("x-max-priority", 2);
        return new Queue(GHTK_QUEUE + "log",true,false,false, args);
    }

    @Bean
    public Queue queueSE() {
        Map<String, Object> args = new HashMap<String, Object>();
        args.put("x-max-priority", 3);
        return new Queue(SE_QUEUE,true,false,false, args);
    }
    @Bean
    public Queue queueSELog() {
        Map<String, Object> args = new HashMap<String, Object>();
        args.put("x-max-priority", 4);
        return new Queue(SE_QUEUE + "log",true,false,false, args);
    }

    @Bean
    public TopicExchange createOrderExchange() {
        return new TopicExchange(createOrderExchange);
    }

    @Bean
    public Binding bindingGHTK(@Qualifier("queueGHTK") Queue queue, @Qualifier("createOrderExchange") TopicExchange exchange) {
        return BindingBuilder.bind(queue).to(exchange).with("ghtk");
    }

    @Bean
    public Binding bindingSE(@Qualifier("queueSE") Queue queue, @Qualifier("createOrderExchange") TopicExchange exchange) {
        return BindingBuilder.bind(queue).to(exchange).with("se");
    }
    @Bean
    public Binding bindingGHTKLog(@Qualifier("queueGHTKLog") Queue queue, @Qualifier("createOrderExchange") TopicExchange exchange) {
        return BindingBuilder.bind(queue).to(exchange).with("ghtk");
    }

    @Bean
    public Binding bindingSELog(@Qualifier("queueSELog") Queue queue, @Qualifier("createOrderExchange") TopicExchange exchange) {
        return BindingBuilder.bind(queue).to(exchange).with("se");
    }

    @Bean
    public MessageConverter jsonMessageConverter() {
        return new Jackson2JsonMessageConverter();
    }

    @Bean
    public RabbitTemplate rabbitTemplate(ConnectionFactory connectionFactory) {
        final RabbitTemplate rabbitTemplate = new RabbitTemplate(connectionFactory);
        rabbitTemplate.setMessageConverter(jsonMessageConverter());
        return rabbitTemplate;
    }

}
