package dao;

import java.util.Iterator;
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
	
	public void editar(int id, String nome, String email, String login, String senha, String perfil, String trocaSenha, String blq) {
		
		Usuario u = new Usuario();
		u.setIdUsuario(id);
		u.setNome(nome);
		u.setEmail(email);
		u.setLogin(login);
		u.setSenha(senha);
		u.setPerfil(perfil);
		u.setTrocaSenha(trocaSenha);
		u.setBloqueado(blq);
		
		this.editar(u);
	}
	
	public void editar (Usuario u) {
		em.getTransaction().begin();
		em.merge(u);
		em.getTransaction().commit();
	}
	
	@SuppressWarnings("unchecked")
	public List<Usuario> listar() {
		Query busca = em.createQuery("SELECT u FROM Usuario u");
		return busca.getResultList();
	}
	
	public Usuario busca(int id) {
		return em.find(Usuario.class, id);
	}
	
	public Usuario buscaPorLogin(String login) {
		List<Usuario> lista = this.listar();
		Usuario u;
		Iterator i = lista.iterator();
		while(i.hasNext()) {
			u = (Usuario)i.next();
			if(u.getLogin().equals(login)) {
				return u;
			}
		}
		return null;
	}
	
	public String getSenha(int id) {
		List<Usuario> lista = this.listar();
		Usuario u;
		Iterator i = lista.iterator();
		while(i.hasNext()) {
			u = (Usuario)i.next();
			if(u.getIdUsuario() == id) {
				return u.getSenha();
			}
		}
		return null;
	}
	
	public void exclui(int id) {
		em.getTransaction().begin();
		em.remove(this.busca(id));
		em.getTransaction().commit();
	}
	
	public String verificaLoginEmail(String login, String email, int id) {
		List<Usuario> lista = this.listar();
		Usuario u;
		Iterator i = lista.iterator();
		while(i.hasNext()) {
			u = (Usuario)i.next();
			if(u.getLogin().equals(login)) {
				if(id == 0) {
					return "loginJaExiste";
				} else {
					if(id == u.getIdUsuario()) {
						return "";
					} else {
						return "loginJaExiste";
					}
				}
			} else {
				if(u.getEmail().equals(email)) {
					if(id == 0) {
						return "emailJaExiste";
					} else {
						if(id == u.getIdUsuario()) {
							return "";
						} else {
							return "emailJaExiste";
						}
					}
				}
			}
		}
		return "";
	}

}
