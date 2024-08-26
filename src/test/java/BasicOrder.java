import io.qameta.allure.Step;
import io.restassured.response.Response;
import io.restassured.specification.RequestSpecification;
import сonstans.OrderData;


import static io.restassured.RestAssured.given;
import static сonstans.URL.*;

public class BasicOrder {


    @Step("Создание заказа")
    public static Response createOrder(OrderData orderData, String accessToken) {
        RequestSpecification requestSpecification =
                given().log().all()
                        .header("Content-type", "application/json");
        if (accessToken != null) {
            requestSpecification.header("Authorization", accessToken);
        }
        Response response =
                requestSpecification
                        .and()
                        .body(orderData)
                        .post(create_order);
        return response;

    }

    @Step(" Заказ без авторизации")
    public static Response createOrder(OrderData orderData) {
        return createOrder(orderData, null);
    }

    @Step("Получение данных об ингредиентах")
    public static Response getIngredients() {
        Response response =
                given()
                        .header("Content-type", "application/json")
                        .when()
                        .get(get_ingredient);
        return response;
    }

    @Step("Получение заказов")
    public static Response getOrders(String accessToken) {
        RequestSpecification requestSpecification =
                given()
                        .header("Content-type", "application/json");
        if (accessToken != null) {
            requestSpecification.header("Authorization", accessToken);
        }
        Response response =
                requestSpecification
                        .and()
                        .when()
                        .get(get_order);
        return response;
    }

    @Step("Получение заказов неавторизованного пользователя")
    public static Response getOrdersWithoutToken() {
        return getOrders(null);
    }
}
