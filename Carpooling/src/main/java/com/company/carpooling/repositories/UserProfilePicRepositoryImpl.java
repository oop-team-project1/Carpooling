package com.company.carpooling.repositories;

import com.company.carpooling.exceptions.EntityNotFoundException;
import com.company.carpooling.models.User;
import com.company.carpooling.models.UserProfilePic;
import lombok.AllArgsConstructor;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.query.Query;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import java.util.List;

@AllArgsConstructor
@Repository
public class UserProfilePicRepositoryImpl implements UserProfilePicRepository{
    private final SessionFactory sessionFactory;

    @Override
    public UserProfilePic getById(int id) {
        try (Session session = sessionFactory.openSession()) {
            Query<UserProfilePic> query = session.createQuery(
                    "from UserProfilePic where picId = :picId", UserProfilePic.class);
            query.setParameter("picId", id);

            List<UserProfilePic> result = query.list();
            if (result.isEmpty()) {
                throw new EntityNotFoundException("Profile picture", id);
            }

            return result.get(0);
        }
    }
}
