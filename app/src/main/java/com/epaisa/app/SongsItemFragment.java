package com.epaisa.app;

import android.content.Context;
import android.os.Bundle;
import android.os.Handler;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.epaisa.Listener.SearchListener;
import com.epaisa.adapter.SongListAdapter;
import com.epaisa.data.DataHandler;
import com.epaisa.data.Results;
import com.epaisa.data.TrackData;
import com.epaisa.service.DownloadData;
import com.epaisa.util.AppUtill;
import com.epesa.com.epaisa.R;

import org.w3c.dom.Text;

import java.util.ArrayList;

/**
 * A fragment representing a list of Items.
 * <p/>
 * interface.
 */
public class SongsItemFragment extends Fragment implements SongListAdapter.OnItemClickListener,SearchListener {

    private Context mContext;
    private static final String ARG_COLUMN_COUNT = "column-count";
    private int mColumnCount = 1;
    private static RecyclerView recyclerView;
    private LinearLayoutManager layoutManager;
    private SongListAdapter songListAdapter;
    private ViewGroup mainView;
    private ArrayList<Results> songList = new ArrayList<>();
    private TextView heading;
    public static String searchQuery;
    public SearchListener searchListener;
    /**
     * Mandatory empty constructor for the fragment manager to instantiate the
     * fragment (e.g. upon screen orientation changes).
     */
    public SongsItemFragment() {
    }

    @Override
    public void onSearchContent(String searchContent) {

        if(searchContent!=null && searchContent.length()>=3){
            this.searchQuery = searchContent;
            loadList(searchQuery);
        }
    }


    public SearchListener getSearchListener() {
        return searchListener;
    }

    public enum Single{
        INSTANCE;
        SongsItemFragment s=new SongsItemFragment();
        public SongsItemFragment getInstance(){
            if(s==null)
                return new SongsItemFragment();
            else return s;
        }
    }

    // TODO: Customize parameter initialization
    @SuppressWarnings("unused")
    public static SongsItemFragment newInstance(int columnCount) {
        SongsItemFragment fragment = new SongsItemFragment();
        Bundle args = new Bundle();
        args.putInt(ARG_COLUMN_COUNT, columnCount);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        if (getArguments() != null) {
            mColumnCount = getArguments().getInt(ARG_COLUMN_COUNT);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        mainView = (ViewGroup) inflater.inflate(R.layout.song_list_main_layout, container, false);
        mContext = getActivity();
        searchListener = this;
        recyclerView = (RecyclerView) mainView.findViewById(R.id.my_recycler_view);
        layoutManager = new LinearLayoutManager(getActivity());
        recyclerView.setLayoutManager(layoutManager);

        if(mainView!=null) {
            mainView.setFocusableInTouchMode(true);
            mainView.requestFocus();
            mainView.setOnKeyListener(new View.OnKeyListener() {
                @Override
                public boolean onKey(View v, int keyCode, KeyEvent event) {
                    if (keyCode == KeyEvent.KEYCODE_BACK) {
                        return true;
                    }
                    return false;
                }
            });
        }
        initList();
        loadList(null);
        return mainView;

    }

    private void initList(){
        if(DataHandler.Single.INSTANCE.getInstance().getTrackData()!=null) {
            heading = (TextView) mainView.findViewById(R.id.heading);

            songList = (ArrayList) DataHandler.Single.INSTANCE.getInstance().getTrackData().getResults();
            if(songList!=null && songList.size()>0) {
                heading.setText("" +songList.get(0).getArtistName() + " : Songs");
            }
            songListAdapter = new SongListAdapter(mContext,songList, this);
            recyclerView.setAdapter(songListAdapter);
        }
    }

    private void loadList(String searchQuery){

        if(AppUtill.isNetworkAvailable(mContext)) {
            if(searchQuery==null){
                new DownloadData(getActivity(),"https://itunes.apple.com/search?term=Michael+jackson", handler);
            }else {
                new DownloadData(getActivity(),"https://itunes.apple.com/search?term=" + searchQuery, handler);
            }
        }else{
            Toast.makeText(mContext, "Check Internet Connection.", Toast.LENGTH_SHORT).show();
        }
    }


    @Override
    public void onDetach() {
        super.onDetach();
    }

    @Override
    public void onPopupBtnClicked(Results results) {
        PlayerFragment playerFragment = PlayerFragment.Single.INSTANCE.getInstance();

        Bundle bundle = new Bundle();
        bundle.putSerializable("trackData", results);
        playerFragment.setArguments(bundle);

//        AppUtill.loadFragment(playerFragment,getActivity(),R.id.container);

        AppUtill.hideShowFrag(Single.INSTANCE.getInstance(),playerFragment,getActivity(),R.id.container);

    }

    private final Handler handler = new Handler(){
        @Override
        public void handleMessage(android.os.Message msg) {
            switch (msg.what) {
                case DownloadData.DONE:
                    initList();
                    break;
                case DownloadData.ERROR:
                    Toast.makeText(mContext, "Oops Some Problem occur.", Toast.LENGTH_SHORT).show();
                    break;
            }
        }
    };
}
