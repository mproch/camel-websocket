package org.apache.camel.component.websocket;
import static org.junit.Assert.*;
import static org.mockito.Mockito.*;

import java.util.Collection;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.ArgumentCaptor;
import org.mockito.InOrder;
import org.mockito.Mock;
import org.mockito.Spy;
import org.mockito.runners.MockitoJUnitRunner;

/**
 *
 */
@RunWith(MockitoJUnitRunner.class)
public class NodeSynchronizationImplTest {

    private static final String KEY_1 = "one";
    private static final String KEY_2 = "two";
    private static final String KEY_3 = "three";

    @Mock
    private WebsocketConsumer consumer;

    private DefaultWebsocket websocket1;
    private DefaultWebsocket websocket2;

    private NodeSynchronization sync;
    
    private MemoryWebsocketStore store1;
    private MemoryWebsocketStore store2;
    
    /**
     * @throws java.lang.Exception
     */
    @Before
    public void setUp() throws Exception {
    	
    	store1 = new MemoryWebsocketStore();
    	store2 = new MemoryWebsocketStore();

    	websocket1 = new DefaultWebsocket(sync, consumer);
    	websocket1.setConnectionKey(KEY_1);

    	websocket2 = new DefaultWebsocket(sync, consumer);
    	websocket2.setConnectionKey(KEY_2);
    	
        //when(websocket1.getConnectionKey()).thenReturn(KEY_1);
        //when(websocket2.getConnectionKey()).thenReturn(KEY_2);
    }
    
    /**
     * Test method for {@link org.apache.camel.component.websocket.NodeSynchronization#addSocket(org.apache.camel.component.websocket.DefaultWebsocket)}.
     */
    @Test
    public void testAddSocketMemoryAndGlobal() {
    	sync = new NodeSynchronizationImpl(store1, store2);

    	sync.addSocket(websocket1);
    	assertEquals(websocket1, store1.get(KEY_1));
    	assertEquals(store1.get(KEY_1), store2.get(KEY_1));

    	sync.addSocket(websocket2);
    	assertEquals(websocket2, store1.get(KEY_2));
    	assertEquals(store1.get(KEY_2), store2.get(KEY_2));
    }
    
    /**
     * Test method for {@link org.apache.camel.component.websocket.NodeSynchronization#addSocket(org.apache.camel.component.websocket.DefaultWebsocket)}.
     */
    @Test
    public void testAddSocketMemoryOnly() {
    	sync = new NodeSynchronizationImpl(store1);
    	
    	sync.addSocket(websocket1);
    	assertEquals(websocket1, store1.get(KEY_1));
    }
    
    /**
     * Test method for {@link org.apache.camel.component.websocket.NodeSynchronization#addSocket(org.apache.camel.component.websocket.DefaultWebsocket)}.
     */
    @Test
    public void testAddSocketMemoryAndNullGlobal() {
    	sync = new NodeSynchronizationImpl(store1, null);
    	
    	sync.addSocket(websocket1);
    	assertEquals(websocket1, store1.get(KEY_1));
    }
    
    /**
     * Test method for {@link org.apache.camel.component.websocket.NodeSynchronization#addSocket(org.apache.camel.component.websocket.DefaultWebsocket)}.
     */
    @Test(expected = NullPointerException.class)
    public void testAddNullValue() {
        sync.addSocket(null);
    }
    
