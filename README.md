# SpoornBountyMobs
https://www.curseforge.com/minecraft/mc-mods/spoorn-bounty-mobs


https://i.imgur.com/vadvELV.gif

https://i.imgur.com/MVdDInI.gif

# Overview

Mobs have a chance to spawn marked with a Bounty!   A marked Bounty mob will have special effects, sizes, stats, and will bountifully reward the player with fully configurable loot drops, enhancing the combat experience.

The vision for this mod is to upgrade the experience when fighting mobs and give incentive for exploring to find these special Bounty mobs and hunt them down.  This can enhance existing mob hunting systems, or players can grind to become a full fledged Bounty Hunter.  A full list of Bounty mob stats, Player Bounty Hunter stats, and rewards are listed below. 

This mod is highly configurable e.g. you can turn off highlights to get a more vanilla feel

# Bounty Mobs
- 6 Tiers: Common, Uncommon, Rare, Epic, Legendary, Doom
- Bonus stats based on tiers: Bonus Health, damage, experience drop 
- Different size scaling
- Each tier has a configurable highlight color
- Weakness Effect
- Wither Effect
- Blindness
- Poison
- Slowness
- Hunger
- Fire Attacks
- Resistance
- Regeneration
- More coming...

# Player Stats
- Kill Counts for each Tier of Bounty mobs killed
- Bounty Hunter score based on the kill counts
- Bounty Hunter Level based on the score and configuration
- Bonus Health based on the highest Bounty Hunter Tier reached
- Bonus Damage based on the highest Bounty Hunter Tier reached
- More coming...
 

# Configuration
The configuration file is fully documented here: ![https://github.com/spoorn/SpoornBountyMobs/blob/main/config-documentation.json5](https://github.com/spoorn/SpoornBountyMobs/blob/main/config-documentation.json5)

(Documentation in the config file is also generated when you load the mod)

The config file is at `config/spoornbountymobs.json5`. There are configurations for each Tier of mobs, and top-level configurations such as chance for mob to be marked with a bounty, Bonus health/damage per Player's Bounty Hunter Level, etc.

For example, if you want to turn off the highlights to have a more immersive and chance encounter experience, you can set `"bountyMobGlowDuration" : 0`.

## Loot Drop Configuration
Loot drops are finally here!  This is FULLY CONFIGURABLE end to end.  You can configure for each tier, what distribution of item drops can drop for any mob entity.  The configuration supports regex meaning you can fully customize the loot drops for any items (including other mods) and any mob entity (including other mods).  See the config documentation for schema and example: https://github.com/spoorn/SpoornBountyMobs/blob/main/config-documentation.json5#L38-L142

 
# Commands
`/spoornbountymobs stats <player> Will display the player's stats`

I want to eventually add a UI for this

## Requirements
Also requires Cloth Config API: https://www.curseforge.com/minecraft/mc-mods/cloth-config

Recommended to include HealthOverlay so you can see how many Hearts you have beyond the vanilla cap: https://www.curseforge.com/minecraft/mc-mods/health-overlay-fabric 

This mod is required on both client and server

If you like what you see, check out my other mods! :  https://www.curseforge.com/members/spoorn/projects
