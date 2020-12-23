package com.hnd.zmusicplayer.activities;

import android.media.MediaMetadataRetriever;
import android.os.Bundle;

import com.bumptech.glide.Glide;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.snackbar.Snackbar;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.util.Log;
import android.view.View;
import android.widget.ImageView;

import com.hnd.zmusicplayer.ADT.MusicList;
import com.hnd.zmusicplayer.R;
import com.hnd.zmusicplayer.adapters.AlbumDetailsAdapter;

import static com.hnd.zmusicplayer.activities.MainActivity.musicList;

public class AlbumDetails extends AppCompatActivity {

    RecyclerView recyclerView;
    AlbumDetailsAdapter adapter;
    ImageView albumPhoto;
    String albumName;
    int position;
    MusicList albumSongs = new MusicList();
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_album_details);

        recyclerView = findViewById(R.id.album_song_container);
        albumPhoto = findViewById(R.id.album_photo);
        albumName = getIntent().getStringExtra("albumName");
        position = getIntent().getIntExtra("position", -1);

        for (int i = 0; i < musicList.getLength(); i++){
            if(albumName.equals(musicList.get(i).getAlbum())){
                albumSongs.addMusic(musicList.get(i));
            }
        }

        byte [] artImage = getAlbumArt(albumSongs.get(0).getPath());
        if (artImage != null){
            Glide.with(this)
                    .asBitmap()
                    .load(artImage)
                    .into(albumPhoto);
        }else {
            Glide.with(this)
                    .asBitmap()
                    .load(R.drawable.album_img)
                    .into(albumPhoto);
        }
    }

    public byte [] getAlbumArt (String uri){
        MediaMetadataRetriever retriever = new MediaMetadataRetriever();
        retriever.setDataSource(uri);
        byte [] art =  retriever.getEmbeddedPicture();
        retriever.release();
        return art;
    }

    @Override
    protected void onResume() {
        super.onResume();

        if (albumSongs.getLength() > 0){
            adapter = new AlbumDetailsAdapter(this, albumSongs);
            recyclerView.setAdapter(adapter);
            recyclerView.setLayoutManager(new LinearLayoutManager(this, RecyclerView.VERTICAL, false));
        }
    }
}