import constans.UserDelete;
import io.qameta.allure.junit4.DisplayName;
import io.restassured.response.Response;
import org.apache.commons.lang3.RandomStringUtils;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import constans.AutorizationUser;
import constans.UserData;
import static org.apache.http.HttpStatus.*;
import static org.hamcrest.Matchers.equalTo;
public class AutorizationTest extends HttpPage {
    private String email;
    private String password;
    private UserData user;
    private String name;
    private String accessToken;
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
    @DisplayName("Авторизация пользователя")
    public void testAutorizationUser() {
        AutorizationUser autorizationUser = new AutorizationUser(email, password);
        Response response = BasicUser.autorizationUser(autorizationUser);
        response
                .then().assertThat().statusCode(SC_OK).body("success", equalTo(true));
           }
    @Test
    @DisplayName("Авторизация  с неверным логином и паролем")
    public void testLoginUserWrongData() {
        AutorizationUser autorizationUser = new AutorizationUser(RandomStringUtils.randomAlphabetic(8) + "@yandex.ru", RandomStringUtils.randomAlphabetic(5));
        Response response = BasicUser.autorizationUser(autorizationUser);
        response.then().assertThat().statusCode(SC_UNAUTHORIZED).body("success", equalTo(false));
    }
    @After
    public void deleteUser() {
   BasicUser.deleteUser(new UserDelete(email,name), accessToken).then().statusCode(SC_ACCEPTED);
    }
    }

