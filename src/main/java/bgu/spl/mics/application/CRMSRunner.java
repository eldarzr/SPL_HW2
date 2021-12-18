package bgu.spl.mics.application;

import bgu.spl.mics.MessageBusImpl;
import bgu.spl.mics.MicroService;
import bgu.spl.mics.application.objects.*;
import bgu.spl.mics.application.services.CPUService;
import bgu.spl.mics.application.services.GPUService;
import bgu.spl.mics.application.services.StudentService;
import bgu.spl.mics.application.services.TimeService;
import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.util.ArrayList;
import java.util.List;

import static bgu.spl.mics.application.objects.Data.Type.Images;
import static bgu.spl.mics.application.objects.GPU.Type.RTX3090;
import static bgu.spl.mics.application.objects.Student.Degree.MSc;
import static com.sun.org.apache.xalan.internal.xsltc.compiler.util.Type.Text;

/** This is the Main class of Compute Resources Management System application. You should parse the input file,
 * create the different instances of the objects, and run the system.
 * In the end, you should output a text file.
 */
public class CRMSRunner {
    public static void main(String[] args) {

        String path = "/home/spl211/SPL_HW2/example_input.json";
        FileParser fp = new FileParser(path);
        runThreads(fp);
        fp.exportFile();
        // parser("/home/spl211/SPL_HW2/example_input.json");

/*
        List<Model> models1 = new ArrayList<>();
        Student s1 = new Student("Simba","Computer Science", MSc,0,0);
        s1.addModel(new Model("1YOLO10",new Data(Images,0,200000),s1));
        s1.addModel(new Model("1ResNet9000",new Data(Images,0,200000),s1));
        s1.addModel(new Model("1hjjj",new Data(Images,0,200000),s1));

        StudentService studentService1 = new StudentService("student1",s1);
        List<Model> models2 = new ArrayList<>();
        Student s2 = new Student("Simba","Computer Science", MSc,0,0);
        s2.addModel(new Model("2YOLO10",new Data(Images,0,200000),s2));
        s2.addModel(new Model("2ResNet9000",new Data(Images,0,200000),s2));
        s2.addModel(new Model("2hjnjnjk",new Data(Images,0,200000),s2));

        StudentService studentService2 = new StudentService("student2",s2);

        GPU gpu1 = new GPU(RTX3090);
        CPU cpu1 = new CPU(16);
        CPU cpu2 = new CPU(16);
        GPUService gpuService1 = new GPUService("111",gpu1);
        GPU gpu2 = new GPU(RTX3090);
        GPUService gpuService2 = new GPUService("222",gpu2);
        CPUService cpuService1 = new CPUService("",cpu1);
        CPUService cpuService2 = new CPUService("",cpu2);
        TimeService timeService = new TimeService(2,9999,"");
        Thread t1 = new Thread(studentService1);
        Thread t5 = new Thread(studentService2);
        Thread t2 = new Thread(gpuService1);
        Thread t6 = new Thread(gpuService2);
        Thread t3 = new Thread(cpuService1);
        Thread t7 = new Thread(cpuService2);
        Thread t4 = new Thread(timeService);

        MessageBusImpl messageBus = MessageBusImpl.getInstance();
        messageBus.register(studentService1);
        messageBus.register(studentService2);
        messageBus.register(gpuService1);
        messageBus.register(gpuService2);
        messageBus.register(cpuService1);
        messageBus.register(cpuService2);
        messageBus.register(timeService);
        try {
            Thread.sleep(1000);
        } catch (InterruptedException e) {

        }
        t2.start();
        t6.start();
        t3.start();
        t7.start();
        t1.start();
        t5.start();
        t4.start();

*/
/*        try {
            Thread.sleep(1000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }*//*

        //t4.interrupt();
        System.       File input = new File(path);
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
                Student s = new Student(name,department, status,0,0);
                JsonArray jsonArrayOfModels = studentObject.get("models").getAsJsonArray();
                for(JsonElement modelsEle : jsonArrayOfModels){
                    JsonObject modelObject = modelsEle.getAsJsonObject();
                    String modelName = modelObject.get("name").getAsString();
                    String modelType = modelObject.get("type").getAsString();
                    int modelSize = modelObject.get("size").getAsInt();
                    Data data = new Data(modelType,modelSize);
                    Model m = new Model(modelName,data,s);
                    s.addModel(m);
                }
            }


        } catch (FileNotFoundException e) {
            System.err.println("Error : input file not found");
            e.printStackTrace();
        }
        catch (Exception e) {
            System.err.println("Error in processing the input file");
            e.printStackTrace();
        }
    }out.println("Hello World!");
*/

    }

    private static void runThreads(FileParser fp) {
        MessageBusImpl messageBus = MessageBusImpl.getInstance();
        ArrayList<MicroService> allMicroServices = fp.get_allMicroServices();
        ArrayList<Thread> allThreads = new ArrayList<>();
        for(MicroService e : allMicroServices){
            Thread t = new Thread(e);
            allThreads.add(t);
            messageBus.register(e);
        }
        for(Thread t : allThreads){
            t.start();
        }
        for(Thread t : allThreads){
            try {
                t.join();
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }

    }
}

