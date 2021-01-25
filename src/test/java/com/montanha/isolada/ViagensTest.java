package com.montanha.isolada;

import com.montanha.config.Configuracoes;
import com.montanha.factory.DadosViagemDataFactory;
import com.montanha.factory.UsuarioDataFactory;
import com.montanha.pojo.Usuario;
import com.montanha.pojo.ViagemCadastrada;
import io.qameta.allure.*;
import io.qameta.allure.junit4.DisplayName;
import io.restassured.http.ContentType;
import org.aeonbits.owner.ConfigFactory;
import org.junit.Before;
import org.junit.Test;


import java.io.IOException;
import static io.restassured.RestAssured.*;
import static org.hamcrest.Matchers.*;

@Epic("Viagens Testes")
@Feature("Cadastro e Pesquisa de Viagens")
public class ViagensTest {
    private String token;
    private String tokenUsuario;

    @Before
    public void setUp() {
        // Configuracoes do Rest-Assured
        Configuracoes config = ConfigFactory.create(Configuracoes.class);
        baseURI = config.baseURI();
        port = config.port();
        basePath = config.basePath();

        Usuario usuarioAdmin = UsuarioDataFactory.criarUsuarioAdministrador();

        this.token = given()
            .contentType(ContentType.JSON)
            .body(usuarioAdmin)
        .when()
            .post(config.pathAuth())
        .then()
            .log()
                .all()
            .assertThat()
                .statusCode(200)
            .extract()
                .path("data.token");

        Usuario usuarioComum = UsuarioDataFactory.criarUsuarioComum();

        this.tokenUsuario = given()
            .contentType(ContentType.JSON)
            .body(usuarioComum)
        .when()
            .post(config.pathAuth())
        .then()
            .log()
                 .all()
            .assertThat()
                .statusCode(200)
            .extract()
             .path("data.token");
    }

    @Test
    @DisplayName("Realizar um cadastro com sucesso na regiao sul")
    @Description("Testes da tag descrição")
    @Severity(SeverityLevel.NORMAL)
    @Story("Cenario - Cadastro viagem validando acompanhante e regiao Sul - status code = 201")
    public void testRealizarUmCadastroComSucessoNaRegiaoSul() throws IOException {
        ViagemCadastrada dadosViagem = DadosViagemDataFactory.criarViagemCadastradaValidaRegiaoSul();

        given()
            .contentType(ContentType.JSON)
            .body(dadosViagem)
            .header("Authorization", token)
        .when()
            .post("v1/viagens")
        .then()
            .log().all()
            .assertThat()
                .statusCode(201)
            .body("data.acompanhante", equalToIgnoringCase("leandro"))
            .body("data.regiao", equalToIgnoringCase("sul"));
    }

    @Test
    @DisplayName("Fazendo login com um usuario invalido")
    @Severity(SeverityLevel.NORMAL)
    @Step("Login com user {0} and pass {1}")
    @Story("Cenario - Cadastro viagem validando acompanhante e regiao Norte - status code = 201")
    public void testRealizarUmCadastroComSucessoNaRegiaoNorte() throws IOException {
        ViagemCadastrada dadosViagem = DadosViagemDataFactory.criarViagemCadastradaValidaRegiaoNorte();

        given()
            .contentType(ContentType.JSON)
            .body(dadosViagem)
            .header("Authorization", token)
        .when()
            .post("v1/viagens")
        .then()
            .log().all()
            .assertThat()
                .statusCode(201)
            .body("data.acompanhante", equalToIgnoringCase("leandro"))
            .body("data.regiao", equalToIgnoringCase("norte"));
    }

    @Test
    @Step("step do teste regiao sudeste")
    @DisplayName("Criando um cadastro para regiao sudeste, validando dois parametros obrigatorios")
    @Description("Validando os parametros acompanhante e regiao no response body")
    @Story("Cenario - Cadastro viagem validando acompanhante e regiao Sudeste - status code = 201")
    @Severity(SeverityLevel.TRIVIAL)
    public void testRealizarUmCadastroComSucessoNaRegiaoSudeste() throws IOException {
        ViagemCadastrada dadosViagem = DadosViagemDataFactory.criarViagemCadastradaValidaRegiaoSudeste();

        given()
            .contentType(ContentType.JSON)
            .body(dadosViagem)
            .header("Authorization", token)
        .when()
            .post("v1/viagens")
        .then()
            .log().all()
            .assertThat()
                .statusCode(201)
            .body("data.acompanhante", equalToIgnoringCase("leandro"))
            .body("data.regiao", equalToIgnoringCase("sudeste"));
    }

    @Test
    @Step("Dado que realizo cadastro // Quando nao informo campo obrigatorio // Entao recebo status code = 400")
    @DisplayName("Realizar Cadastro de uma viagem sem Local de destino")
    @Description("O teste deve ter o status = 400 porque não possui parametro obrigatório")
    @Story("Cenario - Cadastro viagem nao passando passando obrigatorio - status code = 400")
    public void testRealizarUmCadastroDeViagemSemLocalDeDestino() throws IOException {
        ViagemCadastrada dadosViagem = DadosViagemDataFactory.criarViagemSemLocalDeDestino();

        given()
            .contentType(ContentType.JSON)
            .body(dadosViagem)
            .header("Authorization", token)
        .when()
            .post("v1/viagens")
        .then()
            .log().all()
            .assertThat()
                .statusCode(400);
    }

    @Test
    @DisplayName("Pesquisar uma viagem que contem região = Sudeste")
    @Story("Cenario - Pesquisar viagem regiao sudeste - status code = 200")
    public void testRealizarPesquisaDeUmaViagemCadastradaEReceberCode200() {
        given()
            .header("Authorization", tokenUsuario)
        .when()
            .get("v1/viagens/1")
        .then()
            .log().all()
            .assertThat()
                .statusCode(200)
                .body("data.regiao", equalTo("Sudeste"))
                .body("data.regiao", equalToIgnoringCase("sudeste"));
    }

//    @Test
//    public void testRealizarPesquisaDeViagemEValidarATemperaturaMockable() {
//        given()
//                .header("Authorization", tokenUsuario)
//                .when()
//                .get("v1/viagens/1")
//                .then()
//                .log().all()
//                .assertThat()
//                .statusCode(200)
//                .body("data.regiao", equalTo("Sudeste"))
//                .body("data.temperatura", equalTo(44.99f));
//    }

//    @Test
//    public void testRealizarPesquisaDeViagemUtilizandoMounteBank() {
//        given()
//                .header("Authorization", tokenUsuario)
//                .when()
//                .get("v1/viagens/12")
//                .then()
//                .log().all()
//                .assertThat()
//                .statusCode(200);
////                .body("data.regiao", equalTo("Sudeste"))
////                .body("data.temperatura", equalTo(44.99f));
//    }
}
