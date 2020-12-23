package com.hnd.lib2;


import com.hnd.lib2.ADT.MusicList;
import com.hnd.lib2.models.MusicModel;
public class MyClass {
    public static void main(String[] args) {
        System.out.println();

        MusicList list = new MusicList();
        list.addMusic(new MusicModel("AAA", "as" ,"asss","asss","asss","asss"));
        list.addMusic(new MusicModel("ZEAD", "as" ,"asss","asss","asss","asss"));
        list.addMusic(new MusicModel("NEIGA", "as" ,"asss","asss","asss","asss"));
        list.addMusic(new MusicModel("HEIF", "as" ,"asss","asss","asss","asss"));
        list.addMusic(new MusicModel("ABAA", "as" ,"asss","asss","asss","asss"));
        list.addMusic(new MusicModel("FUACK", "as" ,"asss","asss","asss","asss"));
        quickSort(list, 0, list.getLength() - 1);
    }

    public static int pivot(MusicList list, int start, int end){
        start = 0;
        end = list.getLength() + 1;

        String pivot = list.get(start).getTitle();
        int swapIndex = start;

        for (int i = start + 1; i < list.getLength(); i++){
            if (pivot.equalsIgnoreCase(list.get(i).getTitle())){
                swapIndex++;
                swap(list, swapIndex, i);
            }
        }
        return swapIndex;
    }

    public static void quickSort (MusicList list, int left, int right){
        if (right > left){
            int pivotIndex = pivot(list, left, right);

            // Sorting the left part
            quickSort(list, 0, pivotIndex - 1);
            //Sorting the right part
            quickSort(list, pivotIndex + 1, list.getLength() - 1);
        }

        System.out.println(list);
    }


    public static void swap (MusicList list, int i, int j){
        String temp = list.get(i).getTitle();
        list.get(i).setTitle(list.get(j).getTitle());
        list.get(j).setTitle(temp);
    }

}