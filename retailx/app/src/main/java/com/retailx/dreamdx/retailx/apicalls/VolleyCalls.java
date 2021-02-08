package com.retailx.dreamdx.retailx.apicalls;

import android.app.ProgressDialog;
import android.content.Context;
import android.graphics.Bitmap;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.NetworkResponse;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonArrayRequest;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.retailx.dreamdx.retailx.POJO.DataPart;
import com.retailx.dreamdx.retailx.utils.ConstantsUsed;


import org.json.JSONArray;
import org.json.JSONObject;

import java.io.ByteArrayOutputStream;
import java.util.HashMap;
import java.util.Map;

public class VolleyCalls implements ApiCalls {

    private Gson gson;
    private ProgressDialog progressDialog_;
    private ProgressDialog progressDialog1_;
    String response;


    public String connectToNetworkGet(final Context ctx, String url, final int type) {
        setUpGson();
        RequestQueue queue = Volley.newRequestQueue(ctx);
        // Request a string response from the provided URL.
        StringRequest stringRequest = new StringRequest(Request.Method.GET, url,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        try {
                            ResponseParser.PARSE_JSON(ctx, response, type,setUpGson());
                        } catch (Exception e) {
                            Toast.makeText(ctx, "Error", Toast.LENGTH_LONG).show();

                        } finally {
                           /* if (progressDialog1.isShowing())
                                progressDialog1.dismiss();*/
                        }

                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
               /* if (progressDialog1.isShowing())
                    progressDialog1.dismiss();*/
                Toast.makeText(ctx,"connectToNetworkGet"+ error.getMessage().toString(), Toast.LENGTH_LONG).show();

            }
        });

        // Add the request to the RequestQueue.
        queue.add(stringRequest);


        return response;
    }


    public String connectToNetworkPost(final Context ctx, String url, final int type, final Map<String, String> params) {
        setUpGson();
        RequestQueue queue = Volley.newRequestQueue(ctx);
        StringRequest sr = new StringRequest(Request.Method.POST, url, new Response.Listener<String>() {
            @Override
            public void onResponse(String res) {

                try {
                    response=res;
                    ResponseParser.PARSE_JSON(ctx, res, type,setUpGson());

                } catch (Exception e) {
                    Toast.makeText(ctx, "NETWORK"+e.getMessage().toString(), Toast.LENGTH_LONG).show();

                } finally {

                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {

                     Toast.makeText(ctx, error.toString(), Toast.LENGTH_LONG).show();
            }
        }) {
            @Override
            protected Map<String, String> getParams() {

                return params;
            }

        };

        queue.add(sr);

        return response;
    }


    public String connectToNetWorkWithJsonObject(final Context ctx, String url, final int type, JSONObject jObject) {

        setUpGson();

        RequestQueue queue = Volley.newRequestQueue(ctx);
        JsonObjectRequest jobReq = new JsonObjectRequest(Request.Method.POST, url, jObject,
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject jsonObject) {
                        try {
                            response=jsonObject.toString();
                            Toast.makeText(ctx,response.toString(),Toast.LENGTH_LONG).show();
                            // ResponseParser.PARSE_JSON(ctx, jsonObject.toString(), type,setUpGson());

                        } catch (Exception e) {
                            Toast.makeText(ctx, "Error", Toast.LENGTH_LONG).show();

                        } finally {
                           /* if (progressDialog1!=null && progressDialog1.isShowing()) {
                                progressDialog1.dismiss();
                            }*/

                        }
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError volleyError) {
                       /* if (progressDialog1!=null && progressDialog1.isShowing()) {
                            progressDialog1.dismiss();
                        }*/
                        Toast.makeText(ctx, "connectToNetWorkWithJson", Toast.LENGTH_LONG).show();
                    }
                });


        queue.add(jobReq);

        return response;
    }

    public String connectToNetWorkWithJson(final Context ctx, String url, final int type, JSONArray jObject) {

        setUpGson();

        RequestQueue queue = Volley.newRequestQueue(ctx);
        JsonArrayRequest jobReq = new JsonArrayRequest(Request.Method.POST, url, jObject,
                new Response.Listener<JSONArray>() {
                    @Override
                    public void onResponse(JSONArray jsonArray) {
                        try {
                            response=jsonArray.toString();
                            ResponseParser.PARSE_JSON(ctx, jsonArray.toString(), type,setUpGson());

                        } catch (Exception e) {
                            Toast.makeText(ctx, "Error", Toast.LENGTH_LONG).show();

                        } finally {
                            /*if (progressDialog1.isShowing())
                                progressDialog1.dismiss();*/
                        }
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError volleyError) {
                       /* if (progressDialog1.isShowing())
                            progressDialog1.dismiss();*/
                        Toast.makeText(ctx, "NETWORK ERROR"+volleyError.toString()
                                , Toast.LENGTH_LONG).show();
                    }
                });


        queue.add(jobReq);

        return response;
    }

    public String connectTONetworkPostMultipart(
            final Context ctx, String url, final int type,
            final Map<String, String> params, final Bitmap bitmap) {

        //our custom volley request
        VolleyMultipartRequest volleyMultipartRequest = new VolleyMultipartRequest(Request.Method.POST, ConstantsUsed.URL_POST_IMAGE,
                new Response.Listener<NetworkResponse>() {
                    @Override
                    public void onResponse(NetworkResponse response) {
                           ResponseParser.PARSE_JSON(ctx, new String(response.data), type,setUpGson());

                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        //Toast.makeText(ctx, "out of memeory error", Toast.LENGTH_SHORT).show();
                    }
                }) {

            /*
             * If you want to add more parameters with the image
             * you can do it here
             * here we have only one parameter with the image
             * which is tags
             * */
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {

                return params;
            }

            /*
             * Here we are passing image by renaming it with a unique name
             * */
            @Override
            protected Map<String, DataPart> getByteData() {
                Map<String, DataPart> params = new HashMap<>();
                long imagename = System.currentTimeMillis();
                params.put("image", new DataPart(imagename + ".png", getFileDataFromDrawable(bitmap)));
                return params;
            }
        };

        //adding the request to volley
        Volley.newRequestQueue(ctx).add(volleyMultipartRequest);


        return "";

    }


    /*
     * The method is taking Bitmap as an argument
     * then it will return the byte[] array for the given bitmap
     * and we will send this array to the server
     * here we are using PNG Compression with 80% quality
     * you can give quality between 0 to 100
     * 0 means worse quality
     * 100 means best quality
     * */
    public byte[] getFileDataFromDrawable(Bitmap bitmap) {
        ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
        bitmap.compress(Bitmap.CompressFormat.PNG, 80, byteArrayOutputStream);
        return byteArrayOutputStream.toByteArray();
    }



    private Gson setUpGson() {
        GsonBuilder gsonBuilder = new GsonBuilder();
        gsonBuilder.setDateFormat("M/d/yy hh:mm a");
        gson = gsonBuilder.create();
        return gson;
    }

}
