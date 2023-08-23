package testProdutos;

import static io.restassured.RestAssured.given;
import static org.hamcrest.Matchers.equalTo;

import org.junit.jupiter.api.Test;

import Data.Dados;
import io.restassured.http.ContentType;
import io.restassured.response.ExtractableResponse;
import io.restassured.response.Response;

public class putProduct extends Dados {
	@Test
	public void EditaProdutoNoCarrinho() {
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
     		    .body("{ \"nome\": \"Action figure do "+figure+"\", \"preco\": "+price+", \"descricao\": \"Action Figure de Pokemon\", \"quantidade\": "+qtd+" }")
     		.when()
     		    .post(URL + "/produtos")
     		.then()
     		    .log().all()
     		    .statusCode(201)
     		    .extract();
		
		String productId = response.path("_id");
			
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
		        .contentType(ContentType.JSON)
		        .body("{ \"nome\": \"Action figure do "+figure+"\", \"preco\": "+price+", \"descricao\": \"Action Figure de Pokemon\", \"quantidade\": "+qtd+" }")
		    .when()
		        .put(URL + "/carrinhos/"+productId)
		    .then()
		        .log().all()
		        .statusCode(405);		
	}
	@Test
	public void EditaAlteradoComSucesso() {
		String token = given()
                .log().all()
                .contentType(ContentType.JSON)
                .body("{ \"email\": \"fulano@qa.com\", \"password\": \"teste\" }")
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
		    .body("{ \"nome\": \"Action figure do "+figure+"\", \"preco\": "+price+", \"descricao\": \"Action Figure de Pokemon\", \"quantidade\": "+qtd+" }")
		.when()
		    .post(URL + "/produtos")
		.then()
		    .log().all()
		    .statusCode(201)
		    .extract();		
		
		String productId = response.path("_id");
		
			given()
	        .log().all()
	        .header("Authorization", token)
	        .contentType(ContentType.JSON)
	        .body("{ \"nome\": \" "+figure+"\", \"preco\": "+price+", \"descricao\": \"Action figure de Super Heroi\", \"quantidade\": "+qtd+" }")
	    .when()
	        .put(URL + "/produtos/" + productId)
	    .then()
	        .log().all()
	        .statusCode(200)
	        .extract().statusCode();
		
	}
	@Test
	public void CadastroComSucessoEditarProduto() {
		String token = given()
                .log().all()
                .contentType(ContentType.JSON)
                .body("{ \"email\": \"fulano@qa.com\", \"password\": \"teste\" }")
            .when()
                .post(URL + "/login")
            .then()
                .log().all()
                .body("message", equalTo("Login realizado com sucesso"))
                .extract().path("authorization");
		given()
        .log().all()
        .header("Authorization", token)
        .contentType(ContentType.JSON)
        .body("{ \"nome\": \" "+figure+"\", \"preco\": "+price+", \"descricao\": \"Action figure de Super Heroi\", \"quantidade\": "+qtd+" }")
    .when()
        .put(URL + "/produtos/"+price)
    .then()
        .log().all()
        .statusCode(201);
	}
	@Test
	public void TokenAusenteEditarProduto() {
			given()
                .log().all()
                .contentType(ContentType.JSON)
                .body("{ \"email\": \"fulano@qa.com\", \"password\": \"teste\" }")
            .when()
                .post(URL + "/login")
            .then()
                .log().all()
                .body("message", equalTo("Login realizado com sucesso"));
			
			given()
	        .log().all()
	        .contentType(ContentType.JSON)
	        .body("{ \"nome\": \" "+figure+"\", \"preco\": "+price+", \"descricao\": \"Action figure de Super Heroi\", \"quantidade\": "+qtd+" }")
	    .when()
	        .put(URL + "/produtos/" + qtd)
	    .then()
	        .log().all()
	        .statusCode(401);
	}
	@Test
	public void EditarProdutoNoAdmin() {
			given()
		        .log().all()
		        .contentType(ContentType.JSON)
		        .body("{ \"nome\": \"" + firstName + " " + lastName + "\", \"email\": \"" + email + "\", \"password\": \"" + password + "\", \"administrador\": \"false\" }")
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
		
			given()
     		    .log().all()
     		    .header("Authorization", token)
     		    .contentType(ContentType.JSON)
     		    .body("{ \"nome\": \"Action figure do "+figure+"\", \"preco\": "+price+", \"descricao\": \"Action Figure de Pokemon\", \"quantidade\": "+qtd+" }")
     		.when()
     		    .post(URL + "/produtos")
     		.then()
     		    .log().all()
     		    .statusCode(403);
	}
}
