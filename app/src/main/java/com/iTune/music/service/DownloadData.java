package com.iTune.music.service;

import android.app.ProgressDialog;
import android.content.Context;
import android.os.AsyncTask;
import android.os.Handler;
import android.os.Message;
import android.util.Log;

import com.iTune.music.data.DataHandler;
import com.iTune.music.data.TrackData;
import com.iTune.music.util.AppUtill;
import com.google.gson.Gson;

import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;

/**
 * Created by ist on 25/8/16.
 */

public class DownloadData {

    public static final int DONE = 0x201;
    public static final int ERROR = 0x202;

    private String URL;
    private Handler mHandler;
    private Context mContext;


    public DownloadData(Context context,String url, Handler handler) {
        this.URL = url;
        this.mHandler = handler;
        this.mContext = context;
        new TrackDownloadere().execute();
    }

    private class TrackDownloadere extends AsyncTask<String, String, String> {

        TrackData trackDatas = null;
        ProgressDialog progressDialog =null;

        /**
         * Before starting background thread Show Progress Bar Dialog
         */
        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            progressDialog = AppUtill.showProgressDialog(mContext,"Loading","Please Wait...");
        }

        /**
         * Downloading file in background thread
         */
        @Override
        protected String doInBackground(String... f_url) {
            URL url;
            HttpURLConnection urlConnection = null;
            try {
                url = new URL("" + URL);

                urlConnection = (HttpURLConnection) url
                        .openConnection();

                InputStream in = urlConnection.getInputStream();

                InputStreamReader isw = new InputStreamReader(in);

                String response = AppUtill.readData(isw);
//                System.out.println(" JSON Data " + response);

                try {
                    trackDatas = new Gson().fromJson(response,TrackData.class);
                    if(trackDatas!=null) {
                        DataHandler.Single.INSTANCE.getInstance().setTrackData(null);
                        DataHandler.Single.INSTANCE.getInstance().setTrackData(trackDatas);
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                    return "Error";
                }
                if (response != null) {
                    in.close();
                    return "Success";
                }

            } catch (Exception e) {
                Log.e("Error: ", e.getMessage());
            } finally {

            }
            return null;
        }


        /**
         * After completing background task Dismiss the progress dialog
         **/
        @Override
        protected void onPostExecute(String result) {
            // dismiss the dialog after the file was downloaded
            Message message = new Message();
            if (result != null && result.equalsIgnoreCase("Success")) {

                message.what = DONE;
                mHandler.sendMessage(message);
            } else {
                message.what = ERROR;
                mHandler.sendMessage(message);
            }
            AppUtill.hideProgressDialog(progressDialog);
        }

    }
}
