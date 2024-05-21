package com.camping_rental.server.utils;

import com.camping_rental.server.domain.exception.dto.NotMatchedFormatException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;

import java.net.URLDecoder;
import java.util.List;
import java.util.UUID;

public class CustomConverterUtils {
    public static UUID toUUIDElseThrow(Object obj) {
        return toUUIDElseThrow(obj, "요청하신 데이터를 찾을 수 없습니다.");
    }

    public static UUID toUUIDElseThrow(Object obj, String errorMessage) {
        UUID uuid = null;

        try {
            uuid = UUID.fromString(obj.toString());
        } catch (IllegalArgumentException | NullPointerException e) {
            throw new NotMatchedFormatException(errorMessage);
        }

        return uuid;
    }

    public static UUID toUUIDElseNull(Object obj) {
        UUID uuid = null;

        try {
            uuid = UUID.fromString(obj.toString());
        } catch (IllegalArgumentException | NullPointerException e) {
            return null;
        }

        return uuid;
    }

    public static <T> List<T> convertStringToList(String searchFilterStr) {
        ObjectMapper objectMapper = new ObjectMapper();

        try {
            // URL 디코드 수행
            String decodedString = URLDecoder.decode(searchFilterStr, "UTF-8");

            List<T> resultList = objectMapper.readValue(decodedString, new TypeReference<List<T>>() {
            });
            return resultList;
        } catch (Exception e) {
            return null;
        }
    }
}
