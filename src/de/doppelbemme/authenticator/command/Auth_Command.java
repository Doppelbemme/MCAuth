package de.doppelbemme.authenticator.command;

import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.Sound;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.MapMeta;
import org.bukkit.map.MapView;

import de.doppelbemme.authenticator.main.Authenticator;
import de.doppelbemme.authenticator.main.CodeRenderer;
import de.doppelbemme.authenticator.util.Auth_Util;
import net.md_5.bungee.api.chat.ClickEvent;
import net.md_5.bungee.api.chat.ComponentBuilder;
import net.md_5.bungee.api.chat.HoverEvent;
import net.md_5.bungee.api.chat.TextComponent;
import net.md_5.bungee.api.chat.ClickEvent.Action;

public class Auth_Command implements CommandExecutor{

	@Override
	public boolean onCommand(CommandSender sender, Command cmd, String arg, String[] args) {
		
		if(!(sender instanceof Player)) {
			sender.sendMessage(Authenticator.main.messages.NoConsole);
			return false;
		}
		
		Player player = (Player) sender;
		
		if(!player.hasPermission("auth.use")) {
			player.sendMessage(Authenticator.main.messages.NoPerm);
			return false;
		}
		
		if(args.length == 1) {
			//Available Subcommands: setup
			String subCommand = args[0];
			
			if(subCommand.equalsIgnoreCase("setup")) {
				if(Authenticator.main.authapi.isUserExisting(player.getUniqueId())) {
					UsageMessage(player);
					return false;
				}
				
				setup2FA(player);
			}else{
				if(subCommand.equalsIgnoreCase("finish")) {
					UsageMessage(player);
					return false;
				}
				
				if(!Authenticator.main.authapi.isUserExisting(player.getUniqueId())) {
					UsageMessage(player);
					return false;
				}
				
				if(!Authenticator.main.authapi.getVerifyState(player.getUniqueId())) {
					player.sendMessage(Authenticator.main.messages.prefix + "ßcDeine ßeAuthentifizierung ßcist nocht nicht abgeschlossen. ßcNutze: ß7/ßcauth finish ß7<ßcCodeß7>");
					return false;
				}
				
				checkTOTP(player, args[0]);
				
			}
		}else if(args.length == 2) {
			
			String subCommand = args[0];
			
			if(!subCommand.equalsIgnoreCase("finish")) {
				UsageMessage(player);
				return false;
			}
			
			if(!Authenticator.main.authapi.isUserExisting(player.getUniqueId())) {
				UsageMessage(player);
				return false;
			}
			
			if(Authenticator.main.authapi.getVerifyState(player.getUniqueId())) {
				UsageMessage(player);
				return false;
			}
			
			String TOTPCode = "";
			String userInput = args[1];
			
			try {
				TOTPCode = Authenticator.main.auth_util.getTOTPCode(Authenticator.main.authapi.getSecret(player.getUniqueId()));
			} catch (Exception e) {
				e.printStackTrace();
			}
				
				if(userInput.equals(TOTPCode)) {
					player.sendMessage(Authenticator.main.messages.prefix + "ßaDer Authenticator wurde erfolgreich aktiviert.");
					player.playSound(player.getLocation(), Sound.ENTITY_EXPERIENCE_ORB_PICKUP, 1.0F, 1.0F);
					
					Authenticator.main.authapi.verifyUser(player.getUniqueId());
					player.kickPlayer(Authenticator.main.messages.prefix + "ßaDer Authenticator wurde erfolgreich aktiviert.");
					
				}else {
					
					player.sendMessage(Authenticator.main.messages.prefix + "ßcDer angegebene Code ist nicht korrekt! ß7(ße" + userInput + "ß7)");
					player.playSound(player.getLocation(), Sound.ENTITY_ITEM_BREAK, 1.0f, 1.0f);
				}
			
		}else{
			UsageMessage(player);
		}
		
		return false;
	}
	
	
	@SuppressWarnings("deprecation")
	public void setup2FA(Player player){
		String secretKey = Auth_Util.generateSecretKey();
		Authenticator.main.secretKey = secretKey;

		CodeRenderer renderer = new CodeRenderer();

		TextComponent mainKeyComponent = new TextComponent();
		mainKeyComponent.setText(Authenticator.main.messages.prefix + "ß7Dein ßeSecretKey ß7wurde generiert. ");

		TextComponent keyComponent = new TextComponent();
		keyComponent.setText("ßbßnKlicke hier");
		keyComponent.setClickEvent(new ClickEvent(Action.COPY_TO_CLIPBOARD, secretKey));
		keyComponent.setClickEvent(new ClickEvent(Action.SUGGEST_COMMAND, secretKey));
		keyComponent.setHoverEvent(new HoverEvent(HoverEvent.Action.SHOW_TEXT, new ComponentBuilder("ß7Klicke um deinen SecretKey zu kopieren...").create()));
		
		TextComponent afterKeyComponent = new TextComponent();
		afterKeyComponent.setText(" ß7um den Key zu kopieren. ßcBewahre diesen sicher auf und gib ihn nicht weiter!");

		mainKeyComponent.addExtra(keyComponent);
		mainKeyComponent.addExtra(afterKeyComponent);
		player.spigot().sendMessage(mainKeyComponent);

		try {
			Authenticator.main.authapi.registerUser(player.getUniqueId(), secretKey);
		} catch (Exception e) {
			player.sendMessage("ßcFEHLER: Key konnte nicht sicher gespeichert werden...");
		}
		
		MapView view = Bukkit.createMap(player.getWorld());
		view.getRenderers().clear();
		view.addRenderer(renderer);

		ItemStack map = new ItemStack(Material.FILLED_MAP);
		MapMeta mapmeta = (MapMeta) map.getItemMeta();
		mapmeta.setMapView(view);
		map.setItemMeta(mapmeta);

		player.getInventory().setItem(4, map);
		player.getInventory().setHeldItemSlot(4);
		player.sendMessage("ß7[ßbAuthß7] ßeScanne den QR Code ß7mit deinem ßeAuthenticatorß7. ß7Gib anschlieﬂend mit /ßcauth finish ß7<ßcCodeß7> den ßeaktuellen Code ß7aus dem ßeAuthenticator ß7ein um den Vorgang abzuschlieﬂen.");
		player.playSound(player.getLocation(), Sound.ENTITY_EXPERIENCE_ORB_PICKUP, 1.0f, 1.0f);
		
		CodeRenderer.done = false;
		
	}
	
	
	
