package Old;
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
	public void CadastraUser() {
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
	public void CadastraUserRepetido() {

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
	public void CadastraUserEmBranco() {
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
	public void CadastraUserSenhaEmBranco() {
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
	@Test
	public void ListaUsers() {
			given()
	        .log().all()
	    .when()
	        .get(URL + "/usuarios")
	    .then()
	        .log().all()
	        .statusCode(200);
	}
	//Rota User concluida 
	//Rota Login
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
	//Rota Login concluida
	//Rota Produtos
	@Test
	public void CadastraProdutoSucesso() {
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
	public void CadastraProdutoRepetido() {
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
	public void CadastraProdutoTokenAusente() {
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
	public void CadastraProdutoNoAdmin() {
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
	public void DeletaProdutoNoCarrinho() {
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
	@Test
	public void ExcluiProdutoComSucesso() {
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
	public void TokenAusenteDeletaProduto() {
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
	public void ExcluirProdutoNoAdmin() {
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
	public void ListarProdutos() {
			given()
	    	.log().all()
	    .when()
	    	.get(URL + "/produtos")
	    .then()
	    	.log().all()
	    	.statusCode(200);
	}
	//Rota Produtos Concuilda
	//Rota Carrinhos
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
	public void CadastracomSucesso() {
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
	public void NaoepermitidomaisDeumCarrinho() {
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
	public void TokenAusente() {
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
	@Test
	public void ConcluirCompra() {
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
	public void CancelarCompra() {
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
	public void CancelarCompraTokenAusente() {
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
	public void ConcluirCompraTokenAusente() {
		
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
	//Rota Carrinhos concluida
}

