package bgu.spl.mics.application.messages;

import bgu.spl.mics.Broadcast;
import bgu.spl.mics.application.objects.Model;

import java.util.List;

public class PublishConferenceBroadcast implements Broadcast {

    List<Model> models;

    public PublishConferenceBroadcast(List<Model> models) {
        this.models = models;
    }

    public List<Model> getModels() {
        return models;
    }
}
