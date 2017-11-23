package bean;

import javax.faces.application.FacesMessage;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.RequestScoped;
import javax.faces.context.FacesContext;

import dao.UsuarioDAO;
import model.Usuario;
import utils.Criptografia;
import utils.Mensagem;
import utils.Validador;

@ManagedBean
@RequestScoped
public class BeanTrocaDeSenha {
	
	private String senhaAtual;
	private String novaSenha;
	private String confirmaNovaSenha;
	private UsuarioDAO usuarioDAO = new UsuarioDAO();
	
	public String getSenhaAtual() {
		return senhaAtual;
	}

	public void setSenhaAtual(String senhaAtual) {
		this.senhaAtual = senhaAtual;
	}

	public String getNovaSenha() {
		return novaSenha;
	}

	public void setNovaSenha(String novaSenha) {
		this.novaSenha = novaSenha;
	}

	public String getConfirmaNovaSenha() {
		return confirmaNovaSenha;
	}

	public void setConfirmaNovaSenha(String confirmaNovaSenha) {
		this.confirmaNovaSenha = confirmaNovaSenha;
	}

	public String atualizaSenha() {
		FacesContext contexto = FacesContext.getCurrentInstance();
		Usuario usuarioTrocaSenha = (Usuario)FacesContext.getCurrentInstance().getExternalContext().getSessionMap().get("usuarioTrocaSenha");
		usuarioTrocaSenha = usuarioDAO.busca(usuarioTrocaSenha.getIdUsuario());
		if(Criptografia.criptografa(senhaAtual).equals(usuarioTrocaSenha.getSenha())) {
			if(novaSenha.equals(confirmaNovaSenha)) {
				if(Validador.senha(novaSenha)) {
					if(senhaAtual.equals(novaSenha)) {
						contexto.addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, Mensagem.ERRO, Mensagem.SENHA_IGUAL_NOVA_SENHA));
						return "trocadesenha";
					} else {
						usuarioTrocaSenha.setSenha(Criptografia.criptografa(novaSenha));
						usuarioTrocaSenha.setTrocaSenha("N");
						usuarioDAO.editar(usuarioTrocaSenha);
						contexto.getExternalContext().getSessionMap().remove("usuarioTrocaSenha");
						return "/uniao.xhtml?faces-redirect=true";
					}
				} else {
					contexto.addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, Mensagem.ERRO, Mensagem.SENHA_INVALIDA));
					return "trocadesenha";
				}
			} else {
				contexto.addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, Mensagem.ERRO, Mensagem.NOVA_SENHA_NAO_CONFERE));
				return "trocadesenha";
			}
		} else {
			contexto.addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, Mensagem.ERRO, Mensagem.NOVA_SENHA_INCORRETA));
			return "trocadesenha";
		}
	}

}
