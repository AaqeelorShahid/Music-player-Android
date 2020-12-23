package com.hnd.zmusicplayer.adapters;

import android.content.ContentUris;
import android.content.Context;
import android.content.Intent;
import android.media.MediaMetadataRetriever;
import android.net.Uri;
import android.provider.MediaStore;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Filter;
import android.widget.Filterable;
import android.widget.ImageView;
import android.widget.PopupMenu;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.LinearSmoothScroller;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.google.android.material.snackbar.Snackbar;
import com.hnd.zmusicplayer.ADT.MusicList;
import com.hnd.zmusicplayer.R;
import com.hnd.zmusicplayer.activities.PlayerActivity;
import com.hnd.zmusicplayer.algorithms.BinarySearch;
import com.hnd.zmusicplayer.models.MusicModel;
import com.makeramen.roundedimageview.RoundedImageView;

import java.io.File;

import static android.content.ContentValues.TAG;

public class SongAdapter extends RecyclerView.Adapter<SongAdapter.MyViewHolder> implements Filterable {

    Context context;
    MusicList list;
    MusicList secondList;
    RecyclerView recyclerView;

    public SongAdapter(Context context, MusicList list, RecyclerView recyclerView){
        this.context = context;
        this.list = list;
        this.secondList = list.clone(list);
        this.recyclerView = recyclerView;
    }

    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.music_items, parent, false);
        return new MyViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull final MyViewHolder holder, final int position) {

        holder.name.setText(list.get(position).getTitle());
        holder.album.setText(list.get(position).getAlbum());

        byte[] image = getAblumArt(list.get(position).getPath());
        if(image != null){
            Glide.with(context).asBitmap()
                    .load(image)
                    .into(holder.image);
        }else{
            Glide.with(context).asBitmap()
                    .load(R.drawable.album_img)
                    .into(holder.image);
        }
        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent musicIntent = new Intent(context, PlayerActivity.class);
                musicIntent.putExtra("position", position);
                MusicModel n = list.get(position);
                context.startActivity(musicIntent);
            }
        });
        holder.sideMenu.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(final View v) {
                PopupMenu menu = new PopupMenu(context, v);
                menu.getMenuInflater().inflate(R.menu.side_menu_options, menu.getMenu());
                menu.show();

                menu.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {
                    @Override
                    public boolean onMenuItemClick(MenuItem item) {
                        //Make to Switch condition if you're making more than one buttons in popup menu
                        if (item.getItemId() == R.id.delete_btn){
                            //Deleting the file
                            Uri uri = ContentUris.withAppendedId(MediaStore.Audio.Media.EXTERNAL_CONTENT_URI,
                                    Long.parseLong(list.get(position).getId())); // Content://
                            File file = new File(list.get(position).getPath());
                            boolean deleted = file.delete();

                            if(deleted){
                                //Uncomment this unless file details will be in mediastore
                               // context.getContentResolver().delete(uri, null, null);
                                Toast.makeText(context, "Deleted!" , Toast.LENGTH_SHORT).show();
                                list.remove(position);
                                notifyItemRemoved(position);
                                notifyItemRangeChanged(position, list.getLength());

                                Snackbar.make(v, "File Deleted : ", Snackbar.LENGTH_LONG).show();
                            }else{
                                Toast.makeText(context, "Couldn't delete!" , Toast.LENGTH_SHORT).show();

                            }
                        }
                        return true;
                    }
                });

            }
        });
    }

    @Override
    public int getItemCount() {
        return list.getLength();
    }

    @Override
    public Filter getFilter() {
        return listFiler;
    }

    private Filter listFiler = new Filter() {
        @Override
        protected FilterResults performFiltering(CharSequence constraint) {
            MusicList filteredList = new MusicList();
            FilterResults results = new FilterResults();
            BinarySearch search = new BinarySearch();
            String word = constraint.toString().trim();
            MusicList data = search.search(list, word);
            filteredList = data;
            results.values = filteredList;
            return results;
        }

        @Override
        protected void publishResults(CharSequence constraint, FilterResults results) {
            MusicList c = (MusicList) results.values;
            if (c != null && ((MusicList) results.values).getLength() == 1){
               Log.d(TAG, "publishResults: " + constraint.length());
               MusicModel model = c.get(0);
               list.clear();
               list.addMusic(model);
               notifyDataSetChanged();
            }

        }
    };

    public void backTo(){
        list = new MusicList();
        list = list.clone(secondList);
        notifyDataSetChanged();
    }

    public class MyViewHolder extends RecyclerView.ViewHolder{
        RoundedImageView image;
        ImageView sideMenu;
        TextView name;
        TextView album;

        public MyViewHolder(@NonNull View itemView) {
            super(itemView);
            image = itemView.findViewById(R.id.music_img);
            name = itemView.findViewById(R.id.music_name);
            album = itemView.findViewById(R.id.music_album);
            sideMenu = itemView.findViewById(R.id.side_menu_btn);
        }
    }

    private byte[] getAblumArt (String uri){
        byte [] art = new byte[0];
        try {
            MediaMetadataRetriever retriever = new MediaMetadataRetriever();
            retriever.setDataSource(uri);
            art = retriever.getEmbeddedPicture();
            retriever.release();
        }catch (Exception e){
            Toast.makeText(context, "Error : " + e.getLocalizedMessage(),Toast.LENGTH_SHORT).show();
        }
        return art;
    }
}
