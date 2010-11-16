package br.uff.ic.gardener.database;

/**
 * Esta classe permite fornecer métodos para acesso ao servidor de
 * banco de dados do Sistema de Controle de Versão Gardener.
 * O Gardener é resultado de um trabalho da disciplina de Laboratório de
 * Gerencia de Configuração.
 *
 * Criação em 9 de Novembro de 2010.
 *
 * @author Alessandreia Marta de Oliveira
 *
 */
public class ConfigurationBD {
    private int    port;
    private String server;

    public ConfigurationBD(int port, String server) {
        this.setPort(port);
        this.setServer(server);
    }

    public void setPort(int port) {
        this.port = port;
    }

    public int getPort() {
        return this.port;
    }

    public void setServer(String server) {
        this.server = server;
    }

    public String getServer() {
        return this.server;
    }
}


//~ Formatted by Jindent --- http://www.jindent.com
