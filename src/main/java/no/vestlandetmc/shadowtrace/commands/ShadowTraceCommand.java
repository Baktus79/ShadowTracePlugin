package no.vestlandetmc.shadowtrace.commands;

import io.papermc.paper.command.brigadier.BasicCommand;
import io.papermc.paper.command.brigadier.CommandSourceStack;
import no.vestlandetmc.shadowtrace.ShadowTrace;
import no.vestlandetmc.shadowtrace.config.Messages;
import no.vestlandetmc.shadowtrace.handlers.MessageHandler;
import no.vestlandetmc.shadowtrace.handlers.Permissions;
import org.bukkit.Bukkit;
import org.bukkit.OfflinePlayer;
import org.bukkit.World;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;
import org.jspecify.annotations.Nullable;

import java.util.Collection;
import java.util.List;
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
			MessageHandler.sendMessage(player, Messages.SHTC_INVALID_ARG);
			return;
		}

		if (args[0].equals("getores")) {
			if (!player.hasPermission(Permissions.SHT_COMMAND_GETORES)) {
				MessageHandler.sendMessage(player, Messages.SHTC_NO_PERM);
				return;
			}

			if (args.length < 3) {
				MessageHandler.sendMessage(player, Messages.SHTC_INVALID_ARG);
				return;
			}
		}

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

	@Override
	public @NotNull Collection<String> suggest(@NotNull CommandSourceStack commandSourceStack, String @NotNull [] args) {
		if (args.length == 1) {
			return Stream.of("getores")
					.filter(s -> s.startsWith(args[0].toLowerCase()))
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
