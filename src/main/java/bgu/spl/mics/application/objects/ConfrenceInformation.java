package bgu.spl.mics.application.objects;

import java.util.ArrayList;
import java.util.List;

/**
 * Passive object representing information on a conference.
 * Add fields and methods to this class as you see fit (including public methods and constructors).
 */
public class ConfrenceInformation {

    private String name;
    private int date;
    private List<Model> models;

    public ConfrenceInformation(String name, int date) {
        this.name = name;
        this.date = date;
        models = new ArrayList<>();
    }

    public void addModel(Model model) {
        models.add(model);
    }

    public String getName() {
        return name;
    }

    public int getDate() {
        return date;
    }

    public List<Model> getModels() {
        return models;
    }
}
