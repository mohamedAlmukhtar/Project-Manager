package com.example.abdel.projectmanager.adapters;

import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.example.abdel.projectmanager.R;
import com.example.abdel.projectmanager.Task;
import com.example.abdel.projectmanager.activities.TaskActivity;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;

public class OverViewAdapter extends RecyclerView.Adapter<OverViewAdapter.RecyclerViewHolder> {

    private final boolean upcoming;
    private Context context;
    private List<Task> mDataset;
    private ArrayList<String> mProjectNames = new ArrayList<>();

    public OverViewAdapter(Context context, boolean upcoming) {
        this.context = context;
        this.upcoming = upcoming;
    }

    public class RecyclerViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        // each data item is just a string in this case
        public TextView taskTitle;
        public TextView taskDays;
        public TextView taskDate;
        public View taskLine;
        final OverViewAdapter mAdapter;

        public RecyclerViewHolder(View itemview, OverViewAdapter adapter) {
            super(itemview);
            taskTitle = itemView.findViewById(R.id.upcoming_task_title);
            taskDays = itemView.findViewById(R.id.upcoming_task_days);
            taskDate = itemView.findViewById(R.id.upcoming_task_date);
            taskLine = itemview.findViewById(R.id.task_line);

            this.mAdapter = adapter;
            itemView.setOnClickListener(this);
        }

        @Override
        public void onClick(View v) {
            // Get the position of the item that was clicked.
            int mPosition = getLayoutPosition();

            // Use that to access the affected item in mWordList.
            Task task = mDataset.get(mPosition);
            // Change the word in the mWordList.

            Intent intent = new Intent(context, TaskActivity.class);
            intent.putExtra("EXTRA_LABEL", upcoming ? "Upcoming Task" : "Overdue Task");
            intent.putExtra("EXTRA_PRO", task.getProjectID());
            intent.putExtra("EXTRA_ID", task.getID());
            intent.putExtra("EXTRA_NAME", task.getTitle());
            intent.putExtra("EXTRA_PRIORITY", task.getPriority().toString().toLowerCase());
            intent.putExtra("EXTRA_STATUS", task.getStatus().getCode());
            if(task.getStartDate() != null)
                intent.putExtra("EXTRA_START", task.getStartDate().toString());
            if(task.getDueDate() != null)
                intent.putExtra("EXTRA_END", task.getDueDate().toString());
            intent.putExtra("EXTRA_DESC", task.getDescription());

            context.startActivity(intent);

        }
    }

    @NonNull
    @Override
    public OverViewAdapter.RecyclerViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        // create a new view
        View itemview = LayoutInflater.from(viewGroup.getContext())
                .inflate(R.layout.upcoming_task_card, viewGroup, false);



        RecyclerViewHolder vh = new RecyclerViewHolder(itemview, this);
        return vh;
    }

    @Override
    public void onBindViewHolder(@NonNull OverViewAdapter.RecyclerViewHolder holder, int position) {

        // - get element from your dataset at this position
        // - replace the contents of the view with that element
        if(mDataset != null) {

            final Task mCurrent = mDataset.get(position);

            holder.taskTitle.setText(mCurrent.getTitle());

            holder.taskDate.setText(new SimpleDateFormat("EE, MMM dd").format(mCurrent.getDueDate()));

            long now = System.currentTimeMillis();
            long diff;

            if(upcoming) {
                diff = mCurrent.getDueDate().getTime() - now;
            }else {
                holder.taskDays.setTextColor(Color.parseColor(context.getString(R.string.overdue)));
                holder.taskLine.setBackgroundColor(Color.parseColor(context.getString(R.string.overdue)));
                diff =  now - mCurrent.getDueDate().getTime();
            }


            long seconds = diff / 1000;
            long minutes = seconds / 60;
            long hours = minutes / 60;
            long days = hours / 24;

            if(days >= 1)
                holder.taskDays.setText(days + "d");
            else
                holder.taskDays.setText(hours + "h");
        }
    }

    public void setData(List<Task> tasks){
        mDataset = tasks;
        notifyDataSetChanged();
    }

    @Override
    public int getItemCount() {
        if (mDataset != null)
            return mDataset.size();
        else return 0;
    }
}
