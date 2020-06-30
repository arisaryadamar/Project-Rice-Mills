package com.project.ricemills.config;

import android.app.Application;
import android.text.TextUtils;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.toolbox.Volley;

public class AppController extends Application {

    private final String TAG = AppController.class.getSimpleName();

    private static AppController instance;

    RequestQueue mRequest;

    public static AppController getInstance() {
        return instance;
    }

    public void onCreate() {
        super.onCreate();
        instance = this;
    }

    private RequestQueue getmRequest(){
        if(mRequest == null){
            mRequest = Volley.newRequestQueue(getApplicationContext());
        }
        return mRequest;
    }

    public <I> void addToRequestQueue(Request<I> req, String tag){
        req.setTag(TextUtils.isEmpty(tag) ? TAG : tag);
        getmRequest().add(req);
    }

    public <I> void addToRequestQueue(Request<I> req){
        req.setTag(TAG);
        getmRequest().add(req);
    }

    public void cancelAllRequestQueue(Object req){
        if(mRequest != null){
            mRequest.cancelAll(req);
        }
    }
}
