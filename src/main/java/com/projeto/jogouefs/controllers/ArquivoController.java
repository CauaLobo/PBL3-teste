package com.projeto.jogouefs.controllers;


import com.projeto.jogouefs.model.Cidade;

import java.io.*;

/**
 * Responsável por salvar e carregar o estado do jogo em arquivos.
 */
public class ArquivoController {

    private static final String EXTENSAO = ".ser";

    /**
     * Salva o objeto Cidade inteiro em um arquivo.
     * @param cidade A cidade a ser salva.
     * @param nomeArquivo O nome do arquivo (sem extensão).
     * @throws IOException Se houver erro de escrita.
     */
    public void salvarJogo(Cidade cidade, String nomeArquivo) throws IOException {
        String caminhoCompleto = nomeArquivo + EXTENSAO;
        try (ObjectOutputStream oos = new ObjectOutputStream(new FileOutputStream(caminhoCompleto))) {
            oos.writeObject(cidade);
        }
    }

    /**
     * Carrega uma cidade de um arquivo salvo.
     * @param nomeArquivo O nome do arquivo a ser carregado.
     * @return O objeto Cidade recuperado.
     * @throws IOException Se houver erro de leitura.
     * @throws ClassNotFoundException Se a classe do objeto não for encontrada.
     */
    public Cidade carregarJogo(String nomeArquivo) throws IOException, ClassNotFoundException {
        String caminhoCompleto = nomeArquivo + EXTENSAO;
        try (ObjectInputStream ois = new ObjectInputStream(new FileInputStream(caminhoCompleto))) {
            return (Cidade) ois.readObject();
        }
    }
}
