package com.company.carpooling.schedulers;

import com.company.carpooling.models.Trip;
import lombok.AllArgsConstructor;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

@Component
@AllArgsConstructor
public class TripScheduler {

    SessionFactory sessionFactory;

    @Scheduled(cron = "0 */2 * ? * *")
    public void setInProgress() {
        String query = "update carpooling.trips " +
                "set status_id = 2 " +
                "where status_id = 1 AND departure_time <= NOW()";

        try (Session session = sessionFactory.openSession()) {
            session.beginTransaction();
            session.createNativeQuery(query, Trip.class).executeUpdate();
            session.getTransaction().commit();
        }

    }
    @Scheduled(cron = "0 */1 * ? * *")
    public void setCompleted() {
        String query = "update carpooling.trips " +
                "set status_id = 4 " +
                "where status_id = 2 AND departure_time + INTERVAL duration * 60 SECOND <= NOW()";

        try (Session session = sessionFactory.openSession()) {
            session.beginTransaction();
            session.createNativeQuery(query, Trip.class).executeUpdate();
            session.getTransaction().commit();
        }
        System.out.println("Updated");
    }

}
