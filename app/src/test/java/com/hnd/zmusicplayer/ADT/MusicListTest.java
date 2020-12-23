package com.hnd.zmusicplayer.ADT;

import android.util.Log;

import com.hnd.zmusicplayer.models.MusicModel;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import java.lang.reflect.Modifier;

import static android.content.ContentValues.TAG;
import static org.junit.Assert.*;

public class MusicListTest {
    MusicList list;
    @Before
    public void setUp() throws Exception {
        list = new MusicList();
        list.addMusic(new MusicModel("ZZZZZZZ" , "/AA", "/AA", "/AA", "/AA", "/AA"));
        list.addMusic(new MusicModel("FFFFFFF" , "/AA", "/AA", "/AA", "/AA", "/AA"));
        list.addMusic(new MusicModel("DDDDDDD" , "/AA", "/AA", "/AA", "/AA", "/AA"));
        list.addMusic(new MusicModel("HHHHHHH" , "/AA", "/AA", "/AA", "/AA", "/AA"));
        list.addMusic(new MusicModel("AAAAAAA" , "/AA", "/AA", "/AA", "/AA", "/AA"));
    }
    @Test
    public void addMusic() {
        assertEquals(5 ,list.getLength());
    }
    @Test
    public void get() {
        String name = list.get(4).getTitle();
        assertEquals("AAAAAAA", name);
    }
    @Test
    public void getNode() {
        MusicListNode node = list.getNode(2);
        String nextNodeName = node.nextNode.data.getTitle();
        assertEquals("HHHHHHH", nextNodeName);
    }
    @Test
    public void getLength() {
        int length = list.getLength();
        assertEquals(5, length);
    }
    @Test
    public void shuffle() {
        MusicListNode node = list.shuffle(list);
        String name = node.nextNode.data.getTitle();
        assertNotEquals("FFFFFFF", name); // NOT EQUALSSSSSSSS
    }
    @Test
    public void remove() {
        list.remove(0); // Shift function
        String first = list.get(0).getTitle();
        assertNotEquals("ZZZZZZZ", first);
    }
    @Test
    public void clear() {
        list.clear();
        int length = list.getLength();
        assertEquals(0, length);
    }
    @After
    public void tearDown() throws Exception {
    }
}