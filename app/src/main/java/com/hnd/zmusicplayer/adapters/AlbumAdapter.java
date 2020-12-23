    package com.hnd.zmusicplayer.adapters;

import android.content.Context;
import android.content.Intent;
import android.media.MediaMetadataRetriever;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.hnd.zmusicplayer.ADT.MusicList;
import com.hnd.zmusicplayer.R;
import com.hnd.zmusicplayer.activities.AlbumDetails;

public class AlbumAdapter extends RecyclerView.Adapter<AlbumAdapter.MyViewHolder> {

    Context mContext;
    MusicList list;

    public AlbumAdapter(Context mContext, MusicList list) {
        this.mContext = mContext;
        this.list = list;
    }

    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(mContext).inflate(R.layout.album_item, parent, false);
        return new MyViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull MyViewHolder holder, final int position) {

        byte [] image = getAlbumArt(list.get(position).getPath());
        if (image != null){
        Glide.with(mContext)
                .asBitmap()
                .load(image)
                .into(holder.album_img);
        }else {
            Glide.with(mContext)
                    .asBitmap()
                    .load(R.drawable.album_img)
                    .into(holder.album_img);
        }
        holder.album_name.setText(list.get(position).getAlbum());
        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent albumIntent = new Intent(mContext, AlbumDetails.class);
                albumIntent.putExtra("albumName", list.get(position).getAlbum());
                albumIntent.putExtra("position", position);
                mContext.startActivity(albumIntent);
            }
        });
    }

    @Override
    public int getItemCount() {
        return list.getLength();
    }

    public class MyViewHolder extends RecyclerView.ViewHolder {

        ImageView album_img;
        TextView album_name;

        public MyViewHolder(@NonNull View itemView) {
            super(itemView);

            album_img = itemView.findViewById(R.id.album_background);
            album_name = itemView.findViewById(R.id.album_song);
        }
    }

    public byte[] getAlbumArt (String uri){
        byte [] art = new byte[0];
        try {
            MediaMetadataRetriever retriever = new MediaMetadataRetriever();
            retriever.setDataSource(uri);
            art = retriever.getEmbeddedPicture();
            retriever.release();
        }catch (Exception e){
            Toast.makeText(mContext, "Error : " + e.getLocalizedMessage(),Toast.LENGTH_SHORT).show();
        }
        return art;
    }
}
