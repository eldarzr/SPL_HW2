package bgu.spl.mics;

import bgu.spl.mics.application.messages.TrainModelEvent;
import bgu.spl.mics.application.objects.Model;
import bgu.spl.mics.application.services.StudentService;
import bgu.spl.mics.example.messages.ExampleBroadcast;
import bgu.spl.mics.example.messages.ExampleEvent;
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
        MicroService microService = new StudentService("stu");
        messageBus.register(microService);
        assertFalse(messageBus.isEventSubscribedBy(TrainModelEvent.class,microService));
        messageBus.subscribeEvent(TrainModelEvent.class,microService);
        assertTrue(messageBus.isEventSubscribedBy(TrainModelEvent.class,microService));
    }

    public void testSubscribeBroadcast() {
        MicroService microService = new StudentService("stu");
        messageBus.register(microService);
        assertFalse(messageBus.isBroadcastSubscribedBy(ExampleBroadcast.class,microService));
        messageBus.subscribeBroadcast(ExampleBroadcast.class,microService);
        assertTrue(messageBus.isBroadcastSubscribedBy(ExampleBroadcast.class,microService));
    }

    public void testComplete() {
        MicroService microService1 = new StudentService("stu");
        messageBus.register(microService1);
        messageBus.subscribeEvent(ExampleEvent.class,microService1);
        ExampleEvent e1 = new ExampleEvent("aaa");
        Future<String> f1 = messageBus.sendEvent(e1);
        messageBus.complete(e1,"done");
        assertTrue(f1.isDone());
        assertTrue(f1.get().equals("done"));
    }

    public void testSendBroadcast() {
        MicroService microService = new StudentService("stu");
        messageBus.register(microService);
        messageBus.subscribeBroadcast(ExampleBroadcast.class,microService);
        ExampleBroadcast b = new ExampleBroadcast("aaa");
        messageBus.sendBroadcast(b);
        try {
            assertTrue(messageBus.awaitMessage(microService) == b);
        }
        catch (Exception e){
            assertTrue(false);
        }
    }

    public void testSendEvent() {
        assertTrue(messageBus.sendEvent(new ExampleEvent("1")) == null);
        MicroService microService1 = new StudentService("stu");
        MicroService microService2 = new StudentService("stu");
        messageBus.register(microService1);
        messageBus.register(microService2);
        messageBus.subscribeEvent(ExampleEvent.class,microService1);
        messageBus.subscribeEvent(ExampleEvent.class,microService2);
        ExampleEvent e1 = new ExampleEvent("aaa");
        ExampleEvent e2 = new ExampleEvent("bbb");
        Future<String> f1 = messageBus.sendEvent(e1);
        assertTrue(f1 != null);
        messageBus.sendEvent(e2);
        try {
            assertTrue(messageBus.awaitMessage(microService1) == e1);
            assertTrue(messageBus.awaitMessage(microService2) == e2);
        }
        catch (Exception e){
            assertTrue(false);
        }

    }

    public void testRegister() {
        MicroService microService = new StudentService("stu");
        assertFalse(messageBus.isRegistered(microService));
        messageBus.register(microService);
        assertTrue(messageBus.isRegistered(microService));
    }

    public void testUnregister() {
        MicroService microService = new StudentService("stu");
        messageBus.register(microService);
        assertTrue(messageBus.isRegistered(microService));
        messageBus.unregister(microService);
        assertFalse(messageBus.isRegistered(microService));
    }

    public void testAwaitMessage() {
        MicroService microService = new StudentService("stu");
        messageBus.register(microService);
        messageBus.subscribeEvent(ExampleEvent.class,microService);
        try {
            messageBus.awaitMessage(microService);
        } catch (InterruptedException e) {}
    }
}