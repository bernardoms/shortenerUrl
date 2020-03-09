package com.bernardoms.shortenerurl.repository;

import com.bernardoms.shortenerurl.model.URLShortener;
import org.bson.types.ObjectId;
import org.springframework.data.repository.PagingAndSortingRepository;

import java.util.Optional;

public interface ShortenerRepository extends PagingAndSortingRepository<URLShortener, ObjectId> {
    Optional<URLShortener> findByAlias(String alias);
}
