package bgu.spl.mics.application.objects;


import bgu.spl.mics.Callback;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.concurrent.Callable;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentLinkedQueue;

/**
 * Passive object representing the cluster.
 * <p>
 * This class must be implemented safely as a thread-safe singleton.
 * Add all the fields described in the assignment as private fields.
 * Add fields and methods to this class as you see fit (including public methods and constructors).
 */
public class Cluster {

	ConcurrentLinkedQueue<CPU> cpus;
	Collection<GPU> gpus;
	ConcurrentHashMap<DataBatch,Callback> calls;
	String statistics;
	Integer totalProcessedData;
	Integer totalCpusTime;
	Integer totalGpusTime;

	private static class ClusterHolder{
		private static Cluster instance = new Cluster();
	}


	/**
     * Retrieves the single instance of this class.
     */
	public static Cluster getInstance() {
		//TODO: Implement this
		return ClusterHolder.instance;
	}

	private Cluster(){
		gpus = new ArrayList<>();
		cpus = new ConcurrentLinkedQueue<>();
		calls = new ConcurrentHashMap<>();
		statistics = "";
		totalCpusTime=0;
		totalGpusTime=0;
		totalProcessedData=0;
	}

	public void sendData(DataBatch dataBatch, Callback<DataBatch> callback){
		CPU curentCPU;
		synchronized (cpus) {
			curentCPU = cpus.remove();
			cpus.add(curentCPU);
		}
		calls.put(dataBatch,callback);
		//synchronized (curentCPU) {
			curentCPU.addUnprocessedData(dataBatch);
		//}
	}

	public void sendProcessedData(DataBatch dataBatch) {
		calls.get(dataBatch).call(dataBatch);
		synchronized (totalCpusTime) {
			totalProcessedData++;
			totalCpusTime++;
		}
	}

	public void updateGpuTime() {
		synchronized (totalGpusTime) {
			totalGpusTime++;
		}
	}
	public void registerGPU(GPU gpu){
		gpus.add(gpu);
	}

	public void registerCPU(CPU cpu){
		cpus.add(cpu);
	}

	public ConcurrentLinkedQueue<CPU> getCpus() {
		return cpus;
	}

	public Integer getTotalProcessedData() {
		return totalProcessedData;
	}

	public Integer getTotalCpusTime() {
		return totalCpusTime;
	}

	public Integer getTotalGpusTime() {
		return totalGpusTime;
	}
}
