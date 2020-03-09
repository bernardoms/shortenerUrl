package com.bernardoms.shortenerurl.unit.service;


import com.bernardoms.shortenerurl.exception.AliasNotFoundException;
import com.bernardoms.shortenerurl.model.URLShortener;
import com.bernardoms.shortenerurl.model.dto.URLShortenerDTO;
import com.bernardoms.shortenerurl.repository.ShortenerRepository;
import com.bernardoms.shortenerurl.service.ShortenerService;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.domain.*;

import java.util.Collections;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class ShortenerServiceTest {

    @Mock
    private ShortenerRepository shortenerRepository;

    @InjectMocks
    private ShortenerService shortenerService;

    @Test
    public void test_create_alias(){
        var alias = shortenerService.createShortenerURL(URLShortenerDTO.builder().originalURL("http://test.com").build());

        verify(shortenerRepository, times(1)).save(any(URLShortener.class));

        assertEquals(alias.length(), 6);
    }

    @Test
    public void test_redirect_found_alias() throws AliasNotFoundException {
        var optionalURLShortener = Optional.of(URLShortener.builder().originalURL("http://test.com").alias("abdefg").redirectCount(0).build());

        when(shortenerRepository.findByAlias(anyString())).thenReturn(optionalURLShortener);

        String originalURL = shortenerService.redirect("abdefg");

        verify(shortenerRepository, times(1)).save(any(URLShortener.class));

        assertEquals(originalURL, "http://test.com");
    }

    @Test
    public void test_redirect_not_found_alias() {
        try {
            shortenerService.redirect("abdefg");
        } catch (AliasNotFoundException e) {
            verify(shortenerRepository, times(0)).save(any(URLShortener.class));
        }
    }

    @Test
    public void test_find_all_shortened_URL() {

        var urlShortener = URLShortener.builder().originalURL("http://test.com").alias("abdefg").redirectCount(0).build();

        var paging = PageRequest.of(0, 1, Sort.unsorted());

        var page = new PageImpl<>(List.of(urlShortener), paging, 1);

        when(shortenerRepository.findAll(paging)).thenReturn(page);

        var allShortened = shortenerService.findAll(paging);

        assertEquals(allShortened.getTotalElements(), 1);

        assertEquals(allShortened.getTotalPages(), 1);

        assertEquals(allShortened.get().findFirst().get().getAlias(), "abdefg");

        assertEquals(allShortened.get().findFirst().get().getOriginalURL(), "http://test.com");

        assertEquals(allShortened.get().findFirst().get().getRedirectCount(), 0);
    }
}
