package dao;

import java.util.Iterator;
import java.util.List;

import javax.persistence.EntityManager;
import javax.persistence.Persistence;
import javax.persistence.Query;

import model.Arquivo;

public class ArquivoDAO {
	
	private EntityManager em = Persistence.createEntityManagerFactory("PDFMerger").createEntityManager();
	
	public void cadastrar(String nome, int idUsuario, String caminho, String acesso) {
		
		Arquivo arquivo = new Arquivo();
		arquivo.setNome(nome);
		arquivo.setIdUsuario(idUsuario);
		arquivo.setCaminho(caminho);
		arquivo.setAcesso(acesso);
		
		this.cadastrar(arquivo);
		
	}
	
	public void cadastrar(Arquivo arquivo) {
		em.getTransaction().begin();
		em.persist(arquivo);
		em.getTransaction().commit();
	}
	
public void editar(String nome, int idUsuario, String caminho, String acesso) {
		
		Arquivo arquivo = new Arquivo();
		arquivo.setNome(nome);
		arquivo.setIdUsuario(idUsuario);
		arquivo.setCaminho(caminho);
		arquivo.setAcesso(acesso);
		
		this.editar(arquivo);
		
	}
	
	public void editar(Arquivo arquivo) {
		em.getTransaction().begin();
		em.persist(arquivo);
		em.getTransaction().commit();
	}
	
	@SuppressWarnings("unchecked")
	public List<Arquivo> listar() {
		Query busca = em.createQuery("SELECT a FROM Arquivo a");
		return busca.getResultList();
	}
	
	@SuppressWarnings("unchecked")
	public List<Arquivo> listaParaUsuario(int idUsuario) {
		Query busca = em.createQuery("SELECT a FROM Arquivo a WHERE a.acesso = 'P' OR a.idUsuario = " + idUsuario + " ORDER BY a.nome ASC");
		return busca.getResultList();
	}
	
	public Arquivo busca(int id) {
		return em.find(Arquivo.class, id);
	}
	
	public void exclui(int id) {
		em.getTransaction().begin();
		em.remove(this.busca(id));
		em.getTransaction().commit();
	}
	
	public boolean verificaCaminhoDoArquivo(String caminho, int idUsuario) {
		List<Arquivo> lista = this.listaParaUsuario(idUsuario);
		Arquivo a;
		Iterator i = lista.iterator();
		while(i.hasNext()) {
			a = (Arquivo)i.next();
			if(a.getCaminho().equals(caminho)) {
				return false;
			}
		}
		return true;
	}

}
