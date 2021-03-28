package org.spoorn.spoornbountymobs.entity;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;

/**
 * Custom Tracked Data for entities.
 *
 * @deprecated Use Cardinal Components API which does this data management plus networking for us.
 */
@Deprecated
@NoArgsConstructor
@AllArgsConstructor
@Data
public class SpoornTrackedData implements Serializable {

    // Whether the entity has a bounty
    public boolean hasBounty;
}
