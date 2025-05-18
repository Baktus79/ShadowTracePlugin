package no.vestlandetmc.shadowtrace;

import io.papermc.paper.plugin.lifecycle.event.types.LifecycleEvents;
import lombok.Getter;
import no.vestlandetmc.shadowtrace.commands.ShadowTraceCommand;
import no.vestlandetmc.shadowtrace.config.Messages;
import no.vestlandetmc.shadowtrace.handlers.CoreProtectManager;
import no.vestlandetmc.shadowtrace.handlers.PacketHandler;
import no.vestlandetmc.shadowtrace.handlers.Permissions;
import org.bukkit.plugin.java.JavaPlugin;

import java.util.List;

@SuppressWarnings("UnstableApiUsage")
public final class ShadowTrace extends JavaPlugin {

	@Getter
	private static ShadowTrace plugin;
	@Getter
	private static CoreProtectManager coreProtectManager;
	@Getter
	private static PacketHandler packetHandler;

	@Override
	public void onEnable() {
		plugin = this;
		packetHandler = new PacketHandler(this, "shadowtrace:transfer_block_data");
		coreProtectManager = new CoreProtectManager();

		Messages.initialize();
		Permissions.register();

		if (coreProtectManager != null) {
			getLogger().info(coreProtectManager.getReason());
		}

		this.getLifecycleManager().registerEventHandler(LifecycleEvents.COMMANDS, cmd -> {
			cmd.registrar().register(
					"limbo",
					"Place a player in limbo.",
					List.of("strace"),
					new ShadowTraceCommand());
		});

	}
}
