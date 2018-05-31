package com.todo.app;

import android.annotation.SuppressLint;
import android.app.Application;
import android.content.Context;
import android.os.AsyncTask;
import android.text.TextUtils;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.toolbox.Volley;
import com.todo.common.Const;
import com.todo.model.BaseModel;
import com.todo.model.TaskInfo;
import com.todo.servicehelper.OkHttpStack;

import java.util.ArrayList;

import io.realm.Realm;
import io.realm.RealmConfiguration;
import okhttp3.OkHttpClient;


public class TodoApplication extends Application {

    private static TodoApplication _instance = null;
    public static final String TAG = TodoApplication.class
            .getSimpleName();

    private RequestQueue mRequestQueue;

    @Override
    public void onCreate() {
        super.onCreate();
        Realm.init(this);
        RealmConfiguration config = new RealmConfiguration.Builder().name(Const.DATABASE_NAME).schemaVersion(Const.DATABASE_VERSION)
                .deleteRealmIfMigrationNeeded().build();
        Realm.setDefaultConfiguration(config);


        new changeTaskStatus().execute();
    }

    private class changeTaskStatus extends AsyncTask<Void, Void, Void> {
        @Override
        protected Void doInBackground(Void... voids) {
            ArrayList<TaskInfo> arrInprogressTask = new ArrayList<>();
            arrInprogressTask = BaseModel.Instance().getAllObjectById(TaskInfo.class, "isProgress", true);
            if (arrInprogressTask != null && arrInprogressTask.size() > 0) {
                for (TaskInfo taskInfo : arrInprogressTask) {
                    TaskInfo.changeProgressValue(taskInfo, false);
                }
            }
            return null;
        }
    }


    public TodoApplication() {
        _instance = this;
    }

    public static Context getContext() {
        return _instance;
    }

    public RequestQueue getRequestQueue() {
        if (mRequestQueue == null) {
            mRequestQueue = Volley.newRequestQueue(getApplicationContext(), new OkHttpStack(new OkHttpClient()));
        }

        return mRequestQueue;
    }

    public <T> void addToRequestQueue(Request<T> req, String tag) {
        // set the default tag if tag is empty
        req.setTag(TextUtils.isEmpty(tag) ? TAG : tag);
        getRequestQueue().add(req);
    }

    public <T> void addToRequestQueue(Request<T> req) {
        req.setTag(TAG);
        getRequestQueue().add(req);
    }


    public static synchronized TodoApplication getInstance() {
        return _instance;
    }


}