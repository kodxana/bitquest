package com.lbryquest.lbryquest.commands;

import com.lbryquest.lbryquest.LBRYQuest;
import java.io.IOException;
import java.text.ParseException;
import java.util.HashMap;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Location;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class LandCommand extends CommandAction {
  private LBRYQuest lbryQuest;

  public LandCommand(LBRYQuest plugin) {
    this.lbryQuest = plugin;
  }

  public boolean run(
      CommandSender sender, Command cmd, String label, String[] args, Player player) {
    String tempchunk = "";
    if (player.getLocation().getWorld().getName().equals("world")) {
      tempchunk = "chunk";
    } // end world lmao @bitcoinjake09
    else if (player.getLocation().getWorld().getName().equals("world_nether")) {
      tempchunk = "netherchunk";
    } // end nether @bitcoinjake09
    if (args.length == 0) {
      return false;
    } else {
      Location location = player.getLocation();
      if (args[0].equalsIgnoreCase("price")) {
        player.sendMessage("Land price is: "+Math.round((lbryQuest.LAND_PRICE+lbryQuest.MINER_FEE)/lbryQuest.DENOMINATION_FACTOR)+" "+lbryQuest.DENOMINATION_NAME+" (incl. miner fees)");
      } else if (args[0].equalsIgnoreCase("rename")) {
        if(args.length==2) {
          if(lbryQuest.validName(args[1])==false) {
            player.sendMessage(ChatColor.DARK_RED+"Invalid name.");
            return false;
          } else if(lbryQuest.isOwner(location, player)) {
            LBRYQuest.REDIS.set("chunk"+location.getChunk().getX()+","+location.getChunk().getZ()+"name",args[1]);
            player.sendMessage(ChatColor.GREEN+"Land renamed to "+args[1]);
          } else {
            player.sendMessage(ChatColor.DARK_RED+"Only the owner of this land can rename it.");
          }
          return true;
        } else {
          return false;
        }
      } else if (args[0].equalsIgnoreCase("transfer")) {
        if (args.length == 2) {
          if(lbryQuest.isOwner(location,player)) {
            for (final Player onlinePlayer : Bukkit.getOnlinePlayers()) {
              if (onlinePlayer.getName().equalsIgnoreCase(args[1])) {
                if (!args[1].equalsIgnoreCase(player.getDisplayName())) {
                  player.sendMessage("Changing the land ownership to "+onlinePlayer.getDisplayName()+"...");
                  LBRYQuest.REDIS.set("chunk"+location.getChunk().getX()+","+location.getChunk().getZ()+"owner",player.getUniqueId().toString());
                }
              }
            }
            player.sendMessage(ChatColor.DARK_RED+"Cannot find player "+args[1]);
          } else {
            player.sendMessage(ChatColor.DARK_RED+"Only the owner of this land can transfer.");
          }
          return true;
        } else {
          return false;
        }
      } else if (args[0].equalsIgnoreCase("info")) {
        int x = location.getChunk().getX();
        int z = location.getChunk().getZ();
        if(lbryQuest.landIsClaimed(location)) {
          String landname = LBRYQuest.REDIS.get(tempchunk + "" + x + "," + z + "name");
          player.sendMessage(landname);
          String permission_key= "chunk"
                  + location.getChunk().getX()
                  + ","
                  + location.getChunk().getZ()
                  + "permissions";
          if(lbryQuest.REDIS.exists(permission_key)) {
            String permission_code = LBRYQuest.REDIS.get(permission_key);
            if(permission_code.equals("p")) player.sendMessage("Permission: public");
          } else {
            player.sendMessage("Permission: private");
          }


        } else {
          player.sendMessage("Land is not claimed");
        }

        return true;
      } else if (args[0].equalsIgnoreCase("claim")) {
        if (args.length > 1) {
          StringBuilder sb = new StringBuilder(args[1]);
          //            for (int i = 3; i < args.length; i++){
          //                sb.append(" " + args[i]);
          //            }
          String claimName = sb.toString().trim();

          if (!location.getWorld().getName().equals("world")) {
            player.sendMessage(ChatColor.DARK_RED + "You cannot claim land here.");
            return true;
          }

          try {
            lbryQuest.claimLand(claimName, location.getChunk(), player);
          } catch (ParseException e) {
            e.printStackTrace();
            player.sendMessage(ChatColor.RED + "Land claim failed. Please try again later.");
            return true;
          } catch (org.json.simple.parser.ParseException e) {
            e.printStackTrace();
            player.sendMessage(ChatColor.RED + "Land claim failed. Please try again later.");
            return true;
          } catch (IOException e) {
            e.printStackTrace();
            player.sendMessage(ChatColor.RED + "Land claim failed. Please try again later.");
            return true;
          }

        } else {
          player.sendMessage(ChatColor.RED + "You must to specify a name for your land");
          return true;
        }

      } else if (args[0].equalsIgnoreCase("permission")) {
        lbryQuest.land_permission_cache = new HashMap();

        int x = location.getChunk().getX();
        int z = location.getChunk().getZ();

        if (lbryQuest.landIsClaimed(location) && (lbryQuest.isOwner(location, player)||lbryQuest.isModerator(player))) {
          String landname = LBRYQuest.REDIS.get(tempchunk + "" + x + "," + z + "name");

          if (args[1].equalsIgnoreCase("public")) {
            LBRYQuest.REDIS.set(
                tempchunk
                    + ""
                    + location.getChunk().getX()
                    + ","
                    + location.getChunk().getZ()
                    + "permissions",
                "p");
            player.sendMessage(
                ChatColor.GREEN
                    + "The land "
                    + ChatColor.DARK_GREEN
                    + landname
                    + ChatColor.GREEN
                    + " is now public");
            return true;
          } else if (args[1].equalsIgnoreCase("clan")) {
            LBRYQuest.REDIS.set(
                tempchunk
                    + ""
                    + location.getChunk().getX()
                    + ","
                    + location.getChunk().getZ()
                    + "permissions",
                "c");
            player.sendMessage(
                ChatColor.GREEN
                    + "The land "
                    + ChatColor.DARK_GREEN
                    + landname
                    + ChatColor.GREEN
                    + " is now clan-owned");
            return true;
          } else if (args[1].equalsIgnoreCase("private")) {
            LBRYQuest.REDIS.del(
                tempchunk
                    + ""
                    + location.getChunk().getX()
                    + ","
                    + location.getChunk().getZ()
                    + "permissions");
            player.sendMessage(
                ChatColor.GREEN
                    + "The land "
                    + ChatColor.DARK_GREEN
                    + landname
                    + ChatColor.GREEN
                    + " is now private");
            return true;
          } else if ((args[1].equalsIgnoreCase("pvp")) && (args.length == 2)) {
            LBRYQuest.REDIS.set(
                tempchunk
                    + ""
                    + location.getChunk().getX()
                    + ","
                    + location.getChunk().getZ()
                    + "permissions",
                "v");
            player.sendMessage(
                ChatColor.GREEN
                    + "The land "
                    + ChatColor.DARK_GREEN
                    + landname
                    + ChatColor.GREEN
                    + " is now private PvP");
            return true;
          } else if ((args[1].equalsIgnoreCase("pvp")) && (args[2].equalsIgnoreCase("public"))) {
            LBRYQuest.REDIS.set(
                tempchunk
                    + ""
                    + location.getChunk().getX()
                    + ","
                    + location.getChunk().getZ()
                    + "permissions",
                "pv");
            player.sendMessage(
                ChatColor.GREEN
                    + "The land "
                    + ChatColor.DARK_GREEN
                    + landname
                    + ChatColor.GREEN
                    + " is now public PvP");
            return true;
          } else {
            player.sendMessage(
                ChatColor.DARK_RED + "Only the owner of this location can change its permissions.");
            return true;
          }

        } else {
          return false;
        }
      } else {
        player.sendMessage(
            ChatColor.RED
                + "If you want buy claim a land, use /land claim landname. For permissions, use /land permission [public,private,clan]");
      }
    }
    return false;
  }
}
