package com.company.carpooling.helpers.filters;

import lombok.AllArgsConstructor;
import lombok.Getter;
import java.util.Optional;

@Getter
public class FilterOptionsUsers {
    private Optional<String> username;
    private Optional<String> phoneNumber;
    private Optional<String> email;
    private Optional<String> sortBy;
    private Optional<String> sortOrder;

    public FilterOptionsUsers() {
        this(null, null, null, null, null);
    }

    public FilterOptionsUsers(String username,
                              String phoneNumber,
                              String email,
                              String sortBy,
                              String sortOrder) {
        this.username = Optional.ofNullable(username);
        this.phoneNumber = Optional.ofNullable(phoneNumber);
        this.email = Optional.ofNullable(email);
        this.sortBy = Optional.ofNullable(sortBy);
        this.sortOrder = Optional.ofNullable(sortOrder);
    }
}
