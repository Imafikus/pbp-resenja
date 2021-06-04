package hibernate_bp_PrezimeIme_alasNalog_grupa;

import java.util.List;
import java.util.Scanner;
import java.util.stream.Stream;

import org.hibernate.Session;
import org.hibernate.Transaction;
import org.hibernate.query.Query;

public class Main {

	public static void main(String[] args) {
		Session session = HibernateUtil.getSessionFactory().openSession();
		Transaction TR = null;

		try (Scanner ulaz = new Scanner(System.in)) {
			TR = session.beginTransaction();
			
			System.out.println("Unesite karakter za pretragu smerova: ");
			String spKarakter = ulaz.next();
			
			String hqlSP = "FROM StudijskiProgram s WHERE LOWER(s.naziv) LIKE :nazivLike";
			Query<StudijskiProgram> studijskiProgramiUpit = session.createQuery(hqlSP, StudijskiProgram.class);
			studijskiProgramiUpit.setParameter("nazivLike", spKarakter + "%");
			List<StudijskiProgram> studijskiProgrami = studijskiProgramiUpit.list();
			
			for (StudijskiProgram sp : studijskiProgrami) {
				System.out.println(sp.getNaziv().trim() + " (" + sp.getId() + ") -- bodovi: " + sp.getObimespb());
				
				for (Student s : sp.getStudenti()) {
					String hqlStudent = "FROM Praksa p WHERE p.indeks = :indeks";
					Query<Praksa> praksaUpit = session.createQuery(hqlStudent, Praksa.class);
					praksaUpit.setParameter("indeks", s.getIndeks());

					Stream<Praksa> kandidati = praksaUpit.stream();
		            kandidati
		            	.map(k -> s.getIme().trim() + " " + s.getPrezime().trim() + " " + k.getPoeniSaStudija() + " " + k.getPoeniSaTesta())
		                .forEach(info -> System.out.println("\t" + info));
				}
			}
			
			TR.commit();
		} catch (Exception e) {
			e.printStackTrace();
			if (TR != null) {
				TR.rollback();
			}
		}
		
		session.close();
		HibernateUtil.getSessionFactory().close();
	}
}
