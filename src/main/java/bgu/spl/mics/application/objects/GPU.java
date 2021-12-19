package bgu.spl.mics.application.objects;

import bgu.spl.mics.Future;
import bgu.spl.mics.application.messages.TestModelEvent;

import java.util.Collection;
import java.util.Random;
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

    public GPU(String type) {
        this.type = setType(type);
        this.model = null;
        cluster = Cluster.getInstance();
        isCompleted = false;
        ticks = 0;
        dataSent = 0;
        NUM_OF_BATCHES = initNumOfBatches(this.type);
        NUM_OF_TICKS = initNumOfTicks(this.type);
        unProcessedData = new ConcurrentLinkedQueue<>();
        processedData = new ConcurrentLinkedQueue<>();
        trainedData = new ConcurrentLinkedQueue<>();
        numOfDataInProcess=0;
    }

    private Type setType(String type) {
        if(type.equals("RTX3090")){
            return Type.RTX3090;
        }
        if(type.equals("RTX2080")){
            return Type.RTX2080;
        }
        return Type.GTX1080;
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
        this.model=model;
        model.setStatus("Training");
        this.future = future;
        isCompleted = false;
        dataSent=0;

        unProcessedData = new ConcurrentLinkedQueue<>();
        processedData = new ConcurrentLinkedQueue<>();
        trainedData = new ConcurrentLinkedQueue<>();
        for (int i=0; i<model.getData().getSize(); i=i+1000){
            unProcessedData.add(new DataBatch(i,model.getData()));
        }

    }

    public void setTestModel(Model model, Future future){
        this.model=model;
        this.future = future;
        isCompleted = false;
        dataSent=0;
    }


    public void onTick() {
        if(model != null && model.getStatus() == Model.Status.Training) {
            if (trainedData.size() < model.getData().getSize() / 1000) {
                trainData();
                sendUnprocessedData();
            }
            else complete();
        }
        if(model != null && model.getStatus() == Model.Status.Trained){
            testModel();
        }
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
            });
            numOfDataInProcess++;
        }
        cluster.updateGpuTime();
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
        future.resolve("Trained");
        model = null;
    }

    /**
     * test the model.
     * //@PRE: model.getStatus() = "Trained"
     * //@POST: model.getStatus() = "Tested"
     */
    public void testModel(){
        // TODO Auto-generated method stub
        Random random = new Random();
        double result = random.nextDouble();
        if(result >= model.getPROBABILITY())
            model.setResults("Good");
        else model.setResults("Bad");
        future.resolve("Tested");
        model = null;
    }
    /**
     * update ticks
     * //@POST :ticks == (@PRE:ticks+1)
     **/
    public void updateTicks(){
        // TODO Auto-generated method stub
        ticks++;
    }

    public void terminate() {
        if(model != null)
            future.resolve("");
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

    public int getNumOfDataInProcess() {
        return numOfDataInProcess;
    }
}
