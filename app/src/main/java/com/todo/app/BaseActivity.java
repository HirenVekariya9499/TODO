package com.todo.app;

import android.app.ProgressDialog;
import android.os.Build;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;

public class BaseActivity extends AppCompatActivity {

  ProgressDialog m_pd = null;
  boolean checkdialog = true;

  @Override
  protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);

    m_pd = new ProgressDialog(this,R.style.AppCompatAlertDialogStyle);
    m_pd.setCancelable(false);
    m_pd.setMessage("Please wait");
    m_pd.setIndeterminate(true);
  }


  public void showProgress() {
    if (m_pd != null) {
      m_pd.setMessage("Please wait");
      m_pd.setCancelable(false);
      m_pd.show();
    }
  }

  protected void showProgress(String msg) {
    if (m_pd != null) {
      // m_pd.setCancelable(false);
      m_pd.setMessage(msg);
      checkdialog = false;
      m_pd.show();
    }
  }

  protected void showProgress(boolean flag, String msg) {
    if (m_pd != null) {
      m_pd.setCancelable(flag);
      m_pd.setMessage(msg);
      checkdialog = false;
      m_pd.show();
    }
  }

  public void showProgress(boolean flag) {
    if (m_pd != null) {
      m_pd.setMessage("Please wait");
      m_pd.setCancelable(flag);
      checkdialog = false;
      m_pd.show();
    }
  }

  public void hideProgress() {
    if (m_pd != null) {
      try {
        m_pd.dismiss();
      } catch (Exception e) {
      }
    }
  }


}