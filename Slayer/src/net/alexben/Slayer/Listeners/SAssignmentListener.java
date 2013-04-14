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

package net.alexben.Slayer.Listeners;

import net.alexben.Slayer.Events.AssignmentCompleteEvent;
import net.alexben.Slayer.Events.AssignmentExpireEvent;
import net.alexben.Slayer.Events.AssignmentForfeitEvent;
import net.alexben.Slayer.Events.TaskAssignEvent;
import net.alexben.Slayer.Libraries.Objects.Assignment;
import net.alexben.Slayer.Libraries.Objects.SerialItemStack;
import net.alexben.Slayer.Utilities.SConfigUtil;
import net.alexben.Slayer.Utilities.SMiscUtil;
import net.alexben.Slayer.Utilities.SPlayerUtil;

import org.bukkit.ChatColor;
import org.bukkit.Color;
import org.bukkit.FireworkEffect;
import org.bukkit.OfflinePlayer;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.Firework;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.FireworkMeta;

public class SAssignmentListener implements Listener
{
	@EventHandler(priority = EventPriority.MONITOR)
	private void onTaskAssign(TaskAssignEvent event)
	{
		Player player = event.getPlayer();

		if(player.isOnline())
		{
			SMiscUtil.sendMsg(player.getPlayer(), SMiscUtil.getString("new_assignment"));
			SMiscUtil.sendMsg(player.getPlayer(), "For details, please type " + ChatColor.GOLD + "/sl my tasks" + ChatColor.RESET + ".");
		}

		// Update the scoreboard
		SPlayerUtil.updateScoreboard(player);
	}

	@EventHandler(priority = EventPriority.MONITOR)
	private void onAssignmentComplete(AssignmentCompleteEvent event)
	{
		// Define variables
		final Player player = event.getPlayer();
		final Assignment assignment = event.getAssignment();

		// Add the rewards to their reward queue
		for(ItemStack item : assignment.getTask().getReward())
		{
			SPlayerUtil.addReward(player, new SerialItemStack(item));
		}

		// From this point on, return if the player is offline
		if(!player.isOnline()) return;

		SMiscUtil.sendMsg(player, SMiscUtil.getString("assignment_complete").replace("{task}", assignment.getTask().getName()));
		SMiscUtil.sendMsg(player, SMiscUtil.getString("rewards_awaiting").replace("{rewards}", "" + SPlayerUtil.getRewardAmount(player)));

		// Shoot a firework, woohoo!
		if(SConfigUtil.getSettingBoolean("expiration.completion_firework"))
		{
			Firework firework = (Firework) player.getPlayer().getLocation().getWorld().spawnEntity(player.getPlayer().getLocation(), EntityType.FIREWORK);
			FireworkMeta fireworkmeta = firework.getFireworkMeta();
			FireworkEffect.Type type = FireworkEffect.Type.BALL_LARGE;
			FireworkEffect effect = FireworkEffect.builder().flicker(false).withColor(Color.AQUA).withFade(Color.FUCHSIA).with(type).trail(true).build();
			fireworkmeta.addEffect(effect);
			fireworkmeta.setPower(1);
			firework.setFireworkMeta(fireworkmeta);
		}

		// Tracking
		SPlayerUtil.addCompletion(player);

		// Handle points and leveling
		SPlayerUtil.addPoints(player, assignment.getTask().getValue());

		if(SPlayerUtil.getPoints(player) >= SPlayerUtil.getPointsGoal(player))
		{
			// Loop and make sure they didn't get a crap-ton of points and if so update accordingly
			while(SPlayerUtil.getPoints(player) >= SPlayerUtil.getPointsGoal(player))
			{
				// Get rid of the points
				SPlayerUtil.subtractPoints(player, SPlayerUtil.getPointsGoal(player));

				// Add the level
				SPlayerUtil.addLevel(player);
			}

			// Message the player
			SMiscUtil.sendMsg(player, SMiscUtil.getString("level_up_msg1").replace("{level}", "" + SPlayerUtil.getLevel(player)));
			SMiscUtil.sendMsg(player, SMiscUtil.getString("level_up_msg2").replace("{points}", "" + ((int) SPlayerUtil.getPointsGoal(player) - SPlayerUtil.getPoints(player))));

			// Shoot a random firework!
			SMiscUtil.shootRandomFirework(player.getLocation());
		}

		// Update the scoreboard
		SPlayerUtil.updateScoreboard(player);
	}

	@EventHandler(priority = EventPriority.MONITOR)
	private void onAssignmentExpire(AssignmentExpireEvent event)
	{
		// Do nothing if they're offline
		if(!event.getOfflinePlayer().isOnline()) return;

		Player player = event.getOfflinePlayer().getPlayer();
		Assignment assignment = event.getAssignment();

		SMiscUtil.sendMsg(player, SMiscUtil.getString("assignment_expired").replace("{task}", assignment.getTask().getName()));

		// Handle expiration punishment if enabled
		if(SConfigUtil.getSettingBoolean("expiration.punish"))
		{
			// TODO: Update punishments.
		}

		// Tracking
		SPlayerUtil.addExpiration(player);

		// Update the scoreboard
		SPlayerUtil.updateScoreboard(player);
	}

	@EventHandler(priority = EventPriority.MONITOR)
	private void onAssignmentForfeit(AssignmentForfeitEvent event)
	{
		// Define the variables
		OfflinePlayer player = event.getOfflinePlayer();
		Assignment assignment = event.getAssignment();

		SMiscUtil.sendMsg(player, SMiscUtil.getString("assignment_forfeit").replace("{task}", assignment.getTask().getName()));

		// Handle punishments if enabled
		if(SConfigUtil.getSettingBoolean("forfeit.punish"))
		{
			// TODO: Update punishments.
		}

		// Tracking
		SPlayerUtil.addForfeit(player);
	}
}
