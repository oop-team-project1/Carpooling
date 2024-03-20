package com.company.carpooling.repositories.contracts;

import com.company.carpooling.models.Application;
import com.company.carpooling.models.Trip;
import com.company.carpooling.models.User;

public interface ApplicationRepository {

    Application get(Trip trip, User user);
    Application get(int id);
    void update(Application application);
}
