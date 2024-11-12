package no.vestlandetmc.shadowtrace.handlers;

import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.TextComponent;
import net.kyori.adventure.text.serializer.legacy.LegacyComponentSerializer;
import net.kyori.adventure.title.Title;
import no.vestlandetmc.shadowtrace.ShadowTrace;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;

import java.time.Duration;

public class MessageHandler {

	public static void sendAction(Player player, String message) {
		final Component text = colorize(message);
		player.sendActionBar(text);
	}

	public static void sendTitle(Player player, String maintitle, String subtitle) {
		final Component main = colorize(maintitle);
		final Component sub = colorize(subtitle);
		Title title = Title.title(main, sub);
		player.showTitle(title);
	}

	public static void sendTitle(Player player, String maintitle, String subtitle, int fadein, int stay, int fadeout) {
		final Component main = colorize(maintitle);
		final Component sub = colorize(subtitle);
		Title.Times times = Title.Times.times(Duration.ofSeconds(fadein), Duration.ofSeconds(stay), Duration.ofSeconds(fadeout));
		Title title = Title.title(main, sub, times);
		player.showTitle(title);
	}

	public static void sendMessage(Player player, String... message) {
		for (String s : message) {
			sendMessage(player, s);
		}
	}

	public static void sendMessage(Player player, String message) {
		final Component text = colorize(message);
		player.sendMessage(text);
	}

	public static void sendAnnounce(String message) {
		final Component text = colorize(message);

		for (Player player : Bukkit.getOnlinePlayers()) {
			player.sendMessage(text);
		}

	}

	public static void sendConsole(String... message) {
		for (String m : message) {
			sendConsole(m);
		}
	}

	public static void sendConsole(String message) {
		final Component text = colorize(message);
		ShadowTrace.getPlugin().getServer().getConsoleSender().sendMessage(text);
	}

	public static Component colorize(String... messages) {
		TextComponent finalMessage = null;
		int c = 1;

		for (String m : messages) {
			if (finalMessage == null) {
				finalMessage = LegacyComponentSerializer.legacy('&').deserialize(m).append(Component.newline());
			} else if (c == messages.length) {
				Component more = LegacyComponentSerializer.legacy('&').deserialize(m);
				finalMessage = finalMessage.append(more);
			} else {
				Component more = LegacyComponentSerializer.legacy('&').deserialize(m).append(Component.newline());
				finalMessage = finalMessage.append(more);
			}

			++c;
		}

		return finalMessage;
	}

	public static Component colorize(String message) {
		return LegacyComponentSerializer.legacy('&').deserialize(message);
	}
}
