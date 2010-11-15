package br.uff.ic.gardener.diff;

import java.io.File;

public class MainDiff {

    public static void main(String[] args) {

        File file1 = new File("C:/Users/alvaro/Documents/Mestrado - UFF/4o Periodo/Laboratório de Gerência de Configuração/Diretorio teste");
        File file2 = new File("C:/Users/alvaro/Documents/Mestrado - UFF/4o Periodo/Laboratório de Gerência de Configuração/Diretorio teste 2");

        Diff diff = new Diff(file1, file2, 'f');
        diff.setOutputFormat();

    }
}
