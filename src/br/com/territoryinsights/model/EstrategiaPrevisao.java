package br.com.territoryinsights.model;

import java.util.ArrayList;

public abstract class EstrategiaPrevisao {

    private String nome;
    private int minimoLeituras;
    private String explicacao = "";

    public EstrategiaPrevisao(String nome, int minimoLeituras) {
        this.nome = nome;
        this.minimoLeituras = minimoLeituras;
    }

    /**
     * Prevê o valor da próxima leitura a partir das leituras guardadas.
     * É abstrato porque cada estratégia faz a conta de um jeito diferente.
     * Quem chama deve conferir antes se temDadosSuficientes() é true.
     */
    public abstract double prever(ArrayList<Double> leituras);

    public boolean temDadosSuficientes(ArrayList<Double> leituras) {
        return leituras.size() >= this.minimoLeituras;
    }

    protected double calcularMedia(ArrayList<Double> leituras, int inicio, int fim) {
        double soma = 0;
        for (int i = inicio; i < fim; i++) {
            soma += leituras.get(i);
        }
        return soma / (fim - inicio);
    }

    public String descrever() {
        return this.nome + " (precisa de pelo menos " + this.minimoLeituras + " leituras)";
    }

    public String getNome() {
        return this.nome;
    }

    public int getMinimoLeituras() {
        return this.minimoLeituras;
    }

    public String getExplicacao() {
        return this.explicacao;
    }

    protected void setExplicacao(String explicacao) {
        this.explicacao = explicacao;
    }
}
