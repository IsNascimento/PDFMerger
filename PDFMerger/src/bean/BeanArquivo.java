package bean;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.sql.SQLException;
import java.util.List;

import javax.faces.application.FacesMessage;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.RequestScoped;
import javax.faces.context.FacesContext;
import javax.servlet.http.Part;

import dao.ArquivoDAO;
import model.Arquivo;
import model.Usuario;
import utils.Mensagem;

@ManagedBean
@RequestScoped
public class BeanArquivo {
	
	private int idArquivo;
	private String nome;
	private int idUsuario;
	private String caminho;
	private String acesso;
	private ArquivoDAO arquivoDAO = new ArquivoDAO();
	private Part arquivo;
	private Arquivo arquivoLogico = new Arquivo();
	
	public int getIdArquivo() {
		return idArquivo;
	}
	
	public void setIdArquivo(int idArquivo) {
		this.idArquivo = idArquivo;
	}
	
	public String getNome() {
		return nome;
	}
	
	public void setNome(String nome) {
		this.nome = nome;
	}
	
	public int getIdUsuario() {
		return idUsuario;
	}
	
	public void setIdUsuario(int idUsuario) {
		this.idUsuario = idUsuario;
	}
	
	public String getCaminho() {
		return caminho;
	}
	
	public void setCaminho(String caminho) {
		this.caminho = caminho;
	}
	
	public String getAcesso() {
		return acesso;
	}
	
	public void setAcesso(String acesso) {
		this.acesso = acesso;
	}
	
	public Part getArquivo() {
		return arquivo;
	}
	
	public void setArquivo(Part arquivo) {
		this.arquivo = arquivo;
	}
	
	public Arquivo getArquivoLogico() {
		return arquivoLogico;
	}
	
	public void setArquivoLogico(Arquivo arquivo) {
		this.arquivoLogico = arquivo;
	}
	
	public List<Arquivo> getArquivos() {
		Usuario usuarioLogado = (Usuario)FacesContext.getCurrentInstance().getExternalContext().getSessionMap().get("usuarioLogado");
		return arquivoDAO.listaParaUsuario(usuarioLogado.getIdUsuario());
	}
	
	public void carregaArquivo() {
		Usuario usuarioLogado = (Usuario)FacesContext.getCurrentInstance().getExternalContext().getSessionMap().get("usuarioLogado");
		arquivoLogico.setIdUsuario(usuarioLogado.getIdUsuario());
		FacesContext contexto = FacesContext.getCurrentInstance();
		try {
			arquivoLogico.setNome(arquivo.getSubmittedFileName());
			if(arquivoLogico.getAcesso().equals("R")) {
				arquivoLogico.setCaminho("C:/apache-tomcat-8.5.16/PDFMerger/" + arquivoLogico.getIdUsuario() + "/" + arquivoLogico.getNome());
			} else {
				arquivoLogico.setCaminho("C:/apache-tomcat-8.5.16/PDFMerger/Publico/" + arquivoLogico.getNome());
			}
			if(arquivoDAO.verificaCaminhoDoArquivo(arquivoLogico.getCaminho(), arquivoLogico.getIdUsuario())) {
				Files.copy(arquivo.getInputStream(), Paths.get(arquivoLogico.getCaminho()));
				arquivoDAO.cadastrar(arquivoLogico);
				contexto.addMessage(null, new FacesMessage(FacesMessage.SEVERITY_INFO, Mensagem.SUCESSO, ""));
			} else {
				contexto.addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, Mensagem.ERRO, Mensagem.ARQUIVO_JA_EXISTE));
			}
		} catch (IOException e) {
			contexto.addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, Mensagem.ERRO, Mensagem.ERRO_AO_MANIPULAR_ARQUIVO));
			e.printStackTrace();
		} catch (NullPointerException e) {
			contexto.addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, Mensagem.ERRO, Mensagem.NENHUM_ARQUIVO_SELECIONADO));
			e.printStackTrace();
		}
		
	}

}
