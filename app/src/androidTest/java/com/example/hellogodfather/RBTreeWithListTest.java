package com.example.hellogodfather;

import org.junit.Test;
import org.junit.Test.*;

import java.util.ArrayList;
import java.util.Random;

import static org.junit.Assert.assertEquals;

import com.example.hellogodfather.datastructure.RBTreeWithList;

public class RBTreeWithListTest {
    @Test
    public void testRandomInsert() {
        Random r = new Random();
        int[] randomArray = new int[1000];
        for (int i = 0; i < 1000; i++) {
            randomArray[i] = r.nextInt();
        }
        RBTreeWithList<Integer> rbTree = new RBTreeWithList<>();
        for (int i = 0; i < 1000; i++) {
            ArrayList<Object> list = new ArrayList<>();
            for (int j = 0; j < randomArray[i] % 20; j++) {
                list.add(j);
            }
            rbTree.put(randomArray[i], list);
            // assertEquals(randomArray[i], rbTree.get(randomArray[i]).get(0));
        }
    }
    @Test
    public void testRandomDelete() {
        Random r = new Random();
        int[] randomArray = new int[1000];
        for (int i = 0; i < 1000; i++) {
            randomArray[i] = r.nextInt();
        }
        RBTreeWithList<Integer> rbTree = new RBTreeWithList<>();
        for (int i = 0; i < 1000; i++) {
            ArrayList<Object> list = new ArrayList<>();
            for (int j = 0; j < randomArray[i] % 20; j++) {
                list.add(j);
            }
            rbTree.put(randomArray[i], list);
            // assertEquals(randomArray[i], rbTree.get(randomArray[i]).get(0));
        }
        for (int i = 0; i < 1000; i++) {
            rbTree.delete(randomArray[i]);
        }
    }
}
