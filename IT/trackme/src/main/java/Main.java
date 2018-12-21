
import model.*;


import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.Persistence;
import java.sql.Timestamp;


public class Main {

    public static EntityManager em;

    public static void main(String[] args) {
        EntityManagerFactory emf = Persistence.createEntityManagerFactory("NewPersistenceUnit");
        em = emf.createEntityManager();

        System.out.println("Hello");




        //insertBlood();






    }




    private static void insertBlood() {
        for(int i=0; i<5; i++) {

            IndividualEntity ind = em.find(IndividualEntity.class, "ABCDE");

            BloodPressureEntity b = new BloodPressureEntity(ind, Timestamp.valueOf("2018-12-" + (4+i) + " 11:22:00"));
            b.setValue((short) (2*i + 2));

            em.getTransaction().begin();
            em.persist(b);
            em.getTransaction().commit();
        }
    }
}
