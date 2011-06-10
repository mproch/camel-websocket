package org.apache.camel.component.websocket;

import java.io.IOException;
import java.util.Collection;

import org.apache.camel.Endpoint;
import org.apache.camel.Exchange;
import org.apache.camel.impl.DefaultProducer;

public class WebsocketProducer extends DefaultProducer {

    private WebsocketStore store;

    public WebsocketProducer(Endpoint endpoint) {
        super(endpoint);
    }

    public WebsocketProducer(Endpoint endpoint, WebsocketStore store) {
        super(endpoint);
        this.store = store;
    }

    @Override
    public void process(Exchange exchange) throws Exception {

        String connectionKey = exchange.getIn().getHeader(WebsocketConstants.CONNECTION_KEY, String.class);
        String message = exchange.getIn().getBody(String.class);

        // look for connection key and get Websocket

        if (connectionKey != null) {
            DefaultWebsocket websocket = null;
            websocket = store.get(connectionKey);
            this.sendMessage(websocket, message);
        } else {
            // hack --> should be an attribute

            // XXX - 11.06.2011, LK - message procession is stopped after first exception & exception it self is swallowed
            try {
                Collection<DefaultWebsocket> websockets = store.getAll();
                for (DefaultWebsocket websocket : websockets) {
                    this.sendMessage(websocket, message);
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    void sendMessage(DefaultWebsocket websocket, String message) throws IOException {
        // in case there is web socket and socket connection is open - send message
        if (websocket != null && websocket.getConnection().isOpen()) {
            websocket.getConnection().sendMessage(message);
        }
    }
}
