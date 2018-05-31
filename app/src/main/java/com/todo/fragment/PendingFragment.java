package com.todo.fragment;


import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.ActionMode;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.todo.adapter.TaskAdapter;
import com.todo.app.AddTodoActivity;
import com.todo.app.R;
import com.todo.app.TodoApplication;
import com.todo.common.AlertDialogHelper;
import com.todo.common.Const;
import com.todo.common.RecyclerItemClickListener;
import com.todo.common.Utils;
import com.todo.model.BaseModel;
import com.todo.model.TaskInfo;

import java.util.ArrayList;

import butterknife.BindView;
import butterknife.ButterKnife;
import io.realm.Realm;

/**
 * A simple {@link Fragment} subclass.
 */
public class PendingFragment extends BaseFragment implements AlertDialogHelper.AlertDialogListener {

  View view;
  TaskAdapter taskAdapter;
  @BindView(R.id.recyclerPendingTask)
  RecyclerView recyclerPendingTask;
  @BindView(R.id.txtNoTaskFound)
  TextView txtNoTaskFound;
  ActionMode mActionMode;
  Menu context_menu;
  boolean isMultiSelect = false;

  ArrayList<TaskInfo> arrTask = new ArrayList<>();
  ArrayList<TaskInfo> multiselect_list = new ArrayList<>();
  AlertDialogHelper alertDialogHelper;

  public PendingFragment() {
    // Required empty public constructor
  }

  @Override
  public View onCreateView(LayoutInflater inflater, ViewGroup container,
                           Bundle savedInstanceState) {
    // Inflate the layout for this fragment
    view = inflater.inflate(R.layout.fragment_pending, container, false);

    ButterKnife.bind(this, view);

    alertDialogHelper = new AlertDialogHelper(getActivity(), PendingFragment.this);

    // Listening the click events
    recyclerPendingTask.addOnItemTouchListener(new RecyclerItemClickListener(getActivity(), recyclerPendingTask,
      new RecyclerItemClickListener.OnItemClickListener() {
        @Override
        public void onItemClick(View view, int position) {
          if (isMultiSelect)
            multi_select(position);
        }

        @Override
        public void onItemLongClick(View view, int position) {
          if (!isMultiSelect) {
            multiselect_list = new ArrayList<TaskInfo>();
            isMultiSelect = true;

            if (mActionMode == null) {
              mActionMode = getActivity().startActionMode(mActionModeCallback);
            }
          }

          multi_select(position);

        }
      }));
    return view;
  }

  @Override
  public void onResume() {
    super.onResume();
    getDataFromDatabase();

    IntentFilter filterDesignerpick = new IntentFilter();
    filterDesignerpick.addAction(Const.PENDING_BRODCAST);
    getActivity().registerReceiver(updatePendingState, filterDesignerpick);
  }

  BroadcastReceiver updatePendingState = new BroadcastReceiver() {
    @Override
    public void onReceive(Context context, Intent intent) {
      getDataFromDatabase();
    }
  };

  @Override
  public void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    setHasOptionsMenu(true);
  }

  @Override
  public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
    // TODO Add your menu entries here
    inflater.inflate(R.menu.pending_menu, menu);
    super.onCreateOptionsMenu(menu, inflater);
  }

  @Override
  public boolean onOptionsItemSelected(MenuItem item) {
    int id = item.getItemId();
    if (id == R.id.action_add) {
      Intent intent = new Intent(getActivity(), AddTodoActivity.class);
      startActivity(intent);
      getActivity().overridePendingTransition(R.anim.left_in, R.anim.left_out);
    }
    return true;
  }


  public void multi_select(int position) {
    if (mActionMode != null) {
      if (multiselect_list.contains(arrTask.get(position)))
        multiselect_list.remove(arrTask.get(position));
      else {
        if (!arrTask.get(position).isProgress()) {
          multiselect_list.add(arrTask.get(position));
        } else {
          Utils.ShowToastMessage(getString(R.string.cant_select_task));
        }
      }
      if (multiselect_list.size() > 0)
        mActionMode.setTitle("" + multiselect_list.size());
      else
        mActionMode.finish();

      refreshAdapter();

    }
  }

  @Override
  public void setUserVisibleHint(boolean isVisibleToUser) {
    super.setUserVisibleHint(isVisibleToUser);
    if (mActionMode != null)
      mActionMode.finish();
  }

  public void refreshAdapter() {
    taskAdapter.selected_List = multiselect_list;
    taskAdapter.arrTaskInfo = arrTask;
    taskAdapter.notifyDataSetChanged();
  }

  private ActionMode.Callback mActionModeCallback = new ActionMode.Callback() {

    @Override
    public boolean onCreateActionMode(ActionMode mode, Menu menu) {
      // Inflate a menu resource providing context menu items
      MenuInflater inflater = mode.getMenuInflater();
      inflater.inflate(R.menu.delete_menu, menu);
      context_menu = menu;
      return true;
    }

    @Override
    public boolean onPrepareActionMode(ActionMode mode, Menu menu) {
      return false; // Return false if nothing is done
    }

    @Override
    public boolean onActionItemClicked(ActionMode mode, MenuItem item) {
      switch (item.getItemId()) {
        case R.id.action_delete:
          alertDialogHelper.showAlertDialog("", "Delete Task", "DELETE", "CANCEL", 1, false);
          return true;
        default:
          return false;
      }
    }

    @Override
    public void onDestroyActionMode(ActionMode mode) {
      mActionMode = null;
      isMultiSelect = false;
      multiselect_list = new ArrayList<TaskInfo>();
      refreshAdapter();
    }
  };


  private void getDataFromDatabase() {
    arrTask = BaseModel.Instance().getAllObjectById(TaskInfo.class, "state", "pending");
    if (arrTask != null && arrTask.size() > 0) {
      recyclerPendingTask.setVisibility(View.VISIBLE);
      txtNoTaskFound.setVisibility(View.GONE);
      taskAdapter = new TaskAdapter(getActivity(), arrTask, multiselect_list);
      recyclerPendingTask.setHasFixedSize(true);
      RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(getActivity(), LinearLayoutManager.VERTICAL, false);
      recyclerPendingTask.setLayoutManager(layoutManager);
      recyclerPendingTask.setItemAnimator(new DefaultItemAnimator());
      recyclerPendingTask.setAdapter(taskAdapter);
    } else {
      recyclerPendingTask.setVisibility(View.GONE);
      txtNoTaskFound.setVisibility(View.VISIBLE);
    }
  }

  @Override
  public void onPositiveClick(int from) {
    for (int i = 0; i < multiselect_list.size(); i++) {
      int id = multiselect_list.get(i).getTaskId();
      BaseModel.Instance().deleteObjectById(TaskInfo.class, "taskId", id);
    }
    multiselect_list = new ArrayList<>();
    getDataFromDatabase();
    if (mActionMode != null) {
      mActionMode.finish();
    }
  }

  @Override
  public void onNegativeClick(int from) {

  }

  @Override
  public void onNeutralClick(int from) {

  }
}
