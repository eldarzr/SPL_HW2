package bgu.spl.mics;

import bgu.spl.mics.application.messages.TestModelEvent;
import bgu.spl.mics.application.messages.TickBroadcast;
import bgu.spl.mics.application.messages.TrainModelEvent;
import bgu.spl.mics.application.objects.Data;
import bgu.spl.mics.application.objects.Model;
import bgu.spl.mics.application.objects.Student;
import bgu.spl.mics.application.services.StudentService;
import bgu.spl.mics.example.messages.ExampleBroadcast;
import bgu.spl.mics.example.messages.ExampleEvent;
import junit.framework.TestCase;

public class MessageBusTest extends TestCase {

    public static MessageBusImpl messageBus;

    public void setUp() throws Exception {
        super.setUp();
        messageBus = MessageBusImpl.getInstance();
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
        assertFalse(messageBus.isBroadcastSubscribedBy(TickBroadcast.class,microService));
        messageBus.subscribeBroadcast(TickBroadcast.class,microService);
        assertTrue(messageBus.isBroadcastSubscribedBy(TickBroadcast.class,microService));
    }

    public void testComplete() {
        MicroService microService1 = new StudentService("stu");
        messageBus.register(microService1);
        messageBus.subscribeEvent(TestModelEvent.class,microService1);
        Data data = new Data(Data.Type.Text, 0, 10000);
        Student s = new Student("s","cs","MSc",0,0);
        Model m = new Model("a",data,s);
        TestModelEvent e1 = new TestModelEvent(m);
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
        MicroService microService1 = new StudentService("stu");
        MicroService microService2 = new StudentService("stu");
        messageBus.register(microService1);
        messageBus.register(microService2);
        messageBus.subscribeEvent(TrainModelEvent.class,microService1);
        messageBus.subscribeEvent(TrainModelEvent.class,microService2);
        Data data = new Data(Data.Type.Text, 0, 10000);
        Student s = new Student("s","cs","MSc",0,0);
        Model m = new Model("a",data,s);
        TrainModelEvent e1 = new TrainModelEvent(m);
        Future<String> f1 = messageBus.sendEvent(e1);
        assertNotNull(f1);

    }

    public void testRegister() {
        Student s = new Student("s","cs","MSc",0,0);
        MicroService microService = new StudentService("stu",s);
        assertTrue(messageBus.isRegistered(microService));
    }

    public void testUnregister() {
        MicroService microService = new StudentService("stu");
        messageBus.register(microService);
        assertTrue(messageBus.isRegistered(microService));
        messageBus.unregister(microService);
    }

    public void testAwaitMessage() {
        Student s = new Student("s","cs","MSc",0,0);
        MicroService microService = new StudentService("stu",s);
        //Thread t1 = new Thread(microService);
        messageBus.register(microService);
        //t1.start();
        Thread t1 = new Thread(()->{
            try {
                Message msg= messageBus.awaitMessage(microService);
                assertTrue(msg!=null);
            } catch (InterruptedException e) {
                assertTrue(false);
            }});
        t1.start();
    }
}