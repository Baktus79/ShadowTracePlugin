package no.vestlandetmc.shadowtrace.config;

public class Messages extends ConfigHandler {

	public static String
			SHTC_ONLY_PLAYER,
			SHTC_INVALID_ARG_GETORES,
			SHTC_INVALID_ARG_BLOCKS,
			SHTC_NO_PERM,
			SHTC_INVALID_PLAYER,
			SHTC_NO_ORES,
			SHTC_NO_VALID_BLOCKS,
			SHTC_NO_BLOCKS_FOUND,
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
		SHTC_INVALID_ARG_GETORES = getString("shadowtrace-command.invalid-argument-getores");
		SHTC_INVALID_ARG_BLOCKS = getString("shadowtrace-command.invalid-argument-blocks");
		SHTC_NO_PERM = getString("shadowtrace-command.no-permission");
		SHTC_INVALID_PLAYER = getString("shadowtrace-command.invalid-player");
		SHTC_NO_ORES = getString("shadowtrace-command.no-ores-world");
		SHTC_NO_VALID_BLOCKS = getString("shadowtrace-command.invalid-blocks");
		SHTC_NO_BLOCKS_FOUND = getString("shadowtrace-command.no-blocks-found");
		SRC_ACTION_SEARCH = getString("search.actionbar-search");
		SRC_NO_DATA_FOUND = getString("search.no-data-found");
		SRC_ACTION_NO_DATA_FOUND = getString("search.actionbar-no-data-found");
		SRC_DATA_FOUND = getString("search.data-found");
		SRC_ACTION_DATA_FOUND = getString("search.actionbar-data-found");

	}

}
