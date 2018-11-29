package com.akinnear.osp.trigger;

import com.akinnear.osp.trigger.beans.IntegrationConfiguration;
import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.messaging.MessageChannel;
import org.springframework.messaging.support.GenericMessage;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringRunner;

@RunWith(SpringRunner.class)
@ContextConfiguration(classes = {IntegrationConfiguration.class, OspTriggerUdpFlowTestConfiguration.class})
public class OspTriggerUdpFlowTest {
    @Autowired
    @Qualifier("testMsgInputChannel")
    MessageChannel testMsgInputChannel;

    @Autowired
    OspTriggerUdpFlowTestConfiguration.StringMessageHandler stringMessageHandler;

    @Test
    public void testMockMessageHandler() {
        String msg = "This is the test msg";
        testMsgInputChannel.send(new GenericMessage<>(msg));
        Assert.assertEquals(msg, stringMessageHandler.getResult());
    }
}
