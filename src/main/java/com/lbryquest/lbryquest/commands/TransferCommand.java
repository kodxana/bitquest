package com.lbryquest.lbryquest.commands;

import com.lbryquest.lbryquest.User;
import com.lbryquest.lbryquest.LBRYQuest;
import com.lbryquest.lbryquest.Wallet;
import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class TransferCommand extends CommandAction {
  private LBRYQuest lbryQuest;

  public TransferCommand(LBRYQuest plugin) {
    lbryQuest = plugin;
  }

  public boolean run(
      CommandSender sender, Command cmd, String label, final String[] args, final Player player) {
    if (args.length == 2) {
      if (args[0].length() > 8) {
        // maximum transfer is 8 digits
        return false;
      }
      for (char c : args[0].toCharArray()) {
        if (!Character.isDigit(c)) return false;
      }
      final Long sendAmount = Long.parseLong(args[0]) * LBRYQuest.DENOMINATION_FACTOR;

      System.out.println(sendAmount);
      Wallet fromWallet = null;
      try {
        fromWallet = new User(lbryQuest.db_con, player.getUniqueId()).wallet;

        if (sendAmount < (LBRYQuest.MINIMUM_TRANSACTION * LBRYQuest.DENOMINATION_FACTOR)) {
          player.sendMessage(
              ChatColor.DARK_RED
                  + "Minimum transaction is "
                  + ChatColor.LIGHT_PURPLE
                  + LBRYQuest.MINIMUM_TRANSACTION
                  + " "
                  + LBRYQuest.DENOMINATION_NAME
                  + ChatColor.GREEN
                  + ".");
          return true;
        } else {
          if (fromWallet != null) {

            player.sendMessage(
                ChatColor.YELLOW
                    + "Sending "
                    + ChatColor.LIGHT_PURPLE
                    + args[0]
                    + " "
                    + LBRYQuest.DENOMINATION_NAME
                    + ChatColor.YELLOW
                    + " to "
                    + ChatColor.BLUE
                    + args[1]
                    + ChatColor.YELLOW
                    + "...");

            if (fromWallet.payment(args[1], sendAmount) == true) {
              player.sendMessage(
                  ChatColor.GREEN
                      + "Succesfully sent "
                      + ChatColor.LIGHT_PURPLE
                      + args[0]
                      + " "
                      + LBRYQuest.DENOMINATION_NAME
                      + ChatColor.GREEN
                      + " to external address.");
            } else {
              player.sendMessage(
                  ChatColor.DARK_RED + "Transaction failed. Please try again later.");
            }

            lbryQuest.updateScoreboard(player);
          }
        }

      } catch (Exception e) {
        e.printStackTrace();
        player.sendMessage(ChatColor.RED + "Transaction failed. Please try again later.");
      }
      return true;

    } else {
      return false;
    }
  }
}
