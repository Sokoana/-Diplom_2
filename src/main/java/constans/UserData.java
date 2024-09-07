package constans;
import org.apache.commons.lang3.RandomStringUtils;
public class UserData {
    String email;
    String name;
    String password;

    public UserData(String email, String name, String password) {
        this.email = email;
        this.name = name;

        this.password = password;
    }

    public String getName() {
        return name;
    }

    public String getEmail() {
        return email;
    }
    public String getPassword() {
        return password;
    }

    public static UserData makeRandomUser() {
        final String email = RandomStringUtils.randomAlphabetic(7) + "@yandex.ru".toLowerCase();
        final String password = RandomStringUtils.randomAlphabetic(11).toLowerCase();
        final String name = RandomStringUtils.randomAlphabetic(6);
        return new UserData(email, password, name);
    }
}
