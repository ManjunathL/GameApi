package com.mygubbi.game.proposal.model;

import junit.framework.TestCase;

import java.sql.Date;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by User on 31-05-2017.
 */
public class FindNearestNumberTest extends TestCase {





    public void testValidRecord() {

        List<Integer> numbers = new ArrayList<Integer>();
        numbers.add(100);
        numbers.add(200);
        numbers.add(300);
        numbers.add(400);
        numbers.add(600);
        numbers.add(800);
        int myNumber = 490;
        int distance = Math.abs(numbers.get(0) - myNumber);
        int idx = 0;
        for(int c = 1; c < numbers.size(); c++){
            int cdistance = Math.abs(numbers.get(c) - myNumber);
            if(cdistance < distance){
                idx = c;
                distance = cdistance;
            }
        }
        System.out.println(numbers.get(idx));
    }
}
