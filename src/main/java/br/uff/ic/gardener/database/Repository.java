package br.uff.ic.gardener.database;

/**
 * Esta classe permite fornecer métodos para acesso as informações
 * dos repositórios do Sistema de Controle de Versão Gardener.
 * O Gardener é resultado de um trabalho da disciplina de Laboratório de
 * Gerencia de Configuração.
 *
 * Criação em 9 de Novembro de 2010.
 *
 * @author Alessandreia Marta de Oliveira
 *
 */
public class Repository {
    private String repository;

    public Repository(String repository) {
        this.setRepository(repository);
    }

    public void setRepository(String repository) {
        this.repository = repository;
    }

    public String getRepository() {
        return this.repository;
    }
}


//~ Formatted by Jindent --- http://www.jindent.com
