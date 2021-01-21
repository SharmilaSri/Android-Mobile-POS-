package com.retailx.dreamdx.retailx.apicalls;

import android.content.Context;
import android.graphics.Bitmap;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.Map;

public interface ApiCalls  {

    String connectTONetworkPostMultipart(final Context ctx, String url, final int type,final Map<String, String> params, Bitmap bitmap);

    String connectToNetWorkWithJson(final Context ctx, String url, final int type,JSONArray jArray) ;

    String connectToNetWorkWithJsonObject(final Context ctx, String url, final int type, JSONObject jObject);

    String connectToNetworkGet(Context ctx, String url, int type);

    String connectToNetworkPost(Context ctx, String url, int type, final Map<String, String> params);
}
