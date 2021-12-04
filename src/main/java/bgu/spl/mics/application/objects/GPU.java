package bgu.spl.mics.application.objects;

import java.util.Collection;

/**
 * Passive object representing a single GPU.
 * Add all the fields described in the assignment as private fields.
 * Add fields and methods to this class as you see fit (including public methods and constructors).
 */
public class GPU {
    /**
     * Enum representing the type of the GPU.
     */
    enum Type {RTX3090, RTX2080, GTX1080}

    private Type type;
    private Model model;
    private Cluster cluster;
    private Collection<DataBatch> processedData;

    public void sendUnprocessedData(int index){
        // TODO Auto-generated method stub
    }

    public void receivedProcessedData(DataBatch dataBatch){
        // TODO Auto-generated method stub
    }

    public void trainData(){
        // TODO Auto-generated method stub
    }

    public void complete(){
        // TODO Auto-generated method stub
    }

    public Model.Results testModel(){
        // TODO Auto-generated method stub
        return null;
    }

    public void updateTick(){
        // TODO Auto-generated method stub
    }
}
