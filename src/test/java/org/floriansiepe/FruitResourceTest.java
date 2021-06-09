package org.floriansiepe;

import io.quarkus.test.common.http.TestHTTPResource;
import io.quarkus.test.junit.QuarkusTest;
import org.junit.jupiter.api.Test;

import javax.ws.rs.core.MediaType;
import java.net.URL;

import static io.restassured.RestAssured.given;
import static io.restassured.RestAssured.when;
import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.CoreMatchers.isA;

@QuarkusTest
class FruitResourceTest {

    @Test
    void testGetAll() {
        given()
                .when().get("/fruits")
                .then()
                .statusCode(200)
                .body("$.size", is(3));
    }

    @Test
    void testGetSingle() {
        given()
                .when()
                .get("/fruits/2")
                .then()
                .statusCode(200)
                .body("id", is(2))
                .body("name", is("Pineapple"))
                .body("description", is("Tropical fruit"));
    }

    @Test
    void testCreateAndDelete() {
        final Integer id = given()
                .when()
                .header("Content-Type", MediaType.APPLICATION_JSON)
                .body("{\"name\": \"Pear\", \"description\": \"Winter fruit\"}")
                .post("/fruits")
                .then()
                .statusCode(200)
                .body("name", is("Pear"))
                .body("description", is("Winter fruit"))
                .extract().body().path("id");

        given()
                .when().get("/fruits")
                .then()
                .statusCode(200)
                .body("$.size", is(4));

        when()
                .delete("/fruits/" + id)
                .then()
                .statusCode(204);

        given()
                .when().get("/fruits")
                .then()
                .statusCode(200)
                .body("$.size", is(3));
    }

    @Test
    void testUpdate() {
        final Integer id = given()
                .when()
                .header("Content-Type", MediaType.APPLICATION_JSON)
                .body("{\"name\": \"Pear\", \"description\": \"Winter fruit\"}")
                .post("/fruits")
                .then()
                .statusCode(200)
                .body("name", is("Pear"))
                .body("description", is("Winter fruit"))
                .extract().body().path("id");

        given()
                .when()
                .header("Content-Type", MediaType.APPLICATION_JSON)
                .body("{\"name\": \"Banana\", \"description\": \"Tropical fruit\"}")
                .put("/fruits/" + id)
                .then()
                .statusCode(200)
                .body("name", is("Banana"))
                .body("description", is("Tropical fruit"));

        given()
                .when()
                .get("/fruits/" + id)
                .then()
                .statusCode(200)
                .body("id", is(id))
                .body("name", is("Banana"))
                .body("description", is("Tropical fruit"));
    }

    @Test
    void updateInvalid() {
        given()
                .when()
                .header("Content-Type", MediaType.APPLICATION_JSON)
                .body("{\"name\": \"Banana\", \"description\": \"Tropical fruit\"}")
                .put("/fruits/1234" )
                .then()
                .statusCode(400);
    }

}
