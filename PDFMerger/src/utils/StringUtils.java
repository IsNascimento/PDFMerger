package utils;

public class StringUtils {
	
	public static String trocaEspaco(String string) {
		return string.replace(" ", "_");
	}
	
	public static String addPdf(String string) {
		if(string.contains(".pdf")) {
			return string;
		} else {
			return string + ".pdf";
		}
	}

}
