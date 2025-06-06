package no.vestlandetmc.shadowtrace.handlers;

import org.bukkit.Bukkit;
import org.bukkit.permissions.Permission;
import org.bukkit.permissions.PermissionDefault;
import org.bukkit.plugin.PluginManager;

public class Permissions {

	public static final Permission SHT_COMMAND = new Permission("shadowtrace.command.shadowtrace", "Allows command usage.", PermissionDefault.OP);

	public static void register() {
		final PluginManager pm = Bukkit.getPluginManager();
		pm.addPermission(SHT_COMMAND);
	}
}
