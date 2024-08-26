import io.qameta.allure.junit4.DisplayName;
import io.restassured.response.Response;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import сonstans.OrderData;
import сonstans.UserData;
import сonstans.UserDelete;


import static org.apache.http.HttpStatus.*;
import static org.hamcrest.Matchers.equalTo;
public class OderTakeTest extends HttpPage{

    private String email;
    private String name;
    private String accessToken;
    private String ingredientFirst;
    private String ingredientSecond;
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
        OrderData orderData = new OrderData(new String[]{ingredientFirst, ingredientSecond});
        BasicOrder.createOrder(orderData, accessToken);
    }
    @Test
    @DisplayName("Заказы неавторизованного пользователя")
    public void testOrdersWithoutToken() {
        Response response = BasicOrder.getOrdersWithoutToken();
        response
                .then().assertThat().statusCode(SC_UNAUTHORIZED).body("success", equalTo(false));
    }

    @Test
    @DisplayName("Заказы авторизованного пользователя")
    public void testOrders() {
        Response response = BasicOrder.getOrders(accessToken);
        response
                .then().assertThat().statusCode(SC_OK).body("success", equalTo(true));
    }



    @After
    public void deleteUser() {
        BasicUser.deleteUser(new UserDelete(email, name), accessToken).then().statusCode(SC_ACCEPTED);
    }
}
