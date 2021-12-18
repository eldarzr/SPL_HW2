package bgu.spl.mics.application.objects;

import bgu.spl.mics.MicroService;
import bgu.spl.mics.application.services.*;
import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.util.ArrayList;

public class FileParser {
    String path;
    ArrayList<MicroService> _allMicroServices = new ArrayList<>();
    ArrayList<Student> _allStudents= new ArrayList<>();
    ArrayList<StudentService> _allStudentsService = new ArrayList<>();
    ArrayList<GPUService> _allGPUSService = new ArrayList<>();
    ArrayList<CPUService> _allCPUSService = new ArrayList<>();
    ArrayList<ConferenceService> _allConfService = new ArrayList<>();
    TimeService timeService;



    public FileParser(String path) {
        this.path = path;
        readInput(path);
    }

    private void readInput(String path) {
        File input = new File(path);
        try {
            JsonElement fileElement = JsonParser.parseReader(new FileReader(input));
            JsonObject fileObj = fileElement.getAsJsonObject();

            //students
            JsonArray jsonArrayOfStudents = fileObj.get("Students").getAsJsonArray();
            for (JsonElement studentElement : jsonArrayOfStudents){
                JsonObject studentObject = studentElement.getAsJsonObject();

                //data

                String name = studentObject.get("name").getAsString();
                String department = studentObject.get("department").getAsString();
                String status = studentObject.get("status").getAsString();
                Student student = new Student(name,department, status,0,0);
                _allStudents.add(student);
                JsonArray jsonArrayOfModels = studentObject.get("models").getAsJsonArray();
                for(JsonElement modelsEle : jsonArrayOfModels){
                    JsonObject modelObject = modelsEle.getAsJsonObject();
                    String modelName = modelObject.get("name").getAsString();
                    String modelType = modelObject.get("type").getAsString();
                    int modelSize = modelObject.get("size").getAsInt();
                    Data data = new Data(modelType,modelSize);
                    Model m = new Model(modelName,data,student);
                    student.addModel(m);
                }
                StudentService studentService = new StudentService("Student" + student.getName(),student);
                _allStudentsService.add(studentService);
                _allMicroServices.add(studentService);
            }
            //add GPUS :
            JsonArray jsonArrayOfGpus = fileObj.get("GPUS").getAsJsonArray();
            int numOfGPUS = 1;
            for (JsonElement GpuElement : jsonArrayOfGpus){
                JsonObject GPUObject = GpuElement.getAsJsonObject();
                String gpuName =GPUObject.getAsString();
                GPU gpu = new GPU(gpuName);
                GPUService gpuService = new GPUService("Gpu number :"+numOfGPUS,gpu);
                numOfGPUS++;
                _allGPUSService.add(gpuService);
                _allMicroServices.add(gpuService);

            }
            //add CPUS :
            JsonArray jsonArrayOfCpus = fileObj.get("CPUS").getAsJsonArray();
            int numOfCPUS = 1;
            for (JsonElement CpuElement : jsonArrayOfCpus){
                JsonObject CPUObject = CpuElement.getAsJsonObject();
                int cpuCores =CPUObject.getAsInt();
                CPU cpu = new CPU(cpuCores);
                CPUService cpuService = new CPUService("Cpu number :"+numOfCPUS,cpu);
                numOfGPUS++;
                _allCPUSService.add(cpuService);
                _allMicroServices.add(cpuService);

            }
            //add Conferences :
            JsonArray jsonArrayOfConference = fileObj.get("Conferences").getAsJsonArray();
            for (JsonElement confElement : jsonArrayOfConference) {
                JsonObject confObject = confElement.getAsJsonObject();

                String confName = confObject.get("name").getAsString();
                int date = confObject.get("date").getAsInt();
                ConfrenceInformation confInfo = new ConfrenceInformation(confName,date);
                ConferenceService confS = new ConferenceService(confInfo);
                _allConfService.add(confS);
                _allMicroServices.add(confS);

            }
            //Time Service :
            int tickTime =  fileObj.get("TickTime").getAsInt();
            int duration = fileObj.get("Duration").getAsInt();
            timeService = new TimeService(tickTime,duration,"Time Service");
            _allMicroServices.add(timeService);


        } catch (FileNotFoundException e) {
            System.err.println("Error : input file not found");
            e.printStackTrace();
        }
        catch (Exception e) {
            System.err.println("Error in processing the input file");
            e.printStackTrace();
        }
    }
    public ArrayList<MicroService> get_allMicroServices(){
        return _allMicroServices;
    }
}

