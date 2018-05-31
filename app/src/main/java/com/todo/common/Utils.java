package com.todo.common;

import android.app.ProgressDialog;
import android.content.Context;
import android.os.Environment;
import android.support.design.widget.Snackbar;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.FrameLayout;
import android.widget.Toast;

import com.todo.app.TodoApplication;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileWriter;
import java.text.SimpleDateFormat;
import java.util.Date;

import static android.content.Context.INPUT_METHOD_SERVICE;

/**
 * The class contains common utility functions which helps whole application
 */
public class Utils {
  private static volatile Utils _instance = null;

  /**
   * Get the Instance of the Utils Class
   * * @return Utils Object
   */
  public static Utils Instance() {
    if (_instance == null) {
      synchronized (Utils.class) {
        _instance = new Utils();
      }
    }
    return _instance;
  }

  ProgressDialog m_pd = null;

  public static void LogError(String message) {
    Log.e(Const.TAG, message);
  }

  public static void LogInfo(String message) {
    Log.e(Const.TAG, message);
  }

  public static void LogVerbose(String message) {
    Log.v(Const.TAG, message);
  }

  public static void LogException(Exception ex) {
    Log.d(Const.TAG, ex.getMessage() + "\n" + ex);
  }

  public static void LogDebug(String message) {
    Log.d(Const.TAG, message);
  }


  public static void ShowMessage(View parentLayout, String message) {
    if (message.length() > 0) {
      Snackbar snack = Snackbar.make(parentLayout, message, Snackbar.LENGTH_LONG);
      View view = snack.getView();
      FrameLayout.LayoutParams params = (FrameLayout.LayoutParams) view.getLayoutParams();
      params.gravity = Gravity.TOP;
      view.setLayoutParams(params);
      snack.show();
    }
  }

  public static void ShowToastMessage(String message) {
    if (message.length() > 0) {
      Toast.makeText(TodoApplication.getContext(), message, Toast.LENGTH_LONG).show();
    }
  }

  public static void hideKeyboard(View view, Context context) {
    if (view != null) {
      InputMethodManager inputMethodManager = (InputMethodManager) context.getSystemService(INPUT_METHOD_SERVICE);
      inputMethodManager.hideSoftInputFromWindow(view.getWindowToken(), 0);
    }
  }
}

