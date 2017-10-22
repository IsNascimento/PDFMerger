package bean;

import java.util.List;

import javax.faces.application.FacesMessage;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.RequestScoped;
import javax.faces.context.FacesContext;

import dao.UsuarioDAO;
import model.Usuario;
import utils.Mensagem;

@ManagedBean
@RequestScoped
public class BeanUsuario {
	
	private int idUsuario;
	private String nome;
	private String email;
	private String login;
	private String senha;
	private String confirmaSenha;
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
	
	public String getConfirmaSenha() {
		return confirmaSenha;
	}
	
	public void setConfirmaSenha(String senha) {
		this.confirmaSenha = senha;
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
		if(trocaSenha.equals("true")) {
			this.trocaSenha = "S";
		} else {
			this.trocaSenha = "N";
		}
	}
	
	public String getBloqueado() {
		return bloqueado;
	}
	
	public void setBloqueado(String bloqueado) {
		if(bloqueado.equals("true")) {
			this.bloqueado = "S";
		} else {
			this.bloqueado = "N";
		}
	}
	
	public List<Usuario> getUsuarios() {
		return usuarioDAO.listar();
	}
	
	public void cadastrar() {
		FacesContext contexto = FacesContext.getCurrentInstance();
		if(senha.equals(confirmaSenha)) {
			try {
				usuarioDAO.cadastrar(nome, email, login, senha, perfil, trocaSenha, bloqueado);
				contexto.addMessage(null, new FacesMessage(FacesMessage.SEVERITY_INFO, Mensagem.SUCESSO, ""));
			} catch(Exception e) {
				contexto.addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, Mensagem.ERRO, Mensagem.ERRO_NO_SISTEMA));
			}
		} else {
			contexto.addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, Mensagem.ERRO, Mensagem.SENHA_NAO_CONFERE));
		}
	}

}
