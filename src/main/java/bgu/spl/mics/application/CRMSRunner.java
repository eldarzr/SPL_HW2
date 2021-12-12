package bgu.spl.mics.application;

import bgu.spl.mics.application.objects.Data;
import bgu.spl.mics.application.objects.GPU;
import bgu.spl.mics.application.objects.Model;
import bgu.spl.mics.application.objects.Student;
import bgu.spl.mics.application.services.GPUService;
import bgu.spl.mics.application.services.StudentService;
import bgu.spl.mics.application.services.TimeService;

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
    public static void main(String[] args)
    {

        List<Model> models = new ArrayList<>();
        Student s = new Student("Simba","Computer Science", MSc,0,0);
        s.addModel(new Model("YOLO10",new Data(Images,0,200000),s));
        s.addModel(new Model("ResNet9000",new Data(Images,0,200000),s));

        StudentService studentService = new StudentService("",s);

        GPU gpu = new GPU(RTX3090);
        GPUService gpuService = new GPUService("",gpu);
        TimeService timeService = new TimeService(2,9999,"");
        Thread t3 = new Thread(studentService);
        Thread t1 = new Thread(gpuService);
        Thread t2 = new Thread(timeService);
        t1.start();
        t2.start();
        t3.start();


        System.out.println("Hello World!");
    }
}
