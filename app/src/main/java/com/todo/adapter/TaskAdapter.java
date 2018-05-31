package com.todo.adapter;

import android.app.Activity;
import android.content.Intent;
import android.os.CountDownTimer;
import android.support.constraint.ConstraintLayout;
import android.support.v4.content.ContextCompat;
import android.support.v4.content.res.ResourcesCompat;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.todo.app.R;
import com.todo.common.Const;
import com.todo.model.BaseModel;
import com.todo.model.TaskInfo;

import java.util.ArrayList;
import java.util.HashMap;

import butterknife.BindView;
import butterknife.ButterKnife;
import io.realm.Realm;


public class TaskAdapter extends RecyclerView.Adapter<TaskAdapter.MyViewHolder> {

    public ArrayList<TaskInfo> arrTaskInfo = new ArrayList<>();
    public ArrayList<TaskInfo> selected_List = new ArrayList<>();
    public HashMap<Integer, CountDownTimer> mapCountDown = new HashMap<Integer, CountDownTimer>();
    Activity context;

    public class MyViewHolder extends RecyclerView.ViewHolder {

        @BindView(R.id.txtTaskName)
        TextView txtTaskName;
        @BindView(R.id.constMain)
        ConstraintLayout constMain;
        @BindView(R.id.imgState)
        ImageView imgState;
        @BindView(R.id.imgUndo)
        ImageView imgUndo;
        @BindView(R.id.viewLine)
        View viewLine;

        public MyViewHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
        }
    }

    public TaskAdapter(Activity context, ArrayList<TaskInfo> list, ArrayList<TaskInfo> selectedlist) {
        this.arrTaskInfo = list;
        this.selected_List = selectedlist;
        this.context = context;
    }

    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent,
                                           int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.task_list_item, parent, false);

        MyViewHolder myViewHolder = new MyViewHolder(view);
        return myViewHolder;
    }

    @Override
    public void onBindViewHolder(final MyViewHolder holder, final int listPosition) {
        final TaskInfo taskInfo = arrTaskInfo.get(listPosition);

        if (selected_List.contains(arrTaskInfo.get(listPosition))) {
            holder.constMain.setBackgroundColor(ContextCompat.getColor(context, R.color.list_item_selected_state));
        } else {
            holder.constMain.setBackgroundColor(ContextCompat.getColor(context, R.color.list_item_normal_state));
        }

        if (taskInfo != null) {
            holder.imgState.setVisibility(View.VISIBLE);
            holder.imgUndo.setVisibility(View.GONE);
            if (taskInfo.isProgress()) {
                holder.imgState.setVisibility(View.GONE);
                holder.imgUndo.setVisibility(View.VISIBLE);
            } else if (taskInfo.getState().equals("completed")) {
                holder.imgState.setImageDrawable(ResourcesCompat.getDrawable(context.getResources(), R.drawable.ic_complete, null));
            } else {
                holder.imgState.setImageDrawable(ResourcesCompat.getDrawable(context.getResources(), R.drawable.ic_pending, null));
            }
            holder.txtTaskName.setText(taskInfo.getTaskName());

            holder.constMain.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    holder.imgState.setVisibility(View.GONE);
                    holder.imgUndo.setVisibility(View.VISIBLE);
                    TaskInfo.changeProgressValue(taskInfo, true);
                    updateState(taskInfo, listPosition);
                }
            });

            holder.imgUndo.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (mapCountDown.containsKey(listPosition)) {
                        CountDownTimer countDownTimer = mapCountDown.get(listPosition);
                        if (countDownTimer != null) {
                            countDownTimer.cancel();
                        }
                        mapCountDown.remove(listPosition);
                    }

                    TaskInfo.changeProgressValue(taskInfo, false);
                    holder.imgState.setVisibility(View.VISIBLE);
                    holder.imgUndo.setVisibility(View.GONE);
                }
            });
            if (listPosition % 5 == 0) {
                holder.viewLine.setBackgroundColor(context.getResources().getColor(R.color.colorOne));
            } else if (listPosition % 5 == 1) {
                holder.viewLine.setBackgroundColor(context.getResources().getColor(R.color.colorTwo));
            } else if (listPosition % 5 == 2) {
                holder.viewLine.setBackgroundColor(context.getResources().getColor(R.color.colorThree));
            } else if (listPosition % 5 == 3) {
                holder.viewLine.setBackgroundColor(context.getResources().getColor(R.color.colorFour));
            } else if (listPosition % 5 == 4) {
                holder.viewLine.setBackgroundColor(context.getResources().getColor(R.color.colorFive));
            } else {
                holder.viewLine.setBackgroundColor(context.getResources().getColor(R.color.colorOne));
            }
        }
    }

    private void updateState(final TaskInfo taskInfo, final int position) {
        CountDownTimer countDownTimer;
        countDownTimer = new CountDownTimer(5000, 5000) {
            @Override
            public void onTick(long l) {

            }

            @Override
            public void onFinish() {
                if (mapCountDown.containsKey(position)) {
                    mapCountDown.remove(position);
                }
                TaskInfo.changeProgressValue(taskInfo, false);
                if (taskInfo.getState().equals("pending")) {
                    updatePending(taskInfo);
                } else {
                    updateCompleted(taskInfo);
                }

            }
        };
        countDownTimer.start();
        mapCountDown.put(position, countDownTimer);
    }

    /**
     * // updating fragment UI through broadcast receiver
     */
    private void callBrodcast() {
        Intent PendingBroadcast = new Intent();
        PendingBroadcast.setAction(Const.PENDING_BRODCAST);
        context.sendBroadcast(PendingBroadcast);
        Intent CompletedBroadcast = new Intent();
        CompletedBroadcast.setAction(Const.COMPLETED_BRODCAST);
        context.sendBroadcast(CompletedBroadcast);
    }

    private void updateCompleted(final TaskInfo taskInfo) {
        Realm realm = Realm.getDefaultInstance();
        realm.executeTransaction(new Realm.Transaction() {
            @Override
            public void execute(Realm realm) {
                taskInfo.setState("pending");
                BaseModel.Instance().addObject(TaskInfo.class, taskInfo);
                callBrodcast();
                arrTaskInfo.remove(taskInfo);
                notifyDataSetChanged();
            }
        });
    }

    private void updatePending(final TaskInfo taskInfo) {
        Realm realm = Realm.getDefaultInstance();
        realm.executeTransaction(new Realm.Transaction() {
            @Override
            public void execute(Realm realm) {
                taskInfo.setState("completed");
                BaseModel.Instance().addObject(TaskInfo.class, taskInfo);
                callBrodcast();
                arrTaskInfo.remove(taskInfo);
                notifyDataSetChanged();
            }
        });
    }

    @Override
    public int getItemCount() {
        return arrTaskInfo.size();
    }
}
