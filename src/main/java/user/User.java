package user;

import com.github.javafaker.Faker;

public class User extends UserData {


    private String name;

    public User(String email, String password, String name) {
        super(email, password);
        this.name = name;
    }

    public User() {
        super();
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public static User getDefaultUser() {
        Faker faker = new Faker();
        String name = faker.name().username();
        String email = faker.internet().emailAddress();
        String password = faker.internet().password();
        return new User(email, password, name);
    }
}
