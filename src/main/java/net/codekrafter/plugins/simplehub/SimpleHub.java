package net.codekrafter.plugins.simplehub;

import java.util.ArrayList;
import java.util.List;

import net.codekrafter.plugins.simplehub.inventory.HubListener;
import net.codekrafter.plugins.simplehub.tasks.SaveTask;
import net.codekrafter.plugins.utils.ColorParser;

import org.bukkit.Bukkit;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.entity.Player;
import org.bukkit.plugin.java.JavaPlugin;

public class SimpleHub extends JavaPlugin {

	public FileConfiguration config = getConfig();
	public static String prefix = ColorParser.parse("&6[&3Simple&b&lHub&6]");
	public static List<String> inLobby = new ArrayList<String>();
	public SaveTask saveTask = new SaveTask(this);

	@Override
	public void onEnable() {
		manageConfig();
		getServer().getPluginManager().registerEvents(new HubListener(), this);
		SaveTask st = new SaveTask(this);
		st.runTaskTimer(this, 0, 60 * 20 * 5);
		/*TickTask tt = new TickTask();
		tt.runTaskTimer(this, 0, 0);*/
	}

	@Override
	public void onDisable() {
		saveConfigItems();
		saveConfig();
	}

	public void saveTheConfig() {
		saveConfigItems();
		saveConfig();
	}

	private void saveConfigItems() {
		config.set("inHub", inLobby);

	}

	private void manageConfig() {
		addDefaults();
		config.options().copyDefaults(true);
		saveConfig();
		getValues();
	}

	@SuppressWarnings("unchecked")
	private void getValues() {
		prefix = ColorParser.parse(config.getString("prefix"));
		inLobby = (List<String>) config.getList("inHub");

	}

	private void addDefaults() {
		config.addDefault("prefix", "&6[&3Simple&b&lHub&6]");
		config.addDefault("inHub", new ArrayList<String>());
	}

	@SuppressWarnings("null")
	@Override
	public boolean onCommand(CommandSender sender, Command cmd, String label,
			String[] args) {
		boolean output = true;
		if (cmd.getName().equalsIgnoreCase("testcolorcmd")) {
			if (args[0] != null) {
				sender.sendMessage(prefix + " " + ColorParser.parse(args[0]));
				output = true;
			} else {
				output = false;
			}
		} else if (cmd.getName().equalsIgnoreCase("setinhub")) {
			Player targetP = Bukkit.getPlayer(args[0]);
			boolean targetBoolean;
			switch (args[1]) {
			case "true":
				targetBoolean = true;
				break;
			case "false":
				targetBoolean = false;
				break;
			default:
				targetBoolean = (Boolean) null;
			}
			if (targetBoolean) {
				output = true;
				if (inLobby.contains(targetP.getUniqueId().toString())) {
					sender.sendMessage(prefix + " "
							+ ColorParser.parse("&cPlayer Already In Hub"));
					output = true;
				} else {
					inLobby.add(targetP.getUniqueId().toString());
					sender.sendMessage(prefix
							+ " "
							+ ColorParser
									.parse("&aPlayer Sucessfully Added To The Hub"));
					output = true;
				}
			} else if (!targetBoolean) {
				output = true;
				if (inLobby.contains(targetP.getUniqueId().toString())) {
					inLobby.remove(targetP.getUniqueId().toString());
					sender.sendMessage(prefix
							+ " "
							+ ColorParser
									.parse("&aPlayer Sucessfully Removed From Hub"));
				} else {
					sender.sendMessage(prefix + " "
							+ ColorParser.parse("&cPlayer Not In Hub"));
					output = true;
				}
			} else {
				output = false;
			}
		}
		return output;
	}
};
