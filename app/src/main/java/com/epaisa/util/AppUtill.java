package com.epaisa.util;

import android.app.ProgressDialog;
import android.content.Context;
import android.graphics.Color;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.webkit.WebView;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;

import java.io.IOException;
import java.io.InputStreamReader;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.concurrent.TimeUnit;

/**
 * Created by ist on 25/8/16.
 */


public class AppUtill {

    private static char[] chunkBuffer = new char[1024];

    public synchronized static String readData(InputStreamReader rd) {
        try {
            StringBuffer sb = new StringBuffer();
            while (true) {
                int read = rd.read(chunkBuffer, 0, chunkBuffer.length);
                if (read == -1) {
                    break;
                }
                sb.append(chunkBuffer, 0, read);
            }
            return sb.toString();
        } catch (IOException e) {
        } finally {
            try {
                rd.close();
            } catch (IOException e) {
            }
        }
        return "";
    }

    public static boolean isNetworkAvailable(Context context) {
        ConnectivityManager connectivityManager = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo activeNetworkInfo = connectivityManager.getActiveNetworkInfo();
        return activeNetworkInfo != null && activeNetworkInfo.isConnected();
    }

    public static void loadFragment(Fragment loadFragment, FragmentActivity activity, int id) {
      if (loadFragment != null) {
            if (!loadFragment.isAdded()) {
                activity.getSupportFragmentManager().beginTransaction().addToBackStack("tag")
                        .replace(id, loadFragment)
                        .commitAllowingStateLoss();
            }
        }
    }


    public static void hideShowFrag(Fragment hideFrag,Fragment loadFrag, FragmentActivity activity, int container_id){

        if(!loadFrag.isAdded()) {
            activity.getSupportFragmentManager().beginTransaction().hide(hideFrag)
                    .add(container_id, loadFrag)
                    .addToBackStack(null)
                    .setTransition(FragmentTransaction.TRANSIT_FRAGMENT_OPEN)
                    .commit();

        }

    }

    public static void loadImage(Context mContext,ImageView view, String url){
        Glide.with(mContext).load(url).into(view);
    }


    /**
     * Function to convert milliseconds time to
     * Timer Format
     * Hours:Minutes:Seconds
     * */
    public String milliSecondsToTimer(long milliseconds){
        String finalTimerString = "";
        String secondsString = "";

        // Convert total duration into time
        int hours = (int)( milliseconds / (1000*60*60));
        int minutes = (int)(milliseconds % (1000*60*60)) / (1000*60);
        int seconds = (int) ((milliseconds % (1000*60*60)) % (1000*60) / 1000);
        // Add hours if there
        if(hours > 0){
            finalTimerString = hours + ":";
        }

        // Prepending 0 to seconds if it is one digit
        if(seconds < 10){
            secondsString = "0" + seconds;
        }else{
            secondsString = "" + seconds;}

        finalTimerString = finalTimerString + minutes + ":" + secondsString;

        // return timer string
        return finalTimerString;
    }

    /**
     * Function to get Progress percentage
     * @param currentDuration
     * @param totalDuration
     * */
    public int getProgressPercentage(long currentDuration, long totalDuration){
        Double percentage = (double) 0;

        long currentSeconds = (int) (currentDuration / 1000);
        long totalSeconds = (int) (totalDuration / 1000);

        // calculating percentage
        percentage =(((double)currentSeconds)/totalSeconds)*100;

        // return percentage
        return percentage.intValue();
    }

    /**
     * Function to change progress to timer
     * @param progress -
     * @param totalDuration
     * returns current duration in milliseconds
     * */
    public int progressToTimer(int progress, int totalDuration) {
        int currentDuration = 0;
        totalDuration = (int) (totalDuration / 1000);
        currentDuration = (int) ((((double)progress) / 100) * totalDuration);

        // return current duration in milliseconds
        return currentDuration * 1000;
    }

    public static void hideKeybord(Context context, View view) {
        InputMethodManager imm = (InputMethodManager) context
                .getSystemService(Context.INPUT_METHOD_SERVICE);
        imm.hideSoftInputFromWindow(view.getWindowToken(), 0);
    }

    public static ProgressDialog showProgressDialog(Context context, String title, String msg) {
        ProgressDialog _progress = ProgressDialog.show(context, title, msg);
        return _progress;
    }

    /**
     * method for dismissing progress dialog
     *
     * @param _progress
     */
    public static void hideProgressDialog(ProgressDialog _progress) {
        if (_progress.isShowing())
            _progress.dismiss();

    }

   public static void loadTextOnWebView(String text, TextView textView, WebView webView){

        String summary = "<html><FONT style='font-size:14px; font-weight:700; color:#FFFFFF;'>" +
                "<marquee behavior='scroll' direction='left' scrollamount='3'>"
                + text + "</marquee></FONT></html>";
        textView.setVisibility(View.GONE);
        textView.setText(text);
        webView.setBackgroundColor(Color.GRAY);
        webView.loadData(summary, "text/html", "utf-8");
    }

    public static String convertMilisecontToTime(String milisecond) {

        System.out.println("convertMilisecontToTime  "+milisecond);
        String x = "" + milisecond;
        if (milisecond != null && !milisecond.equalsIgnoreCase("")) {
            SimpleDateFormat formatter = null;
            formatter = new SimpleDateFormat("HH:mm:ss");
            long milliSeconds = Long.parseLong(x);
            Calendar calendar = Calendar.getInstance();
            calendar.setTimeInMillis(milliSeconds);
            return formatter.format(calendar.getTime());
        }
    return "0:0";
    }

    public static String millisToMinutesAndSeconds(String time) {
        long millis = Long.parseLong(time);
        long minutes = TimeUnit.MILLISECONDS.toMinutes(millis);
        long seconds = TimeUnit.MILLISECONDS.toSeconds(millis);
        String sec = String.valueOf(seconds);
        if(sec.length()>1){
            return minutes + ":" + sec.substring(0,2);
        }else {
            return minutes + ":" + sec;
        }
    }
}


