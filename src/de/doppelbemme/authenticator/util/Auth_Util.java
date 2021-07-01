package de.doppelbemme.authenticator.util;

import java.awt.image.BufferedImage;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.security.SecureRandom;

import org.apache.commons.codec.binary.Base32;
import org.apache.commons.codec.binary.Hex;
import org.bukkit.Bukkit;
import org.bukkit.World;
import org.bukkit.event.server.MapInitializeEvent;
import org.bukkit.map.MapView;

import com.google.zxing.BarcodeFormat;
import com.google.zxing.MultiFormatWriter;
import com.google.zxing.WriterException;
import com.google.zxing.client.j2se.MatrixToImageWriter;
import com.google.zxing.common.BitMatrix;
import com.google.zxing.qrcode.QRCodeWriter;

import de.taimos.totp.TOTP;

public class Auth_Util {

	public static String generateSecretKey() {
	    SecureRandom random = new SecureRandom();
	    byte[] bytes = new byte[20];
	    random.nextBytes(bytes);
	    Base32 base32 = new Base32();
	    return base32.encodeToString(bytes);
	}
	
	public String getTOTPCode(String secretKey) {
	    Base32 base32 = new Base32();
	    byte[] bytes = base32.decode(secretKey);
	    String hexKey = Hex.encodeHexString(bytes);
	    return TOTP.getOTP(hexKey);
	}
	
	public static String getAuthenticatorBarCode(String secretKey, String user, String issuer) {
	    try {
	        return "otpauth://totp/"
	                + URLEncoder.encode(issuer + ":" + user, "UTF-8").replace("+", "%20")
	                + "?secret=" + URLEncoder.encode(secretKey, "UTF-8").replace("+", "%20")
	                + "&issuer=" + URLEncoder.encode(issuer, "UTF-8").replace("+", "%20");
	    } catch (UnsupportedEncodingException e) {
	        throw new IllegalStateException(e);
	    }
	}
	
	public static void createQRCode(String BarCode, String filePath, int height, int width) throws WriterException, IOException {
	    BitMatrix matrix = new MultiFormatWriter().encode(BarCode, BarcodeFormat.QR_CODE, width, height);
	    try (FileOutputStream out = new FileOutputStream(filePath)) {
	        MatrixToImageWriter.writeToStream(matrix, "png", out);
	    }
	}

	public static BufferedImage generateQRCodeImage(String barcodeText) throws Exception {
	    QRCodeWriter barcodeWriter = new QRCodeWriter();
	    BitMatrix bitMatrix = 
	      barcodeWriter.encode(barcodeText, BarcodeFormat.QR_CODE, 128, 128);

	    return MatrixToImageWriter.toBufferedImage(bitMatrix);
	}
	
}
