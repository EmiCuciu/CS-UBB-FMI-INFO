package org.example.lab6.domain;

public class UserInstance {
    private static UserInstance instance;
    private Long id;

    public UserInstance(Long id) {
        this.id = id;
    }

    public static UserInstance getInstance() {
        if (instance == null) {
            instance = new UserInstance((long)-1);
        }
        return instance;
    }


    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }
}
