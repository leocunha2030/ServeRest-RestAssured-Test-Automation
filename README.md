# Testes Automatizados - README 💻

Este repositório contém testes automatizados para validar as funcionalidade da Api serveRest.

## Pré-requisitos

Antes de executar os testes, certifique-se de ter o seguinte instalado:

- [Java JDK](https://www.oracle.com/java/technologies/javase-downloads.html) (versão recomendada: 8 ou superior)
- [Maven](https://maven.apache.org/download.cgi) (para gerenciamento de dependências)

Além de estar rodando a Api da branch Api-Sicredi em localhost.

- As instruções para a execução da api estão disponibilizadas junto com ela.

## Configuração ⚙️

1. Clone este repositório.

2. Navegue para o diretório do projeto:  cd seu-repositorio

#  Executando os Testes 🧪

- Para executar os testes automatizados, siga estas etapas:

1. Abra um terminal( Windows + R ) e navegue até o diretório do projeto:

    cd caminho/para/seu-repositorio

2. Execute o comando: 

    mvn clean test

3. Em seguida execute o proximo comando:

    mvn allure:report

4. E por ultimo execute:

    mvn allure:serve

--------------------------------------------------------------------

# O relatório de testes apareceu em branco no Allure? 📃

## Realize os passos a seguir: ⬇️

- No mesmo diretório de antes execute os comandos a seguir em ordem

1. mvn clean test

2. allure generate

3. allure open

--------------------------------------------------------------

## Tentou rodar novamente e recebeu esta mensagem? ♻️

    Allure: Target directory C:\Users\leu\Desktop\JavaRestAssured\ServeRest\allure-report for the report is already in use, add a '--clean' option to overwrite

- Realize os passos a seguir:

1. mvn clean test

2. allure generate --clean

3. allure open

-----------------------------------------------------------



