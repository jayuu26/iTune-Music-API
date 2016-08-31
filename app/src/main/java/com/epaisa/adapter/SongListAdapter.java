package com.epaisa.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.epaisa.data.Results;
import com.epaisa.data.TrackData;
import com.epaisa.util.AppUtill;
import com.epesa.com.epaisa.R;

import java.util.ArrayList;

/**
 * Created by ist on 25/8/16.
 */
public class SongListAdapter extends RecyclerView.Adapter<SongListAdapter.ViewHolder> {
    private ArrayList<Results> mDataset;
    private Context mContext;
    private OnItemClickListener listener;

    // Provide a reference to the views for each data item
    // Complex data items may need more than one view per item, and
    // you provide access to all the views for a data item in a view holder
    public class ViewHolder extends RecyclerView.ViewHolder {
        // each data item is just a string in this case
        public TextView txtTitle;
        public TextView txtSubTitle;
        public TextView duration;
        public ImageView imgSongIcon;
        public ImageView imgPopupMenuIcon;
        public RelativeLayout relativeLayout;

        public ViewHolder(View v) {
            super(v);
            txtTitle = (TextView) v.findViewById(R.id.song_title);
            txtSubTitle = (TextView) v.findViewById(R.id.song_sub_title);
            duration = (TextView) v.findViewById(R.id.duration);
            imgSongIcon = (ImageView) v.findViewById(R.id.img_icon);
            imgPopupMenuIcon = (ImageView) v.findViewById(R.id.popup_menu);
            relativeLayout = (RelativeLayout) v.findViewById(R.id.song_row);
        }
    }

    // Provide a suitable constructor (depends on the kind of dataset)
    public SongListAdapter(Context mContext,ArrayList<Results> myDataset, OnItemClickListener onItemClickListener) {
        mDataset = myDataset;
        this.mContext = mContext;
        this.listener = onItemClickListener;
    }

    // Create new views (invoked by the layout manager)
    @Override
    public SongListAdapter.ViewHolder onCreateViewHolder(ViewGroup parent,
                                                         int viewType) {
        // create a new view
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.song_list_row_layout, parent, false);
        // set the view's size, margins, paddings and layout parameters
        ViewHolder vh = new ViewHolder(v);
        return vh;
    }

    // Replace the contents of a view (invoked by the layout manager)
    @Override
    public void onBindViewHolder(ViewHolder holder, final int position) {
        holder.txtTitle.setText(mDataset.get(position).getTrackName());
        holder.txtSubTitle.setText("Genre: " + mDataset.get(position).getPrimaryGenreName());
        holder.duration.setText(AppUtill.millisToMinutesAndSeconds(mDataset.get(position).getTrackTimeMillis()));
        AppUtill.loadImage(mContext,holder.imgSongIcon,mDataset.get(position).getArtworkUrl60());
        holder.imgPopupMenuIcon.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                listener.onPopupBtnClicked(mDataset.get(position));
            }
        });
        holder.relativeLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                listener.onPopupBtnClicked(mDataset.get(position));
            }
        });
    }

    // Return the size of your dataset (invoked by the layout manager)
    @Override
    public int getItemCount() {
        return mDataset.size();
    }

    public interface OnItemClickListener {
        void onPopupBtnClicked(Results results);
    }
}
