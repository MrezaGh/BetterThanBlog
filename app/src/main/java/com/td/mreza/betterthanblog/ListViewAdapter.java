package com.td.mreza.betterthanblog;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.List;

/**
 * Created by abahram77 on 4/28/2019.
 */

public class ListViewAdapter extends ArrayAdapter<Product>{
    public ListViewAdapter(Context context,int resource, List<Product> objects){
        super(context, resource, objects);

    }
    public View getView(int position, View convertView, ViewGroup parent){
        View v = convertView;
        if(v==null){
            LayoutInflater inflater= (LayoutInflater)getContext().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            v=inflater.inflate(R.layout.list_item,null);


        }
        Product product=getItem(position);
        TextView tit=(TextView)v.findViewById(R.id.title);
        TextView body=(TextView)v.findViewById(R.id.body);
        tit.setText(product.getTitle());
        body.setText(product.getBody());
        return v;

    }
}
