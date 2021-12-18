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

    public Student(String name, String department, String status, int publications, int papersRead) {
        this.name = name;
        this.department = department;
        this.status = setStatus(status);
        this.publications = publications;
        this.papersRead = papersRead;
        this.modelLists = new ArrayList<>();
        this.currModel = 0;
    }

    public Model getCurrentModel(){
        if(!hasNext())
            return null; //  **** EXCEPTION >???? ***
        Model m = modelLists.get(currModel);
        currModel++;
        return m;
    }

    public boolean hasNext() {
        return  !(modelLists.isEmpty() || currModel>=modelLists.size());
    }

    public void addModel (Model m){
        this.modelLists.add(m);

    }

    public Degree getStatus() {
        return status;
    }

    public Degree setStatus(String degree) {
        if(degree.equals("MSc"))
            return Degree.MSc;
        return Degree.PhD;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
}
