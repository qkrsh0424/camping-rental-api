package com.camping_rental.server.utils;

import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.*;

@Slf4j
class DataFormatUtilsTest {
    @Test
    public void isPassUsernameFormatValid(){
        List<Map<String, Object>> usernameList = new ArrayList<>();

        usernameList.add(Map.of("username", "u", "bool", false));
        usernameList.add(Map.of("username", "user", "bool", false));
        usernameList.add(Map.of("username", "1user1", "bool", false));
        usernameList.add(Map.of("username", "User1", "bool", false));
        usernameList.add(Map.of("username", "user1@", "bool", false));
        usernameList.add(Map.of("username", "user1@u", "bool", false));
        usernameList.add(Map.of("username", "user1@u", "bool", false));
        usernameList.add(Map.of("username", "user11111111111111111", "bool", false));
        usernameList.add(Map.of("username", "user111 ", "bool", false));

        usernameList.add(Map.of("username", "user1", "bool", true));
        usernameList.add(Map.of("username", "user111111111111111", "bool", true));
        usernameList.add(Map.of("username", "user1111111111111111", "bool", true));

        usernameList.stream().forEach(r->{
            log.info("username : {}", r.get("username"));
            Assertions.assertEquals(DataFormatUtils.isPassUsernameFormatValid(r.get("username").toString()), r.get("bool"));
        });
    }

    @Test
    public void isPassPasswordFormatValid(){
        List<Map<String,Object>> passwordList = new ArrayList<>();

        passwordList.add(Map.of("password", "1", "bool", false));
        passwordList.add(Map.of("password", "user11@", "bool", false));
        passwordList.add(Map.of("password", "@@@@@@@@", "bool", false));
        passwordList.add(Map.of("password", "11111111", "bool", false));
        passwordList.add(Map.of("password", "uuuuuuuu", "bool", false));
        passwordList.add(Map.of("password", "user@@@@", "bool", false));
        passwordList.add(Map.of("password", "user1111", "bool", false));
        passwordList.add(Map.of("password", "user111!!!user111!!!user111!!!user111!!!user111!!!!", "bool", false));

        passwordList.add(Map.of("password", "user111!!!user111!!!user111!!!user111!!!user111!!!", "bool", true));
        passwordList.add(Map.of("password", "111user!", "bool", true));
        passwordList.add(Map.of("password", "user1111@", "bool", true));
        passwordList.add(Map.of("password", "user111@", "bool", true));
        passwordList.add(Map.of("password", "1user11!", "bool", true));
        passwordList.add(Map.of("password", "@user11!", "bool", true));

        passwordList.stream().forEach(r->{
            log.info("password : {}", r.get("password"));
            Assertions.assertEquals(DataFormatUtils.isPassPasswordFormatValid(r.get("password").toString()), r.get("bool"));
        });
    }

    @Test
    public void isPassNicknameFormatValid(){
        List<Map<String,Object>> list = new ArrayList<>();

        list.add(Map.of("nickname", "1", "bool", false));
        list.add(Map.of("nickname", "a", "bool", false));
        list.add(Map.of("nickname", "@", "bool", false));
        list.add(Map.of("nickname", "1111111111111111", "bool", false));
        list.add(Map.of("nickname", "aaaaaaaaaaaaaaaa", "bool", false));
        list.add(Map.of("nickname", "@@@@@@@@@@@@@@@@", "bool", false));
        list.add(Map.of("nickname", "aaa ", "bool", false));
        list.add(Map.of("nickname", " aaa", "bool", false));

        list.add(Map.of("nickname", "11", "bool", true));
        list.add(Map.of("nickname", "aa", "bool", true));
        list.add(Map.of("nickname", "@@", "bool", true));
        list.add(Map.of("nickname", "111111111111111", "bool", true));
        list.add(Map.of("nickname", "aaaaaaaaaaaaaaa", "bool", true));
        list.add(Map.of("nickname", "@@@@@@@@@@@@@@@", "bool", true));
        list.add(Map.of("nickname", "수 비 다 아 웃 도 어", "bool", true));


        list.stream().forEach(r->{
            log.info("nickname : {}", r.get("nickname"));
            Assertions.assertEquals(DataFormatUtils.isPassNicknameFormatValid(r.get("nickname").toString()), r.get("bool"));
        });
    }

    @Test
    public void isPassPhoneNumberFormatValid(){
        List<Map<String,Object>> list = new ArrayList<>();

        list.add(Map.of("phoneNumber", "aaaaaaaaaaa", "bool", false));
        list.add(Map.of("phoneNumber", "10112341234", "bool", false));
        list.add(Map.of("phoneNumber", "010123412345", "bool", false));
        list.add(Map.of("phoneNumber", "010aaaaaaaa", "bool", false));
        list.add(Map.of("phoneNumber", "010aaaaaaa", "bool", false));
        list.add(Map.of("phoneNumber", "010123123", "bool", false));
        list.add(Map.of("phoneNumber", "02012341234", "bool", false));
        list.add(Map.of("phoneNumber", "010@@@@@@@@", "bool", false));
        list.add(Map.of("phoneNumber", "01212341234", "bool", false));
        list.add(Map.of("phoneNumber", "01212341234", "bool", false));

        list.add(Map.of("phoneNumber", "0101231234", "bool", true));
        list.add(Map.of("phoneNumber", "01012341234", "bool", true));
        list.add(Map.of("phoneNumber", "0111231234", "bool", true));
        list.add(Map.of("phoneNumber", "01112341234", "bool", true));
        list.add(Map.of("phoneNumber", "0161231234", "bool", true));
        list.add(Map.of("phoneNumber", "01612341234", "bool", true));
        list.add(Map.of("phoneNumber", "0171231234", "bool", true));
        list.add(Map.of("phoneNumber", "01712341234", "bool", true));
        list.add(Map.of("phoneNumber", "0181231234", "bool", true));
        list.add(Map.of("phoneNumber", "01812341234", "bool", true));
        list.add(Map.of("phoneNumber", "0191231234", "bool", true));
        list.add(Map.of("phoneNumber", "01912341234", "bool", true));


        list.stream().forEach(r->{
            log.info("phoneNumber : {}", r.get("phoneNumber"));
            Assertions.assertEquals(DataFormatUtils.isPassPhoneNumberFormatValid(r.get("phoneNumber").toString()), r.get("bool"));
        });
    }

    @Test
    public void isPassEmailFormatValid(){
        List<Map<String,Object>> list = new ArrayList<>();

        list.add(Map.of("email", "user111", "bool", false));
        list.add(Map.of("email", "user111@naver", "bool", false));
        list.add(Map.of("email", "user111@naver.c", "bool", false));
        list.add(Map.of("email", "user111@@", "bool", false));
        list.add(Map.of("email", "u@naver.com", "bool", false));

        list.add(Map.of("email", "user111@naver.com", "bool", true));
        list.add(Map.of("email", "user111@gmail.com", "bool", true));
        list.add(Map.of("email", "user111@naver.co.kr", "bool", true));
        list.add(Map.of("email", "user111@naver.co.kr.kr", "bool", true));


        list.stream().forEach(r->{
            log.info("email : {}", r.get("email"));
            Assertions.assertEquals(DataFormatUtils.isPassEmailFormatValid(r.get("email").toString()), r.get("bool"));
        });
    }

}