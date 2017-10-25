package model;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;

@Entity
@Table
public class Usuario {
	
	@Id
	@GeneratedValue(strategy=GenerationType.IDENTITY)
	private int idUsuario;
	private String nome;
	private String email;
	private String login;
	private String senha;
	private String perfil;
	private String trocaSenha;
	private String bloqueado;
	
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
		if(trocaSenha.equals("true") || trocaSenha.equals("S")) {
			this.trocaSenha = "S";
		} else {
			this.trocaSenha = "N";
		}
	}
	
	public String getBloqueado() {
		return bloqueado;
	}
	
	public void setBloqueado(String bloqueado) {
		if(bloqueado.equals("true") || bloqueado.equals("S")) {
			this.bloqueado = "S";
		} else {
			this.bloqueado = "N";
		}
	}
}