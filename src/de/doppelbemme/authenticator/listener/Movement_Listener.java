package de.doppelbemme.authenticator.listener;

import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerMoveEvent;

import de.doppelbemme.authenticator.main.Authenticator;

public class Movement_Listener implements Listener{

	@EventHandler
	public void onMove(PlayerMoveEvent event) {
		
		Player player = event.getPlayer();
		
		if(Authenticator.main.blocked.contains(player)) {
			event.setTo(event.getFrom());
			player.sendMessage(Authenticator.main.messages.AuthNeeded);
		}
		
	}
	
}
