package com.hnd.zmusicplayer.algorithms;

import android.util.Log;

import com.hnd.zmusicplayer.ADT.MusicList;
import com.hnd.zmusicplayer.ADT.MusicListNode;
import com.hnd.zmusicplayer.models.MusicModel;

import static android.content.ContentValues.TAG;

public class BinarySearch {
    public MusicList search (MusicList list , String elem){
        int start = 0;
        int end = list.getLength() - 1;
        int mid = (start + end) / 2 ;
        MusicListNode midNode = null;
        while (end >= start){
            midNode = list.getNode((mid));
            if (midNode.getData().getTitle().compareToIgnoreCase(elem) > 0){
                end = mid - 1;
            }else{
                start = mid + 1;
            }
            mid = (end + start) / 2;
        }
        Log.d(TAG, "search: " + midNode.getData().getTitle());
        MusicList fList = new MusicList();
        fList.addMusic(midNode.getData());
        return fList;
    }
//    public int search (int [] list , int elem){
//        int start = 0;
//        int end = list.length - 1;
//        int mid = (end + start) / 2;
//
//        while (elem != mid && start <= end){
//            if (elem > mid){
//                start = mid + 1;
//            }else{
//                end = mid - 1;
//            }
//            mid = (end + start) / 2;
//        }
//
//        return mid;
//    }
}
