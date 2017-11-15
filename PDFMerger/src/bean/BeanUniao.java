package bean;

import java.util.List;

import javax.enterprise.context.SessionScoped;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.RequestScoped;
import javax.faces.context.FacesContext;

import dao.ArquivoDAO;
import model.Arquivo;
import model.Usuario;

@ManagedBean
@RequestScoped
public class BeanUniao {
	
	private String nomeArquivoGerado;
	private String acessoArquivoGerado;
	private boolean salvaNoServidor = true;
	private boolean iniciaDownload = true;
	private List<Arquivo> arquivosSelecionados;
	private Arquivo arquivoGerado;
	private ArquivoDAO arquivoDAO = new ArquivoDAO();
	
	public String getNomeArquivoGerado() {
		return nomeArquivoGerado;
	}
	
	public void setNomeArquivoGerado(String nomeArquivoGerado) {
		this.nomeArquivoGerado = nomeArquivoGerado;
	}
	
	public String getAcessoArquivoGerado() {
		return acessoArquivoGerado;
	}
	
	public void setAcessoArquivoGerado(String acessoArquivoGerado) {
		this.acessoArquivoGerado = acessoArquivoGerado;
	}
	
	public boolean isSalvaNoServidor() {
		return salvaNoServidor;
	}
	
	public void setSalvaNoServidor(boolean salvaNoServidor) {
		this.salvaNoServidor = salvaNoServidor;
	}
	
	public boolean isIniciaDownload() {
		return iniciaDownload;
	}
	
	public void setIniciaDownload(boolean iniciaDownload) {
		this.iniciaDownload = iniciaDownload;
	}
	
	public List<Arquivo> getArquivosSelecionados() {
		return arquivosSelecionados;
	}
	
	public void setArquivosSelecionados(List<Arquivo> arquivosSelecionados) {
		this.arquivosSelecionados = arquivosSelecionados;
	}
	
	public Arquivo getArquivoGerado() {
		return arquivoGerado;
	}
	
	public void setArquivoGerado(Arquivo arquivoGerado) {
		this.arquivoGerado = arquivoGerado;
	}
	
	public List<Arquivo> getArquivosDisponiveis() {
		Usuario usuarioLogado = (Usuario)FacesContext.getCurrentInstance().getExternalContext().getSessionMap().get("usuarioLogado");
		return arquivoDAO.listaParaUsuario(usuarioLogado.getIdUsuario());
	}
	
}
