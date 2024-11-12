package no.vestlandetmc.shadowtrace.commands;

import co.aikar.commands.BaseCommand;
import co.aikar.commands.annotation.*;
import no.vestlandetmc.shadowtrace.ShadowTrace;
import org.bukkit.Bukkit;
import org.bukkit.OfflinePlayer;
import org.bukkit.World;
import org.bukkit.entity.Player;

@CommandAlias("shadowtrace|strace")
@CommandPermission("shadowtrace.command.shadowtrace")
@Description("Send en forespørsel til en annen spiller om å få se hvor mye penger en har.")
public class ShadowTraceCommand extends BaseCommand {

	@Subcommand("getores")
	@Syntax("<player> <time>")
	@CommandCompletion("@playernames 1m|1h|1d")
	public void getores(Player player, String target, String time) {
		final OfflinePlayer targetPlayer = Bukkit.getOfflinePlayerIfCached(target);

		if (player.getWorld().getEnvironment() == World.Environment.NETHER) {
			ShadowTrace.getCoreProtectManager().findOresNether(player, targetPlayer, time);
		} else if (player.getWorld().getEnvironment() == World.Environment.NORMAL) {
			ShadowTrace.getCoreProtectManager().findOres(player, targetPlayer, time);
		}
	}

	@Default
	public void onDefaultCommand(Player player) {

	}

}
