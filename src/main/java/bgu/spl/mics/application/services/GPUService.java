package bgu.spl.mics.application.services;

import bgu.spl.mics.Callback;
import bgu.spl.mics.MicroService;
import bgu.spl.mics.application.messages.TickBroadcast;
import bgu.spl.mics.application.messages.TrainModelEvent;
import bgu.spl.mics.application.objects.GPU;

/**
 * GPU service is responsible for handling the
 * {@link TrainModelEvent} and {@link TestModelEvent},
 * in addition to sending the {@link DataPreProcessEvent}.
 * This class may not hold references for objects which it is not responsible for.
 *
 * You can add private fields and public methods to this class.
 * You MAY change constructor signatures and even add new public constructors.
 */
public class GPUService extends MicroService {

    private GPU gpu;

    public GPUService(String name , GPU gpu) {
        super("Change_This_Name");
        this.gpu=gpu;
        gpu.registerCluster();
        // TODO Implement this
        subscribe();
    }

    @Override
    protected void initialize() {
        // TODO Implement this
        //subscribe();

    }

    private void onTrain(){
        gpu.onTick();
    }

    private void subscribe() {
        subscribeEvent(TrainModelEvent.class, new Callback<TrainModelEvent>() {
            @Override
            public void call(TrainModelEvent c) {
                gpu.setTrainModel(c.getModel(),c.getFuture());
                onTrain();
            }
        });

        subscribeBroadcast(TickBroadcast.class, tick -> {
            gpu.incTicks();
            onTrain();
        });

    }
}
