package com.gnss.web.configuration;

import com.gnss.mqutil.configuration.BaseConfiguration;
import com.gnss.mqutil.constants.MqConstants;
import com.gnss.web.common.constant.RabbitConstants;
import org.springframework.amqp.core.Binding;
import org.springframework.amqp.core.BindingBuilder;
import org.springframework.amqp.core.Queue;
import org.springframework.amqp.core.TopicExchange;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * <p>Description: 中间件配置</p>
 * <p>Company: www.gps-pro.cn</p>
 *
 * @author menghuan
 * @version 1.0.1
 * @date 2018-1-18
 */
@Configuration
public class MiddlewareConfiguration extends BaseConfiguration {

    @Bean
    public Queue upCommandQueue() {
        return new Queue(RabbitConstants.UP_COMMAND_QUEUE);
    }

    @Bean
    public TopicExchange upCommandExchange() {
        return new TopicExchange(MqConstants.UP_COMMAND_EXCHANGE);
    }

    @Bean
    public TopicExchange downCommandExchange() {
        return new TopicExchange(MqConstants.DOWN_COMMAND_EXCHANGE);
    }

    @Bean
    Binding bindingUpCommandExchange(Queue upCommandQueue, TopicExchange upCommandExchange) {
        return BindingBuilder.bind(upCommandQueue).to(upCommandExchange).with(MqConstants.UP_COMMAND_ROUTING_KEY);
    }

}