/*
 * Copyright (c) 2013 Alex Bennett
 *
 * Permission is hereby granted, free of charge, to any person obtaining a copy of this software
 * and associated documentation files (the "Software"), to deal in the Software without restriction,
 * including without limitation the rights to use, copy, modify, merge, publish, distribute, sublicense,
 * and/or sell copies of the Software, and to permit persons to whom the Software is furnished to do
 * so, subject to the following conditions:
 *
 * The above copyright notice and this permission notice shall be included in all copies or substantial
 * portions of the Software.
 *
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR IMPLIED, INCLUDING BUT
 * NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY, FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT.
 * IN NO EVENT SHALL THE AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER LIABILITY,
 * WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM, OUT OF OR IN CONNECTION WITH THE
 * SOFTWARE OR THE USE OR OTHER DEALINGS IN THE SOFTWARE.
 */

package net.alexben.Slayer.Utilities;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import net.alexben.Slayer.Handlers.SFlatFile;
import net.alexben.Slayer.Libraries.Objects.Death;
import net.alexben.Slayer.Libraries.Objects.Kill;
import net.alexben.Slayer.Libraries.Objects.SerialItemStack;

import org.bukkit.Bukkit;
import org.bukkit.OfflinePlayer;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

/**
 * Utility for all player-related methods.
 */
public class SPlayerUtil
{
	/**
	 * Creates a new Slayer save for the <code>player</code>.
	 * 
	 * @param player the player for whom to create the save.
	 */
	public static void createSave(OfflinePlayer player)
	{
		// Return if the player already exists
		if(SDataUtil.playerExists(player)) return;

		// This is really simple for the time being and almost unnecessary, but I'm adding it for future expansion.
		SDataUtil.saveData(player, "points", 0);
		SDataUtil.saveData(player, "task_assignments_total", 0);
		SDataUtil.saveData(player, "task_completions", 0);
		SDataUtil.saveData(player, "task_forfeits", 0);
		SDataUtil.saveData(player, "task_expirations", 0);
		SDataUtil.saveData(player, "kills", new ArrayList<Kill>());
		SDataUtil.saveData(player, "deaths", new ArrayList<Death>());
		SDataUtil.saveData(player, "reward_queue", new ArrayList<SerialItemStack>());

		// Save the data
		SFlatFile.savePlayer(player);
	}

	/**
	 * Returns an ArrayList of all players loaded into memory.
	 * 
	 * @return ArrayList
	 */
	public static ArrayList<OfflinePlayer> getPlayers()
	{
		ArrayList<OfflinePlayer> players = new ArrayList<OfflinePlayer>();

		for(Map.Entry<String, HashMap<String, Object>> player : SDataUtil.getAllData().entrySet())
		{
			players.add(Bukkit.getOfflinePlayer(player.getKey()));
		}

		return players;
	}

	/**
	 * Returns the number of tasks assigned all time for <code>player</code>.
	 * 
	 * @return int
	 */
	public static int getTotalAssignments(OfflinePlayer player)
	{
		if(SDataUtil.getData(player, "task_assignments_total") == null) SDataUtil.saveData(player, "task_assignments_total", 0);
		return SObjUtil.toInteger(SDataUtil.getData(player, "task_assignments_total"));
	}

	/**
	 * Returns the number of task completions for <code>player</code>.
	 * 
	 * @return int
	 */
	public static int getCompletions(OfflinePlayer player)
	{
		if(SDataUtil.getData(player, "task_completions") == null) SDataUtil.saveData(player, "task_completions", 0);
		return SObjUtil.toInteger(SDataUtil.getData(player, "task_completions"));
	}

	/**
	 * Gives the <code>player</code> a task completion.
	 * 
	 * @param player the player to give the completion to.
	 */
	public static void addCompletion(OfflinePlayer player)
	{
		if(SDataUtil.hasData(player, "task_completions"))
		{
			SDataUtil.saveData(player, "task_completions", getCompletions(player) + 1);
		}
	}

	/**
	 * Returns the number of task expirations for <code>player</code>.
	 * 
	 * @return int
	 */
	public static int getExpirations(OfflinePlayer player)
	{
		if(SDataUtil.getData(player, "task_expirations") == null) SDataUtil.saveData(player, "task_expirations", 0);
		return SObjUtil.toInteger(SDataUtil.getData(player, "task_expirations"));
	}

	/**
	 * Gives the <code>player</code> a task expiration.
	 * 
	 * @param player the player to give the expiration to.
	 */
	public static void addExpiration(OfflinePlayer player)
	{
		if(SDataUtil.hasData(player, "task_expirations"))
		{
			SDataUtil.saveData(player, "task_expirations", getExpirations(player) + 1);
		}
	}

	/**
	 * Returns the number of task forfeits for <code>player</code>.
	 * 
	 * @return int
	 */
	public static int getForfeits(OfflinePlayer player)
	{
		if(SDataUtil.getData(player, "task_forfeits") == null) SDataUtil.saveData(player, "task_forfeits", 0);
		return SObjUtil.toInteger(SDataUtil.getData(player, "task_forfeits"));
	}

	/**
	 * Gives the <code>player</code> a task forfeit.
	 * 
	 * @param player the player to give the forfeit to.
	 */
	public static void addForfeit(OfflinePlayer player)
	{
		if(SDataUtil.hasData(player, "task_forfeits"))
		{
			SDataUtil.saveData(player, "task_forfeits", getForfeits(player) + 1);
		}
	}

