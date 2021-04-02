package org.spoorn.spoornbountymobs.command;

import static net.minecraft.server.command.CommandManager.argument;
import static net.minecraft.server.command.CommandManager.literal;
import net.fabricmc.fabric.api.command.v1.CommandRegistrationCallback;
import net.minecraft.command.argument.EntityArgumentType;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.text.LiteralText;
import org.spoorn.spoornbountymobs.util.SpoornBountyMobsUtil;

public class CommandRegistry {

    public static void init() {
        registerCommands();
    }

    private static void registerCommands() {
        CommandRegistrationCallback.EVENT.register((dispatcher, dedicated) -> {
            dispatcher.register(literal("spoornbountymobs")
                .then(literal("stats")
                    .then(argument("player", EntityArgumentType.player())
                        .executes(c -> {
                            PlayerEntity player = EntityArgumentType.getPlayer(c, "player");
                            c.getSource().sendFeedback(new LiteralText(
                                SpoornBountyMobsUtil.getPlayerDataComponent(player).toString()), true);
                            return 1;
                        }))));
        });
    }
}
