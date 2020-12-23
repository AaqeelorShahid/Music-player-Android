package com.hnd.zmusicplayer.ADT;

import com.hnd.zmusicplayer.models.MusicModel;

public class MusicListNode {

    static int noOfLinkedList = 0;


    MusicModel data;
    MusicListNode previousNode;
    MusicListNode nextNode;

    public MusicListNode(MusicModel data) {
        this.data = data;
        noOfLinkedList++;
    }

    public void setData(MusicModel data) {
        this.data = data;
    }
    public MusicListNode getPreviousNode() {
        return previousNode;
    }

    public MusicListNode getNextNode() {
        return nextNode;
    }

    public MusicModel getData() {
        return data;
    }
}
