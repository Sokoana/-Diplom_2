import io.restassured.RestAssured;

public class HttpPage {
    public void baseURL() {
        RestAssured.baseURI = "https://stellarburgers.nomoreparties.site/";
    }
}
