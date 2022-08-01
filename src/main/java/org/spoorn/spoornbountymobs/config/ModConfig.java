package org.spoorn.spoornbountymobs.config;

import static draylar.omegaconfig.OmegaConfig.GSON;
import static draylar.omegaconfig.OmegaConfig.getConfigPath;
import draylar.omegaconfig.OmegaConfig;
import draylar.omegaconfig.api.Comment;
import draylar.omegaconfig.api.Config;
import lombok.extern.log4j.Log4j2;
import org.spoorn.spoornbountymobs.SpoornBountyMobs;
import org.spoorn.spoornbountymobs.config.tiers.*;

import java.io.IOException;
import java.lang.annotation.Annotation;
import java.lang.reflect.Field;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.*;

@Log4j2
public class ModConfig implements Config {

    private static ModConfig CONFIG;

    @Comment("Chance for mob to spawn with a bounty [1/value] [default = 300]")
    public int bountyChance = 300;

    @Comment("True if Bounty mobs should glow through walls permanently, else false [default = false]")
    public boolean bountyMobPermanentGlow = false;

    @Comment("Duration in seconds Bounty mobs should glow when they are first found if bountyMobPermanentGlow is false [default = 20]")
    public int bountyMobGlowDuration = 20;

    @Comment("Player's Bounty Hunter level increments at this value [default = 10]")
    public int playerBountyHunterLevelIntervals = 10;

    @Comment("Bonus health for Player per Bounty Hunter level [default = 2 (one heart)]")
    public double playerBonusHealthPerBountyHunterLevel = 2;

    @Comment("Bonus damage for Player per Bounty Hunter tier [default = 0.5]")
    public double playerBonusDamagePerBountyHunterTier = 0.5;

    @Comment("Percentage of player's Bounty score LOST on death [default = 20, min=0, max=100]")
    public double playerDeathBountyScorePenalty = 20;

    @Comment("Set to false if you don't want potion particle effects on HUD from Bounty mob attacks [default = true]")
    public boolean showBountyParticleEffects = true;

    @Comment("Set to false to disable broadcast message when a player is near a bounty mob [default = true]")
    public boolean broadcastMessageWhenBountySpawned = true;

    @Comment("Set to false to disable broadcast message when a player kills a bounty mob [default = true]")
    public boolean broadcastMessageWhenPlayerKillBountyMob = true;

    @Comment("Set to false to disable broadcast message when a player increases their Bounty Level [default = true]")
    public boolean broadcastMessageWhenBountyLevelUp = true;
    
    @Comment("List of usernames and mob identifiers (case sensitive) to disable broadcast messages for.\n" +
            "When broadcasting if a player killed a bounty mob or increased their bounty level, this list is checked to disable broadcast.\n" +
            "When a bounty mob is spawned or killed, this list is checked against the mob identifier (e.g. \"minecraft:zombie\").\n" +
            "Useful when other mods add fake players, such as Kibe Utilities' mob farming blocks.")
    public List<String> broadcastDisabled = List.of("fake");

    @Comment("Common tier bounty mobs")
    public CommonTierConfig COMMON_TIER = new CommonTierConfig();

    @Comment("Uncommon tier bounty mobs")
    public UncommonTierConfig UNCOMMON_TIER = new UncommonTierConfig();

    @Comment("Rare tier bounty mobs")
    public RareTierConfig RARE_TIER = new RareTierConfig();

    @Comment("Epic tier bounty mobs")
    public EpicTierConfig EPIC_TIER = new EpicTierConfig();

    @Comment("Legendary tier bounty mobs")
    public LegendaryTierConfig LEGENDARY_TIER = new LegendaryTierConfig();

    @Comment("Doom tier bounty mobs")
    public DoomTierConfig DOOM_TIER = new DoomTierConfig();

    public static void init() {
        CONFIG = OmegaConfig.register(ModConfig.class);
    }

    public static ModConfig get() {
        return CONFIG;
    }

    @Override
    public String getName() {
        return SpoornBountyMobs.MODID;
    }

    @Override
    public String getExtension() {
        // For nicer comments parsing in text editors
        return "json5";
    }

    // ==
    // Manually override OmegaConfig save logic to support nested classes.
    // See https://github.com/Draylar/omega-config/issues/22.
    // ==
    
    @Override
    public void save() {
        writeConfig((Class<Config>) super.getClass(), this);
    }

