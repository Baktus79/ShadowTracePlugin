package no.vestlandetmc.shadowtrace;

import co.aikar.commands.PaperCommandManager;
import com.google.common.collect.ImmutableList;
import lombok.Getter;
import no.vestlandetmc.shadowtrace.commands.ShadowTraceCommand;
import no.vestlandetmc.shadowtrace.handlers.CoreProtectManager;
import no.vestlandetmc.shadowtrace.handlers.PacketHandler;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.plugin.java.JavaPlugin;

public final class ShadowTrace extends JavaPlugin {

	@Getter
	private static ShadowTrace plugin;
	@Getter
	private static CoreProtectManager coreProtectManager;
	@Getter
	private static PacketHandler packetHandler;
	private PaperCommandManager manager;

	@Override
	public void onEnable() {
		plugin = this;
		this.manager = new PaperCommandManager(this);
		packetHandler = new PacketHandler(this, "shadowtrace:transfer_block_data");
		coreProtectManager = new CoreProtectManager();

		setupCompletions();
		manager.registerCommand(new ShadowTraceCommand());

		if (coreProtectManager != null) {
			getLogger().info(coreProtectManager.getReason());
		}

	}

	@Override
	public void onDisable() {
		manager.unregisterCommands();
	}

	private void setupCompletions() {
		manager.getCommandCompletions().registerCompletion("playernames", context -> ImmutableList.copyOf(Bukkit.getOnlinePlayers().stream().map(Player::getName).toList()));
	}
}
