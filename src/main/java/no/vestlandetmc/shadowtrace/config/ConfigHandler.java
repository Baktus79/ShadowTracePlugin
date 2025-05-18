package no.vestlandetmc.shadowtrace.config;

import lombok.Setter;
import no.vestlandetmc.shadowtrace.ShadowTrace;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.plugin.java.JavaPlugin;
import org.jetbrains.annotations.NotNull;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.StandardCopyOption;
import java.util.HashSet;
import java.util.Set;

public class ConfigHandler extends YamlConfiguration {

	private final File file;
	private final YamlConfiguration defaults;

	@Setter
	private String pathPrefix;

	public ConfigHandler(String fileName) {
		this(fileName, true);
	}

	public ConfigHandler(String fileName, boolean useDefaults) {
		final JavaPlugin plugin = ShadowTrace.getPlugin();
		this.file = new File(plugin.getDataFolder(), fileName);

		if (useDefaults) {
			try (InputStream defaultStream = plugin.getResource(fileName)) {
				if (defaultStream != null) {
					this.defaults = YamlConfiguration.loadConfiguration(new InputStreamReader(defaultStream, StandardCharsets.UTF_8));
				} else {
					this.defaults = null;
				}
			} catch (IOException e) {
				throw new RuntimeException("Failed to load default config for " + fileName, e);
			}
		} else {
			this.defaults = null;
		}

		extractDefaults(fileName);
		loadConfig();
	}

	public void reloadConfig() {
		saveConfig();
		loadConfig();
	}

	public void write(String path, Object value) {
		set(path, value);
		saveConfig();
	}

	public void saveConfig() {
		try {
			super.save(file);
		} catch (IOException e) {
			ShadowTrace.getPlugin().getLogger().severe("Failed to save config: " + e.getMessage());
		}
	}

	private void loadConfig() {
		try {
			super.load(file);
		} catch (Exception e) {
			ShadowTrace.getPlugin().getLogger().severe("Failed to load config: " + e.getMessage());
		}
	}

	@Override
	public Object get(@NotNull String path, Object def) {
		if (defaults != null && def != null && !PrimitiveWrapper.isWrapperType(def.getClass())) {
			throw new IllegalArgumentException("Defaults must be null. Using defaults from file. Path: " + path);
		}

		if (defaults != null && super.get(path, null) == null) {
			Object defaultValue = defaults.get(path);
			if (defaultValue != null) {
				write(path, defaultValue);
			}
		}

		if (defaults == null && pathPrefix != null && !calledFrom("getConfigurationSection") && !calledFrom("get")) {
			path = pathPrefix + "." + path;
		}

		return super.get(path, null);
	}

	@Override
	public void set(@NotNull String path, Object value) {
		if (defaults == null && pathPrefix != null && !calledFrom("getConfigurationSection") && !calledFrom("get")) {
			path = pathPrefix + "." + path;
		}
		super.set(path, value);
	}

	private void extractDefaults(String fileName) {
		if (!file.exists()) {
			try {
				Files.createDirectories(file.getParentFile().toPath());
				Files.copy(ShadowTrace.getPlugin().getResource(fileName), file.toPath(), StandardCopyOption.REPLACE_EXISTING);
			} catch (IOException e) {
				ShadowTrace.getPlugin().getLogger().severe("Failed to extract default config: " + e.getMessage());
			}
		}
	}

	private boolean calledFrom(String methodName) {
		return new Throwable().getStackTrace()[1].getMethodName().equals(methodName);
	}

	private static final class PrimitiveWrapper {
		private static final Set<Class<?>> WRAPPER_TYPES = getWrapperTypes();

		private static boolean isWrapperType(Class<?> clazz) {
			return WRAPPER_TYPES.contains(clazz);
		}

		private static Set<Class<?>> getWrapperTypes() {
			Set<Class<?>> ret = new HashSet<>();
			ret.add(Boolean.class);
			ret.add(Character.class);
			ret.add(Byte.class);
			ret.add(Short.class);
			ret.add(Integer.class);
			ret.add(Long.class);
			ret.add(Float.class);
			ret.add(Double.class);
			ret.add(Void.class);
			return ret;
		}
	}
}
