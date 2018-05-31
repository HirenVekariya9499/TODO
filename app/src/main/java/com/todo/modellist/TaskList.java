package com.todo.modellist;

import com.todo.app.R;
import com.todo.app.TodoApplication;
import com.todo.common.NetworkConnectivity;
import com.todo.model.ModelDelegates;
import com.todo.model.TaskInfo;
import com.todo.servicehelper.ServiceHelper;
import com.todo.servicehelper.ServiceResponse;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;

import io.realm.Realm;

public class TaskList extends BaseList<TaskInfo> implements ServiceHelper.ServiceHelperDelegate {

    protected TaskList() {
        super(TaskInfo.class);
    }

    private static volatile TaskList _instance = null;
    public ModelDelegates.ModelDelegate<TaskInfo> m_delegate = null;

    Realm realm;

    public static TaskList Instance() {
        if (_instance == null) {
            synchronized (TaskList.class) {
                _instance = new TaskList();
            }
        }
        return _instance;
    }

    public void getAllTask(ModelDelegates.ModelDelegate<TaskInfo> delegate) {
        m_delegate = delegate;
        if (NetworkConnectivity.isConnected()) {
            ServiceHelper helper = new ServiceHelper(ServiceHelper.GET_TASK, ServiceHelper.RequestMethod.GET);
            helper.call(this);
        } else if (m_delegate != null)
            m_delegate.ModelLoadFailedWithError(TodoApplication.getContext().getString(R.string.internet_connection));
    }


    @Override
    public void CallFinish(ServiceResponse res) {
        m_modelList = new ArrayList<TaskInfo>();
        try {
            if (res.RawResponse != null) {
                m_modelList = new ArrayList<TaskInfo>();
                JSONArray taskArr = new JSONArray(res.RawResponse);
                if (taskArr != null && taskArr.length() > 0) {
                    realm = Realm.getDefaultInstance();
                    realm.beginTransaction();
                    realm.createAllFromJson(TaskInfo.class, taskArr);
                    realm.commitTransaction();
                    loadFromDB();
                    if (m_delegate != null)
                        m_delegate.ModelLoaded(m_modelList);
                } else {
                    if (m_delegate != null)
                        m_delegate.ModelLoadFailedWithError(TodoApplication.getContext().getString(R.string.no_data));
                }
            } else {
                if (m_delegate != null)
                    m_delegate.ModelLoadFailedWithError(TodoApplication.getContext().getString(R.string.try_again));
            }

        } catch (Exception e) {
            e.printStackTrace();
            if (m_delegate != null)
                m_delegate.ModelLoadFailedWithError(TodoApplication.getContext().getString(R.string.try_again));
        } finally {
            if (realm != null && realm.isInTransaction())
                realm.cancelTransaction();
        }
    }

    @Override
    public void CallFailure(String ErrorMessage) {
        if (m_delegate != null)
            m_delegate.ModelLoadFailedWithError(ErrorMessage);
    }
}
