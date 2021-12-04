package bgu.spl.mics;

import bgu.spl.mics.application.services.StudentService;
import junit.framework.TestCase;

public class MessageBusTest extends TestCase {

    public static MessageBusImpl messageBus;

    public void setUp() throws Exception {
        super.setUp();
        messageBus = new MessageBusImpl();
    }

    public void tearDown() throws Exception {
    }

    public void testSubscribeEvent() {


    }

    public void testSubscribeBroadcast() {
    }

    public void testComplete() {
    }

    public void testSendBroadcast() {
    }

    public void testSendEvent() {
    }

    public void testRegister() {
        MicroService microService = new StudentService("stu");
        assertFalse(messageBus.queues.containsKey(microService));
        messageBus.register(microService);
        assertTrue(messageBus.queues.containsKey(microService));
        assertTrue(messageBus.queues.get(microService) != null);
    }

    public void testUnregister() {
        MicroService microService = new StudentService("stu");
        messageBus.register(microService);
        assertTrue(messageBus.queues.containsKey(microService));
        messageBus.unregister(microService);
        assertFalse(messageBus.queues.containsKey(microService));
    }

    public void testAwaitMessage() {
    }
}