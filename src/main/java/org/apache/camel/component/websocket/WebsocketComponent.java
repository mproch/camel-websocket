/**
 * Licensed to the Apache Software Foundation (ASF) under one or more
 * contributor license agreements.  See the NOTICE file distributed with
 * this work for additional information regarding copyright ownership.
 * The ASF licenses this file to You under the Apache License, Version 2.0
 * (the "License"); you may not use this file except in compliance with
 * the License.  You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package org.apache.camel.component.websocket;

import java.util.Map;

import org.apache.camel.Endpoint;
import org.apache.camel.impl.DefaultComponent;
import org.eclipse.jetty.server.Server;
import org.eclipse.jetty.servlet.ServletContextHandler;
import org.eclipse.jetty.servlet.ServletHolder;

public class WebsocketComponent extends DefaultComponent {

	private ServletContextHandler context;
	private Server server;
	
	public WebsocketComponent() {
		
		try {
			this.startServer();
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	@Override
	/**
	 * uri --> websocket://foo?storeImplementationClass=org.apache.camel.hazelcast.HazelcastWebsocketStore&storeName=foo
	 */
	protected Endpoint createEndpoint(String uri, String remaining,
			Map<String, Object> parameters) throws Exception {
		
		return  new WebsocketEndpoint(uri, this, remaining);
	}
	
	protected void startServer() throws Exception {
        this.server = new Server(9292);
        
        this.context = new ServletContextHandler(ServletContextHandler.SESSIONS);
        context.setContextPath("/");
 
        server.setHandler(context);
 
        server.start();
	}
	
	public void addServlet(WebsocketStore store, WebsocketConsumer consumer, String remaining) {
		this.context.addServlet(new ServletHolder(new WebsocketComponentServlet(store, consumer)), String.format("/%s/*", remaining));
	}

}
