package com.camping_rental.server.utils;

import org.junit.jupiter.api.Test;

//@ExtendWith(SpringExtension.class)
//@SpringBootTest
class CustomUniqueKeyUtilsTest {

    @Test
    public void generate(){
        System.out.println(CustomUniqueKeyUtils.generateOrderNumber18());
    }
}