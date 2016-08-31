package com.iTune.music.app;

import android.content.Context;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.os.Handler;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.WebView;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.SeekBar;
import android.widget.TextView;

import com.iTune.music.data.Results;
import com.iTune.music.util.AppUtill;
import com.iTune.music.R;
import java.io.IOException;

public class PlayerFragment extends Fragment implements View.OnClickListener, MediaPlayer.OnCompletionListener, SeekBar.OnSeekBarChangeListener {

    private Context mContext = getActivity();
    private LinearLayout playerHeaderBg;
    private TextView songTitle;
    private LinearLayout songThumbnail;
    private LinearLayout playerFooterBg;
    private SeekBar songProgressBar;
    private LinearLayout timerDisplay;
    private TextView songCurrentDurationLabel;
    private TextView songTotalDurationLabel;
    private ImageView songThumbnailImg;
    private Results results;
    private String url;

    // Media Player
    private  MediaPlayer mp;
    // Handler to update UI timer, progress bar etc,.
    private Handler mHandler = new Handler();;
    private int seekForwardTime = 5000; // 5000 milliseconds
    private int seekBackwardTime = 5000; // 5000 milliseconds
    private int currentSongIndex = 0;
    private boolean isShuffle = false;
    private boolean isRepeat = false;
    private AppUtill utils;

    private ImageButton btnPlay;
    private boolean started =false;
    private TextView ticker;
    private WebView webView;


    public enum Single{
        INSTANCE;
        PlayerFragment s=new PlayerFragment();
        public PlayerFragment getInstance(){
            if(s==null)
                return new PlayerFragment();
            else return s;
        }
    }



    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        mContext = getActivity();
        MainActivity.actionBar.hide();
        Bundle bundle = getArguments();
        results = (Results)bundle.getSerializable("trackData");
        url = results.getPreviewUrl();
        // Mediaplayer
        mp = new MediaPlayer();
        utils = new AppUtill();

        return inflater.inflate(R.layout.player, null);
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        playerHeaderBg = (LinearLayout) view.findViewById(R.id.player_header_bg);
        songThumbnailImg = (ImageView) view.findViewById(R.id.songThumbnailImg);
        AppUtill.loadImage(mContext,songThumbnailImg,results.getArtworkUrl100());
        songTitle = (TextView) view.findViewById(R.id.songTitle);
        ticker = (TextView) view.findViewById(R.id.ticker);
        webView = (WebView) view.findViewById(R.id.webView);
        view.findViewById(R.id.btnPlaylist).setOnClickListener(this);
        songThumbnail = (LinearLayout) view.findViewById(R.id.songThumbnail);
        playerFooterBg = (LinearLayout) view.findViewById(R.id.player_footer_bg);
        view.findViewById(R.id.btnPrevious).setOnClickListener(this);
        view.findViewById(R.id.btnBackward).setOnClickListener(this);
        btnPlay = (ImageButton) view.findViewById(R.id.btnPlay);
        btnPlay.setOnClickListener(this);
        view.findViewById(R.id.btnForward).setOnClickListener(this);
        view.findViewById(R.id.btnNext).setOnClickListener(this);
        songProgressBar = (SeekBar) view.findViewById(R.id.songProgressBar);
        timerDisplay = (LinearLayout) view.findViewById(R.id.timerDisplay);
        songCurrentDurationLabel = (TextView) view.findViewById(R.id.songCurrentDurationLabel);
        songTotalDurationLabel = (TextView) view.findViewById(R.id.songTotalDurationLabel);
        view.findViewById(R.id.btnRepeat).setOnClickListener(this);
        view.findViewById(R.id.btnShuffle).setOnClickListener(this);

        AppUtill.loadTextOnWebView(results.getTrackName(),ticker,webView);
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.btnPlaylist:
                //TODO implement
                break;
            case R.id.btnPrevious:
                //TODO implement
                /*if(currentSongIndex > 0){
                    playSong(currentSongIndex - 1);
                    currentSongIndex = currentSongIndex - 1;
                }else{
                    // play last song
                    playSong(songsList.size() - 1);
                    currentSongIndex = songsList.size() - 1;
                }*/
                break;
            case R.id.btnBackward:
                //TODO implement
                // get current song position
                int currentPosition = mp.getCurrentPosition();
                // check if seekBackward time is greater than 0 sec
                if(currentPosition - seekBackwardTime >= 0){
                    // forward song
                    mp.seekTo(currentPosition - seekBackwardTime);
                }else{
                    // backward to starting position
                    mp.seekTo(0);
                }
                break;
            case R.id.btnPlay:
                //TODO implement
               // playMusic(url);

