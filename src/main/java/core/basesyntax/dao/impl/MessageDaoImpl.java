package core.basesyntax.dao.impl;

import core.basesyntax.dao.MessageDao;
import core.basesyntax.model.Message;
import java.util.List;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.Transaction;
import org.hibernate.query.Query;

public class MessageDaoImpl extends AbstractDao implements MessageDao {
    public MessageDaoImpl(SessionFactory sessionFactory) {
        super(sessionFactory);
    }

    @Override
    public Message create(Message message) {
        Transaction transaction = null;
        Session session = null;
        try {
            session = factory.openSession();
            transaction = session.beginTransaction();
            session.save(message);
            transaction.commit();
            return message;
        } catch (Exception e) {
            if (transaction != null) {
                transaction.rollback();
            }
            throw new RuntimeException("Message can't create!", e);
        } finally {
            if (session != null) {
                session.close();
            }
        }
    }

    @Override
    public Message get(Long id) {
        try (Session session = factory.openSession()) {
            Transaction transaction = session.beginTransaction();
            Message message = session.get(Message.class, id);
            transaction.commit();
            return message;
        } catch (Exception e) {
            throw new RuntimeException("Can't get message by id!", e);
        }
    }

    @Override
    public List<Message> getAll() {
        try (Session session = factory.openSession()) {
            Query<Message> getAllMessage = session.createQuery("from Message", Message.class);
            return getAllMessage.getResultList();
        } catch (Exception e) {
            throw new RuntimeException("Can't get all messages!", e);
        }
    }

    @Override
    public void remove(Message message) {
        try (Session session = factory.openSession()) {
            Transaction transaction = session.beginTransaction();
            session.remove(message);
            transaction.commit();
        } catch (Exception e) {
            throw new RuntimeException("Can't delete message!", e);
        }
    }
}
