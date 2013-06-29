package Slayer.Utilities;

import java.util.ArrayList;

import org.bukkit.configuration.Configuration;

/**
 * Utility for all data-related methods.
 */
public class ConfigUtil
{
	public static void initialize()
	{
		Configuration mainConfig = Slayer.Core.Slayer.plugin.getConfig().getRoot();
		mainConfig.options().copyDefaults(true);
		Slayer.Core.Slayer.plugin.saveConfig();
	}

	public static int getSettingInt(String id)
	{
		if(Slayer.Core.Slayer.plugin.getConfig().isInt(id))
		{
			return Slayer.Core.Slayer.plugin.getConfig().getInt(id);
		}
		else return -1;
	}

	public static String getSettingString(String id)
	{
		if(Slayer.Core.Slayer.plugin.getConfig().isString(id))
		{
			return Slayer.Core.Slayer.plugin.getConfig().getString(id);
		}
		else return null;
	}

	public static boolean getSettingBoolean(String id)
	{
		return !Slayer.Core.Slayer.plugin.getConfig().isBoolean(id) || Slayer.Core.Slayer.plugin.getConfig().getBoolean(id);
	}

	public static double getSettingDouble(String id)
	{
		if(Slayer.Core.Slayer.plugin.getConfig().isDouble(id))
		{
			return Slayer.Core.Slayer.plugin.getConfig().getDouble(id);
		}
		else return -1;
	}

	public static ArrayList<String> getSettingArrayListString(String id)
	{
		ArrayList<String> strings = new ArrayList<String>();
		if(Slayer.Core.Slayer.plugin.getConfig().isList(id))
		{
			for(String s : Slayer.Core.Slayer.plugin.getConfig().getStringList(id))
				strings.add(s);
			return strings;
		}
		else return null;
	}
}