	public void checkTOTP(Player player, String userInput) {
		String TOTPCode = "";
		try {
			TOTPCode = Authenticator.main.auth_util.getTOTPCode(Authenticator.main.authapi.getSecret(player.getUniqueId()));
		} catch (Exception e) {
			e.printStackTrace();
		}
			
			if(userInput.equals(TOTPCode)) {
				player.sendMessage(Authenticator.main.messages.prefix + "ßaAuthentifizierung erfolgreich.");
				player.playSound(player.getLocation(), Sound.ENTITY_EXPERIENCE_ORB_PICKUP, 1.0F, 1.0F);
				
				String currentIP = player.getAddress().getHostString();
				if(!Authenticator.main.verified.contains(currentIP)) {
					Authenticator.main.verified.add(currentIP);
				}
				
				Authenticator.main.blocked.remove(player);
			}else {
				player.sendMessage(Authenticator.main.messages.prefix + "ßcDer angegebene Code ist nicht korrekt! ß7(ße" + userInput + "ß7)");
				player.playSound(player.getLocation(), Sound.ENTITY_ITEM_BREAK, 1.0f, 1.0f);
			}
	}
	
	
	
	
	public void UsageMessage(Player player) {
		
		if(!Authenticator.main.authapi.isUserExisting(player.getUniqueId())) {
			player.sendMessage(Authenticator.main.messages.prefix + "ßcNutze: ß7/ßcauth setup");
		}else {
			player.sendMessage(Authenticator.main.messages.prefix + "ßcNutze: ß7/ßcauth ß7<ßcCodeß7>");
		}
		
	}
}
