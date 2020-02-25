package com.example.abdel.projectmanager.adapters;

import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.example.abdel.projectmanager.Project;
import com.example.abdel.projectmanager.ProjectViewModel;
import com.example.abdel.projectmanager.R;
import com.example.abdel.projectmanager.activities.ProjectActivity;

import java.text.DateFormat;
import java.util.Date;
import java.util.List;

public class ProjectsRecyclerAdapter extends RecyclerView.Adapter<ProjectsRecyclerAdapter.RecyclerViewHolder>{


    //Fields
    private List<Project> mDataset;
    private Context context;


    //Constructor
    public ProjectsRecyclerAdapter(Context context) {

        this.context = context;

    }


    //Inner Class
    public class RecyclerViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {

        public TextView projectName;
        public TextView projectDesc;
        public TextView tasks;
        public TextView daysLeft;
        public ProgressBar progress;
        public TextView percentage;
        final ProjectsRecyclerAdapter mAdapter;


        //Constructor
        public RecyclerViewHolder(View itemview, ProjectsRecyclerAdapter adapter) {
            super(itemview);
            projectName = itemView.findViewById(R.id.Project_Name);
            projectDesc = itemView.findViewById(R.id.Project_Description);
            tasks = itemView.findViewById(R.id.Project_Tasks);
            daysLeft = itemview.findViewById(R.id.days_left);
            progress = itemview.findViewById(R.id.progressBar);
            percentage = itemview.findViewById(R.id.project_progress);

            this.mAdapter = adapter;
            itemView.setOnClickListener(this);
        }

        @Override
        public void onClick(View view) {
            // Get the position of the item that was clicked.
            int mPosition = getLayoutPosition();

            Project project = mDataset.get(mPosition);

            Intent intent = new Intent(context, ProjectActivity.class);
            intent.putExtra("EXTRA_ID", project.getID());
            intent.putExtra("EXTRA_NAME", project.getName());

            context.startActivity(intent);
        }
    }

    @Override
    public ProjectsRecyclerAdapter.RecyclerViewHolder onCreateViewHolder(ViewGroup parent,
                                                                         int viewType) {
        // create a new view
        View itemview = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.project_card, parent, false);

        RecyclerViewHolder vh = new RecyclerViewHolder(itemview, this);
        return vh;
    }


    @Override
    public void onBindViewHolder(RecyclerViewHolder holder, int position) {
        // - get element from your dataset at this position
        // - replace the contents of the view with that element
        if(mDataset != null){
            final Project mCurrent = mDataset.get(position);

            holder.projectName.setText(mCurrent.getName());
            holder.projectDesc.setText(mCurrent.getDescription());

            int completedTasks = mCurrent.getCompletedTasks();
            int totalTasks = mCurrent.getTotalTasks();

            holder.tasks.setText(completedTasks + "/" + totalTasks);

            int progress;
            float percentage;

            if (totalTasks != 0)
                percentage = completedTasks * 100 / totalTasks;
            else
                percentage = 0;

            progress = Math.round(percentage);

            holder.progress.setProgress(progress);

            holder.percentage.setText(progress + "%");

            if(progress == 100)
                holder.percentage.setTextColor(Color.parseColor("#2ecc71"));
            else
                holder.percentage.setTextColor(Color.parseColor("#c44569"));

            Date startDate = mCurrent.getStartDate();
            Date endDate = mCurrent.getDueDate();
            long now = System.currentTimeMillis();

            holder.daysLeft.setTextColor(Color.parseColor("#808080"));

            if(completedTasks == totalTasks && totalTasks > 0){
                holder.daysLeft.setTextColor(Color.parseColor("#2ecc71"));
            }else {
                holder.daysLeft.setTextColor(Color.parseColor("#808080"));
            }

            if(mCurrent.getCompletedTasks() == mCurrent.getTotalTasks() && mCurrent.getTotalTasks() != 0){
                holder.daysLeft.setText("Completed on " + DateFormat.getDateInstance().format(new Date()));
            }
            else if(startDate!=null && now < startDate.getTime()){
                long diff = startDate.getTime() - now;
                holder.daysLeft.setText(getTimeMessage(diff,1));
            }
            else if(endDate == null && startDate != null && now > startDate.getTime()){
                long diff = now - startDate.getTime();
                holder.daysLeft.setText(getTimeMessage(diff,2));
            }
            else if(endDate != null && now < endDate.getTime()){
                long diff = endDate.getTime() - now;
                holder.daysLeft.setText(getTimeMessage(diff,3));
            }
            else if(endDate != null && now > endDate.getTime()){
                if(completedTasks != totalTasks){
                    long diff = now - endDate.getTime();
                    holder.daysLeft.setText("Project " + getTimeMessage(diff,4));
                    holder.daysLeft.setTextColor(Color.parseColor(context.getString(R.string.overdue)));
                }else{
                    if(totalTasks>0){
                        holder.daysLeft.setText("Completed on " + DateFormat.getDateInstance().format(endDate));
                        holder.daysLeft.setTextColor(Color.parseColor("#2ecc71"));
                    }
                    else{
                        holder.daysLeft.setText("Ended on " + DateFormat.getDateInstance().format(endDate));
                    }
                }

            }
            else{
                holder.daysLeft.setText("No date specified");
            }

        }else{
            //holder.projectName.setText(mCurrent.getName());
        }

    }//END OF ON BIND VIEW HOLDER



    //------------------------------------MESSAGE DISPLAYED IN PROJECT DATE-------------------------------//
    public static String getTimeMessage(long diff, int type){

        String msg = "";

        long seconds = diff / 1000;
        long minutes = seconds / 60;
        long hours = minutes / 60;
        long days = hours / 24;

        if(type == 1){
            if(days < 1)
                msg = "only " + hours + " hours left to start";
            else if(days > 1){
                msg = "only " + days + " days left to start";
            }
            else
                msg = "only " + days + " day left to start";

        }
        else if(type == 2){
            if(days < 1)
                msg = "started " + hours + " hours ago";
            else if(days > 1){
                msg = "started " + days + " days ago";
            }
            else
                msg = "started " + days + " day ago";
        }
        else if(type == 3){
            if(days < 1)
                msg = hours + " hours left to finish";
            else if(days > 1){
                msg = "only " + days + " days left";
            }
            else
                msg = "only " + days + " day left to finish";
        }
        else if(type == 4){
            if(days < 1)
                msg = "is overdue " + hours + " hours late";
            else if(days > 1){
                msg = "is overdue " + days + " days late";
            }
            else
                msg = "is overdue " + days + " day late";
        }

        return msg;
    }



    public void setData(List<Project> projects){
        mDataset = projects;
        notifyDataSetChanged();
    }


    @Override
    public int getItemCount() {
        if (mDataset != null)
            return mDataset.size();
        else return 0;
    }

}
