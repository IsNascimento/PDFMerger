package bean;

import java.io.File;
import java.util.List;

import javax.faces.application.FacesMessage;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.RequestScoped;
import javax.faces.context.FacesContext;

import dao.PerfilDAO;
import dao.UsuarioDAO;
import model.Perfil;
import model.Usuario;
import utils.Criptografia;
import utils.Mensagem;
import utils.Validador;

@ManagedBean
@RequestScoped
public class BeanUsuario {
	
	private int idUsuario;
	private String nome;
	private String email;
	private String login;
	private String senha;
	private String confirmaSenha;
	private String perfil = "Usuário comum";
	private String trocaSenha = "true";
	private String bloqueado;
	private Usuario usuario;
	private UsuarioDAO usuarioDAO = new UsuarioDAO();
	private PerfilDAO perfilDAO = new PerfilDAO();
	
	private String titulo = "Cadastrar usuário";
	private String limpaCancela = "Limpar";
	private String cadastraAtualiza = "Cadastrar";
	
	public Usuario getUsuario() {
		return usuario;
	}

	public void setUsuario(Usuario u) {
		this.usuario = u;
	}

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
	
	public String getTitulo() {
		return titulo;
	}
	public void setTitulo(String titulo) {
		this.titulo = titulo;
	}
	public String getLimpaCancela() {
		return limpaCancela;
	}
	public void setLimpaCancela(String limaCancela) {
		this.limpaCancela = limaCancela;
	}
	public String getCadastraAtualiza() {
		return cadastraAtualiza;
	}
	
	public void setCadastraAtualiza(String cadastraAtualiza) {
		this.cadastraAtualiza = cadastraAtualiza;
	}
	
	public List<Perfil> getPerfis() {
		return perfilDAO.listar();
	}
	
	public void resetaBean() {
		this.idUsuario = 0;
		this.nome = null;
		this.email = null;
		this.login = null;
		this.senha = null;
		this.confirmaSenha = null;
		this.perfil = "Usuário comum";
		this.trocaSenha = "true";
		this.bloqueado = null;
	}
	
	public void cadastrar() {
		FacesContext contexto = FacesContext.getCurrentInstance();
		if (senha.equals(confirmaSenha)) {
			if (Validador.nome(nome)) {
				if (Validador.email(email)) {
					if (Validador.login(login)) {
						String validaLoginEmail = usuarioDAO.verificaLoginEmail(login, email, idUsuario);
						if (validaLoginEmail.equals("loginJaExiste")) {
							contexto.addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, Mensagem.ERRO, Mensagem.LOGIN_JA_EXISTE));
							if (this.idUsuario != 0) {
								this.editar(this.idUsuario);
							}
						} else if (validaLoginEmail.equals("emailJaExiste")) {
							contexto.addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, Mensagem.ERRO, Mensagem.EMAIL_JA_EXISTE));
							if (this.idUsuario != 0) {
								this.editar(this.idUsuario);
							}
						} else if (this.idUsuario == 0) {//Novo cadastro
							if(Validador.senha(senha)) {
								try {
									usuarioDAO.cadastrar(nome, email, login, senha, perfil, trocaSenha, bloqueado);
									File diretorioDoUsuario = new File("C:/apache-tomcat-8.5.16/PDFMerger/" + usuarioDAO.buscaPorLogin(login).getIdUsuario());
									diretorioDoUsuario.mkdir();
									this.resetaBean();
									contexto.addMessage(null, new FacesMessage(FacesMessage.SEVERITY_INFO, Mensagem.SUCESSO, ""));
								} catch (Exception e) {
									e.printStackTrace();
									contexto.addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, Mensagem.ERRO, Mensagem.ERRO_NO_SISTEMA));
								}
							
							} else {
								contexto.addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, Mensagem.ERRO, Mensagem.SENHA_INVALIDA));
							}
							
						} else {//Atualiza cadastro
							if (senha.equals("")) {
								senha = usuarioDAO.getSenha(idUsuario);
							} else {
								if(Validador.senha(senha)) {
									senha = Criptografia.criptografa(senha);
								} else {
									contexto.addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, Mensagem.ERRO, Mensagem.SENHA_INVALIDA));
									this.editar(this.idUsuario);
									return;
								}
							}
							try {
								usuarioDAO.editar(idUsuario, nome, email, login, senha, perfil, trocaSenha, bloqueado);
								this.resetaBean();
								contexto.addMessage(null, new FacesMessage(FacesMessage.SEVERITY_INFO, Mensagem.SUCESSO, ""));
							} catch (Exception e) {
								e.printStackTrace();
								this.editar(this.idUsuario);
								contexto.addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, Mensagem.ERRO, Mensagem.ERRO_NO_SISTEMA));
								this.editar(this.idUsuario);
							}
						}
					} else {
						contexto.addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, Mensagem.ERRO, Mensagem.LOGIN_INVALIDO));
						if (this.idUsuario != 0) {
							this.editar(this.idUsuario);
						}
					}
				} else {
					contexto.addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, Mensagem.ERRO, Mensagem.EMAIL_INVALIDO));
					if (this.idUsuario != 0) {
						this.editar(this.idUsuario);
					}
				}
			} else {
				contexto.addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, Mensagem.ERRO, Mensagem.NOME_INVALIDO));
				if (this.idUsuario != 0) {
					this.editar(this.idUsuario);
				}
			}
		} else {
			if (this.idUsuario != 0) {
				this.editar(this.idUsuario);
			}
			contexto.addMessage(null,
					new FacesMessage(FacesMessage.SEVERITY_ERROR, Mensagem.ERRO, Mensagem.SENHA_NAO_CONFERE));
		}
	}
	
	public void editar(int id) {
		this.usuario = usuarioDAO.busca(id);
		//BeanCadastro cadastro = (BeanCadastro)FacesContext.getCurrentInstance().getExternalContext().getSessionMap().get("BeanCadastro");
		this.setTitulo("Editar usuário");
		this.setLimpaCancela("Cancelar");
		this.setCadastraAtualiza("Atualizar");
		this.setIdUsuario(usuario.getIdUsuario());
		this.setNome(usuario.getNome());
		this.setEmail(usuario.getEmail());
		this.setLogin(usuario.getLogin());
		this.setPerfil(usuario.getPerfil());
		if(usuario.getTrocaSenha().equals("S")) {
			this.trocaSenha = "true";
		} else {
			this.trocaSenha = "false";
		}
		if(usuario.getBloqueado().equals("S")) {
			this.bloqueado = "true";
		} else {
			this.bloqueado = "false";
		}
	}
	
	public void excluir(int id) {
		FacesContext contexto = FacesContext.getCurrentInstance();
		try {
			usuarioDAO.exclui(id);
			contexto.addMessage(null, new FacesMessage(FacesMessage.SEVERITY_INFO, Mensagem.SUCESSO, ""));
		} catch (Exception e) {
			contexto.addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, Mensagem.ERRO, Mensagem.ERRO_NO_SISTEMA));
		}
	}

}
