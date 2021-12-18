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
    private final double PROBABILITY;

    public Model(String name, Data data, Student student) {
        this.status=Status.PreTrained;
        this.results = Results.None;
        this.name = name;
        this.data = data;
        this.student = student;
        PROBABILITY = initProbability(student.getStatus());
    }

    private double initProbability(Student.Degree degree){
        if(degree == Student.Degree.PhD)
            return 0.8;
        else return 0.6;
    }

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

    public void setStatus(String status) {
        if(status.equals("PreTrained"))
            this.status = Status.PreTrained;
        else if(status.equals("Training"))
            this.status = Status.Training;
        else if(status.equals("Trained"))
            this.status = Status.Trained;
        else if(status.equals("Tested"))
            this.status = Status.Tested;
    }

    public void setResults(String results) {
        if(results.equals("None"))
            this.results = Results.None;
        else if(results.equals("Good"))
            this.results = Results.Good;
        else if(results.equals("Bad"))
            this.results = Results.Bad;
    }

    public double getPROBABILITY() {
        return PROBABILITY;
    }

    public boolean isModelGood(){return results == Results.Good;}
}
