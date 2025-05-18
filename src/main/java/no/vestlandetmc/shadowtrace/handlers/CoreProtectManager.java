package no.vestlandetmc.shadowtrace.handlers;

import lombok.Getter;
import net.coreprotect.CoreProtect;
import net.coreprotect.CoreProtectAPI;
import no.vestlandetmc.shadowtrace.ShadowTrace;
import no.vestlandetmc.shadowtrace.config.Messages;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.OfflinePlayer;
import org.bukkit.entity.Player;
import org.bukkit.plugin.Plugin;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

@Getter
public class CoreProtectManager {

	private CoreProtectAPI coreProtectAPI;
	private String reason;

	public CoreProtectManager() {
		final Plugin plugin = ShadowTrace.getPlugin().getServer().getPluginManager().getPlugin("CoreProtect");

		if (!(plugin instanceof CoreProtect)) {
			this.coreProtectAPI = null;
			this.reason = "CoreProtect was not found.";
		}

		final CoreProtectAPI coreProtectAPI = ((CoreProtect) plugin).getAPI();
		if (!coreProtectAPI.isEnabled() || coreProtectAPI.APIVersion() < 10) {
			this.coreProtectAPI = null;
			this.reason = "CoreProtect requires API version 10 or higher.";
		} else {
			this.coreProtectAPI = coreProtectAPI;
			this.reason = "CoreProtect was found and connected.";
		}
	}

	public void findOres(Player player, OfflinePlayer target, String time) {
		final List<String> username = Collections.singletonList(target.getName());
		final List<Integer> action = List.of(0);

		final List<Object> blocks = Arrays.asList(
				Material.COAL_ORE,
				Material.COPPER_ORE,
				Material.DIAMOND_ORE,
				Material.GOLD_ORE,
				Material.LAPIS_ORE,
				Material.EMERALD_ORE,
				Material.IRON_ORE,
				Material.REDSTONE_ORE,
				Material.DEEPSLATE_COAL_ORE,
				Material.DEEPSLATE_COPPER_ORE,
				Material.DEEPSLATE_DIAMOND_ORE,
				Material.DEEPSLATE_GOLD_ORE,
				Material.DEEPSLATE_LAPIS_ORE,
				Material.DEEPSLATE_EMERALD_ORE,
				Material.DEEPSLATE_IRON_ORE,
				Material.DEEPSLATE_REDSTONE_ORE);

		final int taskId = Bukkit.getScheduler().runTaskTimer(ShadowTrace.getPlugin(), () -> MessageHandler.sendAction(player, Messages.SRC_ACTION_SEARCH), 0L, 40L).getTaskId();

		Bukkit.getScheduler().runTaskAsynchronously(ShadowTrace.getPlugin(), () -> {
			final List<String[]> lookup = coreProtectAPI.performLookup(convertToSeconds(time), username, null, blocks, null, action, 0, null);
			final List<String> messageList = new ArrayList<>();

			for (int i = 0; i < lookup.size() && i < 1500; i++) {
				final String[] result = lookup.get(i);
				final CoreProtectAPI.ParseResult parseResult = coreProtectAPI.parseResult(result);
				final long timestamp = parseResult.getTimestamp() / 1000;
				final int locX = parseResult.getX();
				final int locY = parseResult.getY();
				final int locZ = parseResult.getZ();
				final String world = parseResult.worldName();
				final String block = parseResult.getBlockData().getMaterial().name();
				final String out = world + ":" + locX + ":" + locY + ":" + locZ + ":" + block + ":" + timestamp;
				messageList.add(out);
			}

			if (messageList.isEmpty()) {
				Bukkit.getScheduler().cancelTask(taskId);
				MessageHandler.sendMessage(player, "&6[&eShadowTrace&6] " + Messages.SRC_NO_DATA_FOUND);
				MessageHandler.sendAction(player, Messages.SRC_ACTION_NO_DATA_FOUND);
			} else {
				ShadowTrace.getPacketHandler().sendData(player, messageList);
				Bukkit.getScheduler().cancelTask(taskId);
				MessageHandler.sendMessage(player, "&6[&eShadowTrace&6] " + Messages.SRC_DATA_FOUND);
				MessageHandler.sendAction(player, Messages.SRC_ACTION_DATA_FOUND);
			}
		});
	}

	public void findOresNether(Player player, OfflinePlayer target, String time) {
		final List<String> username = Collections.singletonList(target.getName());
		final List<Integer> action = List.of(0);

		final List<Object> blocks = Arrays.asList(
				Material.ANCIENT_DEBRIS,
				Material.NETHER_GOLD_ORE);

		final int taskId = Bukkit.getScheduler().runTaskTimer(ShadowTrace.getPlugin(), () -> MessageHandler.sendAction(player, "&e---------- &6Samler blokkdata &e----------"), 0L, 40L).getTaskId();

		Bukkit.getScheduler().runTaskAsynchronously(ShadowTrace.getPlugin(), () -> {
			final List<String[]> lookup = coreProtectAPI.performLookup(convertToSeconds(time), username, null, blocks, null, action, 0, null);
			final List<String> messageList = new ArrayList<>();

			for (int i = 0; i < lookup.size() && i < 300; i++) {
				final String[] result = lookup.get(i);
				final CoreProtectAPI.ParseResult parseResult = coreProtectAPI.parseResult(result);
				final long timestamp = parseResult.getTimestamp() / 1000;
				final int locX = parseResult.getX();
				final int locY = parseResult.getY();
				final int locZ = parseResult.getZ();
				final String world = parseResult.worldName();
				final String block = parseResult.getBlockData().getMaterial().name();
				final String out = world + ":" + locX + ":" + locY + ":" + locZ + ":" + block + ":" + timestamp;
				messageList.add(out);
			}

			if (messageList.isEmpty()) {
				Bukkit.getScheduler().cancelTask(taskId);
				MessageHandler.sendMessage(player, "&6[&eShadowTrace&6] " + Messages.SRC_NO_DATA_FOUND);
				MessageHandler.sendAction(player, Messages.SRC_ACTION_NO_DATA_FOUND);
			} else {
				ShadowTrace.getPacketHandler().sendData(player, messageList);
				Bukkit.getScheduler().cancelTask(taskId);
				MessageHandler.sendMessage(player, "&6[&eShadowTrace&6] " + Messages.SRC_DATA_FOUND);
				MessageHandler.sendAction(player, Messages.SRC_ACTION_DATA_FOUND);
			}
		});
	}

	private int convertToSeconds(String timeString) {
		int totalSeconds = 0;

		final String regex = "(\\d+)([dhm])";
		final Pattern pattern = Pattern.compile(regex);
		final Matcher matcher = pattern.matcher(timeString);

		while (matcher.find()) {
			final int value = Integer.parseInt(matcher.group(1));
			final String unit = matcher.group(2);

			switch (unit) {
				case "d":
					totalSeconds += value * 86400;
					break;
				case "h":
					totalSeconds += value * 3600;
					break;
				case "m":
					totalSeconds += value * 60;
					break;
				default:
					return 0;
			}
		}

		return totalSeconds;
	}

}
