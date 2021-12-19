package bgu.spl.mics.application.objects;


import java.util.ArrayList;
import java.util.Collection;
import java.util.Vector;
import java.util.concurrent.ConcurrentLinkedQueue;

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
    private DataBatch currentDB;

    public CPU(int cores) {
        this.cores = cores;
        this.data = new ArrayList<>();
        this.cluster = Cluster.getInstance();
        data= new ConcurrentLinkedQueue<>();
       // this.ticks = ticks;
        this.ticks = 0;
    }

    /**
     * add the new {@code dataBatch} to the data collection.
     * @param dataBatch new dataBatch to add.
     * @PRE: dataBatch != null
     * @POST: data.contains(dataBatch)
     */
    public void addUnprocessedData(DataBatch dataBatch){
        // TODO Auto-generated method stub
        data.add(dataBatch);
    }

    /**
     * process the next dataBatch in the collection and return the processed data.
     * @PRE: !data.isEmpty()
     * @POST: (@return == @pre: data.get(0)) & (data.size() == @pre: data.size()) & (@return: data.processed == @pre: data.get(0).processed+1000)
     */
    public void ProcessData(){
        // TODO Auto-generated method stub
        ConcurrentLinkedQueue<DataBatch> queue = (ConcurrentLinkedQueue<DataBatch>) data;
        if(!queue.isEmpty()) {
            DataBatch dataBatch = queue.peek();
            Data.Type type = dataBatch.getData().getType();
            int requiredTick = getRequiredTicks(type);
            if (ticks >= requiredTick) {
                //System.out.println("cpu process data");
                dataBatch = queue.remove();
                queue.remove(dataBatch);
                ticks = 0;
                sendProcessedData(dataBatch);
                dataBatch.getData().addProcessed(1000);
            }
        }
    }

    private int getRequiredTicks(Data.Type type) {
        int req = 32/cores;
        if(type ==  Data.Type.Images){
            req*=4;
        }
        if(type ==  Data.Type.Text){
            req*=2;
        }

        return req;

    }


    /**
     * send the processed dataBatch to the cluster
     * @PRE: dataBatch != null & data.contains(dataBatch)
     * @POST: (data.size() == @pre:data.size()+1)
     */
    public void sendProcessedData(DataBatch dataBatch){
        // TODO Auto-generated method stub
        cluster.sendProcessedData(dataBatch);
    }
    /**
     *
     * @PRE:
     * @POST: ticks == (@PRE:ticks +1)
     */
    public void updateTicks(){
        // TODO Auto-generated method stub
        ticks++;
    }

    public void registerCluster() {
        cluster.registerCPU(this);
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
