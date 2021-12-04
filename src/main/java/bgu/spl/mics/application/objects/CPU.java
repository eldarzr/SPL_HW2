package bgu.spl.mics.application.objects;

import java.util.Collection;

/**
 * Passive object representing a single CPU.
 * Add all the fields described in the assignment as private fields.
 * Add fields and methods to this class as you see fit (including public methods and constructors).
 */
public class CPU {

    /**
     * @INV: cores != null & data != null & cluster != null
     */


    private int cores;
    private Collection<DataBatch> data;
    private Cluster cluster;
    private long ticks;


    /**
     * add the new {@code dataBatch} to the data collection.
     * @param dataBatch new dataBatch to add.
     * @PRE: dataBatch != null
     * @POST: data.contains(dataBatch)
     */
    public void addUnprocessedData(DataBatch dataBatch){
        // TODO Auto-generated method stub

    }

    /**
     * process the next dataBatch in the collection and return the processed data.
     * @PRE: !data.isEmpty()
     * @POST: (@return == @pre: data.get(0)) & (data.size() == @pre: data.size()) & (@return: data.processed == @pre: data.get(0).processed+1000)
     */
    public DataBatch ProcessData(){
        // TODO Auto-generated method stub
        return null;
    }


    /**
     * send the processed dataBatch to the cluster
     * @PRE: dataBatch != null & data.contains(dataBatch)
     * @POST: (data.size() == @pre:data.size()+1)
     */
    public void sendProcessedData(DataBatch dataBatch){
        // TODO Auto-generated method stub
    }
    /**
     *
     * @PRE:
     * @POST: ticks == (@PRE:ticks +1)
     */
    public void updateTicks(){
        // TODO Auto-generated method stub
    }

    public int getCores() {
        return cores;
    }

    public Collection<DataBatch> getData() {
        return data;
    }

    public Cluster getCluster() {
        return cluster;
    }
}
