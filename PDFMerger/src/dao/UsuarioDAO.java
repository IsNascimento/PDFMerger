package dao;

import java.util.List;

import javax.persistence.EntityManager;
import javax.persistence.Persistence;
import javax.persistence.Query;

import model.Usuario;
import utils.Criptografia;

public class UsuarioDAO {
	
	private EntityManager em = Persistence.createEntityManagerFactory("PDFMerger").createEntityManager();
	
	public void cadastrar(String nome, String email, String login, String senha, String perfil, String trocaSenha, String blq) {
		
		Usuario u = new Usuario();
		u.setNome(nome);
		u.setEmail(email);
		u.setLogin(login);
		u.setSenha(Criptografia.criptografa(senha));
		u.setPerfil(perfil);
		u.setTrocaSenha(trocaSenha);
		u.setBloqueado(blq);
		
		this.cadastrar(u);
	}
	
	public void cadastrar(Usuario u) {
		em.getTransaction().begin();
		em.persist(u);
		em.getTransaction().commit();
	}
	
	@SuppressWarnings("unchecked")
	public List<Usuario> listar() {
		Query busca = em.createQuery("SELECT u FROM Usuario u");
		return busca.getResultList();
	}
	
	public void exclui(Usuario u) {
		em.getTransaction().begin();
		em.remove(u);
		em.getTransaction().commit();
	}

}
