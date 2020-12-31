package com.montanha.isolada;

import com.montanha.factory.DadosViagemDataFactory;
import com.montanha.factory.UsuarioDataFactory;
import com.montanha.pojo.Usuario;
import com.montanha.pojo.ViagemCadastrada;
import io.restassured.http.ContentType;
import org.junit.Before;
import org.junit.Test;

import java.io.IOException;

import static io.restassured.RestAssured.*;
import static io.restassured.RestAssured.given;
import static io.restassured.module.jsv.JsonSchemaValidator.*;

public class ViagensContractTest {
    private String token;

    @Before
    public void setUp() {
        // Configuracoes do Rest-Assured
        baseURI = "http://localhost";
        port = 8089;
        basePath = "/api/";

        Usuario usuarioAdmin = UsuarioDataFactory.criarUsuarioAdministrador();

        this.token = given()
            .contentType(ContentType.JSON)
            .body(usuarioAdmin)
        .when()
            .post("v1/auth")
        .then()
            .log().all()
            .assertThat()
                .statusCode(200)
            .extract()
                .path("data.token");
    }

    @Test
    public void testValidarContratoCadastroViagem() throws IOException {
        ViagemCadastrada dadosViagem = DadosViagemDataFactory.criarViagem();

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
                .body(matchesJsonSchemaInClasspath("schemas/postV1ViagensViagemCadastrada.json"));
    }
}