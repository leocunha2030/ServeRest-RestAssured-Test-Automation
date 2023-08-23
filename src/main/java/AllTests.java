import static io.restassured.RestAssured.given;
import static org.hamcrest.Matchers.equalTo;

import org.junit.jupiter.api.Test;

import Data.Dados;
import io.restassured.http.ContentType;
import io.restassured.response.ExtractableResponse;
import io.restassured.response.Response;

public class AllTests extends Dados{
	private static final String URL = "http://localhost:3000";

	public AllTests() {
		super();
	}
	//Rota Usuários
	@Test
	public void Cadastra_User() {
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
        
	}
	@Test
	public void Cadastra_User_Repetido() {

		given()
        .log().all()
        .contentType(ContentType.JSON)
        .body("{ \"nome\": \"Cara Repetido\", \"email\": \"" + email + "\", \"password\": \"" + password + "\", \"administrador\": \"true\" }")
    .when()
        .post(URL + "/usuarios")
    .then()
        .log().all()
        .statusCode(201);
        
		
		given()
        .log().all()
        .contentType(ContentType.JSON)
        .body("{ \"nome\": \"Cara Repetido\", \"email\": \"" + email + "\", \"password\": \"" + password + "\", \"administrador\": \"true\" }")
    .when()
        .post(URL + "/usuarios")
    .then()
        .log().all()
        .statusCode(400);
        
	}
	@Test
	public void Cadastra_User_Em_Branco() {
		given()
        .log().all()
        .contentType(ContentType.JSON)
        .body("{ \"nome\": \"\", \"email\": \"" + email + "\", \"password\": \"" + password + "\", \"administrador\": \"true\" }")
    .when()
        .post(URL + "/usuarios")
    .then()
        .log().all()
        .statusCode(400);
	}
	@Test
	public void Cadastra_User_Senha_Em_Branco() {
		given()
        .log().all()
        .contentType(ContentType.JSON)
        .body("{ \"nome\": \"" + firstName + " " + lastName + "\", \"email\": \"" + email + "\", \"password\": \"\", \"administrador\": \"true\" }")
    .when()
        .post(URL + "/usuarios")
    .then()
        .log().all()
        .statusCode(400);
	}
	@Test
	public void Excluir_Usuario() {
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
	public void Nao_Exclui_User_Com_Carrinho() {
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
	@Test
	public void Edita_User() {
		
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
	public void Editar_Usuario_Cadastro_Com_Sucesso() {
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
	public void Edita_Email_ja_Cadastrado() {
		
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
	@Test
	public void Lista_Users() {
			given()
	        .log().all()
	    .when()
	        .get(URL + "/usuarios")
	    .then()
	        .log().all()
	        .statusCode(200);
	}
	//Rota Login
	@Test
	public void Login_Sucesso() {
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
	public void Login_Senha_Invalida() {
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
	public void Login_Email_Invalido() {
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
	public void Login_Senha_Menor_Que_Cinco() {
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
	public void Login_Senha_Maior_Que_Dez() {
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
	public void Login_Senha_Em_Branco() {
		given()
	    .log().all()
	    .contentType(ContentType.JSON)
	    .body("{ \"email\": \"fulano@qa.com\", \"password\": \"\" }")
	.when()
	    .post(URL + "/login")
	.then()
	    .log().all();
	}
	//Rota Produtos
	@Test
	public void Cadastra_Produto_Sucesso() {
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
     		    .body("{ \"nome\": \"Action figure do "+figure+"\", \"preco\": "+price+", \"descricao\": \"Action Figure de Pokemon\", \"quantidade\": "+qtd+" }")
     		.when()
     		    .post(URL + "/produtos")
     		.then()
     		    .log().all()
     		    .statusCode(201);
	}
	@Test
	public void Cadastra_Produto_Repetido() {
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
     		    .body("{ \"nome\": \"Action figure do "+figure+"\", \"preco\": "+price+", \"descricao\": \"Action Figure de Pokemon\", \"quantidade\": "+qtd+" }")
     		.when()
     		    .post(URL + "/produtos")
     		.then()
     		    .log().all()
     		    .statusCode(201);
			
			given()
	 		    .log().all()
	 		    .header("Authorization", token)
	 		    .contentType(ContentType.JSON)
	 		    .body("{ \"nome\": \"Action figure do "+figure+"\", \"preco\": "+price+", \"descricao\": \"Action Figure de Pokemon\", \"quantidade\": "+qtd+" }")
	 		.when()
	 		    .post(URL + "/produtos")
	 		.then()
	 		    .log().all()
	 		    .statusCode(400);
	}
	@Test
	public void Cadastra_Produto_Token_Ausente() {
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
     		    .body("{ \"nome\": \"Action figure do "+figure+"\", \"preco\": "+price+", \"descricao\": \"Action Figure de Pokemon\", \"quantidade\": "+qtd+" }")
     		.when()
     		    .post(URL + "/produtos")
     		.then()
     		    .log().all()
     		    .statusCode(401);
	}
	@Test
	public void Cadastra_Produto_No_Admin() {
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
	@Test
	public void Deleta_Produto_No_Carrinho() {
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
            	.header("Authorization", token)
            .when()
            	.delete(URL + "/produtos/"+ productId)
            .then()
            	.log().all()
            	.statusCode(400)
            	.extract().statusCode();
	}
	@Test
	public void Edita_Produto_No_Carrinho() {
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
	public void Edita_Alterado_Com_Sucesso() {
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
	public void Cadastro_Com_Sucesso_Editar_Produto() {
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
	public void Token_Ausente_Editar_Produto() {
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
	public void Editar_Produto_No_Admin() {
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
	@Test
	public void Exclui_Produto_Com_Sucesso() {
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
     		    .body("{ \"nome\": \"Action figure do "+figure+" para Excluir\", \"preco\": "+price+", \"descricao\": \"Action Figure de Pokemon\", \"quantidade\": "+qtd+" }")
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
        .when()
        	.delete(URL + "/produtos/"+ productId)
        .then()
        	.log().all()
        	.statusCode(200)
        	.extract().statusCode();
	}
	@Test
	public void Token_Ausente_Deleta_Produto() {
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
     		    .body("{ \"nome\": \"Action figure do "+figure+" para Excluir\", \"preco\": "+price+", \"descricao\": \"Action Figure de Pokemon\", \"quantidade\": "+qtd+" }")
     		.when()
     		    .post(URL + "/produtos")
     		.then()
     		    .log().all()
     		    .statusCode(201)
     		    .extract();
			
		String productId = response.path("_id");
		
			given()
        	.log().all()
        .when()
        	.delete(URL + "/produtos/"+ productId)
        .then()
        	.log().all()
        	.statusCode(401);
	}
	@Test
	public void Excluir_Produto_NoAdmin() {
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
        .when()
        	.delete(URL + "/produtos/"+ price)
        .then()
        	.log().all()
        	.statusCode(403);
	}
	@Test
	public void Listar_Produtos() {
			given()
	    	.log().all()
	    .when()
	    	.get(URL + "/produtos")
	    .then()
	    	.log().all()
	    	.statusCode(200);
	}
	//Rota Carrinhos
	@Test
	public void Lista_de_Carrinhos() {
		given()
    	.log().all()
    .when()
    	.get(URL + "/carrinhos")
    .then()
    	.log().all()
    	.statusCode(200);
	}
	@Test
	public void Cadastra_com_Sucesso() {
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
	 	    .statusCode(201);
	}
	@Test
	public void Nao_e_permitido_mais_De_um_Carrinho() {
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
	 	    .statusCode(201);
		
		given()
 	    .log().all()
 	    .header("Authorization", token)
 	    .contentType(ContentType.JSON)
 	    .body("{ \"produtos\": [{ \"idProduto\": \"" + productId + "\", \"quantidade\": " + qtd + " }] }")
 	.when()
 	    .post(URL + "/carrinhos")
 	.then()
 	    .log().all()
 	    .statusCode(400);
	}
	@Test
	public void Token_Ausente() {
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
	 	    .contentType(ContentType.JSON)
	 	    .body("{ \"produtos\": [{ \"idProduto\": \"" + productId + "\", \"quantidade\": " + qtd + " }] }")
	 	.when()
	 	    .post(URL + "/carrinhos")
	 	.then()
	 	    .log().all()
	 	    .statusCode(401);
	}
	@Test
	public void Carrinho_Nao_Encontrado() {
		given()
    	.log().all()
    .when()
    	.get(URL + "/carrinhos/"+price)
    .then()
    	.log().all()
    	.statusCode(400);
	}
	@Test
	public void Carrinho_Encontrado() {
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
	@Test
	public void Concluir_Compra() {
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
     		    .body("{ \"nome\": \"Action figures do "+figure+"\", \"preco\": "+price+", \"descricao\": \"Action Figure de Pokemon\", \"quantidade\": "+qtd+" }")
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
  	    .header("Authorization", token)
  	    .contentType(ContentType.JSON)
  	.when()
  		.delete(URL + "/carrinhos/concluir-compra")
  	.then()
  	    .log().all()
  	    .statusCode(200);
	}
	@Test
	public void Cancelar_Compra() {
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
  	    .header("Authorization", token)
  	    .contentType(ContentType.JSON)
  	.when()
  		.delete(URL + "/carrinhos/cancelar-compra")
  	.then()
  	    .log().all()
  	    .statusCode(200);
	}
	@Test
	public void Cancelar_Compra_Token_Ausente() {
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
  	.when()
  		.delete(URL + "/carrinhos/cancelar-compra")
  	.then()
  	    .log().all()
  	    .statusCode(401);
	}
	@Test
	public void Concluir_Compra_Token_Ausente() {
		
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
	  	.when()
	  		.delete(URL + "/carrinhos/concluir-compra")
	  	.then()
	  	    .log().all()
	  	    .statusCode(401);
		
	}
	
}

