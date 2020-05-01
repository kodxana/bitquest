package com.lbryquest.lbryquest.commands;

import com.lbryquest.lbryquest.LBRYQuest;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class KillAllVillagersCommand extends CommandAction {
  private LBRYQuest lbryQuest;

  public KillAllVillagersCommand(LBRYQuest plugin) {
    this.lbryQuest = plugin;
  }

  public boolean run(
      CommandSender sender, Command cmd, String label, String[] args, Player player) {
    lbryQuest.killAllVillagers();
    return true;
  }
}
