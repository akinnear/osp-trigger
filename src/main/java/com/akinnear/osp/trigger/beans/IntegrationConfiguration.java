package com.akinnear.osp.trigger.beans;

import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.integration.channel.DirectChannel;
import org.springframework.integration.channel.QueueChannel;
import org.springframework.integration.config.EnableIntegration;
import org.springframework.integration.dsl.IntegrationFlow;
import org.springframework.integration.dsl.IntegrationFlows;
import org.springframework.integration.ip.udp.UnicastReceivingChannelAdapter;
import org.springframework.integration.transformer.GenericTransformer;
import org.springframework.integration.transformer.ObjectToStringTransformer;
import org.springframework.messaging.MessageChannel;
import org.springframework.messaging.PollableChannel;

@Configuration
@EnableIntegration
public class IntegrationConfiguration {
    @Bean
    public IntegrationFlow handleUdpMsgFlow(
            @Qualifier("udpInboundAdapter") UnicastReceivingChannelAdapter udpInboundAdapter,
            @Qualifier("udpMsgTransformer") GenericTransformer<?,?> udpMsgTransformer) {
        return IntegrationFlows.from(udpInboundAdapter)
                .transform(udpMsgTransformer)
                .channel(udpTransformedInputChannel())
                .get();
    }

    @Bean
    public GenericTransformer<?,?> udpMsgTransformer() {
        return new ObjectToStringTransformer();
    }

    @Bean
    public MessageChannel udpInputChannel() {
        return new DirectChannel();
    }

    @Bean
    public PollableChannel udpTransformedInputChannel() {
        return new QueueChannel();
    }

    @Bean
    public UnicastReceivingChannelAdapter udpInboundAdapter(@Value("${osp.trigger.udp.port:1111}") int port) {
        UnicastReceivingChannelAdapter unicastReceivingChannelAdapter = new UnicastReceivingChannelAdapter(port);
        unicastReceivingChannelAdapter.setOutputChannel(udpInputChannel());
        return unicastReceivingChannelAdapter;
    }
}
