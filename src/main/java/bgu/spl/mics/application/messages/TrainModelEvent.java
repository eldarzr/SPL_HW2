package bgu.spl.mics.application.messages;

import bgu.spl.mics.Event;
import bgu.spl.mics.Future;
import bgu.spl.mics.application.objects.Model;

public class TrainModelEvent implements Event<String> {

    Model model;

    public TrainModelEvent(Model model) {
        this.model = model;
    }

    @Override
    public <T> Future<T> getFuture() {
        return null;
    }

    public Model getModel() {
        return model;
    }
}
