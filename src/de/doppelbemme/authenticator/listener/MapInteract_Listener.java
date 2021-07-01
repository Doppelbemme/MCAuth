package de.doppelbemme.authenticator.listener;

import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.BlockBreakEvent;
import org.bukkit.event.block.BlockPlaceEvent;

import de.doppelbemme.authenticator.main.Authenticator;

public class MapInteract_Listener implements Listener{

	@EventHandler
	public void onBreak(BlockBreakEvent event) {
		
		Player player = event.getPlayer();
		
		if(Authenticator.main.blocked.contains(player)) {
			event.setCancelled(true);
			player.sendMessage(Authenticator.main.messages.AuthNeeded);
		}
	}
	
	
	@EventHandler
	public void onBreak(BlockPlaceEvent event) {
		
		Player player = event.getPlayer();
		
		if(Authenticator.main.blocked.contains(player)) {
			event.setCancelled(true);
			player.sendMessage(Authenticator.main.messages.AuthNeeded);
		}
	}
	
}
