package bgu.spl.mics.application.objects;

/**
 * Passive object representing a data used by a model.
 * Add fields and methods to this class as you see fit (including public methods and constructors).
 */
public class Data {
    /**
     * Enum representing the Data type.
     */
    public enum Type {
        Images, Text, Tabular
    }

    private Type type;
    private int processed;
    private int size;

    public Data(String type, int size) {
        this.type = setType(type);
        this.processed = 0;
        this.size = size;
    }
    public Data(Type type, int p, int size) {
        this.type = type;
        this.processed = p;
        this.size = size;
    }

    public Type getType() {
        return type;
    }
    public Type setType(String s) {
        if(s.equals("Images"))
        return Type.Images;
        if(s.equals("Tabular"))
            return Type.Tabular;
            return Type.Text;
    }


    public int getProcessed() {
        return processed;
    }

    public int getSize() {
        return size;
    }
}
