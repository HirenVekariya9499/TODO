package com.todo.servicehelper;

import android.os.Handler;
import android.os.Handler.Callback;
import android.os.Message;

import com.android.volley.AuthFailureError;
import com.android.volley.DefaultRetryPolicy;
import com.android.volley.NetworkResponse;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonArrayRequest;
import com.android.volley.toolbox.JsonObjectRequest;
import com.todo.app.TodoApplication;
import com.todo.common.NetworkConnectivity;
import com.todo.common.Utils;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

public class ServiceHelper {

  public enum RequestMethod {
    GET, POST, PUT, DELETE
  }

  JSONObject jsonParam = new JSONObject();
  Map<String, byte[]> multipart_params = new HashMap<>();
  Map<String, String> volley_params = new HashMap<String, String>();
  Map<String, Float> volley_params_float = new HashMap<String, Float>();

  public static final String APIURL = "http://techwatts.com/";

  public static final String GET_TASK = "test/tasks.json";


  String token;
  public static final String COMMON_ERROR = "Could not connect to server, please try again later";
  public static final String COMMON_Success = "Success";

  String m_methodName = null;
  private ServiceHelperDelegate m_delegate = null;
  private String TAG = "";
  private int STATUSCODE = 0;
  public static int REQUEST_TIMEOUT = 100000;
  public RequestMethod RequestMethodType = RequestMethod.GET;


  public interface ServiceHelperDelegate {
    public void CallFinish(ServiceResponse res);

    public void CallFailure(String ErrorMessage);
  }

  public interface VolleyDelegate {
    public void VolleyMessage(String res);
  }

  public ServiceHelper(String method, RequestMethod requestMethod) {
    multipart_params = new HashMap<>();
    volley_params = new HashMap<String, String>();
    volley_params_float = new HashMap<String, Float>();
    m_methodName = method;
    RequestMethodType = requestMethod;
    jsonParam = new JSONObject();
  }

  public String getFinalUrl() {
    StringBuilder sb = new StringBuilder();
    sb.append(APIURL);
    sb.append(m_methodName.toString());
    String temp = sb.toString();
    if (temp.contains(" ")) {
      temp = temp.replaceAll(" ", "%20");
    }
    return temp;
  }

  public void call(ServiceHelperDelegate delegate) {
    m_delegate = delegate;
    if (NetworkConnectivity.isConnected()) {
      CallService();
    }
  }

  private void callJsonRequest(final VolleyDelegate vdelegate) {
    final StringBuilder builder = new StringBuilder();
    try {
      int m;
      if (RequestMethodType == RequestMethod.GET) {
        m = Request.Method.GET;
      } else if (RequestMethodType == RequestMethod.PUT) {
        m = Request.Method.PUT;
      } else if (RequestMethodType == RequestMethod.DELETE) {
        m = Request.Method.DELETE;
      } else {
        m = Request.Method.POST;
      }

      String url;
      url = getFinalUrl();
      Utils.LogInfo("URL -> " + url);
      Utils.LogInfo("PARAM -> " + jsonParam.toString());

      JsonArrayRequest responce = new JsonArrayRequest(m, url, new JSONArray(), new Response.Listener<JSONArray>() {
        @Override
        public void onResponse(JSONArray response) {
          Utils.LogInfo(response.toString());
          builder.append(response);
          if (builder.toString().trim() == null || builder.toString().trim().length() <= 0) {
            builder.append("{\"is_error\":true,\"message\":\"" + COMMON_ERROR
              + "\"}");
            Utils.LogInfo("Failed to download file");
            if (vdelegate != null)
              vdelegate.VolleyMessage(builder.toString());
          } else {
            if (vdelegate != null)
              vdelegate.VolleyMessage(builder.toString());
          }
        }
      }, new Response.ErrorListener() {
        @Override
        public void onErrorResponse(VolleyError error) {
          if (error != null && error.networkResponse != null) {
            int statusCode = error.networkResponse.statusCode;
            NetworkResponse response = error.networkResponse;
            builder.append(new String(response.data));
          }
          Utils.LogError("ERROR --> --> " + error.getClass().getName());
          if (builder.toString().trim() == null || builder.toString().trim().length() <= 0) {
            builder.append("{\"is_error\":true,\"message\":\"" + COMMON_ERROR
              + "\"}");
            Utils.LogInfo("Failed to download file");
          }
          if (vdelegate != null)
            vdelegate.VolleyMessage(builder.toString());
        }
      }) {
        @Override
        protected Response<JSONArray> parseNetworkResponse(NetworkResponse response) {
          int mStatusCode = response.statusCode;
          STATUSCODE = mStatusCode;
          return super.parseNetworkResponse(response);
        }


      };
      responce.setRetryPolicy(new DefaultRetryPolicy(
        REQUEST_TIMEOUT,
        DefaultRetryPolicy.DEFAULT_MAX_RETRIES,
        DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));

      TodoApplication.getInstance().addToRequestQueue(responce, m_methodName);
    } catch (Exception e) {
      e.printStackTrace();
      builder.append("{\"is_error\":true,\"message\":\"" + COMMON_ERROR
        + "\"}");
      if (vdelegate != null)
        vdelegate.VolleyMessage(builder.toString());
    }
  }


  private void CallService() {
    String strResponse = "";
    new Thread(new Runnable() {

      @Override
      public void run() {

        callJsonRequest(new VolleyDelegate() {
          @Override
          public void VolleyMessage(String res) {
            Utils.LogError("response --> " + res);
            ServiceResponse response = new ServiceResponse();
            response.RawResponse = res;
            response.statuscode = STATUSCODE;
            response.TAG = TAG;
            Message m = new Message();
            m.obj = response;
            hand.sendMessage(m);
          }


        });

      }
    }).start();
  }


  Handler hand = new Handler(new Callback() {
    @Override
    public boolean handleMessage(Message msg) {
      if (msg != null) {
        if (msg.obj != null) {
          final ServiceResponse response = (ServiceResponse) msg.obj;
          if (m_delegate != null) {
            if (response.getStatusCode() == 200 || response.getStatusCode() == 204)
              m_delegate.CallFinish(response);
            else
              m_delegate.CallFailure(response.getErrorMessage());
          }
        }
      }
      return false;
    }
  });

}
