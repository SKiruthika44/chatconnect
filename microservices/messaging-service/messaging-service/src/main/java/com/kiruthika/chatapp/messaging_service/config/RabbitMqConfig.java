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

import java.nio.file.Watchable;

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
    public Queue messageEditedQueue(){
        return new Queue("ws.message.edited.queue");
    }

    @Bean
    public Queue messageDeletedQueue(){
        return new Queue("ws.delete.for.me.queue");
    }

    @Bean
    public Queue groupCreatedQueue(){
        return new Queue("ws.group.created.queue");
    }
    @Bean
    public Queue emojiCreatedQueue(){
        return new Queue("ws.emoji.created.queue");
    }

    @Bean
    public Queue groupMessageCreatedQueue(){
        return new Queue("ws.group.message.created.queue");
    }

    @Bean
    public Queue groupMessageEditedQueue(){
        return new Queue("ws.group.message.edited.queue");
    }

    @Bean
    public Queue groupMessageSatusUpdatedQueue(){
        return new Queue("ws.group.message.status.queue");
    }

    @Bean
    public Queue groupMessageEmojiCreated(){
        return new Queue("ws.group.message.emoji.created.queue");
    }

    @Bean
    public Queue messageDeletedForEveryoneQueue(){
        return new Queue("ws.delete.for.everyone.queue");
    }

    @Bean
    public Queue groupMessageDeletedForEveryoneQueue(){
        return new Queue("ws.group.message.delete.for.everyone.queue");
    }
    @Bean
    public TopicExchange exchange(){
        return new TopicExchange(exchange);
    }

    @Bean
    public Binding messageCreatedBinding(TopicExchange exchange){
        return BindingBuilder.bind(messageCreatedQueue()).to(exchange).with("message.created");
    }

    @Bean
    public Binding messageDeletedForEveryone(TopicExchange exchange){
        return BindingBuilder.bind(messageDeletedForEveryoneQueue()).to(exchange).with("message.delete.for.everyone");
    }

    @Bean
    public Binding groupMessageEmojiCreatedBinding(TopicExchange exchange){
        return BindingBuilder.bind(groupMessageEmojiCreated()).to(exchange).with("group.message.emoji.created");
    }

    @Bean
    public Binding messageDeleteForMeBinding(TopicExchange exchange){
        return BindingBuilder.bind(messageDeletedQueue()).to(exchange).with("message.delete.for.me");
    }

    @Bean
    public Binding groupMessageEditedBinding(TopicExchange exchange){
        return BindingBuilder.bind(groupMessageEditedQueue()).to(exchange).with("group.message.edited");
    }

    @Bean
    public Binding emojiCreatedBinding(TopicExchange exchange){
        return BindingBuilder.bind(emojiCreatedQueue()).to(exchange).with("message.emoji.created");
    }


    @Bean
    public Binding messageEditedBinding(TopicExchange exchange){
        return BindingBuilder.bind(messageEditedQueue()).to(exchange).with("message.edited");
    }



    @Bean
    public Binding groupMessageCreatedBinding(TopicExchange exchange){
        return BindingBuilder.bind(groupMessageCreatedQueue()).to(exchange).with("group.message.created");
    }

    @Bean
    public Binding groupMessageDeletedForEveryoneBinding(TopicExchange exchange){
        return BindingBuilder.bind(groupMessageDeletedForEveryoneQueue()).to(exchange).with("group.message.delete.for.everyone");
    }

    @Bean
    public Binding groupCreatedBinding(TopicExchange exchange){
        return BindingBuilder.bind(groupCreatedQueue()).to(exchange).with("group.created");
    }

   @Bean
   public Binding messageStatusBinding(TopicExchange exchange){
       return BindingBuilder.bind(messageUpdatedQueue()).to(exchange).with("message.status.*");
   }

    @Bean
    public Binding groupMessageStatusBinding(TopicExchange exchange){
        return BindingBuilder.bind(groupMessageSatusUpdatedQueue()).to(exchange).with("group.message.status.*");
    }

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
