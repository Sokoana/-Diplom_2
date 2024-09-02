import io.qameta.allure.junit4.DisplayName;
import io.restassured.response.Response;
import org.apache.commons.lang3.RandomStringUtils;
import org.junit.Before;
import org.junit.Test;
import constans.OrderData;
import constans.UserData;
import static org.apache.http.HttpStatus.*;
import static org.hamcrest.Matchers.equalTo;
import static org.hamcrest.Matchers.is;
public class OrderTest  extends HttpPage {
    private String email;
    private String accessToken;
    private String ingredientFirst;
    private String ingredientSecond;
    private String name;
    private UserData user;

    @Before
    public void setUp() {
        baseURL();
        user = UserData.makeRandomUser();
        Response response = BasicUser.buildUser(user);
        email = user.getEmail();
        name = user.getName();
        accessToken = response.jsonPath().getString("accessToken");
        response = BasicOrder.getIngredients();
        ingredientFirst = response.jsonPath().getString("data[0]._id");
        ingredientSecond = response.jsonPath().getString("data[1]._id");

    }
    @Test
    @DisplayName("Заказ без авторизации")
    public void testOrderWithoutToken() {
        OrderData createOrder = new OrderData(new String[]{ingredientFirst, ingredientSecond});
        Response response = BasicOrder.createOrder(createOrder);
        response
                .then().assertThat().statusCode(SC_UNAUTHORIZED).body("success", equalTo(false));
    }

    @Test
    @DisplayName("Заказ с ингредиентами")
    public void testOrder() {
        OrderData createOrder = new OrderData(new String[]{ingredientFirst, ingredientSecond});
        Response response = BasicOrder.createOrder(createOrder, accessToken);
        response
                .then().assertThat().statusCode(SC_OK).body("order.ingredients[0]._id", is(ingredientFirst))
                .body("order.ingredients[1]._id", is(ingredientSecond)).body("success", equalTo(true));
    }

    @Test
    @DisplayName("Заказ без ингредиентов")
    public void testOrderWithoutIngredients() {
        OrderData createOrder = new OrderData(new String[]{});
        Response response = BasicOrder.createOrder(createOrder, accessToken);
        response
                .then().assertThat().statusCode(SC_BAD_REQUEST).body("success", equalTo(false));
    }

    @Test
    @DisplayName("Создание заказа с неверным хэшем ингредиентов")
    public void testOrderWithNullIngredients() {
        String nullIngredient = RandomStringUtils.randomAlphabetic(10);
        OrderData createOrder = new OrderData(new String[]{nullIngredient});
        Response response = BasicOrder.createOrder(createOrder, accessToken);
        response
                .then().assertThat().statusCode(SC_INTERNAL_SERVER_ERROR);
    }

}
