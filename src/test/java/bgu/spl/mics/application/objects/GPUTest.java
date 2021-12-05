package bgu.spl.mics.application.objects;

import junit.framework.TestCase;
import org.junit.jupiter.api.BeforeEach;

public class GPUTest extends TestCase {
 public static GPU gpu;

   @BeforeEach
    public void setUp() throws Exception {
        super.setUp();
        gpu = new GPU();
    }

    public void tearDown() throws Exception {
    }

    public void testSendUnprocessedData() {
       Data data = new Data();
       DataBatch dataBatch = new DataBatch(0,data);
       int ds = gpu.getDataSent();
       gpu.sendUnprocessedData();
       assertTrue(ds == gpu.getDataSent() - 1000);
    }

    public void testReceivedProcessedData() {
       Data data = new Data();
       DataBatch dataBatch = new DataBatch(0,data);
       assertFalse(gpu.getProcessedData().contains(dataBatch));
       gpu.receivedProcessedData(dataBatch);
       assertTrue(gpu.getProcessedData().contains(dataBatch));

    }

    public void testTrainData() {
       Data data = new Data();
       DataBatch dataBatch = new DataBatch(0,data);
       gpu.receivedProcessedData(dataBatch);
       //dataBatch=gpu.getProcessedData();
       assertFalse(gpu.getTrainedData().contains(dataBatch));
       gpu.trainData();
       assertTrue(gpu.getTrainedData().contains(dataBatch));
    }

    public void testComplete() {
       assertFalse(gpu.getModel().getStatus()== Model.Status.Trained);
       assertFalse(gpu.isCompleted());
       gpu.complete();
       assertTrue(gpu.getModel().getStatus()== Model.Status.Trained);
       assertFalse(gpu.isCompleted());

    }

    public void testTestModel() {
       assertTrue(gpu.getModel().getStatus()==Model.Status.Trained);
       assertFalse(gpu.getModel().getResults()!= Model.Results.None);
       gpu.testModel();
       assertTrue(gpu.getModel().getStatus()==Model.Status.Tested);
       assertFalse(gpu.getModel().getResults() == Model.Results.None);
    }

    public void testUpdateTick() {
       long currentTicks = gpu.getTicks();
       gpu.updateTicks();
       long updatedTicks = gpu.getTicks();
       assertTrue(currentTicks==updatedTicks-1);
    }
}