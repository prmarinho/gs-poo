package br.com.territoryinsights.model;

import java.util.ArrayList;

public class PrevisaoComparacao extends EstrategiaPrevisao {

    private static final int PERIODOS_COMPARADOS = 2;

    private int tamanhoPeriodo;

    public PrevisaoComparacao(int tamanhoPeriodo) {
        super("Comparação de períodos", tamanhoPeriodo * PERIODOS_COMPARADOS);
        this.tamanhoPeriodo = tamanhoPeriodo;
    }

    @Override
    public double prever(ArrayList<Double> leituras) {
        int total = leituras.size();
        double mediaAtual = this.calcularMedia(leituras, total - this.tamanhoPeriodo, total);
        double mediaAnterior = this.calcularMedia(leituras, total - this.tamanhoPeriodo * PERIODOS_COMPARADOS,
                total - this.tamanhoPeriodo);
        double tendencia = (mediaAtual - mediaAnterior) / this.tamanhoPeriodo;
        double ultimaLeitura = leituras.get(total - 1);
        double previsao = ultimaLeitura + tendencia;

        String direcao;
        if (tendencia > 0) {
            direcao = "subindo";
        } else if (tendencia < 0) {
            direcao = "caindo";
        } else {
            direcao = "estável";
        }

        this.setExplicacao(String.format("Média das últimas %d leituras: %.2f. Média das %d anteriores: %.2f."
                        + " O valor está %s cerca de %.2f por hora. Somando essa tendência à última leitura (%.2f),"
                        + " a próxima deve ser %.2f.",
                this.tamanhoPeriodo, mediaAtual, this.tamanhoPeriodo, mediaAnterior, direcao, Math.abs(tendencia),
                ultimaLeitura, previsao));
        return previsao;
    }

    @Override
    public String descrever() {
        return super.descrever() + " - compara períodos de " + this.tamanhoPeriodo + " leituras";
    }

    public int getTamanhoPeriodo() {
        return this.tamanhoPeriodo;
    }
}
