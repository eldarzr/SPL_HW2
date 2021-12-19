package bgu.spl.mics.application.messages;

import bgu.spl.mics.Event;
import bgu.spl.mics.Future;
import bgu.spl.mics.application.objects.Model;

public class TrainModelEvent implements Event<String> {

    Model model;
    Future future;

    public TrainModelEvent(Model model) {
        this.model = model;
        future = new Future();
    }

    public Model getModel() {
        return model;
    }

    @Override
    public <T> Future<T> getFuture() {
        return future;
    }
}
