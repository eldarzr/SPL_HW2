package bgu.spl.mics.application.objects;

import junit.framework.TestCase;
import org.junit.Before;
import org.junit.jupiter.api.BeforeEach;

public class CPUTest extends TestCase {

    public static CPU cpu;
    public static GPU gpu;
    @BeforeEach
    public void setUp() throws Exception {
        super.setUp();
        //cpu = new CPU();
        cpu=new CPU(16);
        gpu = new GPU("RTX3090");
    }


    public void tearDown() throws Exception {
    }

    public void testAddUnprocessedData() {
        Data data = new Data(Data.Type.Text, 0, 10000);
        DataBatch dataBatch = new DataBatch(0,data);
        assertFalse(cpu.getData().contains(dataBatch));
        cpu.addUnprocessedData(dataBatch);
        assertTrue(cpu.getData().contains(dataBatch));
    }

    public void testProcessData() {
        Data data = new Data(Data.Type.Text, 0, 10000);
        DataBatch dataBatch = new DataBatch(0,data);
        cpu.addUnprocessedData(dataBatch);
        Cluster cluster = Cluster.getInstance();
        cpu.registerCluster();
        gpu.registerCluster();
        cluster.sendData(dataBatch,db -> {
            gpu.getProcessedData();
        });
        assertFalse(dataBatch.isProcessed());
        for(int i=0;i<100;i++){
            cpu.updateTicks();
        }
        cpu.ProcessData();
/*        int unpro = dataBatch1.getData().getProcessed();
        int pro = dataBatch.getData().getProcessed();*/
        assertTrue(dataBatch.isProcessed() == true);
    }

    public void testSendProcessedData() {
        Data data = new Data(Data.Type.Text, 0, 10000);
        DataBatch dataBatch = new DataBatch(0,data);
        int unprocessedSize= dataBatch.getData().getProcessed();
        cpu.addUnprocessedData(dataBatch);
        Cluster cluster = Cluster.getInstance();
        cpu.registerCluster();
        gpu.registerCluster();
        cluster.sendData(dataBatch,db -> {
            gpu.sendUnprocessedData();
        });
        assertFalse(dataBatch.isProcessed());
        for(int i=0;i<100;i++){
            cpu.updateTicks();
        }
        assertTrue(cpu.getData().contains(dataBatch));
        cpu.ProcessData();
        assertTrue(cpu.getData().size() == 0);
        assertFalse(cpu.getData().contains(dataBatch));
        int processedSize = dataBatch.getData().getProcessed();
        assertTrue(unprocessedSize == processedSize - 1000);
    }
}