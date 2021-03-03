package com.limac.diffservice.domain;

import lombok.Data;
import org.springframework.data.annotation.Id;
import org.springframework.data.annotation.TypeAlias;
import org.springframework.data.mongodb.core.mapping.Document;

/**
 * Diff domain object.
 */
@Data
@Document(collection = "diffs")
@TypeAlias("DIFF")
public class Diff {

    @Id
    private String diffId;

    private String left;

    private String right;
}
