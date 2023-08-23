# Plano de testes 🔬
-------------------------------------------------------------------
1. Objetivo: Verificar se as funcionalidade de cadastro do vendedor, login, produtos e carrinhos no Marketplace do ServeRest estão funcionando conforme o esperado.

2. Escopo: Testes de aceitação para as funcionalidades de cadastro do vendedor.

3. Cronograma:  10 dias úteis.

4. Recursos: Uma equipe de 1  testador, um ambiente de teste dedicado e ferramentas de automação de teste.

-------------------------------------------------------------------
#User Stories

    ##US 001: [API] Usuários👨
Sendo um vendedor de uma loja
Gostaria de poder me cadastrar no Marketplace do ServeRest
Para poder realizar as vendas dos meus produtos

- DoR

Banco de dados e infraestrutura para desenvolvimento disponibilizados;

Ambiente de testes disponibilizado.

- DoD

CRUD de cadastro de vendedores (usuários) implementado (CRIAR, ATUALIZAR, LISTAR E DELETAR);

Análise de testes cobrindo todos verbos;

Matriz de rastreabilidade atualizada;

Automação de testes baseado na análise realizada;

- Acceptance Criteria

Os vendedores (usuários) deverão possuir os campos NOME, E-MAIL, PASSWORD e ADMINISTRADOR;

Não deverá ser possível fazer ações e chamadas para usuários inexistentes;

Não deve ser possível criar um usuário com e-mail já utilizado;

Caso não seja encontrado usuário com o ID informado no PUT, um novo usuário deverá ser criado;

Não deve ser possível cadastrar usuário com e-mail já utilizado utilizando PUT;

Os testes executados deverão conter evidências;

Não deverá ser possível cadastrar usuários com e-mails de provedor gmail e hotmail;

Os e-mails devem seguir um padrão válido de e-mail para o cadastro;

As senhas devem possuír no mínimo 5 caracteres e no máximo 10 caracteres;



    ##US 002: [API] Login🆔

Sendo um vendedor de uma loja com cadastro já realizado
Gostaria de poder me autenticar no Marketplace da ServeRest
Para poder cadastrar, editar, atualizar e excluir meus produtos

- DoR

Banco de dados e infraestrutura para desenvolvimento disponibilizados;

API de cadastro de usuários implementada;

Ambiente de testes disponibilizado.

- DoD

Autenticação com geração de token Bearer implementada;

Análise de testes cobrindo a rota de login;

Matriz de rastreabilidade atualizada;

Automação de testes baseado na análise realizada;

- Acceptance Criteria

Usuários não cadastrados não deverão conseguir autenticar;

Usuários com senha inválida não deverão conseguir autenticar;

No caso de não autenticação, deverá ser retornado um status code 401 (Unauthorized);

Usuários existentes e com a senha correta deverão ser autenticados;

A autenticação deverá gerar um token Bearer;

A duração da validade do token deverá ser de 10 minutos;



    ##US 003: [API] Produtos🌽

Sendo um vendedor de uma loja com cadastro já realizado
Gostaria de poder me autenticar e cadastrar produtos no Marketplace do ServeRest
Para poder cadastrar, editar, atualizar e excluir meus produtos

- DoR

Banco de dados e infraestrutura para desenvolvimento disponibilizados;

API de cadastro de usuários implementada;

API de autenticação implementada;

Ambiente de testes disponibilizado.

- DoD

CRUD de cadastro de Produtos implementado (CRIAR, ATUALIZAR, LISTAR E DELETAR);

Análise de testes cobrindo a rota de produtos;

Matriz de rastreabilidade atualizada;

Automação de testes baseado na análise realizada;

- Acceptance Criteria

Usuários não autenticados não devem conseguir realizar ações na rota de Produtos;

Não deve ser possível realizar o cadastro de produtos com nomes já utilizados;

Não deve ser possível excluir produtos que estão dentro de carrinhos (dependência API Carrinhos);

Caso não exista produto com o o ID informado na hora do UPDATE, um novo produto deverá ser criado;

Produtos criados através do PUT não poderão ter nomes previamente cadastrados;

    ##US 004: [API] Carrinho🛒

Sendo um comprador de produtos do Marketplace da ServeRest 
desejo adicionar um produto ao meu carrinho e realizar uma compra.

- DoR

Banco de dados e infraestrutura para desenvolvimento disponibilizados;

Ambiente de testes disponibilizado.

- DoD

Análise de testes cobrindo todos verbos;

Matriz de rastreabilidade atualizada;

Automação de testes baseado na análise realizada;

- Acceptance Criteria

Não deve ser possivel possuir dois carrinhos no mesmo usuário

Os testes executados deverão conter evidências;


--------------------------------------------------------------------------------------

# Cenários de Teste: 🧪

    ##Rota Usuários:🧑‍🔧

CTU-01 Cadastro com Sucesso 

CTU-02 E-mail já Cadastrado 

CTU-03 Nome em branco 

CTU-04 Registro excluído com Sucesso 

CTU-05 Não é permitido excluir usuário com carrinho cadastrado

CTU-06 Alterado com sucesso

CTU-07 Cadastro com sucesso (Editar Usuário) 

CTU-08 E-mail já cadastrado(Editar Usuário) *Bug*

CTU-09 Listar Usuários

CTU-10 Cadastra com senha em Branco

-----------------------------------------------------

    ## Rota login:🧑‍🔧

CTL-01 Login Realizado com sucesso 

CTL-02 E-mail e/ou senha inválidos (Senha)

CTL-03 E-mail e/ou senha inválidos (E-mail)

CTL-04 Senha menor que 5 dígitos

CTL-05 Senha maior que 10 dígitos 

CTL-06 Senha em Branco

-----------------------------------------

    ##Rota produtos🧑‍🔧

CTP-01 Cadastro com Sucesso 

CTP-02 Já existe produto com esse nome 

CTP-03 Token ausente, inválido ou expirado 

CTP-04 Rota exclusiva para administradores 

CTP-05 Produto faz parte de carrinho(Deleta)

CTP-06 Alterado com Sucesso 

CTP-07 Cadastro com sucesso(Editar produto) 

CTP-08Token ausente, inválido ou expirado (Edita)

CTP-09 Rota exclusiva para administradores(Edita)

CTP-10 Registro excluído com sucesso | Nenhum registro excluído 

CTP-11 Token ausente, inválido ou expirado (Delete) 

CTP-12 Rota exclusiva para administradores (Deleta)

CTP-13 Listar Produtos

CTP-14 Edita Produto no Carrinho

----------------------------------------------

    ##Rota Carrinhos🧑‍🔧

CTC-01 Lista de carrinhos 

CTC-02 Cadastro com sucesso 

CTC-03 Não é permitido mais de um carrinho

CTC-04 Token ausente, inválido ou expirado 

CTC-05 Carrinho encontrado 

CTC-06 Carrinho não encontrado 

CTC-07 Registro excluído com sucesso | Não foi encontrado carrinho para esse usuário(Concluir Compra) 

CTC-08 Token ausente, inválido ou expirado(Cancelar compra) 

CTC-09 Registro excluído com sucesso | Não foi encontrado carrinho para esse usuário(Cancelar compra) 

CTC-10 Token ausente, inválido ou expirado(concluir compra)


-------------------------------------------------------------------

# Mapa Mental

<h1>![image](/uploads/b1bff0488aad692c64f681184ddb8bf6/image.png)</h1>
