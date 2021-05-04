package com.githubbrowser;

import android.content.Context;
import android.os.AsyncTask;

import com.android.volley.AuthFailureError;
import com.android.volley.DefaultRetryPolicy;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.HttpHeaderParser;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.githubbrowser.VolleyAPI.VolleyDataReceived;

import java.util.HashMap;
import java.util.Map;

public class VolleyResponse extends AsyncTask<String, Void, Void> {

    Context context;
    VolleyDataReceived dataReceived;
    String type;
    HashMap<String, String> hashMap;
    int requestMethod;
    boolean splashScreen;

    public VolleyResponse(Context context, VolleyDataReceived dataReceived, String type, HashMap<String, String> hashMap, String requestMethodType)
    {
        this.context=context;
        this.dataReceived=dataReceived;
        this.type=type;
        this.hashMap=hashMap;
        requestMethod=Request.Method.POST;
        if(requestMethodType.equals("GET"))
        {
            requestMethod= Request.Method.GET;
        }
    }

    public void setSplashScreen(boolean splashScreen)
    {
        this.splashScreen=splashScreen;
    }

    @Override
    protected Void doInBackground(String... strings) {

        if(!splashScreen) {
            Utilities.showAlertDialog(context);
        }

        StringRequest request=new StringRequest(requestMethod, strings[0], new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {

                Utilities.dismissAlertDialog();

                dataReceived.onDataReceived(response, type);

            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {

                Utilities.dismissAlertDialog();

                try
                {

                    String response=new String(error.networkResponse.data, HttpHeaderParser.parseCharset(error.networkResponse.headers, "utf-8"));

                    dataReceived.onDataReceived(response, type);

                } catch(Exception e)
                {
                    e.printStackTrace();
                }

            }
        })
        {
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                return hashMap;
            }

            @Override
            public Map<String, String> getHeaders() throws AuthFailureError {
                return super.getHeaders();
            }
        };

        request.setRetryPolicy(new DefaultRetryPolicy(Utilities.maxTimeOut, DefaultRetryPolicy.DEFAULT_MAX_RETRIES, DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));

        Volley.newRequestQueue(context).add(request);

        return null;
    }
}
