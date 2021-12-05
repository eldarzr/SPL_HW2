package bgu.spl.mics.application.objects;

import bgu.spl.mics.Broadcast;

import java.util.Collection;

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
    enum Type {RTX3090, RTX2080, GTX1080}

    private Type type;
    private Model model;
    private Cluster cluster;
    private Collection<DataBatch> processedData;
    private Collection<DataBatch> trainedData;
    private boolean isCompleted;
    private long ticks;
    private int dataSent;



    /**
     * Sends {@link DataBatch} unprocessed data using the cluster.
     * //@PRE:model.getData().getSize()>dataSent && model.getData().getProcessed()<model.getData().getSize()
     *             & isCompleted=false
     * //@POST:dataSent= @PRE: dataSent + 1000
     */

    public void sendUnprocessedData(){
        // TODO Auto-generated method stub
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
    }
    /**
     * complete the GPU actions on the TrainModelEvent
     * //@PRE:processedData.size()=0 && trainedData.size() = model.getData().Size() && isCompleted=false
     * //@POST: isCompleted = true && model.getStatus() = "Trained"
     */
    public void complete(){
        // TODO Auto-generated method stub
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

    public long getTicks() {
        return ticks;
    }

    public int getDataSent() {
        return dataSent;
    }
}