    public static <T extends Config> void writeConfig(Class<T> configClass, T instance) {
        // Write the config to disk with the default values.
        String json = GSON.toJson(instance);

        // Cursed time.
        List<String> lines = new ArrayList<>(Arrays.asList(json.split("\n")));
        Map<Integer, String> insertions = new TreeMap<>();
        Map<String, String> keyToComments = populateKeyPathToComments(configClass);

        // Find areas we should insert comments into...

        // Prefix stack that keeps track of the nested json element's parents
        Deque<String> prefix = new ArrayDeque<>();
        int currIndent = 1;  // Past the root, indents start at 1
        for (int i = 0; i < lines.size(); i++) {
            String at = lines.get(i);
            String trimmed = at.trim();

            // Exited a json structure, pop the prefix stack
            int indents = countGsonIndents(at);
            if (indents <= currIndent) {
                if (!prefix.isEmpty()) {
                    prefix.removeLast();
                }
            }

            // Fields start with a quote "
            if (trimmed.startsWith("\"")) {
                // Remove quotes around the "key"
                String key = trimmed.substring(1, trimmed.indexOf("\"", 1));

                // Push onto the prefix stack, appending onto the previous prefix if nested, else just the key itself
                if (indents >= currIndent) {
                    if (prefix.isEmpty()) {
                        prefix.addLast(key);
                    } else {
                        prefix.addLast(prefix.peekLast() + "." + key);
                    }
                }

                String startingWhitespace = "  ".repeat(indents);

                for (Map.Entry<String, String> entry : keyToComments.entrySet()) {
                    String comment = entry.getValue();
                    // Check if we should insert comment
                    if (entry.getKey().equals(prefix.peekLast())) {
                        if (comment.contains("\n")) {
                            comment = startingWhitespace + "//" + String.join(String.format("\n%s//", startingWhitespace), comment.split("\n"));
                        } else {
                            comment = String.format("%s//%s", startingWhitespace, comment);
                        }
                        insertions.put(i + insertions.size(), comment);
                        break;
                    }
                }
            }

            currIndent = indents;
        }

        // insertions -> list
        for (Map.Entry<Integer, String> entry : insertions.entrySet()) {
            Integer key = entry.getKey();
            String value = entry.getValue();
            lines.add(key, value);
        }

        // list -> string
        StringBuilder res = new StringBuilder();
        lines.forEach(str -> res.append(String.format("%s%n", str)));

        try {
            Path configPath = getConfigPath(instance);
            configPath.toFile().getParentFile().mkdirs();
            Files.write(configPath, res.toString().getBytes());
        } catch (IOException ioException) {
            log.error(ioException);
            log.info(String.format("Write error, using default values for config %s.", configClass));
        }
    }

    /**
     * Fetches comments from Json key "paths".  A path is the key itself if at the root top-level, else a period
     * delimited path from the root to the key.
     *
     * For example, if this were our commented json:
     *    ```
     *     {
     *         // first comment
     *         "first": 1,
     *         // nested comment
     *         "nested": {
     *             // second comment
     *             "second": 2,
     *             // more nested comment
     *             "morenested": {
     *                 // third comment
     *                 "third": 3
     *             }
     *         }
     *     }
     *    ```
     *
     * The returned map would be:
     *    ```
     *    "first" => "first comment"
     *    "nested" => "nested comment"
     *    "nested.second" => "second comment"
     *    "nested.morenested" => "more nested comment"
     *    "nested.morenested.third" => "third comment"
     *    ```
     *
     * @param root Root class to recursively map field path keys to comments
     * @return Mapping from field path keys to comments
     */
    public static Map<String, String> populateKeyPathToComments(Class<?> root) {
        Map<String, String> keyPathToComments = new HashMap<>();
        populateKeyPathToComments(root, "", keyPathToComments, new HashSet<>());
        return keyPathToComments;
    }

    /**
     * Counts number of indents.  Gson uses double space as the default indent.
     */
    public static int countGsonIndents(String s) {
        String indent = "  ";
        int count = 0;
        while (s.startsWith(indent)) {
            count++;
            s = s.substring(indent.length());
        }
        return count;
    }

    /**
     * Finds all comments recursively.
     */
    private static void populateKeyPathToComments(Class<?> clazz, String prefix, Map<String, String> keyPathToComments,
                                                  Set<Class<?>> visited) {
        if (visited.contains(clazz)) {
            return;
        }

        visited.add(clazz);
        for (Field field : clazz.getDeclaredFields()) {
            String comment = getFieldComment(field);
            String key = prefix + field.getName();
            // populate top-level key -> comments map
            if (comment != null) {
                keyPathToComments.put(key, comment);
            }

            // Recurse on field classes
            populateKeyPathToComments(field.getType(), key + ".", keyPathToComments, visited);
        }
    }

    private static String getFieldComment(Field field) {
        Annotation[] annotations = field.getDeclaredAnnotations();

        // Find comment
        for (Annotation annotation : annotations) {
            if (annotation instanceof Comment) {
                return ((Comment) annotation).value();
            }
        }

        return null;
    }
}
