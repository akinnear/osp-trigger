package com.akinnear.osp.trigger.beans;

import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.integration.channel.DirectChannel;
import org.springframework.integration.config.EnableIntegration;
import org.springframework.integration.dsl.IntegrationFlow;
import org.springframework.integration.dsl.IntegrationFlows;
import org.springframework.integration.ip.udp.UnicastReceivingChannelAdapter;
import org.springframework.integration.transformer.GenericTransformer;
import org.springframework.integration.transformer.ObjectToStringTransformer;
import org.springframework.messaging.MessageChannel;

@Configuration
public class IntegrationConfiguration {
    @Bean
    public IntegrationFlow handleUdpMsg(
            @Qualifier("udpInboundAdapter") UnicastReceivingChannelAdapter udpInboundAdapter,
            @Qualifier("udpMsgTransformer") GenericTransformer<?,?> udpMsgTransformer,
            @Qualifier("udpTransformedInputChannel") MessageChannel udpTransformedInputChannel) {
        return IntegrationFlows
                .from(udpInboundAdapter)
                .transform(udpMsgTransformer)
                .channel(udpTransformedInputChannel)
                .get();
    }

    @Bean
    public GenericTransformer<?,?> udpMsgTransformer() {
        return new ObjectToStringTransformer() {
            @Override
            protected String transformPayload(Object payload) throws Exception {
                return super.transformPayload(payload);
            }
        };
    }

    @Bean
    public MessageChannel udpInputChannel() {
        return new DirectChannel();
    }

    @Bean
    public MessageChannel udpTransformedInputChannel() {
        return new DirectChannel();
    }

    @Bean
    public UnicastReceivingChannelAdapter udpInboundAdapter(@Qualifier("udpInputChannel") MessageChannel channel) {
        UnicastReceivingChannelAdapter unicastReceivingChannelAdapter = new UnicastReceivingChannelAdapter(1111);
        unicastReceivingChannelAdapter.setOutputChannel(channel);
        return unicastReceivingChannelAdapter;
    }
}
