package bgu.spl.mics.application.objects;

/**
 * Passive object representing a data used by a model.
 * Add fields and methods to this class as you see fit (including public methods and constructors).
 */

public class DataBatch {

    private int index;
    private Data data;
    private boolean isProcessed;

    public DataBatch(int index, Data data) {
        this.index = index;
        this.data = data;
        isProcessed=false;
    }

    public int getIndex() {
        return index;
    }

    public Data getData() {
        return data;
    }

    public boolean isProcessed(){
        return isProcessed;
    }

    public void finishProcessed() {
        isProcessed = true;
    }
}
