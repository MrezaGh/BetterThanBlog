package com.td.mreza.betterthanblog;

import java.util.ArrayList;
import java.util.List;

public class NotificationCenter implements Subject {
    private List<Observer> observers;
    private List list;
    private boolean changed;
    private final Object MUTEX= new Object();

    public NotificationCenter(){
        this.observers=new ArrayList<>();
    }
    @Override
    public void register(Observer obj) {
        if(obj == null) throw new NullPointerException("Null Observer");
        synchronized (MUTEX) {
            if(!observers.contains(obj)) observers.add(obj);
        }
    }

    @Override
    public void unregister(Observer obj) {
        synchronized (MUTEX) {
            observers.remove(obj);
        }
    }

    @Override
    public void notifyObservers() {

        List<Observer> observersLocal = null;
        //synchronization is used to make sure any observer registered after list is received is not notified
        synchronized (MUTEX) {
            if (!changed)
                return;
            observersLocal = new ArrayList<>(this.observers);
            this.changed=false;
        }
        for (Observer obj : observersLocal) {
            obj.update();
        }

    }

    @Override
    public Object getUpdate(Observer obj) {
        return this.list;
    }

    //method to post list to the topic
    public void data_loaded(){
//        System.out.println("Message Posted to Topic:"+msg);

        this.changed=true;
        notifyObservers();
    }
}
