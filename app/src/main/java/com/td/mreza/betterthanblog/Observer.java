package com.td.mreza.betterthanblog;

/**
 * Created by abahram77 on 3/7/2019.
 */

public interface Observer {

    //method to update the observer, used by subject
    public void update();

    //attach with subject to observe
    public void setSubject(Subject sub);
}

