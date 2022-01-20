package org.spoorn.spoornbountymobs.util;

import lombok.AllArgsConstructor;
import lombok.ToString;
import org.apache.commons.math3.distribution.EnumeratedDistribution;

import java.util.regex.Pattern;

@AllArgsConstructor
@ToString
public class DropDistributionData {

    public Pattern entityIdPattern;
    public double dropChance;
    public int rolls;
    public EnumeratedDistribution<String> itemDrops;
}
