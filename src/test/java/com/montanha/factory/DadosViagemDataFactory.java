package com.montanha.factory;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.montanha.pojo.ViagemCadastrada;

import java.io.FileInputStream;
import java.io.IOException;

public class DadosViagemDataFactory {

    public static ViagemCadastrada criarViagem() throws IOException {
        ObjectMapper objectMapper = new ObjectMapper();
        ViagemCadastrada dadosViagem = objectMapper.readValue(new FileInputStream("src/test/resources/requestBody/post_v1_viagens.json"), ViagemCadastrada.class);
        return dadosViagem;
    }

    public static ViagemCadastrada criarViagemCadastradaValidaRegiaoSul() throws IOException {
        ViagemCadastrada dadosViagem = criarViagem();
        dadosViagem.setLocalDeDestino("Santa Catarina");
        dadosViagem.setRegiao("Sul");
        return dadosViagem;
    }

    public static ViagemCadastrada criarViagemCadastradaValidaRegiaoNorte() throws IOException {
        ViagemCadastrada dadosViagem = criarViagem();
        dadosViagem.setLocalDeDestino("Manaus");
        dadosViagem.setRegiao("Norte");
        return dadosViagem;
    }

    public static ViagemCadastrada criarViagemCadastradaValidaRegiaoSudeste() throws IOException {
        ViagemCadastrada dadosViagem = criarViagem();
        dadosViagem.setLocalDeDestino("Uberlandia");
        dadosViagem.setRegiao("Sudeste");
        return dadosViagem;
    }

    public static ViagemCadastrada criarViagemSemLocalDeDestino() throws IOException {
        ViagemCadastrada dadosViagem = criarViagem();
        dadosViagem.setLocalDeDestino("");
        return dadosViagem;
    }
}
