package bgu.spl.mics.application.objects;

import bgu.spl.mics.Future;
import junit.framework.TestCase;
import org.junit.jupiter.api.BeforeEach;

public class GPUTest extends TestCase {

    public static GPU gpu;

   @BeforeEach
    public void setUp() throws Exception {
        super.setUp();
        gpu = new GPU("RTX3090");
    }

    public void tearDown() throws Exception {
    }

    public void testSendUnprocessedData() {
        Data data = new Data(Data.Type.Text, 0, 10000);
        Student s = new Student("s","cs","MSc",0,0);
        Model m = new Model("a",data,s);
        CPU cpu = new CPU(16);
        Cluster cluster = Cluster.getInstance();
        cluster.registerGPU(gpu);
        cluster.registerCPU(cpu);
        gpu.setTrainModel(m,new Future());
        DataBatch dataBatch = new DataBatch(0,data);
        int ds = gpu.getDataSent();
        gpu.sendUnprocessedData();
        assertTrue(gpu.getNumOfDataInProcess() > 0);
    }

    public void testSetTrainModel() {
        Data data = new Data(Data.Type.Text, 0, 10000);
        Student s = new Student("s","cs","MSc",0,0);
        Model m = new Model("a",data,s);
        assertNull(gpu.getModel());
        gpu.setTrainModel(m,new Future());
        assertTrue(gpu.getModel() == m);
    }

    public void testSetTestModel() {
        Data data = new Data(Data.Type.Text, 0, 10000);
        Student s = new Student("s","cs","MSc",0,0);
        Model m = new Model("a",data,s);
        assertNull(gpu.getModel());
        gpu.setTestModel(m,new Future());
        assertTrue(gpu.getModel() == m);
    }


    public void testTrainData() {
        Data data = new Data(Data.Type.Text, 0, 10000);
        DataBatch dataBatch = new DataBatch(0,data);
        Student s = new Student("s","cs","MSc",0,0);
        Model m = new Model("a",data,s);
        gpu.setTrainModel(m,new Future());
       //gpu.receivedProcessedData(dataBatch);
       //dataBatch=gpu.getProcessedData();
        gpu.getProcessedData().add(dataBatch);
       assertFalse(gpu.getTrainedData().contains(dataBatch));
        gpu.updateTicks();
        gpu.updateTicks();
        gpu.updateTicks();
        gpu.updateTicks();
       gpu.trainData();
       assertTrue(gpu.getTrainedData().contains(dataBatch));
    }

    public void testComplete() {
        Data data = new Data(Data.Type.Text, 0, 10000);
        Student s = new Student("s","cs","MSc",0,0);
        Model m = new Model("a",data,s);
        gpu.setTrainModel(m,new Future());
        //m.setStatus("Trained");
        assertFalse(gpu.getModel().getStatus()== Model.Status.Trained);
       assertFalse(gpu.isCompleted());
       gpu.complete();
       //assertTrue(m.getStatus()== Model.Status.Trained);
       assertNull(gpu.getModel());

    }

    public void testTestModel() {
        Data data = new Data(Data.Type.Text, 0, 10000);
        Student s = new Student("s","cs","MSc",0,0);
        Model m = new Model("a",data,s);
        m.setStatus("Trained");
        CPU cpu = new CPU(16);
        gpu.setTestModel(m,new Future());
        assertTrue(gpu.getModel().getStatus()==Model.Status.Trained);
       assertFalse(gpu.getModel().getResults()!= Model.Results.None);
       gpu.testModel();
       assertTrue(m.getResults()!= Model.Results.None);
    }

    public void testUpdateTick() {
       long currentTicks = gpu.getTicks();
       gpu.updateTicks();
       long updatedTicks = gpu.getTicks();
       assertTrue(currentTicks==updatedTicks-1);
    }
}