                if(mp.isPlaying()){
                    if(mp!=null){
                        mp.pause();
                        // Changing button image to play button
                        btnPlay.setImageResource(R.drawable.btn_play);
                    }
                }else{
                    // Resume song
                    if(mp!=null){
                        playSong(0);
                        mp.start();
                        // Changing button image to pause button
                        btnPlay.setImageResource(R.drawable.btn_pause);
                    }
                }
                break;
            case R.id.btnForward:
                //TODO implement
                // get current song position
                int currentPosition1 = mp.getCurrentPosition();
                // check if seekForward time is lesser than song duration
                if(currentPosition1 + seekForwardTime <= mp.getDuration()){
                    // forward song
                    mp.seekTo(currentPosition1 + seekForwardTime);
                }else{
                    // forward to end position
                    mp.seekTo(mp.getDuration());
                }
                break;
            case R.id.btnNext:
                //TODO implement
                // check if next song is there or not
               /* if(currentSongIndex < (songsList.size() - 1)){
                    playSong(currentSongIndex + 1);
                    currentSongIndex = currentSongIndex + 1;
                }else{
                    // play first song
                    playSong(0);
                    currentSongIndex = 0;
                }*/
                break;
            case R.id.btnRepeat:
                //TODO implement
               /* if(isRepeat){
                    isRepeat = false;
                    Toast.makeText(getApplicationContext(), "Repeat is OFF", Toast.LENGTH_SHORT).show();
                    btnRepeat.setImageResource(R.drawable.btn_repeat);
                }else{
                    // make repeat to true
                    isRepeat = true;
                    Toast.makeText(getApplicationContext(), "Repeat is ON", Toast.LENGTH_SHORT).show();
                    // make shuffle to false
                    isShuffle = false;
                    btnRepeat.setImageResource(R.drawable.btn_repeat_focused);
                    btnShuffle.setImageResource(R.drawable.btn_shuffle);
                }*/
                break;
            case R.id.btnShuffle:
                //TODO implement
                break;
        }
    }

    /**
     * Function to play a song
     * @param songIndex - index of song
     * */
    public void  playSong(int songIndex){
        // Play song

        if(!started) {
            started =true;
            try {
                mp.reset();
                mp.setDataSource(url);
                mp.prepare();
                mp.start();
                // Displaying Song title
                songTitle.setText(results.getTrackName());

                // Changing Button Image to pause image
                btnPlay.setImageResource(R.drawable.btn_pause);

                // set Progress bar values
                songProgressBar.setProgress(0);
                songProgressBar.setMax(100);

                // Updating progress bar
                updateProgressBar();
            } catch (IllegalArgumentException e) {
                e.printStackTrace();
            } catch (IllegalStateException e) {
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    /**
     * Update timer on seekbar
     * */
    public void updateProgressBar() {
        mHandler.postDelayed(mUpdateTimeTask, 100);
    }

    /**
     * Background Runnable thread
     * */
    private Runnable mUpdateTimeTask = new Runnable() {
        public void run() {
            long totalDuration = mp.getDuration();
            long currentDuration = mp.getCurrentPosition();

            // Displaying Total Duration time
            songTotalDurationLabel.setText(""+utils.milliSecondsToTimer(totalDuration));
            // Displaying time completed playing
            songCurrentDurationLabel.setText(""+utils.milliSecondsToTimer(currentDuration));

            // Updating progress bar
            int progress = (int)(utils.getProgressPercentage(currentDuration, totalDuration));
            //Log.d("Progress", ""+progress);
            songProgressBar.setProgress(progress);

            // Running this thread after 100 milliseconds
            mHandler.postDelayed(this, 100);
        }
    };

    /**
     *
     * */
    @Override
    public void onProgressChanged(SeekBar seekBar, int progress, boolean fromTouch) {

    }

    /**
     * When user starts moving the progress handler
     * */
    @Override
    public void onStartTrackingTouch(SeekBar seekBar) {
        // remove message Handler from updating progress bar
        mHandler.removeCallbacks(mUpdateTimeTask);
    }

    /**
     * When user stops moving the progress hanlder
     * */
    @Override
    public void onStopTrackingTouch(SeekBar seekBar) {
        mHandler.removeCallbacks(mUpdateTimeTask);
        int totalDuration = mp.getDuration();
        int currentPosition = utils.progressToTimer(seekBar.getProgress(), totalDuration);

        // forward or backward to certain seconds
        mp.seekTo(currentPosition);

        // update timer progress again
        updateProgressBar();
    }

    /**
     * On Song Playing completed
     * if repeat is ON play same song again
     * if shuffle is ON play random song
     * */
    @Override
    public void onCompletion(MediaPlayer arg0) {

        // check for repeat is ON or OFF
        /*if(isRepeat){
            // repeat is on play same song again
            playSong(currentSongIndex);
        } else if(isShuffle){
            // shuffle is on - play a random song
            Random rand = new Random();
            currentSongIndex = rand.nextInt((songsList.size() - 1) - 0 + 1) + 0;
            playSong(currentSongIndex);
        } else{
            // no repeat or shuffle ON - play next song
            if(currentSongIndex < (songsList.size() - 1)){
                playSong(currentSongIndex + 1);
                currentSongIndex = currentSongIndex + 1;
            }else{
                // play first song
                playSong(0);
                currentSongIndex = 0;
            }
        }*/

    }

    @Override
    public void onDestroy(){
        super.onDestroy();
        started =false;
        MainActivity.actionBar.show();
        mHandler.removeCallbacks(mUpdateTimeTask);
        if(mp!=null)
            mp.release();
    }

    private Handler handler = new Handler();
    private Runnable timedTask = new Runnable(){

        @Override
        public void run() {
            // TODO Auto-generated method stub
            handler.postDelayed(this, 100);

            playSong(0);
            handler.removeCallbacks(timedTask);

        }};



}
