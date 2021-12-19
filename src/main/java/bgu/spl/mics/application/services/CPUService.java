package bgu.spl.mics.application.services;

import bgu.spl.mics.Callback;
import bgu.spl.mics.MicroService;
import bgu.spl.mics.application.messages.CancelBroadcast;
import bgu.spl.mics.application.messages.TickBroadcast;
import bgu.spl.mics.application.messages.TrainModelEvent;
import bgu.spl.mics.application.objects.CPU;
import bgu.spl.mics.application.objects.Cluster;

import java.util.concurrent.ConcurrentLinkedQueue;

/**
 * CPU service is responsible for handling the {@link DataPreProcessEvent}.
 * This class may not hold references for objects which it is not responsible for.
 *
 * You can add private fields and public methods to this class.
 * You MAY change constructor signatures and even add new public constructors.
 */
public class CPUService extends MicroService {
    private CPU cpu;

    public CPUService(String name,CPU cpu) {
        super("CPUService " + name);
        this.cpu=cpu;
        cpu.registerCluster();
        // TODO Implement this
        subscribe();
    }

    @Override
    protected void initialize() {
        // TODO Implement this
        //subscribe();

    }

    private void onProcess(){
        cpu.ProcessData();
    }

    private void subscribe() {
        subscribeBroadcast(CancelBroadcast.class, c -> terminate());
        subscribeBroadcast(TickBroadcast.class, tick -> {
            cpu.updateTicks();
            onProcess();
        });

    }

}
