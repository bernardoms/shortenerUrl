package com.bernardoms.shortenerurl.unit.controller;

import com.bernardoms.shortenerurl.controller.ShortenerController;
import com.bernardoms.shortenerurl.model.dto.URLShortenerDTO;
import com.bernardoms.shortenerurl.service.ShortenerService;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.data.web.PageableHandlerMethodArgumentResolver;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import java.util.List;

import static org.hamcrest.core.Is.is;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;
import static org.springframework.test.web.servlet.setup.MockMvcBuilders.standaloneSetup;

@ExtendWith(MockitoExtension.class)
public class ShortenerControllerTest {

    @InjectMocks
    private ShortenerController shortenerController;

    private MockMvc mvc;

    @Mock
    private ShortenerService shortenerService;

    @BeforeEach
    public void setUp() {
        mvc = standaloneSetup(shortenerController).setCustomArgumentResolvers(new PageableHandlerMethodArgumentResolver())
                .build();
    }

    @Test
    public void test_create_alias() throws Exception {

        var mapper = new ObjectMapper();

        var urlShortenerDTO = URLShortenerDTO.builder().originalURL("http://www.test.com").build();

        when(shortenerService.createShortenerURL(any(URLShortenerDTO.class))).thenReturn("abcdef");

        mvc.perform(
                post("/shorteners").content(mapper.writeValueAsString(urlShortenerDTO)).contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isCreated()).andExpect(header().string("Location","http://localhost/shorteners/abcdef"));

    }

    @Test
    public void test_create_invalidURL_alias() throws Exception {

        var mapper = new ObjectMapper();

        var urlShortenerDTO = URLShortenerDTO.builder().originalURL("test.com").build();

        mvc.perform(
                post("/shorteners").content(mapper.writeValueAsString(urlShortenerDTO)).contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isBadRequest()).andExpect(header().doesNotExist("Location"));

    }

    @Test
    public void test_find_all_pageable_shorteners_url() throws Exception {

        var urlShortener = URLShortenerDTO.builder().originalURL("http://www.test.com").build();

        var pageable = PageRequest.of(0,1, Sort.unsorted());

        var urlShortenerPage = new PageImpl<>(List.of(urlShortener), pageable, 1);

        when(shortenerService.findAll(pageable)).thenReturn(urlShortenerPage);

         mvc.perform(
                get("/shorteners").param("size", "1").param("page", "0")
                        .accept(MediaType.APPLICATION_JSON)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.content[0].originalURL", is("http://www.test.com")));

    }

    @Test
    public void test_redirect() throws Exception {
        when(shortenerService.redirect("abcdef")).thenReturn("http://www.test.com");

        mvc.perform(
                get("/shorteners/abcdef")
                        .accept(MediaType.APPLICATION_JSON)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isFound());
    }
}
