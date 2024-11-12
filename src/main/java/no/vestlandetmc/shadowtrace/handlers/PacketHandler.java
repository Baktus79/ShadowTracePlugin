package no.vestlandetmc.shadowtrace.handlers;

import no.vestlandetmc.shadowtrace.ShadowTrace;
import org.bukkit.entity.Player;

import java.io.ByteArrayOutputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.util.List;

public class PacketHandler {

	private final ShadowTrace plugin;
	private final String channel;

	public PacketHandler(ShadowTrace plugin, String channel) {
		this.plugin = plugin;
		this.channel = channel;

		plugin.getServer().getMessenger().registerOutgoingPluginChannel(plugin, channel);
	}

	public void sendData(Player player, List<String> messageList) {
		try (ByteArrayOutputStream byteStream = new ByteArrayOutputStream();
			 DataOutputStream out = new DataOutputStream(byteStream)) {

			out.writeInt(messageList.size());

			for (String message : messageList) {
				byte[] bytes = message.getBytes(StandardCharsets.UTF_8);
				out.writeInt(bytes.length); // Lengden på strengen
				out.write(bytes);            // Selve strengen som bytes
			}

			player.sendPluginMessage(plugin, channel, byteStream.toByteArray());

		} catch (IOException e) {
			plugin.getLogger().severe("Error sending data: " + e.getMessage());
		}
	}
}
