package com.klef.fsad.exam;

import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.Transaction;
import org.hibernate.cfg.Configuration;
import org.hibernate.query.Query;

import java.util.Date;

public class ClientDemo {
    public static void main(String[] args) {
        // Initialize Hibernate
        Configuration cfg = new Configuration().configure("hibernate.cfg.xml");
        SessionFactory sf = cfg.buildSessionFactory();

        // 1. Insert Operation
        insertRestaurant(sf);

        // 2. Update Operation using HQL with Named Parameters
        updateRestaurant(sf, 1, "Spicy Garden", "Active");

        // Close SessionFactory
        sf.close();
    }

    private static void insertRestaurant(SessionFactory sf) {
        Session session = sf.openSession();
        Transaction t = session.beginTransaction();

        Restaurant r = new Restaurant();
        r.setName("Blue Ginger");
        r.setDate(new Date());
        r.setStatus("Pending");

        session.save(r);
        t.commit();

        System.out.println(">> Restaurant Inserted Successfully: " + r);
        session.close();
    }

    private static void updateRestaurant(SessionFactory sf, int id, String newName, String newStatus) {
        Session session = sf.openSession();
        Transaction t = session.beginTransaction();

        try {
            // HQL Update with Named Parameters
            String hql = "UPDATE Restaurant SET name = :newName, status = :newStatus WHERE id = :restId";
            Query query = session.createQuery(hql);
            query.setParameter("newName", newName);
            query.setParameter("newStatus", newStatus);
            query.setParameter("restId", id);

            int result = query.executeUpdate();
            t.commit();

            if (result > 0) {
                System.out.println(">> Restaurant with ID " + id + " updated successfully via HQL.");
            } else {
                System.out.println(">> No restaurant found with ID " + id);
            }
        } catch (Exception e) {
            if (t != null) t.rollback();
            e.printStackTrace();
        } finally {
            session.close();
        }
    }
}
