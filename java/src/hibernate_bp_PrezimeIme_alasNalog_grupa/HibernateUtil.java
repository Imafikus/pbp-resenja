package hibernate_bp_PrezimeIme_alasNalog_grupa;

import org.hibernate.SessionFactory;
import org.hibernate.boot.MetadataSources;
import org.hibernate.boot.registry.StandardServiceRegistry;
import org.hibernate.boot.registry.StandardServiceRegistryBuilder;

public class HibernateUtil {
	private static SessionFactory sessionFactory = null;

	public static SessionFactory getSessionFactory() {
		return sessionFactory;
	}
	
	static {
	try {
            StandardServiceRegistry registry = new StandardServiceRegistryBuilder().configure().build();
            sessionFactory = new MetadataSources(registry)
            					.addAnnotatedClass(Praksa.class)
            					.addAnnotatedClass(Student.class)
            					.addAnnotatedClass(StudijskiProgram.class)
		            			.buildMetadata()
		            			.buildSessionFactory();
        } catch (Throwable e) {
            System.err.println("Session factory error");
            e.printStackTrace();

            System.exit(1);
        }
	}
}
