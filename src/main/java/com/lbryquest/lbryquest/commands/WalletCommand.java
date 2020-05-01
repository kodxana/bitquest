package com.lbryquest.lbryquest.commands;

import com.lbryquest.lbryquest.User;
import com.lbryquest.lbryquest.LBRYQuest;
import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class WalletCommand extends CommandAction {
  private LBRYQuest lbryQuest;

  public WalletCommand(LBRYQuest plugin) {
    lbryQuest = plugin;
  }

  public boolean run(
      CommandSender sender, Command cmd, String label, String[] args, Player player) {

    try {
      User user = new User(lbryQuest.db_con, player.getUniqueId());
      lbryQuest.sendWalletInfo(player, user);
      lbryQuest.updateScoreboard(player);
    } catch (Exception e) {
      e.printStackTrace();
      player.sendMessage(ChatColor.RED + "There was a problem reading your wallet.");
    }

    return true;
  }
}
