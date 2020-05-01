package com.lbryquest.lbryquest.commands;

import com.lbryquest.lbryquest.LBRYQuest;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class ReportCommand extends CommandAction {
  private LBRYQuest lbryQuest;

  public ReportCommand(LBRYQuest plugin) {
    lbryQuest = plugin;
  }

  public boolean run(
      CommandSender sender, Command cmd, String label, String[] args, Player player) {
    // TODO: Rewrite for Discord
    //    if (lbryQuest.slackBotSession != null && lbryQuest.slackBotSession.isConnected()) {
    //      if (args.length >= 2) {
    //        String badPlayer = args[0];
    //        String message = args[1];
    //        for (int i = 2; i < args.length; i++) {
    //          message += " ";
    //          message += args[i];
    //        }
    //
    //        if (LBRYQuest.REDIS.exists("uuid:" + badPlayer)) {
    //          String uuid = LBRYQuest.REDIS.get("uuid:" + badPlayer);
    //          String slackMessage =
    //              "Player "
    //                  + player.getName()
    //                  + " reports "
    //                  + badPlayer
    //                  + " ("
    //                  + uuid
    //                  + ") because: "
    //                  + message;
    //          SlackChannel channel =
    //              lbryQuest.slackBotSession.findChannelByName(LBRYQuest.SLACK_BOT_REPORTS_CHANNEL);
    //          if (channel != null) {
    //            lbryQuest.slackBotSession.sendMessage(channel, slackMessage);
    //            player.sendMessage(
    //                ChatColor.GREEN
    //                    + "The report has been send to a moderator. Thanks for making "
    //                    + ChatColor.GOLD
    //                    + ChatColor.BOLD
    //                    + "LBC"
    //                    + ChatColor.GRAY
    //                    + ChatColor.BOLD
    //                    + "Quest"
    //                    + ChatColor.RESET
    //                    + ChatColor.GREEN
    //                    + " a better place.");
    //            return true;
    //          } else {
    //            player.sendMessage(
    //                ChatColor.RED + "There was a problem sending the report. Please try again
    // later.");
    //            return true;
    //          }
    //        } else {
    //          player.sendMessage(
    //              ChatColor.DARK_RED
    //                  + "Player "
    //                  + ChatColor.BLUE
    //                  + badPlayer
    //                  + ChatColor.DARK_RED
    //                  + " does not play on this server.");
    //          return true;
    //        }
    //      } else {
    //        player.sendMessage(ChatColor.DARK_RED + "Usage: /report <player> <reason>");
    //        return true;
    //      }
    //    } else {
    //      player.sendMessage(ChatColor.RED + "The /report command is not active.");
    //      return true;
    //    }
    return false;
  }
}
