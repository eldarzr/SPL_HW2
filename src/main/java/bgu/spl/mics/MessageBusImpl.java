package bgu.spl.mics;

import bgu.spl.mics.example.ServiceCreator;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Queue;

/**
 * The {@link MessageBusImpl class is the implementation of the MessageBus interface.
 * Write your implementation here!
 * Only private fields and methods can be added to this class.
 */
public class MessageBusImpl implements MessageBus {

	private Map<MicroService , Queue<Message>> queues = new HashMap<>();


	@Override
	public <T> void subscribeEvent(Class<? extends Event<T>> type, MicroService m) {
		// TODO Auto-generated method stub

	}

	@Override
	public void subscribeBroadcast(Class<? extends Broadcast> type, MicroService m) {
		// TODO Auto-generated method stub

	}

	@Override
	public <T> void complete(Event<T> e, T result) {
		// TODO Auto-generated method stub

	}

	@Override
	public void sendBroadcast(Broadcast b) {
		// TODO Auto-generated method stub

	}

	
	@Override
	public <T> Future<T> sendEvent(Event<T> e) {
		// TODO Auto-generated method stub
		return null;
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
