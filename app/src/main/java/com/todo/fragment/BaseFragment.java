package com.todo.fragment;

import android.app.ProgressDialog;
import android.os.Build;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.SparseIntArray;

public class BaseFragment extends Fragment {

    ProgressDialog m_pd = null;
    boolean checkdialog = true;
    private SparseIntArray mErrorString;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        mErrorString = new SparseIntArray();

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            m_pd = new ProgressDialog(getActivity());
            m_pd.setCancelable(false);
            m_pd.setMessage("Please wait");
            m_pd.setIndeterminate(true);
        } else {
            m_pd = new ProgressDialog(getActivity());
            m_pd.setCancelable(false);
            m_pd.setMessage("Please wait");
            m_pd.setIndeterminate(true);
        }

    }

    public void showProgress() {
        if (m_pd != null) {
            m_pd.setMessage("Please wait");
            m_pd.setCancelable(false);
            checkdialog = false;
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

    protected void hideProgress() {
        if (m_pd != null) {
            try {
                m_pd.dismiss();
            } catch (Exception e) {
            }
            if (!checkdialog) {
                m_pd.dismiss();
                checkdialog = true;
            }
        }
    }

}