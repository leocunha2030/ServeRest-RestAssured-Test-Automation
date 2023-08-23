package testProdutos;

import static io.restassured.RestAssured.given;

import org.junit.jupiter.api.Test;

import Data.Dados;

public class getProduct extends Dados{
	@Test
	public void ListarProdutos() {
			given()
	    	.log().all()
	    .when()
	    	.get(URL + "/produtos")
	    .then()
	    	.log().all()
	    	.statusCode(200);
	}

}
