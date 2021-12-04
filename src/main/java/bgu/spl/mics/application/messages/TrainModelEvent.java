package bgu.spl.mics.application.messages;

import bgu.spl.mics.Event;
import bgu.spl.mics.Future;

public class TrainModelEvent implements Event<String> {
    @Override
    public <T> Future<T> getFuture() {
        return null;
    }
}
