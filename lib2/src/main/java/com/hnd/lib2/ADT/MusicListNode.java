package com.hnd.lib2.ADT;

import com.hnd.lib2.models.MusicModel;

public class MusicListNode {

    static int noOfLinkedList = 0;

    MusicModel data;
    MusicListNode previousNode;
    MusicListNode nextNode;

    public MusicListNode(MusicModel data) {
        this.data = data;
        noOfLinkedList++;
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
