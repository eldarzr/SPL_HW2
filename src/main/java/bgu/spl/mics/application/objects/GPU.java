package bgu.spl.mics.application.objects;

import bgu.spl.mics.Future;

import java.util.Collection;
import java.util.concurrent.ConcurrentLinkedQueue;

/**
 * Passive object representing a single GPU.
 * Add all the fields described in the assignment as private fields.
 * Add fields and methods to this class as you see fit (including public methods and constructors).
 * @INV: trainedData.Size()<=Model.getData().getSize() && dataSent<=Model.getData().getSize()
 */
public class GPU {

    /**
     * Enum representing the type of the GPU.
     */
    public enum Type {RTX3090, RTX2080, GTX1080}

    private Type type;
    private Model model;
    private Cluster cluster;
    private ConcurrentLinkedQueue<DataBatch> unProcessedData;
    private ConcurrentLinkedQueue<DataBatch> processedData;
    private ConcurrentLinkedQueue<DataBatch> trainedData;
    private boolean isCompleted;
    private long ticks;
    private int dataSent;
    private final int NUM_OF_BATCHES;
    private final int NUM_OF_TICKS;
    private int numOfDataInProcess;
    private Future future;

    public GPU(Type type) {
        this.type = type;
        this.model = null;
        cluster = Cluster.getInstance();
        isCompleted = false;
        ticks = 0;
        dataSent = 0;
        NUM_OF_BATCHES = initNumOfBatches(type);
        NUM_OF_TICKS = initNumOfTicks(type);
        unProcessedData = new ConcurrentLinkedQueue<>();
        processedData = new ConcurrentLinkedQueue<>();
        trainedData = new ConcurrentLinkedQueue<>();
        numOfDataInProcess=0;
    }

    private int initNumOfBatches(Type type){
        if(type == Type.GTX1080)
            return 8;
        else if(type == Type.RTX2080)
            return 16;
        else return 32;
    }

    private int initNumOfTicks(Type type){
        if(type == Type.GTX1080)
            return 4;
        else if(type == Type.RTX2080)
            return 2;
        else return 1;
    }

    public void setTrainModel(Model model, Future future){
        System.out.println("gpu got new model");
        this.model=model;
        this.future = future;
        //processedData;
        //trainedData;
        isCompleted = false;
        dataSent=0;

        unProcessedData = new ConcurrentLinkedQueue<>();
        processedData = new ConcurrentLinkedQueue<>();
        trainedData = new ConcurrentLinkedQueue<>();
        for (int i=0; i<model.getData().getSize(); i=i+1000){
            unProcessedData.add(new DataBatch(i,model.getData()));
        }

    }


    public void onTick() {
        if(trainedData.size() < model.getData().getSize()/1000){
            trainData();
            sendUnprocessedData();
        }
        else complete();
    }

    /**
     * Sends {@link DataBatch} unprocessed data using the cluster.
     * //@PRE:model.getData().getSize()>dataSent && model.getData().getProcessed()<model.getData().getSize()
     *             & isCompleted=false
     * //@POST:dataSent= @PRE: dataSent + 1000
     */

    public void sendUnprocessedData(){
        // TODO Auto-generated method stub
        while (!unProcessedData.isEmpty() && numOfDataInProcess<NUM_OF_BATCHES){
            cluster.sendData(unProcessedData.remove(), db -> {
                processedData.add(db);
                numOfDataInProcess++;
            });
        }
    }
    /**
     * Adds the {@link DataBatch} {@code b} to the proccesedData
     * field acorrding to its limitation, where limitation is:
     * 8 - for RTX 1080 type , 16 - for RTX 2080 type ,  32 - for RTX 3090 type.
     * @param dataBatch - the DataBatch which contains the processed data.
     * //@PRE:dataBatch!=null processedData.Size()< limitation && isCompleted=false
     * //@POST:processedData.Size()<= limitation
     */

    public void receivedProcessedData(DataBatch dataBatch){
        // TODO Auto-generated method stub
    }
    /**
     * train processed DataBatch and adding it to the trainedData
     * //@PRE:processedData.size()>0 && isCompleted=false
     * //@POST:trainedData.size()==@pre:trainedData.size()+1
     */
    public void trainData(){
        // TODO Auto-generated method stub
        if(ticks>= NUM_OF_TICKS && !processedData.isEmpty()){
            DataBatch db = processedData.remove();
            trainedData.add(db);
            ticks = 0;
            numOfDataInProcess--;
        }
    }
    /**
     * complete the GPU actions on the TrainModelEvent
     * //@PRE:processedData.size()=0 && trainedData.size() = model.getData().Size() && isCompleted=false
     * //@POST: isCompleted = true && model.getStatus() = "Trained"
     */
    public void complete(){
        // TODO Auto-generated method stub
        future.resolve(Model.Status.Trained);
    }

    /**
     * test the model.
     * //@PRE: model.getStatus() = "Trained"
     * //@POST: model.getStatus() = "Tested"
     */
    public void testModel(){
        // TODO Auto-generated method stub

    }
    /**
     * update ticks
     * //@POST :ticks == (@PRE:ticks+1)
     **/
    public void updateTicks(){
        // TODO Auto-generated method stub
        ticks++;
    }

    public void registerCluster() {
        cluster.registerGPU(this);
    }

    public Type getType() {
        return type;
    }

    public Model getModel() {
        return model;
    }

    public Collection<DataBatch> getProcessedData() {
        return processedData;
    }

    public Collection<DataBatch> getTrainedData() {
        return trainedData;
    }

    public boolean isCompleted() {
        return isCompleted;
    }

    public void incTicks() {
        ticks++;
    }

    public long getTicks() {
        return ticks;
    }

    public int getDataSent() {
        return dataSent;
    }
}
