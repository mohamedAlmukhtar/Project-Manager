package com.example.abdel.projectmanager.adapters;

import android.annotation.TargetApi;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.Paint;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.view.ActionMode;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.daimajia.swipe.SwipeLayout;
import com.daimajia.swipe.adapters.RecyclerSwipeAdapter;
import com.example.abdel.projectmanager.Project;
import com.example.abdel.projectmanager.ProjectAndAllTasks;
import com.example.abdel.projectmanager.ProjectViewModel;
import com.example.abdel.projectmanager.R;
import com.example.abdel.projectmanager.Task;
import com.example.abdel.projectmanager.TaskViewModel;
import com.example.abdel.projectmanager.activities.TaskActivity;

import java.text.DateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import static com.example.abdel.projectmanager.adapters.ProjectsRecyclerAdapter.getTimeMessage;

public class SwipeRecyclerViewAdapter extends RecyclerSwipeAdapter<SwipeRecyclerViewAdapter.SimpleViewHolder> {

    private int projectID;
    private CallBackInterface mCallBack;
    private static final String LOG_TAG = "My Activity";
    private List<Task> TaskList;
    private TaskViewModel mTaskViewModel;
    private ProjectViewModel mProjectViewModel;
    private Context mContext;
    private Project project;

    private boolean multiSelect = false;
    private ArrayList<Task> selectedItems = new ArrayList<>();

    public interface CallBackInterface{

        /**
         * Callback invoked when clicked
         * @param task - the position
         */
        void onHandleSelection(Task task);
    }

    public SwipeRecyclerViewAdapter(Context context,ProjectViewModel projectViewModel , TaskViewModel taskViewModel, int projectID) {
        this.mContext = context;
        mProjectViewModel = projectViewModel;
        mTaskViewModel = taskViewModel;
        this.projectID = projectID;

        // .. Attach the interface
        try{
            mCallBack = (CallBackInterface) context;
        }catch(ClassCastException ex){
            ex.printStackTrace();
        }
    }


