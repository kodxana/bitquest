package com.lbryquest.lbryquest.events;

import com.lbryquest.lbryquest.LBRYQuest;

import java.io.IOException;
import java.text.ParseException;

import org.bukkit.ChatColor;
import org.bukkit.Chunk;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.SignChangeEvent;

public class SignEvents implements Listener {
  LBRYQuest lbryQuest;

  public SignEvents(LBRYQuest plugin) {
    lbryQuest = plugin;
  }

  @EventHandler
  public void onSignChange(SignChangeEvent event)
      throws ParseException, org.json.simple.parser.ParseException, IOException {

    final Player player = event.getPlayer();
    // Check that the world is overworld
    if (!event.getBlock().getWorld().getName().endsWith("_end")) {
      final String specialCharacter = "^";
      final String[] lines = event.getLines();
      final String signText = lines[0] + lines[1] + lines[2] + lines[3];
      Chunk chunk = event.getBlock().getWorld().getChunkAt(event.getBlock().getLocation());

      if (signText.length() > 0
          && signText.substring(0, 1).equals(specialCharacter)
          && signText.substring(signText.length() - 1).equals(specialCharacter)) {

        final String name = signText.substring(1, signText.length() - 1);
        lbryQuest.claimLand(name, chunk, player);
      }

    } else if (event.getBlock().getWorld().getName().endsWith("_end")) {
      player.sendMessage(ChatColor.DARK_RED + "No claiming in the end!");
    }
  }
}
