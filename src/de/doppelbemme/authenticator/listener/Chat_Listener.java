package de.doppelbemme.authenticator.listener;

import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.AsyncPlayerChatEvent;
import org.bukkit.event.player.PlayerCommandPreprocessEvent;

import de.doppelbemme.authenticator.main.Authenticator;

public class Chat_Listener implements Listener{

	@EventHandler
	public void onChat(AsyncPlayerChatEvent event) {
		
		Player player = event.getPlayer();
		
		if(Authenticator.main.blocked.contains(player)) {
			event.setCancelled(true);
			player.sendMessage(Authenticator.main.messages.AuthNeeded);
		}
		
	}
	
	@EventHandler
	public void onCommand(PlayerCommandPreprocessEvent event) {
		
		Player player = event.getPlayer();
		String command = event.getMessage();
		
		if(!command.contains("auth")) {
			event.setCancelled(true);
			player.sendMessage(Authenticator.main.messages.AuthNeeded);
		}
	}
	
}
