package testUsuarios;

import static io.restassured.RestAssured.given;
import static org.hamcrest.Matchers.equalTo;

import org.junit.jupiter.api.Test;

import Data.Dados;
import io.restassured.http.ContentType;
import io.restassured.response.ExtractableResponse;
import io.restassured.response.Response;

public class deleteUser extends Dados{
	@Test
	public void ExcluirUsuario() {
		ExtractableResponse<Response> response = given()
                .log().all()
                .contentType(ContentType.JSON)
                .body("{ \"nome\": \"" + firstName + " " + lastName + "\", \"email\": \"" + email + "\", \"password\": \"" + password + "\", \"administrador\": \"true\" }")
            .when()
                .post(URL + "/usuarios")
            .then()
                .statusCode(201)
                .body("message", equalTo("Cadastro realizado com sucesso"))
                .extract();

            String userId = response.path("_id");
        
			given()
	        .log().all()
	    .when()
	        .delete(URL + "/usuarios/" + userId)
	    .then()
	        .log().all()
	        .statusCode(200);
	}
	@Test
	public void NaoExcluiUserComCarrinho() {
		ExtractableResponse<Response> UsId = given()
                .log().all()
                .contentType(ContentType.JSON)
                .body("{ \"nome\": \"" + firstName + " " + lastName + "\", \"email\": \"" + email + "\", \"password\": \"" + password + "\", \"administrador\": \"true\" }")
            .when()
                .post(URL + "/usuarios")
            .then()
                .statusCode(201)
                .body("message", equalTo("Cadastro realizado com sucesso"))
                .extract();
			
			String userId = UsId.path("_id");
			
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
		
		ExtractableResponse<Response> proID = given()
     		    .log().all()
     		    .header("Authorization", token)
     		    .contentType(ContentType.JSON)
     		    .body("{ \"nome\": \"Boneco "+figure+"\", \"preco\": "+price+", \"descricao\": \"Action Figure de Pokemon\", \"quantidade\": "+qtd+" }")
     		.when()
     		    .post(URL + "/produtos")
     		.then()
     		    .log().all()
     		    .statusCode(201)
     		    .extract();

     		String productId = proID.path("_id");
     		
     			given()
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
     			
     			given()
    	        .log().all()
    	    .when()
    	        .delete(URL + "/usuarios/" + userId)
    	    .then()
    	        .log().all()
    	        .statusCode(400);
	}
}
