package bgu.spl.mics.application.objects;

import java.util.Collection;

/**
 * Passive object representing a single CPU.
 * Add all the fields described in the assignment as private fields.
 * Add fields and methods to this class as you see fit (including public methods and constructors).
 */
public class CPU {

    private int cores;
    private Collection<DataBatch> data;
    private Cluster cluster;

    void addUnprocessedData(DataBatch dataBatch){
        // TODO Auto-generated method stub

    }

    DataBatch ProcessData(){
        // TODO Auto-generated method stub
        return null;
    }

    void sendProcessedData(DataBatch dataBatch){
        // TODO Auto-generated method stub
    }
}
