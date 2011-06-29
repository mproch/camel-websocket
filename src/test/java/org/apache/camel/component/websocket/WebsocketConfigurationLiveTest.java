package org.apache.camel.component.websocket;

import org.apache.camel.builder.RouteBuilder;
import org.apache.camel.test.junit4.CamelTestSupport;
import org.junit.Test;

public class WebsocketConfigurationLiveTest extends CamelTestSupport {

    @Test
    public void testWebsocketCallWithGlobalStore() throws Exception {
        //LOG.info("*** open URL http://localhost:1989 and start chating ***");
        Thread.sleep(1 * 60 * 1000);
    }

    @Override
    protected RouteBuilder createRouteBuilder() throws Exception {
        return new RouteBuilder() {
            public void configure() {
                
                WebsocketComponent component = getContext().getComponent("websocket", WebsocketComponent.class);
                component.setHost("localhost");
                component.setPort(1989);
                component.setStaticResources("src/test/resources");

                from("websocket://foo?globalStore=org.apache.camel.component.websocket.WebsocketConfigurationTestMemStoreTestImpl")
                    .log("${body}")
                    .setHeader(WebsocketConstants.SEND_TO_ALL, constant(true))
                    .to("websocket://foo");
            }
        };
    }
}