package core.basesyntax.dao.impl;

import core.basesyntax.dao.CommentDao;
import core.basesyntax.model.Comment;
import java.util.List;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.Transaction;
import org.hibernate.query.Query;

public class CommentDaoImpl extends AbstractDao implements CommentDao {
    public CommentDaoImpl(SessionFactory sessionFactory) {
        super(sessionFactory);
    }

    @Override
    public Comment create(Comment comment) {
        Transaction transaction = null;
        Session session = null;
        try {
            session = factory.openSession();
            transaction = session.beginTransaction();
            session.save(comment);
            transaction.commit();
            return comment;
        } catch (Exception e) {
            if (transaction != null) {
                transaction.rollback();
            }
            throw new RuntimeException("Can't insert Content entity", e);
        } finally {
            if (session != null) {
                session.close();
            }
        }
    }

    @Override
    public Comment get(Long id) {
        try (Session session = factory.openSession()) {
            Transaction transaction = session.beginTransaction();
            Comment comment = session.get(Comment.class, id);
            transaction.commit();
            return comment;
        } catch (Exception e) {
            throw new RuntimeException("Can't get comment by id!", e);
        }
    }

    @Override
    public List<Comment> getAll() {
        try (Session session = factory.openSession()) {
            Query<Comment> getAllComments = session.createQuery("from Comment ", Comment.class);
            return getAllComments.getResultList();
        } catch (Exception e) {
            throw new RuntimeException("Can't get all comments!", e);
        }
    }

    @Override
    public void remove(Comment comment) {
        try (Session session = factory.openSession()) {
            Transaction transaction = session.beginTransaction();
            session.remove(comment);
            transaction.commit();
        } catch (Exception e) {
            throw new RuntimeException("Can't delete comment!", e);
        }
    }
}
