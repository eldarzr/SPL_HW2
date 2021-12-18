package bgu.spl.mics.application.objects;

import bgu.spl.mics.MicroService;
import bgu.spl.mics.application.services.*;
import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;

import java.io.*;
import java.util.ArrayList;
import java.util.List;

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
                //JsonObject GPUObject = GpuElement.getAsJsonObject();
                String gpuName =GpuElement.getAsString();
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
               // JsonObject CPUObject = CpuElement.getAsJsonObject();
                //String gpuName =CpuElement.getAsString();
                int cpuCores =CpuElement.getAsInt();
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
                ConferenceService confS = new ConferenceService(confName,confInfo);
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

    public void exportFile() {
        try {
            FileWriter myWriter = new FileWriter("output.txt");
            String space = "    ";
            String space8 = space + space;
            String space12 = space8 + space;
            String space16 = space8 + space8;
            String space20 = space16 + space;
            String space24 = space12 + space12;


            myWriter.write("{\n" + space + "\"students\":[" + "\n");
            for (Student s : _allStudents) {
                myWriter.write(space8 + "{" + "\n");
                myWriter.write(space12 + "\"name\":" + "\"" + s.getName() + "\",\n");
                myWriter.write(space12 + "\"department\":" + "\"" + s.getDepartment() + "\",\n");
                myWriter.write(space12 + "\"status\":" + "\"" + s.getStatus().toString() + "\",\n");
                myWriter.write(space12 + "\"publications\":" + "\"" + s.getPublications() + "\",\n");
                myWriter.write(space12 + "\"papersRead\":" + "\"" + s.getPapersRead() + "\",\n");
                myWriter.write(space12 + "\"trainedModels\":" + "[");
                List<Model> trainedModels = s.getModelLists();
                for (Model m : trainedModels) {
                    if (m.getStatus() == Model.Status.Tested) {
                        myWriter.write("\n" + space16 + "{" + "\n" + space20 + "\"name\":" + "\"" + m.getName() + "\",\n");
                        myWriter.write(space20 + "\"data\":{" + "\n");
                        myWriter.write(space24 + "\"type\":" + "\"" + m.getData().getType().toString() + "\",\n");
                        myWriter.write(space24 + "\"size\":" + "\"" + m.getData().getSize() + "\"\n");
                        myWriter.write(space20 + "},\n");
                        myWriter.write(space20 + "\"status\":" + "\"" + m.getStatus().toString() + "\",\n");
                        myWriter.write(space20 + "\"results\":" + "\"" + m.getResults().toString() + "\"\n");
                        myWriter.write(space16 + "},\n" + space12);
                    }
                }
                myWriter.write("]\n");
                myWriter.write(space8 + "}\n");
            }
            myWriter.write(space + "],\n");

            myWriter.write(space + "\"conferences\":[");
            for (ConferenceService cs : _allConfService) {
                myWriter.write(space8 + "{" + "\n");
                myWriter.write(space12 + "\"name\":" + "\"" + cs.getConfrenceInformation().getName() + "\",\n");
                myWriter.write(space12 + "\"date\":" + "\"" + cs.getConfrenceInformation().getName() + "\",\n");
                myWriter.write(space12 + "\"publications:\":[\n");
                List<Model> confModels = cs.getConfrenceInformation().getModels();
                for (Model m : confModels) {
                        myWriter.write("\n" + space16 + "{" + "\n" + space20 + "\"name\":" + "\"" + m.getName() + "\",\n");
                        myWriter.write(space20 + "\"data\":{" + "\n");
                        myWriter.write(space24 + "\"type\":" + "\"" + m.getData().getType().toString() + "\",\n");
                        myWriter.write(space24 + "\"size\":" + "\"" + m.getData().getSize() + "\"\n");
                        myWriter.write(space20 + "},\n");
                        myWriter.write(space20 + "\"status\":" + "\"" + m.getStatus().toString() + "\",\n");
                        myWriter.write(space20 + "\"results\":" + "\"" + m.getResults().toString() + "\"\n");
                        myWriter.write(space16 + "},\n" + space12);
                }
                myWriter.write("]\n");
                myWriter.write(space8 + "}\n");
            }
            myWriter.write(space + "],\n");
            Cluster cluster =Cluster.getInstance();
            myWriter.write("\"cpuTimeUsed\":"+cluster.getTotalCpusTime()+",\n");
            myWriter.write("\"gpuTimeUsed\":"+cluster.getTotalGpusTime()+",\n");
            myWriter.write("\"batchesProcessed\":"+cluster.getTotalProcessedData()+",\n");


            myWriter.close();
            } catch(IOException e){
                System.out.println("An error occurred.");
                e.printStackTrace();
            }

        }
    }


