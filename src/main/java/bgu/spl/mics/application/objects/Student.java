package bgu.spl.mics.application.objects;

import java.util.ArrayList;
import java.util.List;

/**
 * Passive object representing single student.
 * Add fields and methods to this class as you see fit (including public methods and constructors).
 */
public class Student {


    /**
     * Enum representing the Degree the student is studying for.
     */
    public enum Degree {
        MSc, PhD
    }

    private String name;
    private String department;
    private Degree status;
    private int publications;
    private int papersRead;
    private List<Model> modelLists;
    private int currModel;

    public Student(String name, String department, Degree status, int publications, int papersRead) {
        this.name = name;
        this.department = department;
        this.status = status;
        this.publications = publications;
        this.papersRead = papersRead;
        this.modelLists = new ArrayList<>();
        this.currModel = 0;
    }

    public Model getCurrentModel(){
    if(!hasNext())
            return null; //  **** EXCEPTION >???? ***
        currModel++;
        return modelLists.get(currModel);
    }

    public boolean hasNext() {
        return (currModel >= modelLists.size() || modelLists.isEmpty());
    }

    public void addModel (Model m){
        this.modelLists.add(m);

    }
}
