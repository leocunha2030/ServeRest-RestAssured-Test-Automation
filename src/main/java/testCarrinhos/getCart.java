package testCarrinhos;

import static io.restassured.RestAssured.given;
import static org.hamcrest.Matchers.equalTo;

import org.junit.jupiter.api.Test;

import Data.Dados;
import io.restassured.http.ContentType;
import io.restassured.response.ExtractableResponse;
import io.restassured.response.Response;

public class getCart extends Dados{
	@Test
	public void ListadeCarrinhos() {
		given()
    	.log().all()
    .when()
    	.get(URL + "/carrinhos")
    .then()
    	.log().all()
    	.statusCode(200);
	}
	@Test
	public void CarrinhoNaoEncontrado() {
		given()
    	.log().all()
    .when()
    	.get(URL + "/carrinhos/"+price)
    .then()
    	.log().all()
    	.statusCode(400);
	}
	@Test
	public void CarrinhoEncontrado() {
		given()
        .log().all()
        .contentType(ContentType.JSON)
        .body("{ \"nome\": \"" + firstName + " " + lastName + "\", \"email\": \"" + email + "\", \"password\": \"" + password + "\", \"administrador\": \"true\" }")
    .when()
        .post(URL + "/usuarios")
    .then()
        .log().all()
        .statusCode(201)
        .body("message", equalTo("Cadastro realizado com sucesso"));
		
		String token = given()
                .log().all()
                .contentType(ContentType.JSON)
                .body("{ \"email\": \""+email+"\", \"password\": \""+password+"\" }")
            .when()
                .post(URL + "/login")
            .then()
                .log().all()
                .body("message", equalTo("Login realizado com sucesso"))
                .extract().path("authorization");
		
		ExtractableResponse<Response> response = given()
     		    .log().all()
     		    .header("Authorization", token)
     		    .contentType(ContentType.JSON)
     		    .body("{ \"nome\": \"Action figure "+figure+"\", \"preco\": "+price+", \"descricao\": \"Action Figure de Pokemon\", \"quantidade\": "+qtd+" }")
     		.when()
     		    .post(URL + "/produtos")
     		.then()
     		    .log().all()
     		    .statusCode(201)
     		    .extract();
		
		String productId = response.path("_id");
		
		ExtractableResponse<Response> IdCart = given()
	 	    .log().all()
	 	    .header("Authorization", token)
	 	    .contentType(ContentType.JSON)
	 	    .body("{ \"produtos\": [{ \"idProduto\": \"" + productId + "\", \"quantidade\": " + qtd + " }] }")
	 	.when()
	 	    .post(URL + "/carrinhos")
	 	.then()
	 	    .log().all()
	 	    .statusCode(201)
	 	    .extract();
		
		String CartId = IdCart.path("_id");
		
		given()
    	.log().all()
    .when()
    	.get(URL + "/carrinhos/"+CartId)
    .then()
    	.log().all()
    	.statusCode(200);
	}

}
