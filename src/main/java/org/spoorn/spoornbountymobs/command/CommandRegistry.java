package org.spoorn.spoornbountymobs.command;

import static net.minecraft.server.command.CommandManager.argument;
import static net.minecraft.server.command.CommandManager.literal;
import net.fabricmc.fabric.api.command.v2.CommandRegistrationCallback;
import net.minecraft.command.argument.EntityArgumentType;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.text.Text;
import net.minecraft.util.Formatting;
import org.spoorn.spoornbountymobs.util.SpoornBountyMobsUtil;

public class CommandRegistry {

    public static void init() {
        registerCommands();
    }

    private static void registerCommands() {
        CommandRegistrationCallback.EVENT.register((dispatcher, registryAccess, environment) -> {
            dispatcher.register(literal("spoornbountymobs")
                .then(literal("stats")
                    .then(argument("player", EntityArgumentType.player())
                        .executes(c -> {
                            PlayerEntity player = EntityArgumentType.getPlayer(c, "player");
                            c.getSource().sendFeedback(() -> Text.literal(
                                    SpoornBountyMobsUtil.getPlayerDataComponent(player).toString()).formatted(Formatting.ITALIC), true);
                            return 1;
                        }))));
        });
    }
}
