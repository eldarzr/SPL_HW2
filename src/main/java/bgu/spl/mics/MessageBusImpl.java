package bgu.spl.mics;

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
		// TODO Auto-generated method stub

	}

	@Override
	public void sendBroadcast(Broadcast b) {
		// TODO Auto-generated method stub
		List<MicroService> _allb = broadcastServices.get(b.getClass());
		for (MicroService microService : _allb) {
			queues.get(microService).add(b);
		}
	}

	
	@Override
	public <T> Future<T> sendEvent(Event<T> e) {
		// TODO Auto-generated method stub
		//// *** CHECK IF Q EXISTS ****
		Queue<MicroService> ms = eventServices.get(e.getClass());
		MicroService m = ms.remove();
		queues.get(m).add(e);
		ms.add(m);
		return e.getFuture();
	}

	@Override
	/**
	 *@PRE: queues.containsKey(m)==false
	 * @POST: queues.containsKey(m)==true
	 */
	public void register(MicroService m) {
		// TODO Auto-generated method stub
	}

	@Override
	/**@PRE: queues.containsKey(m)==true
	* @POST: queues.containsKey(m)==false
	 */
	public void unregister(MicroService m) {
		// TODO Auto-generated method stub

	}

	@Override
	public boolean isRegistered(MicroService m) {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public boolean isEventSubscribedBy(Class<? extends Event> type, MicroService m) {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public boolean isBroadcastSubscribedBy(Class<? extends Broadcast> type, MicroService m) {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public Message awaitMessage(MicroService m) throws InterruptedException {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public List<MicroService> getMicroServiceSubscribedToEvent(Broadcast b) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public <T> MicroService getMicroServiceSubscribedToEvent(Event<T> e) {
		// TODO Auto-generated method stub
		return null;
	}
}
