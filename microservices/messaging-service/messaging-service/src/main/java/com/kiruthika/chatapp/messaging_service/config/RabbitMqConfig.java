package com.kiruthika.chatapp.messaging_service.config;


import com.rabbitmq.client.AMQP;
import jakarta.annotation.PostConstruct;
import lombok.AllArgsConstructor;
import org.springframework.amqp.core.*;
import org.springframework.amqp.rabbit.config.SimpleRabbitListenerContainerFactory;
import org.springframework.amqp.rabbit.connection.ConnectionFactory;
import org.springframework.amqp.rabbit.core.RabbitAdmin;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.amqp.support.converter.JacksonJsonMessageConverter;
import org.springframework.amqp.support.converter.MessageConverter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.ApplicationRunner;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration

public class RabbitMqConfig {

    private static final String queue1="ws.queue";
    private static final String exchange="chat.exchange";

    private static final String routingKey="message.created";


    @Bean
    public Queue messageCreatedQueue(){
        return new Queue("ws.message.created.queue");
    }

    @Bean
    public Queue messageUpdatedQueue(){
        return new Queue("ws.message.status.queue");
    }



    @Bean
    public TopicExchange exchange(){
        return new TopicExchange(exchange);
    }

    @Bean
    public Binding messageCreatedBinding(TopicExchange exchange){
        return BindingBuilder.bind(messageCreatedQueue()).to(exchange).with("message.created");
    }

   /* @Bean
    public RabbitTemplate rabbitTemplate(
            ConnectionFactory connectionFactory
    ) {
        return new RabbitTemplate(connectionFactory);
    }
*/
   @Bean
   public Binding messageStatusBinding(TopicExchange exchange){
       return BindingBuilder.bind(messageUpdatedQueue()).to(exchange).with("message.status.*");
   }
    /*@Bean
    public MessageConverter messageConverter() {
        return new JacksonJsonMessageConverter();
    }

    @Bean(name = "rabbitListenerContainerFactory")
    public SimpleRabbitListenerContainerFactory
    rabbitListenerContainerFactory(
            ConnectionFactory connectionFactory,
            MessageConverter messageConverter
    ) {
        SimpleRabbitListenerContainerFactory factory =
                new SimpleRabbitListenerContainerFactory();

        factory.setConnectionFactory(connectionFactory);
        factory.setMessageConverter(messageConverter);

        return factory;
    }

    @Bean
    public RabbitTemplate rabbitTemplate(
            ConnectionFactory connectionFactory,
            MessageConverter messageConverter
    ) {
        RabbitTemplate template =
                new RabbitTemplate(connectionFactory);

        template.setMessageConverter(messageConverter);

        return template;
    }*/

    @Bean
    public MessageConverter messageConverter() {
        return new JacksonJsonMessageConverter();
    }

    @Bean
    public SimpleRabbitListenerContainerFactory
    rabbitListenerContainerFactory(
            ConnectionFactory connectionFactory,
            MessageConverter messageConverter
    ) {
        SimpleRabbitListenerContainerFactory factory =
                new SimpleRabbitListenerContainerFactory();

        factory.setConnectionFactory(connectionFactory);
        factory.setMessageConverter(messageConverter);

        return factory;
    }

    @Bean
    public RabbitTemplate rabbitTemplate(
            ConnectionFactory connectionFactory,
            MessageConverter messageConverter
    ) {
        RabbitTemplate template =
                new RabbitTemplate(connectionFactory);

        template.setMessageConverter(messageConverter);

        return template;
    }




    @Bean
    public RabbitAdmin rabbitAdmin(ConnectionFactory connectionFactory) {
        RabbitAdmin admin= new RabbitAdmin(connectionFactory);
        admin.setAutoStartup(true);
        return admin;
    }


    @PostConstruct
    public void init() {
        System.out.println("RabbitMqConfig LOADED");
        //rabbitTemplate.convertAndSend("chat.exchange","message.status.updated","sent");
    }

    @Bean
    public CommandLineRunner checkQueues(ApplicationContext ctx) {
        return args -> {
            System.out.println("---- Checking Queue Beans ----");
            System.out.println(ctx.getBeansOfType(org.springframework.amqp.core.Queue.class));
        };
    }

    @Bean
    public CommandLineRunner checkConnectionFactory(ApplicationContext ctx) {
        return args -> {
            System.out.println(
                    "ConnectionFactory beans: " +
                            ctx.getBeansOfType(
                                    org.springframework.amqp.rabbit.connection.ConnectionFactory.class
                            )
            );
        };
    }

    @Bean
    public ApplicationRunner declareQueues(RabbitAdmin rabbitAdmin) {
        return args -> {
            System.out.println("Forcing Rabbit declarations...");
            rabbitAdmin.initialize();
        };
    }

}
