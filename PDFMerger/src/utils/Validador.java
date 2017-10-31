package utils;

public class Validador {
	
	public static boolean nome(String nome) {
		if (nome != null && nome.replaceAll("[A-Z]|[a-z]|ç|ã|õ|é|ê| ", "").isEmpty()) {
            return true;
        } else {
            return false;
        }
	}
	
	public static boolean email(String email) {
		if (email != null && email.replaceAll("[A-Z]|[a-z]|[0-9]|_|\\.", "").matches("@")) {
            return true;
        } else {
            return false;
        }
	}
	
	public static boolean login(String login) {
		if (login != null && !(login.contains(" "))) {
            return true;
        } else {
            return false;
        }
	}
	
	public static boolean senha(String senha) {
		if (senha != null && senha.length() >= 6) {
			return true;
		} else {
			return false;
		}
	}

}
