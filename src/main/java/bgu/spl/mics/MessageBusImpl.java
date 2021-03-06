package bgu.spl.mics;

import bgu.spl.mics.application.messages.CancelBroadcast;
import bgu.spl.mics.application.objects.Cluster;
import bgu.spl.mics.example.ServiceCreator;

import java.util.*;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentLinkedQueue;

/**
 * The {@link MessageBusImpl class is the implementation of the MessageBus interface.
 * Write your implementation here!
 * Only private fields and methods can be added to this class.
 */
public class MessageBusImpl implements MessageBus {

	private ConcurrentHashMap<MicroService , ConcurrentLinkedQueue<Message>> queues = new ConcurrentHashMap<>();
	private ConcurrentHashMap<Class<? extends Event>,ConcurrentLinkedQueue<MicroService>> eventServices;
	private ConcurrentHashMap<Class<? extends Broadcast>,List<MicroService>> broadcastServices;
	int nextRobinGpu;

	private static class MessageBusImplHolder{
		private static MessageBusImpl instance = new MessageBusImpl();
	}

	/**
	 * Retrieves the single instance of this class.
	 */
	public static MessageBusImpl getInstance() {
		//TODO: Implement this
		return MessageBusImpl.MessageBusImplHolder.instance;
	}

	private MessageBusImpl(){
		queues = new ConcurrentHashMap<>();
		eventServices = new ConcurrentHashMap<>();
		broadcastServices = new ConcurrentHashMap<>();
	}

	@Override
	public <T> void subscribeEvent(Class<? extends Event<T>> type, MicroService m) {
		// TODO Auto-generated method stub
		if(!eventServices.containsKey(type))
			eventServices.put(type, new ConcurrentLinkedQueue<>());
		eventServices.get(type).add(m);
	}

	@Override
	public void subscribeBroadcast(Class<? extends Broadcast> type, MicroService m) {
		// TODO Auto-generated method stub
		if(!broadcastServices.containsKey(type))
			broadcastServices.put(type, new ArrayList<>());
		broadcastServices.get(type).add(m);

	}

	@Override
	public <T> void complete(Event<T> e, T result) {
		e.getFuture().resolve(result);
		// TODO Auto-generated method stub

	}

	@Override
	public void sendBroadcast(Broadcast b) {
		// TODO Auto-generated method stub
		List<MicroService> _allb = broadcastServices.get(b.getClass());
			for (MicroService microService : _allb) {
				ConcurrentLinkedQueue<Message> queue = queues.get(microService);
				if (queue != null)
					synchronized (queue) {
						queue.add(b);
						queue.notifyAll();
					}
			}
	}

	
	@Override
	public <T> Future<T> sendEvent(Event<T> e) {
		// TODO Auto-generated method stub
			Queue<MicroService> ms = eventServices.get(e.getClass());
			MicroService m;
			//System.out.println("333333333333333333");
			if (ms != null && !ms.isEmpty()) {
				synchronized (ms) {
					m = ms.remove();
					ms.add(m);
					System.out.println(m.getName());
				}
				ConcurrentLinkedQueue<Message> queue = queues.get(m);
				if(queue != null) {
					synchronized (queue) {
						queue.add(e);
						queue.notifyAll();
					}
				}
				return e.getFuture();
			}
		return null;
	}

	@Override
	/**
	 *@PRE: queues.containsKey(m)==false
	 * @POST: queues.containsKey(m)==true
	 */
	public void register(MicroService m) {
		// TODO Auto-generated method stub
		queues.put(m,new ConcurrentLinkedQueue<>());
	}

	@Override
	/**@PRE: queues.containsKey(m)==true
	* @POST: queues.containsKey(m)==false
	 */
	public void unregister(MicroService m) {
		// TODO Auto-generated method stub
		for (ConcurrentLinkedQueue q : eventServices.values())
			if(q.contains(m)) {
				q.remove(m);
			}
	}

	@Override
	public boolean isRegistered(MicroService m) {
		// TODO Auto-generated method stub
		return queues.containsKey(m);
	}

	@Override
	public boolean isEventSubscribedBy(Class<? extends Event> type, MicroService m) {
		// TODO Auto-generated method stub
		if(eventServices.containsKey(type))
			if(eventServices.get(type).contains(m))
				return true;
		return false;
	}

	@Override
	public boolean isBroadcastSubscribedBy(Class<? extends Broadcast> type, MicroService m) {
		// TODO Auto-generated method stub
		if(broadcastServices.containsKey(type))
			if(broadcastServices.get(type).contains(m))
				return true;
		return false;
	}

	@Override
	public Message awaitMessage(MicroService m) throws InterruptedException {
		// TODO Auto-generated method stub
		ConcurrentLinkedQueue<Message> queue = queues.get(m);
		synchronized (queue){
			while (queue.isEmpty())
				queue.wait();
			return queue.remove();
		}
		//return null;
	}

}
