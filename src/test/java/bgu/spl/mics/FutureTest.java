package bgu.spl.mics;

import junit.framework.TestCase;

import java.util.concurrent.TimeUnit;

public class FutureTest extends TestCase {

    public static Future<String> future1;

    public void setUp() throws Exception {
        super.setUp();
        future1 = new Future<>();
    }

    public void tearDown() throws Exception {
    }

    public void testGet() {
        Future<String> future2= new Future<>();
        future2.resolve("done");
        assertTrue(future2.isDone());
        assertTrue(future2.get().equals("done"));

        Thread thread1 = new Thread(()->
        {
            String ret = future1.get();
            assertTrue(future1.isDone());
            assertTrue(ret.equals("done"));
        });
        Thread thread2 = new Thread(()->
        {
            try {
                Thread.sleep(500);
                future1.resolve("done");
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        });

        thread1.start();
        thread2.start();

    }

    public void testResolve() {
        assertFalse(future1.isDone());
        future1.resolve("done");
        assertTrue(future1.isDone());
    }

    public void testIsDone() {
        assertFalse(future1.isDone());
        future1.resolve("done");
        assertTrue(future1.isDone());
    }

    public void testTestGet() {
        Future<String> future2= new Future<>();
        future2.resolve("done");
        assertTrue(future2.isDone());
        assertTrue(future2.get(1000, TimeUnit.MILLISECONDS).equals("done"));

        Thread thread1 = new Thread(()->
        {
            String ret = future1.get(10000,TimeUnit.MILLISECONDS);
            assertTrue(future1.isDone());
            assertTrue(ret.equals("done"));
        });
        Thread thread2 = new Thread(()->
        {
            try {
                Thread.sleep(100);
                future1.resolve("done");
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        });
        thread1.start();
        thread2.start();

        Future<String> future3 = new Future<>();

        Thread thread3 = new Thread(()->
        {
            String ret = future3.get(100,TimeUnit.MILLISECONDS);
            assertFalse(future3.isDone());
        });
        Thread thread4 = new Thread(()->
        {
            try {
                Thread.sleep(10000);
                future3.resolve("done");
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        });
        thread3.start();
        thread4.start();

    }
}