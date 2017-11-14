package bean;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.URL;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.sql.SQLException;
import java.util.List;

import javax.faces.application.FacesMessage;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.RequestScoped;
import javax.faces.context.ExternalContext;
import javax.faces.context.FacesContext;
import javax.servlet.ServletContext;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.Part;

import dao.ArquivoDAO;
import model.Arquivo;
import model.Usuario;
import utils.Mensagem;
import utils.StringUtils;

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
				contexto.addMessage("up:inputFile", new FacesMessage(FacesMessage.SEVERITY_INFO, Mensagem.SUCESSO, ""));
			} else {
				contexto.addMessage("up:inputFile", new FacesMessage(FacesMessage.SEVERITY_ERROR, Mensagem.ERRO, Mensagem.ARQUIVO_JA_EXISTE));
			}
		} catch (IOException e) {
			contexto.addMessage("up:inputFile", new FacesMessage(FacesMessage.SEVERITY_ERROR, Mensagem.ERRO, Mensagem.ERRO_AO_MANIPULAR_ARQUIVO));
			e.printStackTrace();
		} catch (NullPointerException e) {
			contexto.addMessage("up:inputFile", new FacesMessage(FacesMessage.SEVERITY_ERROR, Mensagem.ERRO, Mensagem.NENHUM_ARQUIVO_SELECIONADO));
			e.printStackTrace();
		}
		
	}
	
	public void editar(int idArquivo) {
		Arquivo arquivo = arquivoDAO.busca(idArquivo);
		this.idArquivo = arquivo.getIdArquivo();
		this.nome = arquivo.getNome();
		this.idUsuario = arquivo.getIdUsuario();
		this.caminho = arquivo.getCaminho();
		this.acesso = arquivo.getAcesso();
	}
	
	public void atualizar() {
		FacesContext contexto = FacesContext.getCurrentInstance();
		if(this.idArquivo != 0) {
			Usuario usuarioLogado = (Usuario)FacesContext.getCurrentInstance().getExternalContext().getSessionMap().get("usuarioLogado");
			
			this.nome = StringUtils.addPdf(this.nome);
			String caminhoNovo;
			if(this.acesso.equals("R")) {
				caminhoNovo = "C:/apache-tomcat-8.5.16/PDFMerger/" + usuarioLogado.getIdUsuario() + "/" + this.nome;
			} else {
				caminhoNovo = "C:/apache-tomcat-8.5.16/PDFMerger/Publico/" + this.nome;
			}
			
			if(arquivoDAO.verificaCaminhoDoArquivo(caminhoNovo, usuarioLogado.getIdUsuario())) {
				try {
					Arquivo arquivo = arquivoDAO.busca(this.idArquivo);
					File arquivoAntigo = new File(arquivo.getCaminho());
					File arquivoNovo = new File(StringUtils.addPdf(caminhoNovo));
					arquivoAntigo.renameTo(arquivoNovo);
					
					arquivoDAO.editar(this.idArquivo, this.nome, usuarioLogado.getIdUsuario(), caminhoNovo, this.acesso);
					this.resetaBean();
					contexto.addMessage("editar", new FacesMessage(FacesMessage.SEVERITY_INFO, Mensagem.SUCESSO, ""));
				} catch(Exception e) {
					contexto.addMessage("editar", new FacesMessage(FacesMessage.SEVERITY_ERROR, Mensagem.ERRO, Mensagem.ERRO_NO_SISTEMA));
				}
			} else {
				contexto.addMessage("editar", new FacesMessage(FacesMessage.SEVERITY_ERROR, Mensagem.ERRO, Mensagem.ARQUIVO_JA_EXISTE));
			}
		} else {
			contexto.addMessage("editar", new FacesMessage(FacesMessage.SEVERITY_ERROR, Mensagem.ERRO, Mensagem.NENHUM_ARQUIVO_SELECIONADO));
		}
		
	}
	
	public void excluir(int idArquivo) {
		FacesContext context = FacesContext.getCurrentInstance();
		try {
			Arquivo arquivo = arquivoDAO.busca(idArquivo);
			File arquivoFisico = new File(arquivo.getCaminho());
			if(arquivoFisico.delete()) {
				arquivoDAO.exclui(idArquivo);
				context.addMessage("editar", new FacesMessage(FacesMessage.SEVERITY_INFO, Mensagem.SUCESSO, ""));
			} else {
				context.addMessage("editar", new FacesMessage(FacesMessage.SEVERITY_ERROR, Mensagem.ERRO, Mensagem.ERRO_AO_MANIPULAR_ARQUIVO));
			}
		} catch (Exception e) {
			e.printStackTrace();
			context.addMessage("editar", new FacesMessage(FacesMessage.SEVERITY_ERROR, Mensagem.ERRO, Mensagem.ERRO_NO_SISTEMA));
		}
	}
	
	public void download(String nomeDoArquivo, String caminho) throws IOException {
		FacesContext context = FacesContext.getCurrentInstance();
		ExternalContext externalContext = context.getExternalContext();
		
		externalContext.responseReset();
		externalContext.setResponseContentType("pdf");
		externalContext.setResponseHeader("Content-Disposition", "attachment;filename=" + StringUtils.addPdf(StringUtils.trocaEspaco(nomeDoArquivo)));
		
		FileInputStream inputStream = new FileInputStream(new File(StringUtils.addPdf(caminho)));
		OutputStream outPutStream = externalContext.getResponseOutputStream();
		
		byte[] buffer = new byte[1024];
		int length;
		while((length = inputStream.read(buffer)) > 0) {
			outPutStream.write(buffer, 0, length);
		}
		
		inputStream.close();
		context.responseComplete();
	}
	
	public void resetaBean() {
		this.idArquivo = 0;
		this.nome = null;
		this.idUsuario = 0;
		this.caminho = null;
		this.acesso = null;
	}

}
