package com.todo.app;

import android.annotation.SuppressLint;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.widget.EditText;

import com.todo.common.Utils;
import com.todo.model.BaseModel;
import com.todo.model.TaskInfo;

import butterknife.BindView;
import butterknife.ButterKnife;

public class AddTodoActivity extends BaseActivity {

    @BindView(R.id.edtName)
    EditText edtName;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_todo);
        ButterKnife.bind(this);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setTitle(getResources().getString(R.string.add_task));
    }

    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.add_todo_menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.action_save:
                addTodo();
                break;
            case android.R.id.home:
                onBackPressed();
                break;

        }
        return super.onOptionsItemSelected(item);
    }


    // Add task in database
    @SuppressLint("StaticFieldLeak")
    public void addTodo(){
        if(isValidate()){
            new AsyncTask<Void,Void,Void>(){

                @Override
                protected Void doInBackground(Void... voids) {
                    int nextId = BaseModel.Instance().autoIncrementID(TaskInfo.class,"taskId");
                    TaskInfo info = new TaskInfo();
                    info.setTaskId(nextId);
                    info.setTaskName(edtName.getText().toString());
                    info.setState("pending");
                    BaseModel.Instance().addObject(TaskInfo.class,info);
                    return null;
                }

                @Override
                protected void onPostExecute(Void aVoid) {
                    super.onPostExecute(aVoid);
                    finish();
                }
            }.execute();
        }
    }

    // Check validations for add task
    public boolean isValidate(){
        if(edtName.getText().toString().length()<=0){
            Utils.ShowToastMessage(getResources().getString(R.string.error_first_name));
            return  false;
        }else{
            return true;
        }
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        overridePendingTransition(R.anim.right_in, R.anim.right_out);
    }
}
