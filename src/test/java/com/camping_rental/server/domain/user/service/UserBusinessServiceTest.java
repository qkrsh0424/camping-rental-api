package com.camping_rental.server.domain.user.service;

import org.junit.jupiter.api.Test;

import java.util.Arrays;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import static org.junit.jupiter.api.Assertions.*;

class UserBusinessServiceTest {
    @Test
    public void makeEncUsername(){
        String username = "user1";
        Integer usernameLength = username.length();
        Integer starCount = (int) Math.floor(usernameLength / 2);
        Set<Integer> starIndex = new HashSet<>();
        List<String> usernameList = Arrays.asList(username.split(""));
        StringBuilder encUsername = new StringBuilder();

        for(int i = 0; i < starCount; i++){
            Integer idx = (int) (Math.random()*usernameLength);
            while(!starIndex.add(idx)){
                idx = (int) (Math.random()*usernameLength);
            }
        }

        for(int i = 0; i< usernameLength; i++){
            if(starIndex.contains(i)){
                   encUsername.append("*");
            }else{
                encUsername.append(usernameList.get(i));
            }
        }
    }
}