package utils;

import dao.UsuarioDAO;
import model.Usuario;

public class Teste {

	public static void main(String[] args) {
		// TODO Auto-generated method stub
		Usuario u = new Usuario();
		
		u.setNome("Usuario Teste 1");
		u.setEmail("pdfmerger.usuteste1@gmail.com");
		u.setLogin("usuario1");
		u.setSenha(Criptografia.criptografa("Teste123"));
		u.setPerfil("Administrador");
		u.setTrocaSenha("N");
		u.setBloqueado("N");
		
		UsuarioDAO bd = new UsuarioDAO();
		
		bd.cadastrar(u);
		
	}

}
