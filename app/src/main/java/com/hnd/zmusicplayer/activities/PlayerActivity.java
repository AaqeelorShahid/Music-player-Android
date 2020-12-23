package com.hnd.zmusicplayer.activities;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.media.MediaMetadataRetriever;
import android.media.MediaPlayer;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ImageView;
import android.widget.SeekBar;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.hnd.zmusicplayer.ADT.MusicList;
import com.hnd.zmusicplayer.ADT.MusicListNode;
import com.hnd.zmusicplayer.R;
import com.hnd.zmusicplayer.adapters.AlbumDetailsAdapter;
import com.hnd.zmusicplayer.models.MusicModel;

import java.util.Arrays;
import java.util.Collections;

import static com.hnd.zmusicplayer.activities.MainActivity.musicList;
import static com.hnd.zmusicplayer.adapters.AlbumDetailsAdapter.AlbumList;

public class PlayerActivity extends AppCompatActivity {

    SeekBar musicBar;
    TextView startTime, endTime, albumName, songName;
    ImageView playBtn, forwardBtn, backwardBtn, loopBtn, shuffleBtn, backBtn, menuBtn, albumImg;
    int position = -1;
    MusicList list = new MusicList();
    MusicListNode nextNode, prevNode;
    Uri uri;
    MediaPlayer mediaPlayer;
    Handler handler = new Handler();
    MusicListNode currentNode;
    Thread playThread, nextThread, prevThread;
    boolean shuffle = false, loop = false;
    String sender;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_player);
        initViews();
        getIntentData();

        //Assigning name and album
        songName.setText(list.get(position).getTitle());
        albumName.setText(list.get(position).getAlbum());

        musicBar.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                if (mediaPlayer != null && fromUser) {
                    mediaPlayer.seekTo(progress * 1000);
                }
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {

            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {

            }
        });

        PlayerActivity.this.runOnUiThread(new Runnable() {
            @Override
            public void run() {
                if(mediaPlayer != null){
                     int currentPosition = mediaPlayer.getCurrentPosition() / 1000;
                     musicBar.setProgress(currentPosition);
                     startTime.setText(formattedTime(currentPosition));
                }
                handler.postDelayed(this, 1000);
            }
        });
//....................Shuffle Button Config ..............................
        shuffleBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (shuffle){
                    shuffle = false;
                    shuffleBtn.setImageResource(R.drawable.shuffle);
                }else{
                    shuffle = true;
                    shuffleBtn.setImageResource(R.drawable.shuffle_off);
                }
            }
        });

