package ma.projet.util;



import org.hibernate.SessionFactory;
import org.hibernate.boot.registry.StandardServiceRegistryBuilder;
import org.hibernate.cfg.Configuration;
 
public class HibernateUtil {
 
   private static final SessionFactory sessionFactory=buildSessionFactory();
 
  public static SessionFactory buildSessionFactory(){
       try {
    	 /*  Configuration configuration = new Configuration().configure();
    	   StandardServiceRegistryBuilder builder = new StandardServiceRegistryBuilder().
    	   applySettings(configuration.getProperties());
    	    SessionFactory factory = configuration.buildSessionFactory(builder.build());
    	    return factory; */
         return new Configuration().configure().buildSessionFactory();
       } catch (Throwable ex) {
           System.err.println("Initial SessionFactory creation failed." + ex);
           throw new ExceptionInInitializerError(ex);
       }
   }
 
   public static SessionFactory getSessionFactory() {
       return sessionFactory;
   }
}