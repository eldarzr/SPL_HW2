package bgu.spl.mics.application.services;

import bgu.spl.mics.Broadcast;
import bgu.spl.mics.MicroService;
import bgu.spl.mics.application.messages.CancelBroadcast;
import bgu.spl.mics.application.messages.PublishConferenceBroadcast;
import bgu.spl.mics.application.messages.PublishResultsEvent;
import bgu.spl.mics.application.messages.TickBroadcast;
import bgu.spl.mics.application.objects.ConfrenceInformation;
import bgu.spl.mics.application.objects.Model;

/**
 * Conference service is in charge of
 * aggregating good results and publishing them via the {@link PublishConfrenceBroadcast},
 * after publishing results the conference will unregister from the system.
 * This class may not hold references for objects which it is not responsible for.
 *
 * You can add private fields and public methods to this class.
 * You MAY change constructor signatures and even add new public constructors.
 */
public class ConferenceService extends MicroService {
    
    private ConfrenceInformation confrenceInformation;
    private int ticks;
    
    public ConferenceService(String name, ConfrenceInformation confrenceInformation) {
        super("confernceService " + name);
        // TODO Implement this
        this.confrenceInformation= confrenceInformation;
        subscribeBroadcast(CancelBroadcast.class, c-> terminate());
        subscribeBroadcast(TickBroadcast.class, c -> onTick());
        subscribeEvent(PublishResultsEvent.class, e -> this.confrenceInformation.addModel(e.getModel()));
        ticks = 0;
    }

    private void onTick() {
        ticks++;
        if(ticks == confrenceInformation.getDate()){
            terminate();
            sendBroadcast(new PublishConferenceBroadcast(confrenceInformation.getModels()));
        }
    }

    public ConfrenceInformation getConfrenceInformation() {
        return confrenceInformation;
    }

    @Override
    protected void initialize() {
        // TODO Implement this

    }
}
