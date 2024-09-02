import io.qameta.allure.junit4.DisplayName;
import io.restassured.response.Response;
import org.apache.commons.lang3.RandomStringUtils;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import constans.AutorizationUser;
import constans.UserData;
import constans.UserDelete;
import constans.UserUpdate;

import static org.apache.http.HttpStatus.*;
import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.Matchers.equalTo;

public class UserUpdateTest  extends  HttpPage{
    private String email;
    private String name;
    private String password;
    private String accessToken;
    private UserData user;

    @Before
    public void setUp() {
        baseURL();
        user = UserData.makeRandomUser();
        Response response = BasicUser.buildUser(user);
        email = user.getEmail();
        name = user.getName();
        password = user.getPassword();
        accessToken = response.jsonPath().getString("accessToken");
    }
    @Test
    @DisplayName("Редактирование пользователя без авторизации")
    public void testUpdateUserWithoutToken() {
        UserUpdate userUpdate = new UserUpdate(RandomStringUtils.randomAlphabetic(8) + "@yandex.ru", RandomStringUtils.randomAlphabetic(5), RandomStringUtils.randomAlphabetic(7));
        Response response = BasicUser.updateUser(userUpdate);
        response
                .then().assertThat().statusCode(SC_UNAUTHORIZED).body("success", equalTo(false));
    }

    @Test
    @DisplayName("Редактирование данных пользователя")
    public void testUpdateUser() {
        UserUpdate updateUser = new UserUpdate(RandomStringUtils.randomAlphabetic(8) + "@yandex.ru", RandomStringUtils.randomAlphabetic(5), RandomStringUtils.randomAlphabetic(7));
        email = updateUser.getEmail();
        name = updateUser.getName();
        password = updateUser.getPassword();
        Response response = BasicUser.updateUser(updateUser, accessToken);
        response
                .then().assertThat().statusCode(SC_OK).body("success", equalTo(true)).body("user.email", is(email.toLowerCase())).body("user.name", is(name));
        AutorizationUser autorizationUser = new AutorizationUser(email, password);
        BasicUser.autorizationUser(autorizationUser).then().assertThat().statusCode(SC_OK);
    }
    @After
    public void deleteUser() {
        BasicUser.deleteUser(new UserDelete(email, name), accessToken).then().statusCode(SC_ACCEPTED);
    }
}
