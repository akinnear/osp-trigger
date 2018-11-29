package com.akinnear.osp.trigger.beans;

import org.junit.Test;
import org.springframework.beans.factory.config.ConfigurableListableBeanFactory;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;
import org.springframework.integration.dsl.IntegrationFlow;
import org.springframework.messaging.Message;
import org.springframework.messaging.MessageChannel;
import org.springframework.messaging.PollableChannel;
import org.springframework.messaging.support.GenericMessage;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

public class IntegrationConfigurationTest {
    @Test
    public void handleUdpMsgFlow_simple() {
        try (AnnotationConfigApplicationContext ctx = new AnnotationConfigApplicationContext(IntegrationConfiguration.class)) {
            // to run the flow must be registered as a bean
            ConfigurableListableBeanFactory beanFactory = ctx.getBeanFactory();
            MessageChannel udpInputChannel = beanFactory.getBean("udpInputChannel", MessageChannel.class);
            // Input goes to the defined channel for the flow
            IntegrationFlow flow = f -> f.channel(udpInputChannel);
            beanFactory.registerSingleton("testFlow", flow);
            beanFactory.initializeBean(flow, "testFlow");

            ctx.start();

            // by registering the flow and starting the context spring auto creates the input message channel as {name}.input
            MessageChannel input = ctx.getBean("testFlow.input", MessageChannel.class);
            // output channel of the flow we are testing
            PollableChannel output = ctx.getBean("udpTransformedInputChannel", PollableChannel.class);

            String msg = "This is the test msg";
            input.send(new GenericMessage<>(msg.getBytes()));
            Message<?> receive = output.receive(10000);
            assertNotNull(receive);
            assertEquals(msg, receive.getPayload());
        }
    }
}
