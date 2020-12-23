package com.hnd.zmusicplayer.ADT;

import android.graphics.drawable.ColorDrawable;
import android.media.audiofx.NoiseSuppressor;
import android.util.Log;

import com.hnd.zmusicplayer.models.MusicModel;

import java.util.LinkedList;
import java.util.Random;

public class MusicList {

    MusicListNode head, tail;
    int length;

    public MusicList(){
        this.head = null;
        this.tail = null;
        this.length = 0;
    }

    public void addMusic(MusicModel data){
        MusicListNode node = new MusicListNode(data);
        if(length  == 0) {
            head = node;
        }
        else {
            tail.nextNode = node;
            node.previousNode = tail;
        }
        tail = node;
        length++;
    }

    public void pop(){
        MusicListNode poppedNode = tail;
        if(length == 1){
            head = null;
            tail = null;
        }
        else
        {
            tail = poppedNode.previousNode;
            tail.nextNode = null;
            poppedNode.previousNode = null;
        }
        length--;
    }

    public void shift(){
        MusicListNode shiftedNode = this.head;
        if (length == 1){
            head = null;
            tail = null;
        }else{
            head = shiftedNode.nextNode;
            head.previousNode = null;
            shiftedNode.nextNode = null;
        }
        length--;
    }

    public MusicModel get(int index){
        int count = 0;
        MusicListNode current = this.head;
        while (count != index){
            current = current.nextNode;
            count++;
        }
       // Log.d("Model name" , current.data.getTitle());
        return current.data;
    }

    public MusicListNode getNode(int index){
        int count = 0;
        MusicListNode current = this.head;
        while (count != index){
            current = current.nextNode;
            count++;
        }
        // Log.d("Model name" , current);
        return current;
    }

    public MusicList clone (MusicList list) {
        MusicList clone = new MusicList();

        for(int i = 0; i < list.getLength(); i++) {
            if (list.getLength() != 0) {
                clone.addMusic(list.get(i));
            }
        }
        return clone;
    }

//    public void insert(int index, MusicModel n){
//        MusicListNode node = new MusicListNode(n);
//        MusicListNode found = getNode(index);
//        MusicListNode  beforeNode = getNode( index - 1);
//        MusicListNode afterNode = beforeNode.nextNode;
//
//        beforeNode.nextNode = node;
//        node.previousNode = beforeNode;
//        node.nextNode = afterNode;
//        afterNode.previousNode = node;
//        length++;
//    }

    public int getLength()
    {
        return length;
    }

    public boolean hasNext(int index){
        return index < getLength();
    }
    
    public MusicListNode shuffle(MusicList list){
        Random r = new Random();
        int position = r.nextInt(list.getLength() - 1);
        MusicListNode rNode = list.getNode(position);
        return rNode;
    }

    public void clear (){
        MusicListNode pointer = head;
        while (head != null){
            MusicListNode temp = head.nextNode;
            pop();
            pointer = temp;
        }
    }

    public void remove(int position){
        if(position == length)
        {
            pop();
        }
        if(position <= 0 || position >= length) {
            shift();
        }
        else{
            MusicListNode removedNode = getNode(position);
            removedNode.previousNode.nextNode = removedNode.nextNode;
            removedNode.nextNode.previousNode = removedNode.previousNode;

            //Some cleaning up
            removedNode.nextNode = null;
            removedNode.previousNode = null;
            length--;
        }
    }


}
