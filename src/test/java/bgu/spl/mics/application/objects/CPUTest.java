package bgu.spl.mics.application.objects;

import junit.framework.TestCase;
import org.junit.Before;
import org.junit.jupiter.api.BeforeEach;

public class CPUTest extends TestCase {

    public static CPU cpu;

    @BeforeEach
    public void setUp() throws Exception {
        super.setUp();
        cpu = new CPU();
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
        DataBatch dataBatch1 = cpu.ProcessData();
        int unpro = dataBatch1.getData().getProcessed();
        int pro = dataBatch.getData().getProcessed();
        assertFalse(pro == unpro);
    }

    public void testSendProcessedData() {
        Data data = new Data(Data.Type.Text, 0, 10000);
        DataBatch dataBatch = new DataBatch(0,data);
        cpu.addUnprocessedData(dataBatch);
        assertTrue(cpu.getData().contains(dataBatch));
        int size = cpu.getData().size();
        DataBatch dataBatch1 = cpu.ProcessData();
        assertTrue(dataBatch == dataBatch1);
        assertTrue(size == cpu.getData().size() +1);
    }
}