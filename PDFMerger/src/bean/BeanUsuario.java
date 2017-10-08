package bean;

import java.util.List;

import javax.faces.bean.ManagedBean;
import javax.faces.bean.RequestScoped;

import dao.UsuarioDAO;
import model.Usuario;

@ManagedBean
@RequestScoped
public class BeanUsuario {
	
	private int idUsuario;
	private String nome;
	private String email;
	private String login;
	private String senha;
	private String perfil;
	private String trocaSenha;
	private String bloqueado;
	private UsuarioDAO usuarioDAO = new UsuarioDAO();
	
	public int getIdUsuario() {
		return idUsuario;
	}

	public void setIdUsuario(int idUsuario) {
		this.idUsuario = idUsuario;
	}
	
	public String getNome() {
		return nome;
	}
	
	public void setNome(String nome) {
		this.nome = nome;
	}
	
	public String getEmail() {
		return email;
	}
	
	public void setEmail(String email) {
		this.email = email;
	}
	
	public String getLogin() {
		return login;
	}
	
	public void setLogin(String login) {
		this.login = login;
	}
	
	public String getSenha() {
		return senha;
	}
	
	public void setSenha(String senha) {
		this.senha = senha;
	}
	
	public String getPerfil() {
		return perfil;
	}
	
	public void setPerfil(String perfil) {
		this.perfil = perfil;
	}
	
	public String getTrocaSenha() {
		return trocaSenha;
	}
	
	public void setTrocaSenha(String trocaSenha) {
		this.trocaSenha = trocaSenha;
	}
	
	public String getBloqueado() {
		return bloqueado;
	}
	
	public void setBloqueado(String bloqueado) {
		this.bloqueado = bloqueado;
	}
	
	public List<Usuario> getUsuarios() {
		return usuarioDAO.listar();
	}
	
	public void cadastrar() {
		usuarioDAO.cadastrar(nome, email, login, senha, perfil, trocaSenha, bloqueado);
	}

}
