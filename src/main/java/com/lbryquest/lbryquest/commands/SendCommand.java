package com.lbryquest.lbryquest.commands;

import com.lbryquest.lbryquest.User;
import com.lbryquest.lbryquest.LBRYQuest;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class SendCommand extends CommandAction {
  private LBRYQuest lbryQuest;

  public SendCommand(LBRYQuest plugin) {
    lbryQuest = plugin;
  }

  public boolean run(
      CommandSender sender, Command cmd, String label, String[] args, final Player player) {
    int MAX_SEND = 10000; // to be multiplied by DENOMINATION_FACTOR
    if (args.length == 2) {
      for (char c : args[0].toCharArray()) {
        if (!Character.isDigit(c)) return false;
      }
      if (args[0].length() > 8) {
        // maximum send is 8 digits
        return false;
      }
      final Long amount = Long.parseLong(args[0]);
      final Long sat = amount * LBRYQuest.DENOMINATION_FACTOR;

      if (amount != 0 && amount <= MAX_SEND) {

        for (final Player onlinePlayer : Bukkit.getOnlinePlayers()) {
          if (onlinePlayer.getName().equalsIgnoreCase(args[1])) {
            if (!args[1].equalsIgnoreCase(player.getDisplayName())) {
              try {
                final User user = new User(lbryQuest.db_con, player.getUniqueId());

                Long balance = user.wallet.getBalance(0);

                if (balance >= sat) {
                  User user_tip = new User(lbryQuest.db_con, onlinePlayer.getUniqueId());
                  // TODO: Pay to user address
                  if (user.wallet.payment(user_tip.wallet.address, sat)) {
                    lbryQuest.updateScoreboard(onlinePlayer);
                    lbryQuest.updateScoreboard(player);
                    player.sendMessage(
                        ChatColor.GREEN
                            + "You sent "
                            + ChatColor.LIGHT_PURPLE
                            + amount
                            + " "
                            + LBRYQuest.DENOMINATION_NAME
                            + ChatColor.GREEN
                            + " to user "
                            + ChatColor.BLUE
                            + onlinePlayer.getName());
                    onlinePlayer.sendMessage(
                        ChatColor.GREEN
                            + "You got "
                            + ChatColor.LIGHT_PURPLE
                            + amount
                            + " "
                            + LBRYQuest.DENOMINATION_NAME
                            + ChatColor.GREEN
                            + " from user "
                            + ChatColor.BLUE
                            + player.getName());
                  } else {
                    player.sendMessage(ChatColor.RED + "Tip failed.");
                  }
                } else {
                  player.sendMessage(ChatColor.DARK_RED + "Not enough balance");
                }
              } catch (Exception e) {
                player.sendMessage(ChatColor.DARK_RED + "Error. Please try again later.");
                System.out.println(e);
              }
            }
          }
        }
      } else {
        player.sendMessage(
            "Minimum tip is 1 " + LBRYQuest.DENOMINATION_NAME + ". Maximum is " + MAX_SEND);
      }
    } else {
      return false;
    }
    return true;
  }
}
