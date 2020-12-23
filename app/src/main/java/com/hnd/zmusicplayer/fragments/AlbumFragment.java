package com.hnd.zmusicplayer.fragments;

import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.hnd.zmusicplayer.R;
import com.hnd.zmusicplayer.adapters.AlbumAdapter;

import static com.hnd.zmusicplayer.activities.MainActivity.musicList;


public class AlbumFragment extends Fragment {
    RecyclerView recyclerView;
    AlbumAdapter adapter;

    public AlbumFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment

        View view = inflater.inflate(R.layout.fragment_album, container, false);

        adapter = new AlbumAdapter(getContext(), musicList);
        Log.d("Size" , String.valueOf(musicList.getLength()));
        recyclerView = view.findViewById(R.id.album_container);
        recyclerView.hasFixedSize();
        recyclerView.setAdapter(adapter);

        recyclerView.setLayoutManager(new GridLayoutManager(getContext(), 2));

        //recyclerView.addItemDecoration(new DividerItemDecoration(recyclerView.getContext(), DividerItemDecoration.VERTICAL));

        return view;
    }
}