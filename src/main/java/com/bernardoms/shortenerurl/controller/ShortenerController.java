package com.bernardoms.shortenerurl.controller;

import com.bernardoms.shortenerurl.exception.AliasNotFoundException;
import com.bernardoms.shortenerurl.model.URLShortener;
import com.bernardoms.shortenerurl.model.dto.URLShortenerDTO;
import com.bernardoms.shortenerurl.service.ShortenerService;
import io.swagger.annotations.ApiResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.util.UriComponentsBuilder;

import javax.validation.constraints.NotNull;
import java.util.List;

@RestController
@RequestMapping("/shorteners")
@RequiredArgsConstructor
public class ShortenerController {

    private final ShortenerService shortenerService;

    @PostMapping
    @ResponseStatus(value = HttpStatus.CREATED)
    public ResponseEntity<URLShortener> create(@RequestBody @Validated URLShortenerDTO urlShortener,  UriComponentsBuilder uriComponentsBuilder) {

        var shortenedURL = shortenerService.createShortenerURL(urlShortener);

        var uriComponents =
                uriComponentsBuilder.path("/shorteners/{id}").buildAndExpand(shortenedURL);

        return ResponseEntity.created(uriComponents.toUri()).build();
    }

    @GetMapping(path = "/{alias}")
    @ResponseStatus(value = HttpStatus.FOUND)
    public ResponseEntity<String> redirect(@PathVariable String alias) throws AliasNotFoundException {

        var headers = new HttpHeaders();

        String url = shortenerService.redirect(alias);

        headers.add("Location", url);

        return new ResponseEntity<>(headers, HttpStatus.FOUND);
    }

    @GetMapping
    @ResponseStatus(value = HttpStatus.OK)
    public ResponseEntity<Page<URLShortenerDTO>> getAllShortenedUrl(@NotNull Pageable pageable) {
        Page<URLShortenerDTO> all = shortenerService.findAll(pageable);
        return new ResponseEntity<>(all, HttpStatus.OK);
    }
}
