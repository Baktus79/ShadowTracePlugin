package no.vestlandetmc.shadowtrace.commands;

import io.papermc.paper.command.brigadier.BasicCommand;
import io.papermc.paper.command.brigadier.CommandSourceStack;
import no.vestlandetmc.shadowtrace.ShadowTrace;
import no.vestlandetmc.shadowtrace.config.Messages;
import no.vestlandetmc.shadowtrace.handlers.MessageHandler;
import no.vestlandetmc.shadowtrace.handlers.Permissions;
import org.bukkit.*;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.event.player.PlayerTeleportEvent;
import org.jetbrains.annotations.NotNull;
import org.jspecify.annotations.Nullable;

import java.util.Arrays;
import java.util.Collection;
import java.util.List;
import java.util.Objects;
import java.util.stream.Stream;

@SuppressWarnings("UnstableApiUsage")
public class ShadowTraceCommand implements BasicCommand {

	@Override
	public void execute(@NotNull CommandSourceStack commandSourceStack, String @NotNull [] args) {
		if (!(commandSourceStack.getSender() instanceof Player player)) {
			MessageHandler.sendConsole(Messages.SHTC_ONLY_PLAYER);
			return;
		}

		if (args.length == 0) {
			MessageHandler.sendMessage(player, Messages.SHTC_INVALID_ARG_GETORES);
			return;
		}

		switch (args[0]) {
			case "getores" -> {
				if (args.length < 3) {
					MessageHandler.sendMessage(player, Messages.SHTC_INVALID_ARG_GETORES);
				} else {
					final String targetName = args[1];
					final String time = args[2];
					final OfflinePlayer targetPlayer = Bukkit.getOfflinePlayerIfCached(targetName);

					if (targetPlayer == null) {
						MessageHandler.sendMessage(player, Messages.SHTC_INVALID_PLAYER);
						return;
					}

					final World.Environment env = player.getWorld().getEnvironment();
					if (env == World.Environment.NETHER) {
						ShadowTrace.getCoreProtectManager().findOresNether(player, targetPlayer, time);
					} else if (env == World.Environment.NORMAL) {
						ShadowTrace.getCoreProtectManager().findOres(player, targetPlayer, time);
					} else {
						MessageHandler.sendMessage(player, Messages.SHTC_NO_ORES);
					}
				}
			}
			case "teleport" -> {
				if (args.length >= 4) {
					final double x = Double.parseDouble(args[1]);
					final double y = Double.parseDouble(args[2]);
					final double z = Double.parseDouble(args[3]);

					final Location loc = new Location(player.getWorld(), x, y, z);
					player.teleport(loc, PlayerTeleportEvent.TeleportCause.COMMAND);
				}
			}
			case "blocks" -> {
				if (args.length < 4) {
					MessageHandler.sendMessage(player, Messages.SHTC_INVALID_ARG_BLOCKS);
				} else {
					final String targetName = args[1];
					final String time = args[2];
					final List<Object> blocks = Arrays.stream(args[3].split(",")).map(Material::matchMaterial).filter(Objects::nonNull).filter(Material::isSolid).map(m -> (Object) m).toList();
					final OfflinePlayer targetPlayer = Bukkit.getOfflinePlayerIfCached(targetName);

					if (targetPlayer == null) {
						MessageHandler.sendMessage(player, Messages.SHTC_INVALID_PLAYER);
						return;
					}

					if (blocks.isEmpty()) {
						MessageHandler.sendMessage(player, Messages.SHTC_NO_VALID_BLOCKS);
						return;
					}

					ShadowTrace.getCoreProtectManager().findBlocks(player, targetPlayer, time, blocks);
				}
			}
		}
	}

	@Override
	public @NotNull Collection<String> suggest(@NotNull CommandSourceStack commandSourceStack, String @NotNull [] args) {
		final List<String> subs = List.of("getores", "blocks");

		if (args.length == 0) {
			return subs;
		} else if (args.length == 1) {
			return subs.stream()
					.filter(s -> s.startsWith(args[0].toLowerCase()) || args[0].isEmpty())
					.toList();
		} else if (args.length == 2) {
			return Bukkit.getOnlinePlayers().stream()
					.map(Player::getName)
					.filter(name -> name.toLowerCase().startsWith(args[1].toLowerCase()))
					.toList();
		} else if (args.length == 3) {
			return Stream.of("1m", "1h", "1d")
					.filter(s -> s.startsWith(args[2].toLowerCase()))
					.toList();
		}

		return List.of();
	}

	@Override
	public boolean canUse(@NotNull CommandSender sender) {
		return BasicCommand.super.canUse(sender);
	}

	@Override
	public @Nullable String permission() {
		return Permissions.SHT_COMMAND.getName();
	}
}
