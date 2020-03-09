package com.bernardoms.shortenerurl.integration.controller;

import com.bernardoms.shortenerurl.integration.IntegrationTest;
import com.bernardoms.shortenerurl.model.dto.URLShortenerDTO;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import static org.hamcrest.core.Is.is;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@AutoConfigureMockMvc
public class ShortenerControllerTest extends IntegrationTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper mapper;

    @Test
    public void test_create_alias() throws Exception {
        var urlShortenerDTO = URLShortenerDTO.builder().originalURL("http://www.test.com").build();

        mockMvc.perform(
                post("/shorteners").content(mapper.writeValueAsString(urlShortenerDTO)).contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isCreated()).andExpect(header().exists("Location"));
    }

    @Test
    public void test_find_all_pageable_shorteners_url() throws Exception {

        mockMvc.perform(
                get("/shorteners").param("size", "1").param("page", "0")
                        .accept(MediaType.APPLICATION_JSON)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.content[0].originalURL", is("http://www.test.com")))
                .andExpect(jsonPath("$.content[0].alias", is("abdefg")));
    }

    @Test
    public void test_redirect_url() throws Exception {
        mockMvc.perform(
                get("/shorteners/abdefg")
                        .accept(MediaType.APPLICATION_JSON)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isFound());
    }

}
