package com.bernardoms.shortenerurl.repository;

import com.bernardoms.shortenerurl.model.URLShortener;
import org.bson.types.ObjectId;
import org.springframework.cache.annotation.CacheConfig;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.data.repository.PagingAndSortingRepository;

import java.util.Optional;

@CacheConfig(cacheNames = {"alias"})
public interface ShortenerRepository extends PagingAndSortingRepository<URLShortener, ObjectId> {
    @Cacheable
    Optional<URLShortener> findByAlias(String alias);
}
