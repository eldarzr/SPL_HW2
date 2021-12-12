package bgu.spl.mics.application.services;

import bgu.spl.mics.Future;
import bgu.spl.mics.MicroService;
import bgu.spl.mics.application.messages.TrainModelEvent;
import bgu.spl.mics.application.objects.Model;
import bgu.spl.mics.application.objects.Student;
import jdk.net.SocketFlow;

/**
 * Student is responsible for sending the {@link TrainModelEvent},
 * {@link TestModelEvent} and {@link PublishResultsEvent}.
 * In addition, it must sign up for the conference publication broadcasts.
 * This class may not hold references for objects which it is not responsible for.
 *
 * You can add private fields and public methods to this class.
 * You MAY change constructor signatures and even add new public constructors.
 */
public class StudentService extends MicroService {
   private Student student;

    public StudentService(String name) {
        super("Student Service");
        // TODO Implement this
        this.student = student;

    }
    public StudentService(String name, Student student) {
        super("Student Service");
        // TODO Implement this
        this.student = student;

    }

    @Override
    protected void initialize() {
        // TODO Implement this
        startProcess();
    }

    public void startProcess(){
        Model model = student.getCurrentModel();
        Future<String> future = sendEvent(new TrainModelEvent(model));
        String result = future.get();
    }
}
