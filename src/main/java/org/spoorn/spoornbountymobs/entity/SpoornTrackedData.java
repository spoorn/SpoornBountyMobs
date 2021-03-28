package org.spoorn.spoornbountymobs.entity;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;

@NoArgsConstructor
@AllArgsConstructor
@Data
public class SpoornTrackedData implements Serializable {

    // Whether the entity has a bounty
    public boolean hasBounty;
}
