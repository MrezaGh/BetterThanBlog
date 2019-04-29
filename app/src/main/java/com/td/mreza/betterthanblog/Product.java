/**
 * Created by abahram77 on 4/28/2019.
 */
package com.td.mreza.betterthanblog;
public class Product {
    private String title;
    private String body;
    private String Description;
    public Product(String title, String body,String description){
        this.title=title;
        this.body=body;
        this.Description=description;
    }

    public String getBody() {
        return body;
    }

    public String getTitle() {
        return title;
    }

    public void setBody(String body) {
        this.body = body;
    }

    public void setTitle(String title) {
        this.title = title;

    }

    public String getDescription() {
        return Description;
    }

    public void setDescription(String description) {
        Description = description;
    }
}
