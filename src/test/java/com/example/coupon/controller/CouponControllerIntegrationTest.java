package com.example.coupon.controller;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;

import java.time.LocalDateTime;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@SpringBootTest
@AutoConfigureMockMvc
public class CouponControllerIntegrationTest {

    @Autowired
    private MockMvc mockMvc;

    @Test
    void testFullCouponLifecycle() throws Exception {
        String createJson = "{""
                + "\"templateId\":\"TPL_LIFECYCLE\","
                + "\"name\":\"\u751f\u547d\u5468\u671f\u6d4b\u8bd5\u5238\","
                + "\"couponType\":\"CASH\","
                + "\"couponValue\":1000,"
                + "\"minConsume\":5000,"
                + "\"startTime\":\"" + LocalDateTime.now().minusDays(1).toString() + "\","
                + "\"endTime\":\"" + LocalDateTime.now().plusDays(30).toString() + "\","
                + "\"totalQuantity\":1000,"
                + "\"userMaxQuantity\":5"
                + "}";

        mockMvc.perform(post("/api/coupon/templates")
                .contentType(MediaType.APPLICATION_JSON)
                .content(createJson))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.templateId").value("TPL_LIFECYCLE"));

        MvcResult receiveResult = mockMvc.perform(post("/api/coupon/users/1/receive")
                .param("templateId", "TPL_LIFECYCLE"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.couponCode").exists())
                .andReturn();

        String responseBody = receiveResult.getResponse().getContentAsString();
        String couponCode = responseBody.replaceAll(".*\"couponCode\":\\"([^\"]+)\".*", "$1");

        mockMvc.perform(get("/api/coupon/users/1/coupons"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0]").exists());

        String useJson = "{\"couponCode\":\"" + couponCode + "\",\"orderAmount\":\"100.00\"}";
        mockMvc.perform(post("/api/coupon/users/1/use")
                .contentType(MediaType.APPLICATION_JSON)
                .content(useJson))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.success").value(true));
    }

    @Test
    void testCreateTemplateReturnsCreated() throws Exception {
        String createJson = "{""
                + "\"templateId\":\"TPL_CREATE_001\","
                + "\"name\":\"\u521b\u5efa\u6d4b\u8bd5\u5238\","
                + "\"couponType\":\"DISCOUNT\","
                + "\"couponValue\":2000,"
                + "\"minConsume\":10000,"
                + "\"startTime\":\"" + LocalDateTime.now().minusDays(1).toString() + "\","
                + "\"endTime\":\"" + LocalDateTime.now().plusDays(30).toString() + "\","
                + "\"totalQuantity\":500,"
                + "\"userMaxQuantity\":3"
                + "}";

        mockMvc.perform(post("/api/coupon/templates")
                .contentType(MediaType.APPLICATION_JSON)
                .content(createJson))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.templateId").value("TPL_CREATE_001"));
    }

    @Test
    void testReceiveNonExistentTemplate() throws Exception {
        mockMvc.perform(post("/api/coupon/users/1/receive")
                .param("templateId", "NON_EXISTENT"))
                .andExpect(status().isBadRequest());
    }
}
