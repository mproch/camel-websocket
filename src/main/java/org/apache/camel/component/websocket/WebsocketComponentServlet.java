package org.apache.camel.component.websocket;

import java.io.IOException;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.eclipse.jetty.websocket.WebSocket;
import org.eclipse.jetty.websocket.WebSocketServlet;

public class WebsocketComponentServlet extends WebSocketServlet {
	
	private static final long serialVersionUID = -1864704532811123602L;
	private WebsocketStore store;
	private WebsocketConsumer consumer;
	
	public WebsocketComponentServlet(WebsocketStore store, WebsocketConsumer consumer) {
		this.store = store;
		this.consumer = consumer;
	}
	
	@Override
	protected void doGet(HttpServletRequest request,
			HttpServletResponse response) throws ServletException, IOException {
		
		getServletContext().getNamedDispatcher("default").forward(request,
				response);
	}

	@Override
	public WebSocket doWebSocketConnect(HttpServletRequest request,
			String protocol) {
		return new DefaultWebsocket(store, consumer);
	}

}
