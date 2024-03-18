package com.lifu.wsms.reload.api;

import java.util.Optional;

/**
 * The SequenceService interface represents a service for generating
 * sequential values for specified sequence names.
 */
public interface SequenceService {

    /**
     * Retrieves the next sequential value for the specified sequence name.
     * The sequence name is used to identify the sequence for which the
     * next value needs to be retrieved.
     *
     * @param sequenceName the name of the sequence for which to retrieve the next value
     * @return the next sequential value for the specified sequence name
     */
    Optional<Long> getNextSequenceValue(String sequenceName);
}

