package com.akinnear.osp.trigger;

import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.integration.channel.DirectChannel;
import org.springframework.integration.config.EnableIntegration;
import org.springframework.integration.dsl.IntegrationFlow;
import org.springframework.integration.dsl.IntegrationFlows;
import org.springframework.messaging.Message;
import org.springframework.messaging.MessageChannel;
import org.springframework.messaging.MessageHandler;
import org.springframework.messaging.MessagingException;

@Configuration
@EnableIntegration
public class OspTriggerUdpFlowTestConfiguration {
    @Bean
    public MessageChannel testMsgInputChannel() {
        return new DirectChannel();
    }

    @Bean
    public IntegrationFlow inputChannelToUdpInputChannel(
            @Qualifier("udpInputChannel") MessageChannel udpInputChannel) {
        return IntegrationFlows
                .from(testMsgInputChannel())
                .transform(m -> m.toString().getBytes())
                .channel(udpInputChannel)
                .get();
    }

    @Bean
    public StringMessageHandler stringMessageHandler() {
        return new StringMessageHandler();
    }

    @Bean
    public IntegrationFlow testFlow(
            @Qualifier("udpTransformedInputChannel") MessageChannel udpTransformedInputChannel) {
        return IntegrationFlows
                .from(udpTransformedInputChannel)
                .handle(stringMessageHandler())
                .get();
    }

    public static class StringMessageHandler implements MessageHandler {
        private String result = "NOT DEFINED";
        @Override
        public void handleMessage(Message<?> message) throws MessagingException {
            result = (String) message.getPayload();
        }

        public String getResult() {
            return result;
        }
    }
}
