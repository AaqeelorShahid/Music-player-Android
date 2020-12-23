package com.hnd.zmusicplayer.fragments;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import androidx.appcompat.widget.SearchView;

import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.hnd.zmusicplayer.R;
import com.hnd.zmusicplayer.adapters.SongAdapter;
import com.hnd.zmusicplayer.algorithms.QuickSort;
import com.hnd.zmusicplayer.algorithms.bubbleSort;

import static android.content.ContentValues.TAG;
import static com.hnd.zmusicplayer.activities.MainActivity.musicList;



public class SongsFragment extends Fragment {
    RecyclerView recyclerView;
    SongAdapter adapter;


    public SongsFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_songs, container, false);
        final FloatingActionButton sortBtn = view.findViewById(R.id.sortBtn);



        recyclerView = view.findViewById(R.id.songs_recycler);
        recyclerView.hasFixedSize();
        adapter = new SongAdapter(getContext(), musicList, recyclerView);
        recyclerView.setAdapter(adapter);
        LinearLayoutManager layoutManager = new LinearLayoutManager(getContext(), RecyclerView.VERTICAL, false);
        recyclerView.setLayoutManager(layoutManager);
        //recyclerView.addItemDecoration(new DividerItemDecoration(recyclerView.getContext(), DividerItemDecoration.VERTICAL));
        sortBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(getContext(), "Working", Toast.LENGTH_SHORT).show();
                    bubbleSort sortAlgo = new bubbleSort();
                    sortAlgo.sort(musicList);
                    adapter.notifyDataSetChanged();
            }
        });

        return view;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setHasOptionsMenu(true);
    }

    @Override
    public void onCreateOptionsMenu(@NonNull Menu menu, @NonNull MenuInflater inflater) {
        super.onCreateOptionsMenu(menu, inflater);

        inflater.inflate(R.menu.search_menu, menu);
        MenuItem item = menu.findItem(R.id.search);
        SearchView view = (SearchView) item.getActionView();

        item.setOnActionExpandListener(new MenuItem.OnActionExpandListener() {
            @Override
            public boolean onMenuItemActionExpand(MenuItem item) {
               // Toast.makeText(getContext(), "onMenuItemActionExpand called", Toast.LENGTH_SHORT).show();
                return true;
            }

            @Override
            public boolean onMenuItemActionCollapse(MenuItem item) {
             //   Toast.makeText(getContext(), "onMenuItemActionColl called", Toast.LENGTH_SHORT).show();
                adapter.backTo();
                return true;
            }
        });
        view.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                adapter.getFilter().filter(query);
                Log.d(TAG, "onQueryTextSubmit: " + query);
                return true;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                return true;
            }
        });
    }

}