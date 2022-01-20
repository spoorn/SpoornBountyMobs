package org.spoorn.spoornbountymobs.config;

import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

/**
 * Represents a mob drop.
 */
@AllArgsConstructor
@NoArgsConstructor(access = AccessLevel.PRIVATE)
@Data
public class Drop {

    // Total chance for any of the following list of items to drop per roll
    public double dropChance;

    // number of times to roll for a drop to allow for multiple drops
    public int rolls;

    // List of possible items to drop with weights on each entry
    public List<WeightedItem> items;
}
