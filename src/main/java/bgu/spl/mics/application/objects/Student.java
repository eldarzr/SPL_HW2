package bgu.spl.mics.application.objects;

import java.util.List;

/**
 * Passive object representing single student.
 * Add fields and methods to this class as you see fit (including public methods and constructors).
 */
public class Student {
    /**
     * Enum representing the Degree the student is studying for.
     */
    enum Degree {
        MSc, PhD
    }

    private int name;
    private String department;
    private Degree status;
    private int publications;
    private int papersRead;
    private List<Model> modelLists;
    private int currModel;

    public Student(int name, String department, Degree status, int publications, int papersRead, List<Model> modelLists) {
        this.name = name;
        this.department = department;
        this.status = status;
        this.publications = publications;
        this.papersRead = papersRead;
        this.modelLists = modelLists;
        this.currModel = 0;
    }

    public Model getCurrentModel(){
        if (currModel >= modelLists.size() || modelLists.isEmpty())
            return null; //  **** EXCEPTION >???? ***
        return modelLists.get(currModel);
    }
}
