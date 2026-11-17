package br.com.territoryinsights.model;

import java.util.ArrayList;

public class PrevisaoMediaMovel extends EstrategiaPrevisao {

    private int tamanhoJanela;

    public PrevisaoMediaMovel(int tamanhoJanela) {
        super("Média Móvel", tamanhoJanela);
        this.tamanhoJanela = tamanhoJanela;
    }

    @Override
    public double prever(ArrayList<Double> leituras) {
        int total = leituras.size();
        double media = this.calcularMedia(leituras, total - this.tamanhoJanela, total);
        this.setExplicacao(String.format("A média das últimas %d leituras é %.2f. Se o comportamento continuar"
                + " parecido, a próxima leitura deve ficar perto desse valor.", this.tamanhoJanela, media));
        return media;
    }

    @Override
    public String descrever() {
        return super.descrever() + " - usa as últimas " + this.tamanhoJanela + " leituras";
    }

    public int getTamanhoJanela() {
        return this.tamanhoJanela;
    }
}
