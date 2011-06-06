/**
 * 
 */
package org.apache.camel.component.websocket;

import static org.junit.Assert.*;

import java.util.HashMap;
import java.util.Map;

import org.apache.camel.Endpoint;
import org.eclipse.jetty.server.Server;
import org.eclipse.jetty.servlet.ServletContextHandler;
import org.junit.Before;
import org.junit.Test;

/**
 *
 */
public class WebsocketComponentTest {

    private WebsocketComponent component;

    /**
     * @throws java.lang.Exception
     */
    @Before
    public void setUp() throws Exception {
        component = new WebsocketComponent();
    }

    /**
     * Test method for {@link org.apache.camel.component.websocket.WebsocketComponent#createContext()}.
     */
    @Test
    public void testCreateContext() {
        ServletContextHandler handler = component.createContext();
        assertNotNull(handler);
    }

    /**
     * Test method for {@link org.apache.camel.component.websocket.WebsocketComponent#createServer(org.eclipse.jetty.servlet.ServletContextHandler, java.lang.String, int, java.lang.String)}.
     */
    @Test
    public void testCreateServerWithoutStaticContent() {
        ServletContextHandler handler = component.createContext();
        Server server = component.createServer(handler, "localhost", 1988, null);
        assertEquals(1, server.getConnectors().length);
        assertEquals("localhost", server.getConnectors()[0].getHost());
        assertEquals(1988, server.getConnectors()[0].getPort());
        assertFalse(server.getConnectors()[0].isStarted());
        assertEquals(handler, server.getHandler());
        assertEquals(1, server.getHandlers().length);
        assertEquals(handler, server.getHandlers()[0]);
        assertEquals("/", handler.getContextPath());
        assertNotNull(handler.getSessionHandler());
        assertNull(handler.getResourceBase());
        assertNull(handler.getServletHandler().getHolderEntry("/"));
    }

    /**
     * Test method for {@link org.apache.camel.component.websocket.WebsocketComponent#createServer(org.eclipse.jetty.servlet.ServletContextHandler, java.lang.String, int, java.lang.String)}.
     */
    @Test
    public void testCreateServerWithStaticContent() {
        ServletContextHandler handler = component.createContext();
        Server server = component.createServer(handler, "localhost", 1988, "public/");
        assertEquals(1, server.getConnectors().length);
        assertEquals("localhost", server.getConnectors()[0].getHost());
        assertEquals(1988, server.getConnectors()[0].getPort());
        assertFalse(server.getConnectors()[0].isStarted());
        assertEquals(handler, server.getHandler());
        assertEquals(1, server.getHandlers().length);
        assertEquals(handler, server.getHandlers()[0]);
        assertEquals("/", handler.getContextPath());
        assertNotNull(handler.getSessionHandler());
        assertNotNull(handler.getResourceBase());
        assertTrue(handler.getResourceBase().endsWith("public"));
        assertNotNull(handler.getServletHandler().getHolderEntry("/"));
    }

    /**
     * Test method for {@link org.apache.camel.component.websocket.WebsocketComponent#createEndpoint(String, String, java.util.Map)}.
     */
    @Test
    public void testCreateEndpoint() throws Exception {
        Map<String, Object> parameters = new HashMap<String, Object>();
        Endpoint e1 = component.createEndpoint("websocket://foo", "foo", parameters);
        Endpoint e2 = component.createEndpoint("websocket://foo", "foo", parameters);
        Endpoint e3 = component.createEndpoint("websocket://bar", "bar", parameters);
        assertNotNull(e1);
        assertNotNull(e1);
        assertNotNull(e1);
        assertEquals(e1, e2);
        assertNotSame(e1, e3);
        assertNotSame(e2, e3);
    }
}
