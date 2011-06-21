package org.apache.camel.component.websocket;

import org.apache.camel.Consumer;
import org.apache.camel.Processor;
import org.apache.camel.Producer;
import org.apache.camel.impl.DefaultEndpoint;

public class WebsocketEndpoint extends DefaultEndpoint {

	
	// Todo: Change to Options
    private NodeSynchronization sync = new NodeSynchronizationImpl(new MemoryWebsocketStore(), new MemoryWebsocketStore());
    
    private String remaining;

	private WebsocketStore store;

    public WebsocketEndpoint() {

    }
    
    public WebsocketEndpoint (String uri, WebsocketComponent component, String remaining) {
        super(uri, component);
        this.remaining = remaining;
        
        this.store = new MemoryWebsocketStore(); 
        // TODO: init globalStore
        this.sync = new NodeSynchronizationImpl(this.store, null);
        
    }

    @Override
    public Consumer createConsumer(Processor processor) throws Exception {

        // init consumer
        WebsocketConsumer consumer = new WebsocketConsumer(this, processor);

        // register servlet
        ((WebsocketComponent) super.getComponent()).addServlet(sync, consumer, this.remaining);

        return consumer;
    }

    @Override
    public Producer createProducer() throws Exception {

        // register servlet without consumer
        ((WebsocketComponent) super.getComponent()).addServlet(sync, null, this.remaining);

        return new WebsocketProducer(this, this.store);
    }

    @Override
    public boolean isSingleton() {
        return true;
    }

    // TODO --> implement store factory
}
