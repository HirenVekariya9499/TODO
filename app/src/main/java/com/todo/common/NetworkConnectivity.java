package com.todo.common;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;

import com.todo.app.TodoApplication;


public class NetworkConnectivity {

  public static boolean isConnected() {
    try {
      ConnectivityManager cm = (ConnectivityManager) TodoApplication
          .getContext()
          .getSystemService(Context.CONNECTIVITY_SERVICE);
      NetworkInfo netInfo = cm.getActiveNetworkInfo();

      if (netInfo != null && netInfo.isConnected()) {
        return true;
      } else {
        return false;
      }
    } catch (Exception e) {
      return false;
    }
  }

}
