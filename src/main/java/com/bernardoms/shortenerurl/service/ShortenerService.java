package com.bernardoms.shortenerurl.service;

import com.bernardoms.shortenerurl.exception.AliasNotFoundException;
import com.bernardoms.shortenerurl.model.URLShortener;
import com.bernardoms.shortenerurl.model.dto.URLShortenerDTO;
import com.bernardoms.shortenerurl.repository.ShortenerRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.stream.Stream;

@Service
@RequiredArgsConstructor
@Slf4j
public class ShortenerService {

    private final ShortenerRepository shortenerRepository;

    public String createShortenerURL(URLShortenerDTO urlShortener) {
        urlShortener.generateShortnerURL();
        shortenerRepository.save(URLShortener.builder().alias(urlShortener.getAlias()).originalURL(urlShortener.getOriginalURL()).build());
        return urlShortener.getAlias();
    }


    public String redirect(String alias) throws AliasNotFoundException {
        var urlShortener = findURLByAlias(alias);

        urlShortener.setRedirectCount(urlShortener.getRedirectCount() + 1);

        shortenerRepository.save(urlShortener);

        return urlShortener.getOriginalURL();
    }

    public URLShortener findURLByAlias(String alias) throws AliasNotFoundException {
        return shortenerRepository.findByAlias(alias).orElseThrow(() -> {
            log.info("Alias not found: " + alias);
            return new AliasNotFoundException("Alias not found!");
        });
    }

    public Page<URLShortenerDTO> findAll(Pageable pageable) {

        var paging = PageRequest.of(pageable.getPageNumber(), pageable.getPageSize(), pageable.getSort());
        var pageUrlShortener = shortenerRepository.findAll(paging);
        return pageUrlShortener.map(p -> new URLShortenerDTO(p.getOriginalURL(), p.getAlias(), p.getRedirectCount()));
    }
}
