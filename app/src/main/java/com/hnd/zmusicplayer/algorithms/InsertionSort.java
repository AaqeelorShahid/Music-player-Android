package com.hnd.zmusicplayer.algorithms;

import android.util.Log;

import com.hnd.zmusicplayer.ADT.MusicList;
import com.hnd.zmusicplayer.ADT.MusicListNode;

public class InsertionSort {

    public void sort(MusicList list){
        for (int i = 1; i < list.getLength(); i++){
            int a = 0;
            MusicListNode currentNode = list.getNode(i);
            for (int j = i-1; j >=0 && list.get(j).getTitle().compareTo(currentNode.getData().getTitle()) > 0; i++){
                list.getNode(j + 1).setData(list.getNode(j).getData());
                a = j;
            }
            Log.d("aasasas" , String.valueOf(a));
            list.getNode(a + 1).setData(currentNode.getData());
        }
    }
}