    /**
     * Test method for {@link org.apache.camel.component.websocket.NodeSynchronization#removeSocket(org.apache.camel.component.websocket.DefaultWebsocket)}.
     */
    @Test
    public void testRemoveDefaultWebsocket() {
    	sync = new NodeSynchronizationImpl(store1, store2);
    	
        // first call of websocket1.getConnectionKey()
    	sync.addSocket(websocket1);
    	assertEquals(websocket1, store1.get(KEY_1));
    	assertEquals(store1.get(KEY_1), store2.get(KEY_1));
    	
    	sync.addSocket(websocket2);
    	assertEquals(websocket2, store1.get(KEY_2));
    	assertEquals(store1.get(KEY_2), store2.get(KEY_2));

        // second call of websocket1.getConnectionKey()
        sync.removeSocket(websocket1);
        assertNull(store1.get(KEY_1));
        assertNull(store2.get(KEY_1));
        
        assertNotNull(store1.get(KEY_2));
        assertNotNull(store2.get(KEY_2));
        
        sync.removeSocket(websocket2);
        assertNull(store1.get(KEY_2));
        assertNull(store2.get(KEY_2));
    }
    
    /**
     * Test method for {@link org.apache.camel.component.websocket.NodeSynchronization#removeSocket(org.apache.camel.component.websocket.DefaultWebsocket)}.
     */
    @Test
    public void testRemoveDefaultWebsocketKeyNotSet() {
    	sync = new NodeSynchronizationImpl(store1);
    	
        // first call of websocket1.getConnectionKey()
    	sync.addSocket(websocket1);
    	assertEquals(websocket1, store1.get(KEY_1));
    	
    	// setConnectionKey(null) after sync.addSocket()- otherwise npe
        websocket1.setConnectionKey(null);
        
        try {
        	// second call of websocket1.getConnectionKey()
            sync.removeSocket(websocket1);
            fail("Exception expected");
        }
        catch (Exception e) {
            assertEquals(NullPointerException.class, e.getClass());
        }
    }
    
    /**
     * Test method for {@link org.apache.camel.component.websocket.NodeSynchronization#removeSocket(org.apache.camel.component.websocket.DefaultWebsocket)}.
     */
    @Test
    public void testRemoveNotExisting() {
    	sync = new NodeSynchronizationImpl(store1);
    	
        // first call of websocket1.getConnectionKey()
    	sync.addSocket(websocket1);
    	assertEquals(websocket1, store1.get(KEY_1));

        assertNull(store1.get(KEY_2));
        sync.removeSocket(websocket2);
        
        assertEquals(websocket1, store1.get(KEY_1));
        assertNull(store1.get(KEY_2));
    }

    /**
     * Test method for {@link org.apache.camel.component.websocket.NodeSynchronization#removeSocket(java.lang.String)}.
     */
    @Test
    public void testRemoveString() {
    	sync = new NodeSynchronizationImpl(store1, store2);
    	
        // first call of websocket1.getConnectionKey()
    	sync.addSocket(websocket1);
    	assertEquals(websocket1, store1.get(KEY_1));
    	assertEquals(store1.get(KEY_1), store2.get(KEY_1));
    	
    	sync.addSocket(websocket2);
    	assertEquals(websocket2, store1.get(KEY_2));
    	assertEquals(store1.get(KEY_2), store2.get(KEY_2));

        // second call of websocket1.getConnectionKey()
        sync.removeSocket(KEY_1);
        assertNull(store1.get(KEY_1));
        assertNull(store2.get(KEY_1));
        
        assertNotNull(store1.get(KEY_2));
        assertNotNull(store2.get(KEY_2));
        
        sync.removeSocket(KEY_2);
        assertNull(store1.get(KEY_2));
        assertNull(store2.get(KEY_2));
    }
    
    /**
     * Test method for {@link org.apache.camel.component.websocket.NodeSynchronization#removeSocket(java.lang.String)}.
     */
    @Test
    public void testRemoveStringNotExisting() {
        
    	sync = new NodeSynchronizationImpl(store1);
    	
        // first call of websocket1.getConnectionKey()
    	sync.addSocket(websocket1);
    	assertEquals(websocket1, store1.get(KEY_1));

        assertNull(store1.get(KEY_3));
        sync.removeSocket(KEY_3);
        
        assertEquals(websocket1, store1.get(KEY_1));
        assertNull(store1.get(KEY_3));
        
    }
	
}
