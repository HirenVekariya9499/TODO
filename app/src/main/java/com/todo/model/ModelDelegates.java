package com.todo.model;

import android.graphics.Bitmap;

import java.util.ArrayList;

import io.realm.RealmObject;


public class ModelDelegates {


  public interface ModelDelegate<T extends RealmObject> {
    public void ModelLoaded(ArrayList<T> list);

    public void ModelLoadFailedWithError(String error);
  }

}
