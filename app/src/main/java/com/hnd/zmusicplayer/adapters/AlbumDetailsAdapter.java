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
import com.hnd.zmusicplayer.activities.PlayerActivity;

public class AlbumDetailsAdapter extends RecyclerView.Adapter<AlbumDetailsAdapter.MyHolder> {
    Context mContext;
    public static MusicList AlbumList;

    public AlbumDetailsAdapter(Context mContext, MusicList mList) {
        this.mContext = mContext;
        this.AlbumList = mList;
    }

    @NonNull
    @Override
    public AlbumDetailsAdapter.MyHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(mContext).inflate(R.layout.music_items, parent, false);
        return new AlbumDetailsAdapter.MyHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull AlbumDetailsAdapter.MyHolder holder, final int position) {

        byte [] image = getAlbumArt(AlbumList.get(position).getPath());
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
        holder.album_name.setText(AlbumList.get(position).getTitle());

        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent playerIntent = new Intent(mContext, PlayerActivity.class);
                playerIntent.putExtra("sender", "albumDetails");
                playerIntent.putExtra("position", position);
                mContext.startActivity(playerIntent);
            }
        });
        holder.album.setVisibility(View.INVISIBLE);
    }

    @Override
    public int getItemCount() {
        return AlbumList.getLength();
    }

    public class MyHolder extends RecyclerView.ViewHolder {

        ImageView album_img;
        TextView album_name;
        TextView album;

        public MyHolder(@NonNull View itemView) {
            super(itemView);

            album_img = itemView.findViewById(R.id.music_img);
            album_name = itemView.findViewById(R.id.music_name);
            album = itemView.findViewById(R.id.music_album);
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
