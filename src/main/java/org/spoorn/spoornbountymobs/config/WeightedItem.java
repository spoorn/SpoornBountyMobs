package org.spoorn.spoornbountymobs.config;

import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * Contains an item identifier and its relative weight.
 */
@AllArgsConstructor
@NoArgsConstructor(access = AccessLevel.PRIVATE)
@Data
public class WeightedItem {

    public String item;
    public int weight;
}
