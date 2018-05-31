package com.todo.servicehelper;

import org.json.JSONException;
import org.json.JSONObject;

public class ServiceResponse {

    public String RawResponse;
    public boolean isSuccess = false;
    public String Message = "";
    public int Tag = 0;
    public boolean isException = false;
    public int ErrorCode = 0;
    public int statuscode = 0;
    public String TAG = "";

    public int getStatusCode() {
        return statuscode;
    }

    public boolean isSuccess() {
        try {
            JSONObject main = new JSONObject(RawResponse);
            boolean is_error = main.optBoolean("is_error");
            return !is_error;
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return isSuccess;
    }

    public String getErrorMessage() {
        try {
            JSONObject main = new JSONObject(RawResponse);
            String result = main.optString("message");
            return result;
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return "";
    }

    public String GetSuccessMessage() {
        try {
            JSONObject main = new JSONObject(RawResponse);
            String result = main.optString("message");
            return result;
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return "";
    }

}
