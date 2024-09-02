import io.qameta.allure.Step;
import io.restassured.response.Response;
import io.restassured.specification.RequestSpecification;
import constans.AutorizationUser;
import constans.UserData;
import constans.UserDelete;
import constans.UserUpdate;
import static io.restassured.RestAssured.given;
import static constans.URL.*;
public class BasicUser {

    @Step("Создание пользователя")
    public static Response buildUser(UserData userData) {
        return given().log().all()
                .header("Content-type", "application/json")
                .and()
                .body(userData)
                .when()
                .post(create_user);
    }

    @Step("Авторизация пользователя")
    public static Response autorizationUser(AutorizationUser autorizationUser) {
        return given().log().all()
                .header("Content-type", "application/json")
                .and()
                .body(autorizationUser)
                .when()
                .post(autorization_user);
    }

    @Step("Удаление пользователя")
    public static Response deleteUser(UserDelete userDelete, String accessToken) {
        return given()
                .header("Content-type", "application/json")
                .header("Authorization", accessToken)
                .and()
                .body(userDelete)
                .when()
                .delete(delete_user);
    }

    @Step("Редатирование  пользователя с токеном")
    public static Response updateUser(UserUpdate userUpdate, String accessToken) {
        RequestSpecification requestSpecification =
                given().log().all()
                        .header("Content-type", "application/json");
        if (accessToken != null) {
            requestSpecification.header("Authorization", accessToken);
        }
        Response response =
                requestSpecification
                        .and()
                        .body(userUpdate)
                        .patch(update_user);
        return response;
    }

    @Step("Редактирование пользователя без токена")
    public static Response updateUser(UserUpdate userUpdate) {
        return updateUser(userUpdate, null);
    }
}
