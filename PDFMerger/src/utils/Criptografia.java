package utils;

import java.math.BigInteger;
import java.security.MessageDigest;

public class Criptografia {
	
public static String criptografa(String string) {
		
		String retorno = "";
		MessageDigest md;
		try {
			md = MessageDigest.getInstance("SHA-1");
			BigInteger hash = new BigInteger(1,md.digest(string.getBytes()));
			retorno = hash.toString(16);
		} catch(Exception e) {
			System.out.println("Erro ao criptografar senha");
		}
		return retorno;
	}

}
