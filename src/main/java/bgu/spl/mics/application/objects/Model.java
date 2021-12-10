package bgu.spl.mics.application.objects;

import com.sun.org.apache.xpath.internal.operations.Mod;

/**
 * Passive object representing a Deep Learning model.
 * Add all the fields described in the assignment as private fields.
 * Add fields and methods to this class as you see fit (including public methods and constructors).
 */
public class Model {

    enum Status {PreTrained, Training, Trained, Tested}
    enum Results {None, Good, Bad}

    private Model.Status status;
    private Model.Results results;
    private String name;
    private Data data;
    private Student student;
Model m = new Model();

    public Status getStatus() {
        return status;
    }

    public Results getResults() {
        return results;
    }

    public String getName() {
        return name;
    }

    public Data getData() {
        return data;
    }

    public Student getStudent() {
        return student;
    }
}
