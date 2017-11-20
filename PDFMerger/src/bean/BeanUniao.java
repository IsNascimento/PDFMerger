package bean;

import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;

import javax.faces.application.FacesMessage;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.SessionScoped;
import javax.faces.context.FacesContext;

import dao.ArquivoDAO;
import model.Arquivo;
import model.Usuario;
import utils.ArquivoUtils;
import utils.Mensagem;
import utils.PdfUtils;

@ManagedBean
@SessionScoped
public class BeanUniao {
	
	private String nomeArquivoGerado;
	private String acessoArquivoGerado;
	private boolean salvaNoServidor = true;
	private boolean iniciaDownload = true;
	private List<Arquivo> arquivosSelecionados = new ArrayList<Arquivo>();
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
	
	public void geraArquivoUnido() {
		FacesContext contexto = FacesContext.getCurrentInstance();
		Usuario usuarioLogado = (Usuario)FacesContext.getCurrentInstance().getExternalContext().getSessionMap().get("usuarioLogado");
		arquivoGerado = new Arquivo();
		arquivoGerado.setIdUsuario(usuarioLogado.getIdUsuario());
		arquivoGerado.setNome(nomeArquivoGerado);
		arquivoGerado.setAcesso(acessoArquivoGerado);
		if(arquivoGerado.getAcesso().equals("R")) {
			arquivoGerado.setCaminho("C:/apache-tomcat-8.5.16/PDFMerger/" + arquivoGerado.getIdUsuario() + "/" + arquivoGerado.getNome());
		} else {
			arquivoGerado.setCaminho("C:/apache-tomcat-8.5.16/PDFMerger/Publico/" + arquivoGerado.getNome());
		}
		
		if(arquivoDAO.verificaCaminhoDoArquivo(arquivoGerado.getCaminho(), arquivoGerado.getIdUsuario())) {
			try{
				PdfUtils.juntaPDF(arquivosSelecionados, arquivoGerado);
				if(salvaNoServidor) {
					arquivoDAO.cadastrar(arquivoGerado);
				}
				if(iniciaDownload) {
					ArquivoUtils.download(arquivoGerado.getNome(), arquivoGerado.getCaminho());
				}
				contexto.addMessage(null, new FacesMessage(FacesMessage.SEVERITY_INFO, Mensagem.SUCESSO, ""));
			}catch(Exception e) {
				e.printStackTrace();
				contexto.addMessage(null , new FacesMessage(FacesMessage.SEVERITY_ERROR, Mensagem.ERRO, Mensagem.ERRO_NO_SISTEMA));
			}
		} else {
			contexto.addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, Mensagem.ERRO, Mensagem.ARQUIVO_JA_EXISTE));
		}
		
	}
	
	public void adicionaArquivo(int idArquivo) {
		try {
			this.arquivosSelecionados.add(arquivoDAO.busca(idArquivo));
		} catch (Exception e) {
			FacesContext contexto = FacesContext.getCurrentInstance();
			contexto.addMessage(null , new FacesMessage(FacesMessage.SEVERITY_ERROR, Mensagem.ERRO, Mensagem.ERRO_AO_MANIPULAR_ARQUIVO));
		}
	}
	
	public void removeArquivo(int index) {
		try {
			this.arquivosSelecionados.remove(index);
		} catch (Exception e) {
			FacesContext contexto = FacesContext.getCurrentInstance();
			contexto.addMessage(null , new FacesMessage(FacesMessage.SEVERITY_ERROR, Mensagem.ERRO, Mensagem.ERRO_AO_MANIPULAR_ARQUIVO));
		}
	}
	
	public void reordenaArquivoSobe(int index) {
		try {
			Arquivo arquivo = this.arquivosSelecionados.remove(index);
			arquivosSelecionados.add((index - 1), arquivo);
		} catch (Exception e) {
			FacesContext contexto = FacesContext.getCurrentInstance();
			contexto.addMessage(null , new FacesMessage(FacesMessage.SEVERITY_ERROR, Mensagem.ERRO, Mensagem.ERRO_AO_MANIPULAR_ARQUIVO));
		}
	}
	
	public void reordenaArquivoDesce(int index) {
		try {
			Arquivo arquivo = this.arquivosSelecionados.remove(index);
			arquivosSelecionados.add((index + 1), arquivo);
		} catch (Exception e) {
			FacesContext contexto = FacesContext.getCurrentInstance();
			contexto.addMessage(null , new FacesMessage(FacesMessage.SEVERITY_ERROR, Mensagem.ERRO, Mensagem.ERRO_AO_MANIPULAR_ARQUIVO));
		}
	}
	
	public void resetaBean() {
		nomeArquivoGerado = null;
		acessoArquivoGerado = null;
		salvaNoServidor = true;
		iniciaDownload = true;
		arquivosSelecionados = new ArrayList<Arquivo>();
		arquivoGerado = null;
	}
	
}
