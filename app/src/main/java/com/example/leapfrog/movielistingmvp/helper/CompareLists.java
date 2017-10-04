package com.example.leapfrog.movielistingmvp.helper;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;


public class CompareLists {

    public static boolean pbShowed = false;

    public boolean equalLists(List<Integer> newOnes, List<Integer> oldOnes) {
        if (newOnes == null && oldOnes == null) {
            return true;
        }

        if ((newOnes == null && oldOnes != null)
                || newOnes != null && oldOnes == null
                || newOnes.size() != oldOnes.size()) {
            return false;
        }

        //to avoid messing the order of the lists we will use a copy

        newOnes = new ArrayList<>(newOnes);
        oldOnes = new ArrayList<>(oldOnes);

        Collections.sort(newOnes);
        Collections.sort(oldOnes);
        return newOnes.equals(oldOnes);
    }


}
