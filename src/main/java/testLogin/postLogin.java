package testLogin;

import static io.restassured.RestAssured.given;
import static org.hamcrest.Matchers.equalTo;

import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Test;

import Data.Dados;
import io.restassured.http.ContentType;

public class postLogin extends Dados{
	
	@Test 
	public void LoginSucesso() {
		given()
	    .log().all()
	    .contentType(ContentType.JSON)
	    .body("{ \"email\": \"fulano@qa.com\", \"password\": \"teste\" }")
	.when()
	    .post(URL + "/login")
	.then()
	    .log().all()
	    .body("message", equalTo("Login realizado com sucesso"));
	}
	@Test
	public void LoginSenhaInvalida() {
		given()
	    .log().all()
	    .contentType(ContentType.JSON)
	    .body("{ \"email\": \"fulano@qa.com\", \"password\": \"errada\" }")
	.when()
	    .post(URL + "/login")
	.then()
	    .log().all();;
	}
	@Test
	public void LoginEmailInvalido() {
		given()
	    .log().all()
	    .contentType(ContentType.JSON)
	    .body("{ \"email\": \"invalido@qa.com\", \"password\": \"teste\" }")
	.when()
	    .post(URL + "/login")
	.then()
	    .log().all()
	    .statusCode(401);
	}
	@Test
	public void LoginSenhaMenorQueCinco() {
		given()
        .log().all()
        .contentType(ContentType.JSON)
        .body("{ \"nome\": \"menor que 5\", \"email\": \""+email+"\", \"password\": \"1\", \"administrador\": \"true\" }")
    .when()
        .post(URL + "/usuarios")
    .then()
        .log().all()
        .statusCode(201)
        .body("message", equalTo("Cadastro realizado com sucesso"));
		
		given()
	    .log().all()
	    .contentType(ContentType.JSON)
	    .body("{ \"email\": \""+email+"\", \"password\": \"1\" }")
	.when()
	    .post(URL + "/login")
	.then()
	    .log().all()
	    .body("message", equalTo("Login realizado com sucesso"));
	}
	@Test
	public void LoginSenhaMaiorQueDez() {
		given()
        .log().all()
        .contentType(ContentType.JSON)
        .body("{ \"nome\": \"Maior que 10\", \"email\": \""+email+"\", \"password\": \"12345678910\", \"administrador\": \"true\" }")
    .when()
        .post(URL + "/usuarios")
    .then()
        .log().all()
        .statusCode(201)
        .body("message", equalTo("Cadastro realizado com sucesso"));
		
		given()
	    .log().all()
	    .contentType(ContentType.JSON)
	    .body("{ \"email\": \""+email+"\", \"password\": \"12345678910\" }")
	.when()
	    .post(URL + "/login")
	.then()
	    .log().all()
	    .body("message", equalTo("Login realizado com sucesso"));
	}
	@Test
	public void LoginSenhaEmBranco() {
		given()
	    .log().all()
	    .contentType(ContentType.JSON)
	    .body("{ \"email\": \"fulano@qa.com\", \"password\": \"\" }")
	.when()
	    .post(URL + "/login")
	.then()
	    .log().all();
	}

}
