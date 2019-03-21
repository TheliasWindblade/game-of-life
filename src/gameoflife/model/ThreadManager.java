package gameoflife.model;

import javafx.concurrent.Task;

import java.util.ArrayList;

public class ThreadManager {
    private ArrayList<Thread> threads;
    private static ThreadManager instance = new ThreadManager();

    private ThreadManager(){
        threads=new ArrayList<>();
    }

    public void start(Task task){
        Thread thread=new Thread(task);
        thread.start();
        threads.add(thread);
    }

    public void stopAll(){
        for(Thread t : threads){
            t.interrupt();
        }
        threads.clear();
    }

    public static ThreadManager getInstance() {
        return instance;
    }

    public Thread getThread(int index){
        return threads.get(index);
    }

    @Override
    public String toString() {
        return threads.toString();
    }
}
