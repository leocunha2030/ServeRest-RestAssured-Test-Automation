package testUsuarios;

import static io.restassured.RestAssured.given;
import static org.hamcrest.Matchers.equalTo;

import org.junit.jupiter.api.Test;

import Data.Dados;
import io.restassured.http.ContentType;
import io.restassured.response.ExtractableResponse;
import io.restassured.response.Response;

public class putUser extends Dados{
	@Test
	public void EditaUser() {
		
		ExtractableResponse<Response> response = given()
                .log().all()
                .contentType(ContentType.JSON)
                .body("{ \"nome\": \"" + firstName + " " + lastName + "\", \"email\": \"" + email + "\", \"password\": \"" + password + "\", \"administrador\": \"true\" }")
            .when()
                .post(URL + "/usuarios")
            .then()
                .log().all()
                .statusCode(201)
                .body("message", equalTo("Cadastro realizado com sucesso"))
                .extract();

            String userId = response.path("_id");
            
	         given()
	            .log().all()
	            .contentType(ContentType.JSON)
	            .body("{ \"nome\": \"" + firstName + " " + lastName + "\", \"email\": \"" + email + "\", \"password\": \"" + password + "\", \"administrador\": \"false\" }")
	        .when()
	            .put(URL + "/usuarios/" + userId)
	        .then()
	            .log().all()
	            .statusCode(200);
	}
	@Test
	public void EditarUsuarioCadastroComSucesso() {
		given()
        .log().all()
        .contentType(ContentType.JSON)
        .body("{ \"nome\": \"" + firstName + " " + lastName + "\", \"email\": \"" + email + "\", \"password\": \"" + password + "\", \"administrador\": \"false\" }")
    .when()
        .put(URL + "/usuarios/"+idEdit)
    .then()
        .log().all()
        .statusCode(201);
	}
	@Test
	public void EditaEmailjaCadastrado() {
		
		ExtractableResponse<Response> response = given()
                .log().all()
                .contentType(ContentType.JSON)
                .body("{ \"nome\": \"" + firstName + " " + lastName + "\", \"email\": \"" + email + "\", \"password\": \"" + password + "\", \"administrador\": \"true\" }")
            .when()
                .post(URL + "/usuarios")
            .then()
                .log().all()
                .statusCode(201)
                .body("message", equalTo("Cadastro realizado com sucesso"))
                .extract();

            String userId = response.path("_id");
            
	         given()
	            .log().all()
	            .contentType(ContentType.JSON)
	            .body("{ \"nome\": \"" + firstName + " " + lastName + "\", \"email\": \"" + email + "\", \"password\": \"" + password + "\", \"administrador\": \"true\" }")
	        .when()
	            .put(URL + "/usuarios/" + userId)
	        .then()
	            .log().all()
	            .statusCode(200);//BUG registrado nesta request
	}
}
