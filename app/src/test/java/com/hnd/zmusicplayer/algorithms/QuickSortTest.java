package com.hnd.zmusicplayer.algorithms;

import com.hnd.zmusicplayer.ADT.MusicList;
import com.hnd.zmusicplayer.models.MusicModel;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import static org.junit.Assert.*;

public class QuickSortTest {

    MusicList list;
    QuickSort sort;
    @Before
    public void setUp() throws Exception {
        list = new MusicList();
        list.addMusic(new MusicModel("ZZZZZZZ" , "/AA", "/AA", "/AA", "/AA", "/AA"));
        list.addMusic(new MusicModel("FFFFFFF" , "/AA", "/AA", "/AA", "/AA", "/AA"));
        list.addMusic(new MusicModel("DDDDDDD" , "/AA", "/AA", "/AA", "/AA", "/AA"));
        list.addMusic(new MusicModel("HHHHHHH" , "/AA", "/AA", "/AA", "/AA", "/AA"));
        list.addMusic(new MusicModel("AAAAAAA" , "/AA", "/AA", "/AA", "/AA", "/AA"));

        sort = new QuickSort();
    }

//    @Test
//    public void pivot() {
//    }

    @Test
    public void quickSort() {
        sort.quickSort(list, 0, list.getLength() - 1);

       String l =  list.get(0).getTitle();
        assertNotEquals("ZZZZZZZ" , l);
    }

    @After
    public void tearDown() throws Exception {
    }

}