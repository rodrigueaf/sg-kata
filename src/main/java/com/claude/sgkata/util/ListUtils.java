package com.claude.sgkata.util;

import java.util.ArrayList;
import java.util.List;

public class ListUtils {

    public static <T> List<List<T>> partition(List<T> list, int size) {
        List<List<T>> newList = new ArrayList<>();
        int index = 0;
        do {
            try{
                newList.add(list.subList(index, index + size));
            }catch (IndexOutOfBoundsException ex){
                newList.add(list.subList(index, list.size()));
            }
            index += size;
        } while (index < list.size());
        return newList;
    }
}
