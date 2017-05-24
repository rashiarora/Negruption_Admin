package com.codeogic.negruption_admin;

import android.content.Context;
import android.content.Intent;
import android.support.annotation.LayoutRes;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.toolbox.Volley;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Admin on 24-05-2017.
 */

public class ApproveAdapter extends ArrayAdapter<StoryBean> {

    Context context;
    int resource,views=0,newView=0;
    ArrayList<StoryBean> storyList;
    StoryBean story;
    public ApproveAdapter( Context context, int resource,  ArrayList<StoryBean> objects) {
        super(context, resource, objects);
        this.context = context;
        this.resource = resource;
        storyList = objects;
    }

    @NonNull
    @Override
    public View getView(final int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        View view = null;
        LayoutInflater inflater = LayoutInflater.from(context);
        view = inflater.inflate(resource,parent,false);

        TextView txtName = (TextView)view.findViewById(R.id.txtVName);
        TextView txtTitle = (TextView)view.findViewById(R.id.txtVStoryTitle);
        TextView txtDescription = (TextView)view.findViewById(R.id.txtVStoryDesc);

        TextView txtReadMore = (TextView)view.findViewById(R.id.txtVViewMore);

        story = storyList.get(position);
        txtName.setText(story.getUsername());
        txtTitle.setText(story.getStoryTitle());
        txtDescription.setText(story.getStoryDesc());
        txtReadMore.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(v.getId() == R.id.txtVViewMore){
                    StoryBean storyBean = storyList.get(position);
                    Log.i("ApproveStory",story.toString());
                    Toast.makeText(getContext(),"Read More",Toast.LENGTH_LONG).show();
                    Intent intent = new Intent(context,ApproveStoryActivity.class);
                    intent.putExtra("keyApproveStory",storyBean);
                    context.startActivity(intent);
                }
            }
        });

        return view;
    }
}
