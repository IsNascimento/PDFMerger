package model;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;

@Entity
@Table
public class Arquivo {
	
	@Id
	@GeneratedValue(strategy=GenerationType.IDENTITY)
	private int idArquivo;
	private String nome;
	private int idUsuario;
	private String caminho;
	private String acesso;
	
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
}
