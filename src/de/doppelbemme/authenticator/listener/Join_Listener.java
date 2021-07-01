package de.doppelbemme.authenticator.listener;

import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerLoginEvent;

import de.doppelbemme.authenticator.main.Authenticator;

public class Join_Listener implements Listener{

	
	@EventHandler
	public void onJoin(PlayerJoinEvent event) {
		Player player = event.getPlayer();
		
		Bukkit.getScheduler().runTaskLater(Authenticator.main, new Runnable() {
			
			@Override
			public void run() {
				
				if(Authenticator.main.authapi.isUserExisting(player.getUniqueId()) && Authenticator.main.authapi.getVerifyState(player.getUniqueId())) {
					
					String currentIP = player.getAddress().getHostString();
					Bukkit.broadcastMessage(currentIP);
					
					if(Authenticator.main.verified.contains(currentIP)) {
						return;
					}
					
					Authenticator.main.blocked.add(player);
						
				}
				
				if(Authenticator.main.blocked.contains(player)) {
					player.sendMessage(Authenticator.main.messages.prefix + "§7Bitte authentifiziere dich, bevor du spielen kannst. §7/§cauth §7<§cCode§7>");
				}
				
			}
		}, 2);
	}
	
}
