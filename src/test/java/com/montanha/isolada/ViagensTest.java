package com.montanha.isolada;

import com.montanha.config.Configuracoes;
import com.montanha.factory.DadosViagemDataFactory;
import com.montanha.factory.UsuarioDataFactory;
import com.montanha.pojo.Usuario;
import com.montanha.pojo.ViagemCadastrada;
import io.restassured.http.ContentType;
import org.aeonbits.owner.ConfigFactory;
import org.junit.Before;
import org.junit.Test;

import java.io.IOException;
import static io.restassured.module.jsv.JsonSchemaValidator.*;
import static io.restassured.RestAssured.*;
import static io.restassured.matcher.RestAssuredMatchers.*;
import static org.hamcrest.Matchers.*;

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

    @Test
    public void testRealizarPesquisaDeViagemEValidarATemperaturaMockable() {
        given()
                .header("Authorization", tokenUsuario)
                .when()
                .get("v1/viagens/1")
                .then()
                .log().all()
                .assertThat()
                .statusCode(200)
                .body("data.regiao", equalTo("Sudeste"))
                .body("data.temperatura", equalTo(44.99f));
    }

    @Test
    public void testRealizarPesquisaDeViagemUtilizandoMounteBank() {
        given()
                .header("Authorization", tokenUsuario)
                .when()
                .get("v1/viagens/12")
                .then()
                .log().all()
                .assertThat()
                .statusCode(200);
//                .body("data.regiao", equalTo("Sudeste"))
//                .body("data.temperatura", equalTo(44.99f));
    }
}
