import io.qameta.allure.junit4.DisplayName;
import io.restassured.response.Response;
import org.apache.commons.lang3.RandomStringUtils;
import org.junit.Before;
import org.junit.Test;
import constans.UserData;

import static org.apache.http.HttpStatus.*;
import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.Matchers.equalTo;

public class UserTest extends HttpPage{
    private String email;
    private String name;
    private String accessToken;
    private UserData user;

    @Before
    public void setUp() {
        baseURL();
        user = UserData.makeRandomUser();
        email = user.getEmail();
        name = user.getName();

    }

    @Test
    @DisplayName("Создание нового пользователя")
    public void testNewUser() {
        Response response = BasicUser.buildUser(user);
        response
                .then().assertThat().statusCode(SC_OK).body("success", equalTo(true));
        accessToken = response.jsonPath().getString("accessToken");
    }
    @Test
    @DisplayName("Создание пользователя, который уже зарегистрирован")
    public void testNextUser() {
        Response response = BasicUser.buildUser(user);
        Response responseOne = BasicUser.buildUser(user);
        responseOne
                .then().assertThat().statusCode(SC_FORBIDDEN).body("message", is("User already exists"));
        accessToken = response.jsonPath().getString("accessToken");
    }


    @Test
    @DisplayName("Создание пользователя без обязательного поля")
    public void testUserWithoutData() {
        UserData createUser = new UserData(RandomStringUtils.randomAlphabetic(8) + "@yandex.ru".toLowerCase(), null, RandomStringUtils.randomAlphabetic(7));
        Response response = BasicUser.buildUser(createUser);
        response
                .then().assertThat().statusCode(SC_FORBIDDEN).body("message", is("Email, password and name are required fields"));
        accessToken = response.jsonPath().getString("accessToken");
    }





    }

