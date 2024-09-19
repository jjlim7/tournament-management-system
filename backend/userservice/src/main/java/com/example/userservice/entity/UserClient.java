package com.example.userservice.entity;

import org.springframework.boot.web.client.RestTemplateBuilder;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;

@Component
public class UserClient {
    private RestTemplate template;

    UserClient(RestTemplateBuilder builder) {
        this.template = builder.build();
    }

    /* CRUD methods for User based on userId */

    public User getUser(final String URI, final Long userId) {
        final User user = template.getForObject(URI + "/" + userId, User.class);
        return user;
    }

    public User addUser(final String URI, final User newUser) {
        final User createdUser = template.postForObject(URI, newUser, User.class);
        return createdUser;
    }

    public void updateUser(final String URI, final Long userId, final User updatedUser) {
        template.put(URI + "/" + userId, updatedUser);
    }

    public void deleteUser(final String URI, final Long userId) {
        template.delete(URI + "/" + userId);
    }
}
