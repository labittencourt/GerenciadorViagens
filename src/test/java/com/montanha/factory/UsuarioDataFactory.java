package com.montanha.factory;

import com.montanha.pojo.Usuario;

public class UsuarioDataFactory {
    public static Usuario criarUsuarioAdministrador() {
        Usuario usuarioAdmin = new Usuario();
        usuarioAdmin.setEmail("admin@email.com");
        usuarioAdmin.setSenha("654321");
        return usuarioAdmin;
    }

    public static Usuario criarUsuarioComum() {
        Usuario usuarioComum = new Usuario();
        usuarioComum.setEmail("usuario@email.com");
        usuarioComum.setSenha("123456");
        return usuarioComum;
    }
}
