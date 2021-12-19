package bgu.spl.mics.application.services;

import bgu.spl.mics.Callback;
import bgu.spl.mics.Event;
import bgu.spl.mics.Future;
import bgu.spl.mics.MicroService;
import bgu.spl.mics.application.messages.*;
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

    private class ModelEvent implements Event<Model> {
        @Override
        public <T> Future<T> getFuture() {
            return null;
        }
    }


   private Student student;

    public StudentService(String name) {
        super("Student Service" + name);
        // TODO Implement this
        subscribeBroadcast(CancelBroadcast.class, c -> terminate());
        subscribeEvent(ModelEvent.class, e -> startProcess());
        subscribeBroadcast(PublishConferenceBroadcast.class, cb ->{
            for (Model m: cb.getModels()) {
                if(m.getStudent() == student)
                    student.addPublication();
                else student.addPapersRead();
            }
        });
    }
    public StudentService(String name, Student student) {
        super("Student Service");
        // TODO Implement this
        this.student = student;
        subscribeBroadcast(CancelBroadcast.class, c -> terminate());
        subscribeEvent(ModelEvent.class, e -> startProcess());
        subscribeBroadcast(PublishConferenceBroadcast.class, cb ->{
            for (Model m: cb.getModels()) {
                if(m.getStudent() == this.student)
                    this.student.addPublication();
                else this.student.addPapersRead();
            }
        });
    }

    @Override
    protected void initialize() {
        // TODO Implement this
        //startProcess();
        sendEvent(new ModelEvent());
    }

    public void startProcess(){
        if (student.hasNext()) {
            Model model = student.getCurrentModel();
            System.out.println(getName() + "send the model " + model.getName());
            //System.out.println("student " + model.getName());
            Future<String> future = sendEvent(new TrainModelEvent(model));
            if(future == null)
                return;
            String status = future.get();
            if(status.equals(""))
                return;
                //Thread.currentThread().interrupt();
            model.setStatus(status);
            System.out.println("model " + model.getName() + " is " + status);
            future = sendEvent(new TestModelEvent(model));
            if(future == null)
                return;
            status = future.get();
            if(status.equals(""))
                return;
                //Thread.currentThread().interrupt();
            model.setStatus(status);
            System.out.println("model " + model.getName() + " is " + status);
            if(model.isModelGood())
                sendEvent(new PublishResultsEvent(model));
            sendEvent(new ModelEvent());
        }
    }
}
