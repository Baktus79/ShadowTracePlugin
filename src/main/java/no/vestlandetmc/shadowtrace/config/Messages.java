package no.vestlandetmc.shadowtrace.config;

public class Messages extends ConfigHandler {

	public static String
			SHTC_ONLY_PLAYER,
			SHTC_INVALID_ARG,
			SHTC_NO_PERM,
			SHTC_INVALID_PLAYER,
			SHTC_NO_ORES,
			SRC_ACTION_SEARCH,
			SRC_NO_DATA_FOUND,
			SRC_ACTION_NO_DATA_FOUND,
			SRC_DATA_FOUND,
			SRC_ACTION_DATA_FOUND;


	private Messages(String fileName) {
		super(fileName);
	}

	public static void initialize() {
		new Messages("messages.yml").onLoad();
	}

	public static String placeholders(String message, String player, String bywhom, String time, String reason) {
		String converted = message;

		if (player != null) {
			converted = converted.replace("%player%", player);
		}
		if (bywhom != null) {
			converted = converted.replace("%bywhom%", bywhom);
		}
		if (reason != null) {
			converted = converted.replace("%reason%", reason);
		}
		if (time != null) {
			converted = converted.replace("%time%", time);
		}

		return converted;
	}

	private void onLoad() {

		SHTC_ONLY_PLAYER = getString("shadowtrace-command.only-player-usage");
		SHTC_INVALID_ARG = getString("shadowtrace-command.invalid-argument");
		SHTC_NO_PERM = getString("shadowtrace-command.no-permission");
		SHTC_INVALID_PLAYER = getString("shadowtrace-command.invalid-player");
		SHTC_NO_ORES = getString("shadowtrace-command.no-ores-world");
		SRC_ACTION_SEARCH = getString("search.actionbar-search");
		SRC_NO_DATA_FOUND = getString("search.no-data-found");
		SRC_ACTION_NO_DATA_FOUND = getString("search.actionbar-no-data-found");
		SRC_DATA_FOUND = getString("search.data-found");
		SRC_ACTION_DATA_FOUND = getString("search.actionbar-data-found");

	}

}