    @Override
    public SimpleViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.task_card, parent, false);
        View progressview = LayoutInflater.from(parent.getContext()).inflate(R.layout.activity_project, parent, false);
        return new SimpleViewHolder(view, progressview);
    }

    @Override
    public void onBindViewHolder(final SimpleViewHolder viewHolder, final int position) {

        //--------------------------------UPDATING VIEWS------------------------------------//

        if(TaskList != null) {
            final Task mCurrent = TaskList.get(position);

            if (selectedItems.contains(mCurrent)) {
                viewHolder.itemView.setBackgroundColor(Color.parseColor("#a5b1c2"));
            } else {
                viewHolder.itemView.setBackgroundColor(Color.WHITE);
            }

            String order = Integer.toString(position + 1);
            String taskPriority = mCurrent.getPriority().toString().toLowerCase();

            Date startDate = mCurrent.getStartDate();
            Date endDate = mCurrent.getDueDate();
            long now = System.currentTimeMillis();

            String priorityColor = "#a5b1c2";
            String dateColor = "#b2bec3";
            String statusColor = "#666666";
            int statusIcon = R.drawable.ic_checkmark_incomplete;

            viewHolder.taskTitle.setPaintFlags(viewHolder.taskTitle.getPaintFlags() & (~ Paint.STRIKE_THRU_TEXT_FLAG));

            if(mCurrent.getStatus().toString() == "IN_PROGRESS"){
                statusColor = "#fa8231";
                viewHolder.taskTitle.setPaintFlags(viewHolder.taskTitle.getPaintFlags() & (~ Paint.STRIKE_THRU_TEXT_FLAG));
            }
            else if(mCurrent.getStatus().toString() == "COMPLETED"){
                statusIcon = R.drawable.ic_checkmark_completed;
                statusColor = "#2ecc71";
                viewHolder.taskTitle.setPaintFlags(viewHolder.taskTitle.getPaintFlags() | Paint.STRIKE_THRU_TEXT_FLAG);
                dateColor = statusColor;
            }

            viewHolder.taskStatus.setImageResource(statusIcon);
            viewHolder.taskStatus.setColorFilter(Color.parseColor(statusColor));

            viewHolder.taskDueDate.setTextColor(Color.parseColor(dateColor));

            viewHolder.taskOrder.setText(order);
            viewHolder.taskTitle.setText(mCurrent.getTitle());
            viewHolder.taskDesc.setText("Description: " + mCurrent.getDescription());

            switch (taskPriority){
                case "low":
                    priorityColor = "#4b7bec";
                    break;
                case "medium":
                    priorityColor = "#fa8231";
                    break;
                case "high":
                    priorityColor = "#eb2f06";
                    break;
            }

            viewHolder.taskPriority.setText(taskPriority);
            viewHolder.taskPriority.setTextColor(Color.parseColor(priorityColor));

            if(mCurrent.getStatus().toString() == "COMPLETED"){
                viewHolder.taskDueDate.setText("Completed on " + DateFormat.getDateInstance().format(new Date()));
            }
            else if(startDate!=null && now < startDate.getTime()){
                long diff = startDate.getTime() - now;
                viewHolder.taskDueDate.setText(getTimeMessage(diff,1));
            }
            else if(endDate == null && startDate != null && now > startDate.getTime()){
                long diff = now - startDate.getTime();
                viewHolder.taskDueDate.setText(getTimeMessage(diff,2));
            }
            else if(endDate != null && now < endDate.getTime()){
                long diff = endDate.getTime() - now;
                viewHolder.taskDueDate.setText(getTimeMessage(diff,3));
            }
            else if(endDate != null && now > endDate.getTime()){
                if(mCurrent.getStatus().toString() != "COMPLETED"){
                    long diff = now - endDate.getTime();
                    viewHolder.taskDueDate.setText("Task " + getTimeMessage(diff,4));
                    viewHolder.taskDueDate.setTextColor(Color.parseColor(mContext.getString(R.string.overdue)));
                }else{
                    viewHolder.taskDueDate.setText("Completed on " + DateFormat.getDateInstance().format(endDate));
                }

            }
            else{
                viewHolder.taskDueDate.setText("No date specified");
            }


            if(mCurrent.getStatus().toString() == "NOT_STARTED"){
                viewHolder.progressSetter1.setBackgroundColor(Color.parseColor("#fa8231"));
                viewHolder.progressSetter2.setBackgroundColor(Color.parseColor("#09997D"));
            }
            else if(mCurrent.getStatus().toString() == "IN_PROGRESS"){
                viewHolder.progressSetter1.setBackgroundColor(Color.parseColor("#a5b1c2"));
                viewHolder.progressSetter2.setBackgroundColor(Color.parseColor("#09997D"));
            }
            else if(mCurrent.getStatus().toString() == "COMPLETED"){
                viewHolder.progressSetter1.setBackgroundColor(Color.parseColor("#a5b1c2"));
                viewHolder.progressSetter2.setBackgroundColor(Color.parseColor("#fa8231"));
            }


            //--------------------------------END OF UPDATING VIEWS------------------------------------//


            viewHolder.swipeLayout.setShowMode(SwipeLayout.ShowMode.PullOut);

            //dari kiri
            viewHolder.swipeLayout.addDrag(SwipeLayout.DragEdge.Left, viewHolder.swipeLayout.findViewById(R.id.bottom_wrapper1));

            //dari kanan
            viewHolder.swipeLayout.addDrag(SwipeLayout.DragEdge.Right, viewHolder.swipeLayout.findViewById(R.id.bottom_wraper));


            viewHolder.swipeLayout.addSwipeListener(new SwipeLayout.SwipeListener() {
                @Override
                public void onStartOpen(SwipeLayout layout) {

                }

                @Override
                public void onOpen(SwipeLayout layout) {

                }

                @Override
                public void onStartClose(SwipeLayout layout) {

                }

                @Override
                public void onClose(SwipeLayout layout) {

                }

                @Override
                public void onUpdate(SwipeLayout layout, int leftOffset, int topOffset) {

                }

                @Override
                public void onHandRelease(SwipeLayout layout, float xvel, float yvel) {

                }
            });


            //--------------------------------CLICK ON ITEM-------------------------------//
            viewHolder.swipeLayout.getSurfaceView().setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {

                    if (multiSelect) {
                        if (selectedItems.contains(mCurrent)) {
                            selectedItems.remove(mCurrent);
                            viewHolder.itemView.setBackgroundColor(Color.WHITE);
                        } else {
                            selectedItems.add(mCurrent);
                            viewHolder.itemView.setBackgroundColor(Color.parseColor("#a5b1c2"));
                        }
                    }else {

                        // Change the word in the mWordList.

                        Intent intent = new Intent(mContext, TaskActivity.class);
                        intent.putExtra("EXTRA_LABEL", "Task");
                        intent.putExtra("EXTRA_PRO", projectID);
                        intent.putExtra("EXTRA_ID", mCurrent.getID());
                        intent.putExtra("EXTRA_NAME", mCurrent.getTitle());
                        intent.putExtra("EXTRA_PRIORITY", mCurrent.getPriority().toString().toLowerCase());
                        intent.putExtra("EXTRA_STATUS", mCurrent.getStatus().getCode());
                        if (mCurrent.getStartDate() != null)
                            intent.putExtra("EXTRA_START", mCurrent.getStartDate().toString());
                        if (mCurrent.getDueDate() != null)
                            intent.putExtra("EXTRA_END", mCurrent.getDueDate().toString());
                        intent.putExtra("EXTRA_DESC", mCurrent.getDescription());

                        mContext.startActivity(intent);
                    }
                }
            });


            //---------------------------------------LONG PRESS ON ITEM---------------------------------------//
            viewHolder.swipeLayout.getSurfaceView().setOnLongClickListener(new View.OnLongClickListener() {

                @Override
                public boolean onLongClick(View v) {
                    ((AppCompatActivity)v.getContext()).startSupportActionMode(actionModeCallbacks);

                    return false;
                }


                //--------------------------ACTION MODE CALLBACKS------------------------//
                private ActionMode.Callback actionModeCallbacks = new ActionMode.Callback() {
                    @Override
                    public boolean onCreateActionMode(ActionMode mode, Menu menu) {
                        multiSelect = true;
                        menu.add("Delete Tasks");
                        return true;
                    }

                    @Override
                    public boolean onPrepareActionMode(ActionMode mode, Menu menu) {
                        return false;
                    }


                    //---------------------------CLICK ON ACTION MENU ITEM---------------------------//
                    @Override
                    public boolean onActionItemClicked(final ActionMode mode, MenuItem item) {

                        if(selectedItems.size()>0) {
                            AlertDialog.Builder builder = new AlertDialog.Builder(mContext);
                            builder.setTitle(R.string.delete_task_title)
                                    .setMessage("Are you sure you want to delete " + selectedItems.size() + " task(s)")
                                    .setPositiveButton(R.string.delete, new DialogInterface.OnClickListener() {
                                        public void onClick(DialogInterface dialog, int id) {

                                            for (int i = 0; i < selectedItems.size(); i++) {

                                                //--------------delete selected items------------//
                                                mItemManger.removeShownLayouts(viewHolder.swipeLayout);
                                                mTaskViewModel.deleteTask(selectedItems.get(i));
                                                mProjectViewModel.setTotalTasks(-1, projectID);
                                                if (selectedItems.get(i).getStatus() == Task.Status.COMPLETED) {
                                                    mProjectViewModel.setCompletedTasks(-1, projectID);
                                                }
                                                notifyItemRangeChanged(position, TaskList.size());
                                                mItemManger.closeItem(position);

                                            }
                                            mode.finish();
                                        }
                                    })
                                    .setNegativeButton(R.string.cancel, new DialogInterface.OnClickListener() {
                                        public void onClick(DialogInterface dialog, int id) {

                                        }
                                    }).show();
                        }else {
                            Toast.makeText(mContext,"Nothing Selected", Toast.LENGTH_SHORT).show();
                        }


                        return true;
                    }

                    @Override
                    public void onDestroyActionMode(ActionMode mode) {
                        multiSelect = false;
                        selectedItems.clear();
                        notifyDataSetChanged();
                    }
                };
            });


            //-----------------------------------PROGRESS BUTTON 1-----------------------------------//
            viewHolder.progressSetter1.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {

                    Task.Status status;

                    if(mCurrent.getStatus().toString() == "NOT_STARTED"){
                        status = Task.Status.IN_PROGRESS;
                        Toast.makeText(v.getContext(), "In progress", Toast.LENGTH_SHORT).show();
                    }
                    else{
                        status = Task.Status.NOT_STARTED;
                        Toast.makeText(v.getContext(), "Not started", Toast.LENGTH_SHORT).show();
                    }

                    if(mCurrent.getStatus().getCode() == 2){
                        mProjectViewModel.setCompletedTasks(-1, projectID);
                    }

                    Task task = new Task(mCurrent.getID(), mCurrent.getTitle(), mCurrent.getStartDate(), mCurrent.getDueDate(), mCurrent.getPriority(), status, mCurrent.getDescription());
                    task.setProjectID(projectID);
                    mTaskViewModel.update(task);
                    mItemManger.closeItem(position);
                }
            });


            //-----------------------------------PROGRESS BUTTON 2-----------------------------------//
            viewHolder.progressSetter2.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {

                    Task.Status status;

                    if(mCurrent.getStatus().toString() == "COMPLETED"){
                        status = Task.Status.IN_PROGRESS;
                        Toast.makeText(v.getContext(), "In progress", Toast.LENGTH_SHORT).show();
                        mProjectViewModel.setCompletedTasks(-1, projectID);
                    }
                    else{
                        status = Task.Status.COMPLETED;
                        Toast.makeText(v.getContext(), "Completed", Toast.LENGTH_SHORT).show();
                        mProjectViewModel.setCompletedTasks(1, projectID);
                    }



                    Task task = new Task(mCurrent.getID(), mCurrent.getTitle(), mCurrent.getStartDate(), mCurrent.getDueDate(), mCurrent.getPriority(), status, mCurrent.getDescription());
                    task.setProjectID(projectID);
                    mTaskViewModel.update(task);
                    mItemManger.closeItem(position);
                }
            });


            //-----------------------------------EDIT TASK BUTTON-----------------------------------//
            viewHolder.Edit.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    if(mCallBack != null){
                        mCallBack.onHandleSelection(mCurrent);
                    }
                    notifyItemChanged(position);
                    mItemManger.closeItem(position);
                }
            });


            //-----------------------------------DELETE TASK BUTTON-----------------------------------//
            viewHolder.Delete.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    AlertDialog.Builder builder = new AlertDialog.Builder(mContext);
                    builder.setTitle(R.string.delete_task_title)
                            .setMessage("Are you sure you want to delete this task: " + mCurrent.getTitle())
                            .setPositiveButton(R.string.delete, new DialogInterface.OnClickListener() {
                                public void onClick(DialogInterface dialog, int id) {
                                    mItemManger.removeShownLayouts(viewHolder.swipeLayout);
                                    mTaskViewModel.deleteTask(TaskList.get(position));
                                    mProjectViewModel.setTotalTasks(-1,projectID);
                                    if (TaskList.get(position).getStatus() == Task.Status.COMPLETED){
                                        mProjectViewModel.setCompletedTasks(-1,projectID);
                                    }
                                    notifyItemRangeChanged(position, TaskList.size());
                                    mItemManger.closeItem(position);
                                    //Toast.makeText(v.getContext(), "Deleted " + viewHolder.taskTitle.getText().toString(), Toast.LENGTH_SHORT).show();
                                }
                            })
                            .setNegativeButton(R.string.cancel, new DialogInterface.OnClickListener() {
                                public void onClick(DialogInterface dialog, int id) {

                                }
                            }).show();


                }
            });

            mItemManger.bindView(viewHolder.itemView, position);

        }else{
            viewHolder.recyclerView.setVisibility(View.GONE);
            viewHolder.emptyText.setVisibility(View.VISIBLE);
        }
    }//END OF ON BIND VIEW HOLDER


    public void setData(ProjectAndAllTasks tasks){
        if(tasks != null){
            TaskList = tasks.getTasks();
            project = tasks.project;
            notifyDataSetChanged();
        }


    }

    @Override
    public int getItemCount() {
        if(TaskList != null){
            return TaskList.size();
        }else {
            return 0;
        }

    }

    @Override
    public int getSwipeLayoutResourceId(int position) {
        return R.id.swipe;
    }


    //--------------------------------VIEW HOLDER IN ADAPTER------------------------------//
    public class SimpleViewHolder extends RecyclerView.ViewHolder{

        //Fields
        public SwipeLayout swipeLayout;
        public RecyclerView recyclerView;
        public RelativeLayout emptyText;
        public TextView taskOrder;
        public TextView taskTitle;
        public TextView taskDesc;
        public TextView taskPriority;
        public TextView taskDueDate;
        public ImageView taskStatus;
        public ImageButton Delete;
        public ImageButton Edit;
        public ImageButton progressSetter1;
        public ImageButton progressSetter2;


        //Constructor
        public SimpleViewHolder(View itemView, View progressItem) {
            super(itemView);

            swipeLayout = (SwipeLayout) itemView.findViewById(R.id.swipe);
            recyclerView = (RecyclerView) itemView.findViewById(R.id.recyclerview);
            emptyText = (RelativeLayout) itemView.findViewById(R.id.empty_view);
            taskOrder = (TextView) itemView.findViewById(R.id.task_order);
            taskTitle = (TextView) itemView.findViewById(R.id.task_title);
            taskDesc = (TextView) itemView.findViewById(R.id.task_dueDate);
            taskPriority = (TextView) itemView.findViewById(R.id.task_priority);
            taskDueDate = (TextView) itemView.findViewById(R.id.task_dueDate);
            taskStatus = (ImageView) itemView.findViewById(R.id.task_status);
            Delete = (ImageButton) itemView.findViewById(R.id.Delete);
            Edit = (ImageButton) itemView.findViewById(R.id.Edit);
            progressSetter1 = (ImageButton) itemView.findViewById(R.id.progress_setter1);
            progressSetter2 = (ImageButton) itemView.findViewById(R.id.progress_setter2);
        }

    }
}
