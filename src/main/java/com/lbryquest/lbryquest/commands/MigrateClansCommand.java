package com.lbryquest.lbryquest.commands;

import com.lbryquest.lbryquest.LBRYQuest;
import java.util.*;
import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import redis.clients.jedis.ScanParams;
import redis.clients.jedis.ScanResult;

public class MigrateClansCommand extends CommandAction {
  public boolean run(
      CommandSender sender, Command cmd, String label, String[] args, Player player) {
    List<String> clans = new ArrayList<String>(LBRYQuest.REDIS.smembers("clans"));
    Map<String, List<String>> clansMembers = new HashMap<String, List<String>>();

    ScanParams scanParams = new ScanParams();
    scanParams.match("clan:*[^:members]");
    String cursor = ScanParams.SCAN_POINTER_START;

    do {
      ScanResult<String> scanResult = LBRYQuest.REDIS.scan(cursor, scanParams);
      List<String> result = scanResult.getResult();

      for (String key : result) {
        String clan = LBRYQuest.REDIS.get(key);
        if (!clansMembers.containsKey(clan)) {
          clansMembers.put(clan, new ArrayList<String>());
        }

        String uuid = key.split(":")[1];
        clansMembers.get(clan).add(uuid);
      }

      cursor = scanResult.getStringCursor();
    } while (!cursor.equals(ScanParams.SCAN_POINTER_START));

    for (String clan : clans) {
      if (!clansMembers.containsKey(clan)) {
        LBRYQuest.REDIS.srem("clans", clan);
        LBRYQuest.REDIS.del("invitations:" + clan);
        player.sendMessage(
            ChatColor.GRAY
                + "Clan "
                + ChatColor.DARK_GRAY
                + clan
                + ChatColor.GRAY
                + " is empty. Deleted");
      }
    }

    for (Map.Entry<String, List<String>> entry : clansMembers.entrySet()) {
      String clan = entry.getKey();
      for (String member : entry.getValue()) {
        LBRYQuest.REDIS.sadd("clan:" + clan + ":members", member);
        player.sendMessage(
            ChatColor.GRAY
                + "Player "
                + ChatColor.BLUE
                + member
                + ChatColor.GREEN
                + " added to clan "
                + ChatColor.DARK_GRAY
                + clan);
      }
    }

    player.sendMessage(ChatColor.GREEN + "Clans migrated.");
    return true;
  }
}
