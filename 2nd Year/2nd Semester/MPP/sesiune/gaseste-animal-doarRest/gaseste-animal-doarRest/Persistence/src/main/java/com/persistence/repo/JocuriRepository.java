package com.persistence.repo;

import com.model.Incercare;
import com.model.Joc;
import com.network.dto.JocNeghicitDTO;
import com.persistence.IRepoJocuri;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.hibernate.Session;
import org.springframework.stereotype.Repository;

import java.util.ArrayList;
import java.util.List;

@Repository
public class JocuriRepository implements IRepoJocuri {

    private static final Logger logger = LogManager.getLogger();

    public JocuriRepository() {
        logger.info("JocuriRepository initialized");
    }

    @Override
    public List<JocNeghicitDTO> findJocuriNeghicite(int id) {
        logger.traceEntry("Finding Jocuri Neghicite for id {}", id);

        int nrColoane = 4;

        try (Session session = HibernateUtils.getSessionFactory().openSession()){
            List<Joc> jocuri = session.createQuery("from Joc j where j.jucator.id = :idJucator and j.ghicit = false ", Joc.class)
                    .setParameter("idJucator", id)
                    .list();

            List<Joc> toateJocurile = session.createQuery("from Joc ", Joc.class).list();

            logger.info("Toate jocurile: {}", toateJocurile);

            List<JocNeghicitDTO> rezultat = new ArrayList<>();

            for (Joc joc : jocuri) {
                List<Incercare> incercari = session.createQuery("from Incercare i where i.joc.id = :idJoc", Incercare.class)
                        .setParameter("idJoc", joc.getId())
                        .list();

                List<Integer> pozitiiPropuse = incercari.stream()
                        .map(i -> i.getLinie() * nrColoane + i.getColoana())
                        .toList();

                int nrIncercari = incercari.size();
                int pozitieAnimal = joc.getConfiguratie().getLinie() * nrColoane + joc.getConfiguratie().getColoana();

                JocNeghicitDTO dto = new JocNeghicitDTO(
                        joc.getId(),
                        nrIncercari,
                        pozitiiPropuse,
                        pozitieAnimal
                );

                rezultat.add(dto);

                logger.info("Joc Neghicit DTO: {}", dto);
            }
            logger.info("Found {} jocuri neghicite for id: {}", rezultat.size(), id);
            return rezultat;
        }
        catch (Exception e) {
            logger.error("Error finding jocuri neghicite for id {}: {}", id, e.getMessage());
            return new ArrayList<>();
        }
    }

    @Override
    public Joc findOne(Integer integer) {
        logger.traceEntry("Finding Joc with id {}", integer);
        try (Session session = HibernateUtils.getSessionFactory().openSession()) {
            return session.get(Joc.class, integer);
        } catch (Exception e) {
            logger.error("Error finding Joc with id {}: {}", integer, e.getMessage());
            return null;
        }
    }

    @Override
    public Iterable<Joc> findAll() {
        logger.traceEntry("Finding all Jocuri");
        try (Session session = HibernateUtils.getSessionFactory().openSession()) {
            return session.createQuery("from Joc", Joc.class).getResultList();
        } catch (Exception e) {
            logger.error("Error finding all Jocuri: {}", e.getMessage());
            return new ArrayList<>();
        }
    }

    @Override
    public void save(Joc entity) {
        logger.traceEntry("Saving Joc: {}", entity);
        try (Session session = HibernateUtils.getSessionFactory().openSession()) {
            session.beginTransaction();
            session.persist(entity);
            session.getTransaction().commit();
        } catch (Exception e) {
            logger.error("Error saving Joc: {}", e.getMessage());
            throw new RuntimeException("Error saving Joc: " + e.getMessage(), e);
        }
    }

    @Override
    public void delete(Integer integer) {
        logger.traceEntry("Deleting Joc with id {}", integer);
        try (Session session = HibernateUtils.getSessionFactory().openSession()) {
            session.beginTransaction();
            Joc joc = session.get(Joc.class, integer);
            if (joc != null) {
                session.remove(joc);
            }
            session.getTransaction().commit();
            logger.info("Joc with id {} deleted successfully", integer);
        } catch (Exception e) {
            logger.error("Error deleting Joc with id {}: {}", integer, e.getMessage());
            throw new RuntimeException("Error deleting Joc: " + e.getMessage(), e);
        }
    }

    @Override
    public void update(Joc entity) {
        logger.traceEntry("Updating Joc: {}", entity);
        try (Session session = HibernateUtils.getSessionFactory().openSession()) {
            session.beginTransaction();
            session.merge(entity);
            session.getTransaction().commit();
            logger.info("Joc with id {} updated successfully", entity.getId());
        } catch (Exception e) {
            logger.error("Error updating Joc: {}", e.getMessage());
            throw new RuntimeException("Error updating Joc: " + e.getMessage(), e);
        }
    }
}
