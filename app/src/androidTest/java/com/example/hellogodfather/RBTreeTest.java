package com.example.hellogodfather;
import org.junit.Test;
import org.junit.Test.*;

import java.util.ArrayList;
import java.util.Random;

import static org.junit.Assert.assertEquals;

import com.example.hellogodfather.datastructure.RBTree;

public class RBTreeTest {
    @Test
    public void testRandomInsert() {
        Random r = new Random();
        int[] randomArray = new int[1000];
        for (int i = 0; i < 1000; i++) {
            randomArray[i] = r.nextInt();
        }
        RBTree<Integer, Integer> rbTree = new RBTree<>();
        for (int i = 0; i < 1000; i++) {
            ArrayList<Object> list = new ArrayList<>();
            rbTree.put(randomArray[i], randomArray[i]);
            // assertEquals(randomArray[i], rbTree.get(randomArray[i]).get(0));
        }
    }
}
