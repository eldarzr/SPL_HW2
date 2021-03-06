package bgu.spl.mics.application.services;

import bgu.spl.mics.Callback;
import bgu.spl.mics.MicroService;
import bgu.spl.mics.application.messages.CancelBroadcast;
import bgu.spl.mics.application.messages.TestModelEvent;
import bgu.spl.mics.application.messages.TickBroadcast;
import bgu.spl.mics.application.messages.TrainModelEvent;
import bgu.spl.mics.application.objects.GPU;
import bgu.spl.mics.application.objects.Model;
import org.junit.internal.runners.TestMethod;

import java.util.concurrent.ConcurrentLinkedQueue;

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
    private ConcurrentLinkedQueue<TrainModelEvent> trainModelsQueue;
    private ConcurrentLinkedQueue<TestModelEvent> testModelsQueue;

    public GPUService(String name , GPU gpu) {
        super(name);
        this.gpu=gpu;
        gpu.registerCluster();
        trainModelsQueue = new ConcurrentLinkedQueue<>();
        testModelsQueue = new ConcurrentLinkedQueue<>();
        // TODO Implement this
        subscribe();
    }

    @Override
    protected void initialize() {
        // TODO Implement this
    }

    private void onTrain(){
        if(gpu.getModel() == null && !testModelsQueue.isEmpty()) {
            TestModelEvent c = testModelsQueue.remove();
            gpu.setTestModel(c.getModel(), c.getFuture());
        }
        else if(gpu.getModel() == null && !trainModelsQueue.isEmpty()) {
            TrainModelEvent c = trainModelsQueue.remove();
            gpu.setTrainModel(c.getModel(), c.getFuture());
        }
        gpu.onTick();
    }

    private void subscribe() {
        subscribeBroadcast(CancelBroadcast.class, c -> {
            for(TrainModelEvent t : trainModelsQueue)
                t.getFuture().resolve("");
            for(TestModelEvent t : testModelsQueue)
                t.getFuture().resolve("");
            gpu.terminate();
            terminate();
        });
        subscribeEvent(TrainModelEvent.class, new Callback<TrainModelEvent>() {
            @Override
            public void call(TrainModelEvent c) {
                trainModelsQueue.add(c);
                System.out.println(getName() + "got the model " + c.getModel().getName());
                onTrain();
            }
        });

        subscribeEvent(TestModelEvent.class, new Callback<TestModelEvent>() {
            @Override
            public void call(TestModelEvent c) {
                testModelsQueue.add(c);
                System.out.println(getName() + "got the model " + c.getModel().getName());
                onTrain();
            }
        });

        subscribeBroadcast(TickBroadcast.class, tick -> {
            gpu.incTicks();
            onTrain();
        });

    }
}
