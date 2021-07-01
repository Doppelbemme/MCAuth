package de.doppelbemme.authenticator.mysql;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

import org.bukkit.Bukkit;
import org.bukkit.command.ConsoleCommandSender;

public class MySQL {

	static ConsoleCommandSender console = Bukkit.getServer().getConsoleSender();
	
	public static String host;
	public static String port;
	public static String database;
	public static String username;
	public static String password;
	public static Connection con;
	
	
	public static void connect()
	{
		if(!isConnected()) 
		{
			try
			{
				con = DriverManager.getConnection("jdbc:mysql://" + host + ":" + port + "/" +  database + "?autoReconnect=true", username, password);
				console.sendMessage("§bMySQL Connection §awas established succesfully!");
			} 
			catch (SQLException e) 
			{
				console.sendMessage("§bMySQL Connection §cfailled!");
				e.printStackTrace();
			}
		}
	}
	
	public static void disconnect()
	{
		if(isConnected()) 

			try 
			{
				con.close();
				console.sendMessage("§bMySQL Connections §awas closed gracefully!");
			} 
			catch (SQLException e) {
				console.sendMessage("§bMySQL Connection §ccrashed. Please check your data!");
				e.printStackTrace();
			}
		}
	
	public static boolean isConnected()
	{
		return (con == null ? false : true);
	}
	
	public static Connection getConnection()
	{
		return con;
	}

}