//...........................Loop Button Config ......................................

        loopBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (loop){
                    loop = false;
                    loopBtn.setImageResource(R.drawable.loop);
                }else{
                    loop = true;
                    loopBtn.setImageResource(R.drawable.loop_off);
                }
            }

        });
    }

    private String formattedTime(int currentPosition) {
        String totalNew = "";
        String totalOut = "";
        String seconds = String.valueOf(currentPosition % 60);
        String minutes = String.valueOf(currentPosition / 60);
        totalOut = minutes + ":" + seconds;
        totalNew = minutes + ":" + "0" + seconds;

        if (seconds.length() == 1) {
            return totalNew;
        } else {
            return totalOut;
        }
    }

    private void getIntentData() {
        position = getIntent().getIntExtra("position", -1);

        sender = getIntent().getStringExtra("sender");
        if (sender != null && sender.equals("albumDetails")){
            list = AlbumList;
        }else{
            list = musicList;
        }

        currentNode = list.getNode(position);

        if (list != null) {
            playBtn.setImageResource(R.drawable.pause);
            uri = Uri.parse(list.get(position).getPath());
        }
        if (mediaPlayer != null) {
            mediaPlayer.stop();
            mediaPlayer.release();
            mediaPlayer = MediaPlayer.create(getApplicationContext(), uri);
            mediaPlayer.start();
        } else {
            mediaPlayer = MediaPlayer.create(getApplicationContext(), uri);
            mediaPlayer.start();
        }
        musicBar.setMax(mediaPlayer.getDuration() / 1000);
        metaData(uri);
    }

    private void metaData (Uri uri){
        MediaMetadataRetriever retriever = new MediaMetadataRetriever();
        retriever.setDataSource(uri.toString());
        int duration = Integer.parseInt(musicList.get(position).getDuration()) / 1000;
        endTime.setText(formattedTime(duration));

        byte[] art  = retriever.getEmbeddedPicture();
        Bitmap bitmap;
        if(art != null){
            bitmap = BitmapFactory.decodeByteArray(art, 0, art.length);
            ImageAnimation(this, albumImg, bitmap);

            //TODO : Uncomment this if you want to enable palette API Background
//            bitmap = BitmapFactory.decodeByteArray(art, 0, art.length);
//            Palette.from(bitmap).generate(new Palette.PaletteAsyncListener() {
//                @Override
//                public void onGenerated(@Nullable Palette palette) {
//                    Palette.Swatch swatch = palette.getDominantSwatch();
//
//                    if(swatch != null){
//                        ConstraintLayout layout = findViewById(R.id.playerBg);
//                        GradientDrawable gradientDrawable = new GradientDrawable(GradientDrawable.Orientation.TOP_BOTTOM,
//                                new int[]{swatch.getRgb(), 0x00000000});
//                        layout.setBackground(gradientDrawable);
//                    }
//                }
//            });
        }
        else{
            Glide.with(getApplicationContext()).asBitmap()
                    .load(R.drawable.album_img)
                    .into(albumImg);
        }
    }

    @Override
    protected void onResume() {
        Log.d("OnResume :     " , "Is working" );

        playThreadBtn();
        prevThreadBtn();
        nextThreadBtn();
        super.onResume();
    }

    private void nextThreadBtn() {
        nextThread = new Thread(){
            @Override
            public void run() {
                super.run();
                forwardBtn.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        forwardBtnClicked();
                    }
                });
            }
        };
        nextThread.start();

    }

    private void forwardBtnClicked() {
        if (mediaPlayer.isPlaying()) {
            mediaPlayer.pause();
            mediaPlayer.release();

            //...........Shuffle Btn and Loop Btn .................

            if(shuffle && !loop){
               nextNode =  list.shuffle(list);
            }
            else if (!shuffle && loop){
                nextNode = currentNode;
            }
            else if (!shuffle && !loop) {
                nextNode = currentNode.getNextNode();
                if (nextNode == null ) {
                    nextNode = musicList.getNode(0);
                }
                currentNode = nextNode;
            }
                MusicModel next = nextNode.getData();

                uri = Uri.parse(next.getPath());
                mediaPlayer = MediaPlayer.create(getApplicationContext(), uri);
                metaData(uri);

                songName.setText(next.getTitle());
                albumName.setText(next.getAlbum());


// ......................... Implementation 2 ...........................
//            position = ((position + 1) % musicList.getLength());
//            uri = Uri.parse(musicList.get(position).getPath());
//
//            mediaPlayer = MediaPlayer.create(getApplicationContext(), uri);
//            metaData(uri);
//            songName.setText(musicList.get(position).getTitle());
//            albumName.setText(musicList.get(position).getAlbum());

            PlayerActivity.this.runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    if(mediaPlayer != null){
                       int currentPosition = mediaPlayer.getCurrentPosition() / 1000;
                       musicBar.setProgress(currentPosition);
                    }
                    handler.postDelayed(this, 1000);
                }
            });
            playBtn.setImageResource(R.drawable.pause);
            mediaPlayer.start();


        }
        else
            {
            mediaPlayer.pause();
            mediaPlayer.release();


                if(shuffle && !loop){
                    nextNode =  list.shuffle(list);
                }
                else if (!shuffle && loop){
                    nextNode = currentNode;
                }
                else if (!shuffle && !loop) {
                    nextNode = currentNode.getNextNode();
                    if (nextNode == null ) {
                        nextNode = musicList.getNode(0);
                    }
                    currentNode = nextNode;
                }
                MusicModel next = nextNode.getData();

                uri = Uri.parse(next.getPath());
                mediaPlayer = MediaPlayer.create(getApplicationContext(), uri);
                metaData(uri);

                songName.setText(next.getTitle());
                albumName.setText(next.getAlbum());

            // ..................  IM .  2 ....................
//            position = ((position + 1) % musicList.getLength());
//            uri = Uri.parse(musicList.get(position).getPath());
//
//            mediaPlayer = MediaPlayer.create(getApplicationContext(), uri);
//            metaData(uri);
//            songName.setText(musicList.get(position).getTitle());
//            albumName.setText(musicList.get(position).getAlbum());

            PlayerActivity.this.runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    if(mediaPlayer != null){
                        int currentPosition = mediaPlayer.getCurrentPosition() / 1000;
                        musicBar.setProgress(currentPosition);
                    }
                    handler.postDelayed(this, 1000);
                }
            });
            playBtn.setImageResource(R.drawable.play);
            mediaPlayer.start();
        }
    }

    private void prevThreadBtn() {
        prevThread = new Thread(){
            @Override
            public void run() {
                super.run();
                backwardBtn.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        prevBtnClicked();
                    }
                });
            }
        };
        prevThread.start();
    }

    private void prevBtnClicked() {
        if (mediaPlayer.isPlaying()) {
            mediaPlayer.pause();
            mediaPlayer.release();

            if(shuffle && !loop){
                prevNode =  list.shuffle(list);
            }
            else if (!shuffle && loop){
                prevNode = currentNode;
            }
            else if (!shuffle && !loop) {
                prevNode = currentNode.getPreviousNode();
                if (prevNode == null ) {
                    prevNode = musicList.getNode(musicList.getLength() - 1);
                }
                currentNode = prevNode;
            }
            MusicModel prev = prevNode.getData();

            uri = Uri.parse(prev.getPath());
            mediaPlayer = MediaPlayer.create(getApplicationContext(), uri);
            metaData(uri);

            songName.setText(prev.getTitle());
            albumName.setText(prev.getAlbum());

            // ..................... Implementation 2 ................
//            position = ((position - 1)  < 0 ? (musicList.getLength()-1) : (position - 1)); // if its lower than 0 then it has to start from end of the songs - ternary operators
//            uri = Uri.parse(musicList.get(position).getPath());
//
//            mediaPlayer = MediaPlayer.create(getApplicationContext(), uri);
//            metaData(uri);
//            songName.setText(musicList.get(position).getTitle());
//            albumName.setText(musicList.get(position).getAlbum());

            PlayerActivity.this.runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    if(mediaPlayer != null){
                        int currentPosition = mediaPlayer.getCurrentPosition() / 1000;
                             musicBar.setProgress(currentPosition);
                    }
                    handler.postDelayed(this, 1000);
                }
            });
            playBtn.setImageResource(R.drawable.pause);
            mediaPlayer.start();
        }
        else
        {
            mediaPlayer.pause();
            mediaPlayer.release();

            if(shuffle && !loop){
                prevNode =  list.shuffle(list);
            }
            else if (!shuffle && loop){
                prevNode = currentNode;
            }
            else if (!shuffle && !loop) {
                prevNode = currentNode.getPreviousNode();
                if (prevNode == null ) {
                    prevNode = musicList.getNode(musicList.getLength() - 1);
                }
                currentNode = prevNode;
            }
            MusicModel prev = prevNode.getData();

            uri = Uri.parse(prev.getPath());
            mediaPlayer = MediaPlayer.create(getApplicationContext(), uri);
            metaData(uri);

            songName.setText(prev.getTitle());
            albumName.setText(prev.getAlbum());

            // ............... Implementation 2 .......................
//            position = ((position - 1)  < 0 ? (musicList.getLength()-1) : (position - 1));
//            uri = Uri.parse(musicList.get(position).getPath());
//
//            mediaPlayer = MediaPlayer.create(getApplicationContext(), uri);
//            metaData(uri);
//            songName.setText(musicList.get(position).getTitle());
//            albumName.setText(musicList.get(position).getAlbum());

            PlayerActivity.this.runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    if(mediaPlayer != null){
                        int currentPosition = mediaPlayer.getCurrentPosition() / 1000;
                        musicBar.setProgress(currentPosition);
                    }
                    handler.postDelayed(this, 1000);
                }
            });
            playBtn.setImageResource(R.drawable.play);
            mediaPlayer.start();
        }

    }

    private void playThreadBtn() {
        playThread = new Thread(){
            @Override
            public void run() {
                super.run();
                Log.d("Play Thread" , " working");
                playBtn.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        playBtnClicked();
                    }
                });
            }
        };
        playThread.start();
    }

    private void playBtnClicked() {
        if(mediaPlayer.isPlaying()){
            playBtn.setImageResource(R.drawable.play);
            mediaPlayer.pause();

            musicBar.setMax(mediaPlayer.getDuration()/1000);
            PlayerActivity.this.runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    if(mediaPlayer != null){
                        int currentPosition = mediaPlayer.getCurrentPosition() / 1000;
                        musicBar.setProgress(currentPosition);
                    }
                    handler.postDelayed(this, 1000);
                }
            });

        }
        else
            {
            playBtn.setImageResource(R.drawable.pause);
            mediaPlayer.start();

            musicBar.setMax(mediaPlayer.getDuration() /1000);
            musicBar.setMax(mediaPlayer.getDuration()/1000);
            PlayerActivity.this.runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    if(mediaPlayer != null){
                        int currentPosition = mediaPlayer.getCurrentPosition() / 1000;
                        musicBar.setProgress(currentPosition);
                    }
                    handler.postDelayed(this, 1000);
                }
            });
        }

    }

    public void ImageAnimation (final Context context, final ImageView image, final Bitmap bitmap){
        Animation animOut = AnimationUtils.loadAnimation(context, android.R.anim.fade_out);
        final Animation animIn = AnimationUtils.loadAnimation(context, android.R.anim.fade_in);
        animOut.setAnimationListener(new Animation.AnimationListener() {
            @Override
            public void onAnimationStart(Animation animation) {

            }

            @Override
            public void onAnimationEnd(Animation animation) {
                Glide.with(context).asBitmap()
                        .load(bitmap)
                        .into(image);
                animIn.setAnimationListener(new Animation.AnimationListener() {
                    @Override
                    public void onAnimationStart(Animation animation) {

                    }

                    @Override
                    public void onAnimationEnd(Animation animation) {

                    }

                    @Override
                    public void onAnimationRepeat(Animation animation) {

                    }
                });
                image.startAnimation(animIn);
            }

            @Override
            public void onAnimationRepeat(Animation animation) {

            }
        });
        image.startAnimation(animOut);

    }

    private void initViews() {
        musicBar = findViewById(R.id.musicBar);
        startTime = findViewById(R.id.start_time);
        endTime = findViewById(R.id.end_time);
        albumName = findViewById(R.id.album_name);
        songName = findViewById(R.id.song_name);
        playBtn = findViewById(R.id.play_btn);
        forwardBtn = findViewById(R.id.forward_btn);
        backwardBtn = findViewById(R.id.backward_btn);
        loopBtn = findViewById(R.id.loopBtn);
        shuffleBtn = findViewById(R.id.shuffleBtn);
        backBtn = findViewById(R.id.back_btn);
        menuBtn = findViewById(R.id.menu_btn);
        albumImg = findViewById(R.id.album_img);

    }
}

