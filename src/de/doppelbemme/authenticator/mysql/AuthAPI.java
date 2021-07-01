package de.doppelbemme.authenticator.mysql;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.UUID;

public class AuthAPI {

	public boolean isUserExisting(UUID uuid){
		
		try {
			PreparedStatement ps = (PreparedStatement) MySQL.getConnection().prepareStatement("SELECT UUID FROM user WHERE UUID = ?");
			ps.setString(1, uuid.toString());
			ResultSet rs = ps.executeQuery();
			return rs.next();
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return false;
	
	}
	
	public boolean getVerifyState(UUID uuid){
		
		try {
			PreparedStatement ps = (PreparedStatement) MySQL.getConnection().prepareStatement("SELECT Verified FROM user WHERE UUID = ?");
			ps.setString(1, uuid.toString());
				ResultSet rs = ps.executeQuery();
				while(rs.next()) {
					return rs.getBoolean("Verified");
				}
			} catch (SQLException e) {
				e.printStackTrace();
			}
			return false;
	}
	
	public String getSecret(UUID uuid) {
		
		try {
			PreparedStatement ps = (PreparedStatement) MySQL.getConnection().prepareStatement("SELECT Secret FROM user WHERE UUID = ?");
			ps.setString(1, uuid.toString());
				ResultSet rs = ps.executeQuery();
				while(rs.next()) {
					return rs.getString("Secret");
				}
			} catch (SQLException e) {
				e.printStackTrace();
			}
			return null;
	}
	
	public void registerUser(UUID uuid, String secret){
		
		try {
			PreparedStatement ps = (PreparedStatement) MySQL.getConnection().prepareStatement("INSERT INTO user (UUID,Secret,Verified) VALUES(?,?,?)"); 
			ps.setString(1, uuid.toString());
			ps.setString(2, secret);
			ps.setBoolean(3, false);
			ps.executeUpdate();
		} catch (SQLException e) {
			e.printStackTrace();
		}
		
	}
	
	public void verifyUser(UUID uuid) {

		try {
			PreparedStatement ps = (PreparedStatement) MySQL.getConnection().prepareStatement("UPDATE user SET Verified = ? WHERE UUID = ?");
			ps.setBoolean(1, true);
			ps.setString(2, uuid.toString());
			ps.executeUpdate();
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}
	
}
