package dao;

import java.util.List;

import javax.persistence.EntityManager;
import javax.persistence.Persistence;
import javax.persistence.Query;

import model.Perfil;

public class PerfilDAO {
	
	private EntityManager em = Persistence.createEntityManagerFactory("PDFMerger").createEntityManager();
	
	@SuppressWarnings("unchecked")
	public List<Perfil> listar() {
		Query busca = em.createQuery("SELECT p FROM Perfil p");
		return busca.getResultList();
	}

}
