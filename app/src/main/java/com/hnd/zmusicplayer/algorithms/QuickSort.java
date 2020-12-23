package com.hnd.zmusicplayer.algorithms;

import android.util.Log;

import com.hnd.zmusicplayer.ADT.MusicList;
import com.hnd.zmusicplayer.ADT.MusicListNode;
import com.hnd.zmusicplayer.models.MusicModel;

import java.io.Console;

public class QuickSort {

    public int pivot(MusicList list, int start, int end){

        String pivot = list.get(start).getTitle();
        int swapIndex = start;

        for (int i = start + 1; i < end; i++){
            int val = pivot.compareTo(list.get(i).getTitle());
//            if (string1 > string2) it returns a positive value.
//            if both the strings are equal lexicographically
//            i.e.(string1 == string2) it returns 0.
//            if (string1 < string2) it returns a negative value // .

            if (val > 0){
                swapIndex++;
                MusicModel temp = list.getNode(start).getData();
//                Log.d("Value" , String.valueOf(swapIndex));
                list.getNode(start).setData(list.getNode(swapIndex).getData());
                list.getNode(swapIndex).setData(temp);
            }
        }
        return swapIndex;
    }

    public void quickSort (MusicList list, int left, int right){
        if (left < right){
            int pivotIndex = pivot(list, left, right);
             // Sorting the left part
            quickSort(list, left, pivotIndex - 1);
//            Sorting the right part
            quickSort(list, pivotIndex + 1, right);
        }
    }


}