	/**
	 * Adds the <code>item</code> to the <code>player</code>'s reward queue.
	 * 
	 * @param player the player to add the item to.
	 * @param item the item to add.
	 */
	public static void addReward(OfflinePlayer player, SerialItemStack item)
	{
		((ArrayList<SerialItemStack>) SDataUtil.getData(player, "reward_queue")).add(item);
	}

	/**
	 * Removes the reward matching <code>item</code> from the <code>player</code>'s
	 * reward queue.
	 * 
	 * @param player the player to remove the reward from.
	 * @param item the reward to remove.
	 */
	public static void removeReward(OfflinePlayer player, ItemStack item)
	{
		ArrayList<SerialItemStack> rewards = (ArrayList<SerialItemStack>) SDataUtil.getData(player, "reward_queue");

		int i = 0;

		for(SerialItemStack reward : rewards)
		{
			if(reward.toItemStack().equals(item))
			{
				rewards.remove(i);
				return;
			}
			i++;
		}
	}

	/**
	 * Returns an ArrayList of <code>player</code>'s rewards.
	 * 
	 * @param player the player to get rewards for.
	 * @return ArrayList
	 */
	public static ArrayList<ItemStack> getRewards(OfflinePlayer player)
	{
		ArrayList<ItemStack> rewards = new ArrayList<ItemStack>();

		for(SerialItemStack reward : (ArrayList<SerialItemStack>) SDataUtil.getData(player, "reward_queue"))
		{
			rewards.add(reward.toItemStack());
		}

		return rewards;
	}

	/**
	 * Sets the <code>player</code>'s points to <code>points</code>.
	 * 
	 * @param player the player to edit.
	 * @param points the points to set to.
	 */
	public static void setPoints(OfflinePlayer player, int points)
	{
		if(SDataUtil.hasData(player, "points"))
		{
			SDataUtil.saveData(player, "points", points);
		}
	}

	/**
	 * Gives the <code>player</code> the <code>points</code>.
	 * 
	 * @param player the player to give points to.
	 * @param points the number of points to give.
	 */
	public static void addPoints(OfflinePlayer player, int points)
	{
		if(SDataUtil.hasData(player, "points"))
		{
			setPoints(player, getPoints(player) + points);
		}
	}

	/**
	 * Subtracts <code>points</code> from the <code>player</code>'s total points.
	 * 
	 * @param player the player to edit.
	 * @param points the number of points to subtract.
	 */
	public static void subtractPoints(OfflinePlayer player, int points)
	{
		if(points > getPoints(player)) setPoints(player, 0);
		else setPoints(player, getPoints(player) - points);
	}

	/**
	 * Returns the <code>player</code>'s points.
	 * 
	 * @param player the player to check.
	 * @return Integer
	 */
	public static int getPoints(OfflinePlayer player)
	{
		return SObjUtil.toInteger(SDataUtil.getData(player, "points"));
	}

	/**
	 * Adds a kill to the <code>player</code>'s overall kills.
	 * 
	 * @param player the player to give a kill to.
	 */
	public static void addKill(Player player, Entity entity)
	{
		Kill kill = new Kill(player, entity);
		ArrayList<Kill> kills;

		if(SDataUtil.hasData(player, "kills")) kills = (ArrayList<Kill>) SDataUtil.getData(player, "kills");
		else kills = new ArrayList<Kill>();

		kills.add(kill);
	}

	/**
	 * Adds a death to the <code>player</code>'s overall deaths.
	 * 
	 * @param player the player that died.
	 * @param entity the entity that killed the <code>player</code>.
	 */
	public static void addDeath(Player player, Entity entity)
	{
		Death death = new Death(player, entity);
		ArrayList<Death> deaths;

		if(SDataUtil.hasData(player, "deaths")) deaths = (ArrayList<Death>) SDataUtil.getData(player, "deaths");
		else deaths = new ArrayList<Death>();

		deaths.add(death);
	}

	/**
	 * Returns the kill count for <code>player</code>.
	 * 
	 * @param player the player to check.
	 * @return Integer
	 */
	public static int getKillCount(OfflinePlayer player)
	{
		return ((ArrayList<Kill>) SDataUtil.getData(player, "kills")).size();
	}

	/**
	 * Returns the death count for <code>player</code>.
	 * 
	 * @param player the player to check.
	 * @return Integer
	 */
	public static int getDeathCount(OfflinePlayer player)
	{
		return ((ArrayList<Kill>) SDataUtil.getData(player, "deaths")).size();
	}

	/**
	 * Returns an ArrayList of all kills for <code>player</code>.
	 * 
	 * @param player the player to check.
	 * @return ArrayList
	 */
	public static ArrayList<Kill> getKills(OfflinePlayer player)
	{
		return (ArrayList<Kill>) SDataUtil.getData(player, "kills");
	}

	/**
	 * Returns an ArrayList of all deaths for <code>player</code>.
	 * 
	 * @param player the player to check.
	 * @return ArrayList
	 */
	public static ArrayList<Death> getDeaths(OfflinePlayer player)
	{
		return (ArrayList<Death>) SDataUtil.getData(player, "deaths");
	}
}