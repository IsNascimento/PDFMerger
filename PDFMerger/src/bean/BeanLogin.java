package bean;

import javax.faces.application.FacesMessage;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.RequestScoped;
import javax.faces.context.ExternalContext;
import javax.faces.context.FacesContext;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;

import dao.UsuarioDAO;
import model.Usuario;
import utils.Criptografia;
import utils.Mensagem;

@ManagedBean
@RequestScoped
public class BeanLogin {
	
	private String login;
	private String senha;

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
		this.senha = Criptografia.criptografa(senha);
	}

	public String entrar() {
		FacesContext context = FacesContext.getCurrentInstance();
		ExternalContext externalContext = context.getExternalContext();
		HttpServletRequest request = (HttpServletRequest) externalContext.getRequest();
		
		try {
			UsuarioDAO usuarioDAO = new UsuarioDAO();
			Usuario u = usuarioDAO.buscaPorLogin(login);
			if(u.getBloqueado().equals("S")) {
				context.addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, Mensagem.ERRO, Mensagem.LOGIN_BLOQUEADO));
				return "/login.xhtml?faces-redirect=true";
			} else {
				request.login(login, senha);
				externalContext.getSessionMap().put("usuarioLogado", u);
			}
		} catch (ServletException e) {
			context.addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, Mensagem.ERRO, Mensagem.EUSUARIO_NAO_CADASTRADO));
		}

		return "/cadastro.xhtml?faces-redirect=true";
	}

	public String sair() {
		FacesContext context = FacesContext.getCurrentInstance();
		ExternalContext externalContext = context.getExternalContext();
		externalContext.invalidateSession();
		
		return "/cadastro.xhtml?faces-redirect=true";
	}

}
