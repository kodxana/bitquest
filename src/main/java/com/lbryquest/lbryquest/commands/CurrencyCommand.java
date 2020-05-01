package com.lbryquest.lbryquest.commands;

import com.lbryquest.lbryquest.LBRYQuest;
import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class CurrencyCommand extends CommandAction {
  private LBRYQuest lbryQuest;

  public CurrencyCommand(LBRYQuest plugin) {
    lbryQuest = plugin;
  }

  public boolean run(
      CommandSender sender, Command cmd, String label, String[] args, final Player player) {
    // CHANGE CURRENCY BY BITCOINJAKE09
    if (cmd.getName().equalsIgnoreCase("currency")) {

      if ((args[0].equalsIgnoreCase("ems"))
          || (args[0].equalsIgnoreCase("emerald"))
          || (args[0].equalsIgnoreCase("emeralds"))) {
        LBRYQuest.REDIS.set("currency" + player.getUniqueId().toString(), "emerald");
        player.sendMessage(
            ChatColor.GREEN
                + "Currency changed to "
                + LBRYQuest.REDIS.get("currency" + player.getUniqueId().toString()));

      } else if (args[0].equalsIgnoreCase(LBRYQuest.DENOMINATION_NAME)) {
        LBRYQuest.REDIS.set(
            "currency" + player.getUniqueId().toString(), LBRYQuest.DENOMINATION_NAME);
        player.sendMessage(
            ChatColor.GREEN
                + "Currency changed to "
                + LBRYQuest.REDIS.get("currency" + player.getUniqueId().toString()));

      } else {
        player.sendMessage(ChatColor.RED + "There was a problem changing your currency.");
      }
      try {
        lbryQuest.updateScoreboard(player);
      } catch (Exception e) {
        e.printStackTrace();
      }
      return true;
    }
    return false;
  }
}
