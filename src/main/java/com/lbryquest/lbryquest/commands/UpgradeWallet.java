package com.lbryquest.lbryquest.commands;

import com.lbryquest.lbryquest.LegacyWallet;
import com.lbryquest.lbryquest.User;
import com.lbryquest.lbryquest.LBRYQuest;
import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class UpgradeWallet extends CommandAction {
  private LBRYQuest lbryQuest;

  public UpgradeWallet(LBRYQuest plugin) {
    lbryQuest = plugin;
  }

  public boolean run(
      CommandSender sender, Command cmd, String label, String[] args, Player player) {
    LegacyWallet legacyWallet = new LegacyWallet(player.getUniqueId().toString());
    try {
      User user = new User(lbryQuest.db_con, player.getUniqueId());
      LegacyWallet legacy_wallet = new LegacyWallet(player.getUniqueId().toString());

      Long balance = legacy_wallet.getBalance(2);
      if (balance > 0) {
        legacy_wallet.sendFrom(user.wallet.address, balance);
      } else {
        player.sendMessage(
            ChatColor.RED + "You don't have balance in your old account or its already migrated.");
      }
    } catch (Exception e) {
      e.printStackTrace();
      player.sendMessage(
          ChatColor.RED + "Command failed. This incident was logged. Please try again later.");
    }

    return true;
  }
}
