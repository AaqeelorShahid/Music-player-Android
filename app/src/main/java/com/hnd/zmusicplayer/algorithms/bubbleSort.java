package com.hnd.zmusicplayer.algorithms;

import com.hnd.zmusicplayer.ADT.MusicList;
import com.hnd.zmusicplayer.ADT.MusicListNode;
import com.hnd.zmusicplayer.models.MusicModel;

public class bubbleSort {

    public void sort (MusicList list){
        for (int i = 0; i < list.getLength(); i++){
            for (int j = 1; j < list.getLength(); j++){
                if (list.get(j-1).getTitle().compareTo(list.get(j).getTitle()) > 0){
//
//                    String temp = list.get(j-1).getTitle();
//                    list.get(j-1).setTitle(list.get(j).getTitle());
//                    list.get(j).setTitle(temp);

                    MusicModel temp = list.getNode(j-1).getData();
                    list.getNode(j-1).setData(list.getNode(j).getData());
                    list.getNode(j).setData(temp);
                }
            }
        }
    }
}
