package com.lbryquest.lbryquest.commands;

import com.lbryquest.lbryquest.LBRYQuest;
import java.util.Set;
import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

// this is an old command proposed by AltQuest, I re add it here.

// created by @BitcoinJake09 5/14/18 THIS IS TESTED!!!! and seems to be correct!! i added an extra
// feature also, you can now just run /fixabandonland to see list of land with owners and
// permissions in redis and what would be removed. then can also run as /fixabandonland <true|yes>
public class FixAbandonLand extends CommandAction {
  public boolean run(
      CommandSender sender, Command cmd, String label, String[] args, Player player) {
    int XYsSize = 0;
    Set<String> ownerList = LBRYQuest.REDIS.keys("chunk*owner");
    Set<String> permissionsList = LBRYQuest.REDIS.keys("*permissions");
    String[] XYs = new String[ownerList.size()];
    String[] subPerms = new String[permissionsList.size()];
    for (String tempOwnerList : ownerList) {
      XYs[XYsSize] = tempOwnerList.substring(0, tempOwnerList.length() - 5);
      sender.sendMessage(
          ChatColor.DARK_RED
              + LBRYQuest.REDIS.get(XYs[XYsSize] + "name")
              + " is owned by: "
              + (LBRYQuest.REDIS.get(tempOwnerList)));
      XYsSize++;
    }

    XYsSize = 0;

    for (String tempPermissionsList : permissionsList) {
      subPerms[XYsSize] = tempPermissionsList.substring(0, tempPermissionsList.length() - 11);
      sender.sendMessage(
          ChatColor.YELLOW
              + tempPermissionsList
              + " is set to: "
              + (LBRYQuest.REDIS.get(subPerms[XYsSize] + "permissions")));
      XYsSize++;
    }

    for (int i = 0; i <= permissionsList.size() - 1; i++) {
      if ((LBRYQuest.REDIS.get(subPerms[i] + "owner")) == null) {
        sender.sendMessage(
            ChatColor.GREEN
                + "To Be Removed: "
                + subPerms[i]
                + "permissions is set to: "
                + LBRYQuest.REDIS.get(subPerms[i] + "permissions"));
      }
    }
    if ((args[0].equalsIgnoreCase("true")) || (args[0].equalsIgnoreCase("yes"))) {
      for (int i = 0; i <= permissionsList.size() - 1; i++) {
        if ((LBRYQuest.REDIS.get(subPerms[i] + "owner")) == null) {
          LBRYQuest.REDIS.del(subPerms[i] + "permissions");
          sender.sendMessage(ChatColor.GREEN + "Removed: " + subPerms[i] + "permissions");
        }
      }
    }

    sender.sendMessage(ChatColor.GREEN + "finished");
    return true;
  }
}
