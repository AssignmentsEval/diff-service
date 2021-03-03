package com.limac.diffservice.repository;

import com.limac.diffservice.domain.Diff;
import org.springframework.data.mongodb.repository.MongoRepository;

/**
 * Repository for {@link Diff} domain.
 */
public interface DiffRepository extends MongoRepository<Diff, String> {
}
