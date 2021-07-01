package de.doppelbemme.authenticator.main;

import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.ArrayList;

import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.plugin.PluginManager;
import org.bukkit.plugin.java.JavaPlugin;

import de.doppelbemme.authenticator.command.Auth_Command;
import de.doppelbemme.authenticator.listener.Chat_Listener;
import de.doppelbemme.authenticator.listener.Join_Listener;
import de.doppelbemme.authenticator.listener.MapInteract_Listener;
import de.doppelbemme.authenticator.listener.Movement_Listener;
import de.doppelbemme.authenticator.mysql.AuthAPI;
import de.doppelbemme.authenticator.mysql.MySQL;
import de.doppelbemme.authenticator.mysql.MySQLFile;
import de.doppelbemme.authenticator.util.Auth_Util;


public class Authenticator extends JavaPlugin{

	public static Authenticator main;
	public Auth_Util auth_util;
	public Messages messages;
	public AuthAPI authapi;
	
	public String secretKey;
	public ArrayList<String> verified = new ArrayList<String>();
	public ArrayList<Player> blocked = new ArrayList<Player>();
	
	@Override
	public void onEnable() {
		
		main = this;
		auth_util = new Auth_Util();
		messages = new Messages();
		authapi = new AuthAPI();
		
		MySQLFile MySQLfile = new MySQLFile();
		MySQLfile.setStandard();
		MySQLfile.readData();
		MySQL.connect();
		
		if(MySQL.isConnected()){
			PreparedStatement ps;
			try {
				ps = (PreparedStatement) MySQL.getConnection().prepareStatement("CREATE TABLE IF NOT EXISTS user (UUID VARCHAR(100),Secret VARCHAR(100),Verified BOOLEAN)");
				ps.executeUpdate();
			} catch (SQLException e) {
				e.printStackTrace();
			}
		}
		
		getCommand("auth").setExecutor(new Auth_Command());
		
		PluginManager pluginManager = Bukkit.getServer().getPluginManager();
		
		pluginManager.registerEvents(new Join_Listener(), this);
		pluginManager.registerEvents(new Movement_Listener(), this);
		pluginManager.registerEvents(new Chat_Listener(), this);
		pluginManager.registerEvents(new MapInteract_Listener(), this);
		
	}
	
	@Override
	public void onDisable() {
		MySQL.disconnect();
	}
	
}
