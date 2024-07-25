package com.itheima.mp.service;

import com.itheima.mp.domain.po.Address;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import static org.junit.jupiter.api.Assertions.*;
@SpringBootTest
class IAddressServiceTest {
    @Autowired
    private IAddressService iAddressService;
    @Test
    void testdelete(){
        iAddressService.removeById(59);
        Address address = iAddressService.getById(59);
        System.out.println(address);
    }
}