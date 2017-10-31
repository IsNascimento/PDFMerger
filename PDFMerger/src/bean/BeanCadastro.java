package bean;

import javax.faces.bean.ManagedBean;
import javax.faces.bean.RequestScoped;

@ManagedBean
@RequestScoped
public class BeanCadastro {
	
	private String titulo = "Cadastrar usuário";
	private String limpaCancela = "Limpar";
	private String cadastraAtualiza = "Cadastrar";
	
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
}
