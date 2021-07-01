package de.doppelbemme.authenticator.main;

import org.bukkit.entity.Player;
import org.bukkit.map.MapCanvas;
import org.bukkit.map.MapRenderer;
import org.bukkit.map.MapView;

import de.doppelbemme.authenticator.util.Auth_Util;

public class CodeRenderer extends MapRenderer{

	public static boolean done = false;
	
	@Override
	public void render(MapView map, MapCanvas canvas, Player player) {
		if(done){
			return;
		}
		
		try {
			String secret = Authenticator.main.secretKey;
			canvas.drawImage(0, 0, Auth_Util.generateQRCodeImage(Auth_Util.getAuthenticatorBarCode(secret, player.getName().toString(), "Hyperknox.world")));
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		done = true;
	}
	
}
