package bgu.spl.mics.application.services;

import bgu.spl.mics.Callback;
import bgu.spl.mics.MicroService;
import bgu.spl.mics.application.messages.CancelBroadcast;
import bgu.spl.mics.application.messages.TickBroadcast;

import java.util.Timer;
import java.util.TimerTask;

/**
 * TimeService is the global system timer There is only one instance of this micro-service.
 * It keeps track of the amount of ticks passed since initialization and notifies
 * all other micro-services about the current time tick using {@link TickBroadcast}.
 * This class may not hold references for objects which it is not responsible for.
 * 
 * You can add private fields and public methods to this class.
 * You MAY change constructor signatures and even add new public constructors.
 */
public class TimeService extends MicroService {

	private int speed;
	private int duration;
	private int counter;
	private Timer timer;

	public TimeService(int speed, int duration, String name) {
		super(name);
		// TODO Implement this
		this.duration = duration;
		this.speed = speed;
		counter = 0;
		timer = new Timer();


		subscribeBroadcast(CancelBroadcast.class, c -> {
			timer.cancel();
			terminate();
			sendBroadcast(new CancelBroadcast());
		});
		subscribeBroadcast(TickBroadcast.class, c ->{
			if (counter < duration) {
				timer.schedule(new TimerTask() {
					@Override
					public void run() {
						sendBroadcast(new TickBroadcast());
					}
				}, speed);
				counter++;
			}
			else sendBroadcast(new CancelBroadcast());
		});
	}

	@Override
	protected void initialize() {
		// TODO Implement this
		sendBroadcast(new TickBroadcast());
	}





	private void run2(){
		while (counter<=duration) {
			try {
				Thread.sleep(speed);
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
			counter++;
		}
	}

}
