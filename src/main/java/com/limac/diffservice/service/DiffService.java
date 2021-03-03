package com.limac.diffservice.service;

import com.limac.diffservice.domain.Diff;
import com.limac.diffservice.rest.dto.DiffDto;
import com.limac.diffservice.type.InputType;

/**
 * Service to execute {@link Diff} operations.
 */
public interface DiffService {

    /**
     * Saves the {@link Diff}.
     *
     * @param diff diff to be saved.
     * @param inputType input type to be saved.
     *
     * @return {@link Diff} saved.
     */
    Diff save(Diff diff, InputType inputType);

    /**
     * Finds a {@link Diff} by an identifier.
     *
     * @param diffId diff identifier.
     *
     * @return {@link Diff} found.
     */
    Diff findById(String diffId);

    /**
     * Compares the left and right inputs of the given {@link Diff}.
     *
     * @param diff diff to have the left and right inputs compared.
     *
     * @return {@link DiffDto} with the results.
     */
    DiffDto diff(Diff diff);
}
