package br.com.territoryinsights.model;

import java.util.ArrayList;

public class SensorTemperatura implements Sensor {

    private static final double VARIACAO_MAXIMA = 1.0;

    private double[] perfilHorario = {
            26.5, 26.0, 25.5, 25.0, 24.6, 24.3,
            24.8, 26.2, 28.3, 30.2, 31.8, 33.2,
            34.4, 35.4, 36.2, 36.6, 36.2, 35.2,
            33.6, 32.0, 30.6, 29.4, 28.4, 27.4
    };

    private ArrayList<Double> historico = new ArrayList<>();
    private double limiteAlerta;
    private double ultimaLeitura;
    private boolean funcionando = true;

    public SensorTemperatura(double limiteAlerta) {
        this.limiteAlerta = limiteAlerta;
    }

    @Override
    public double lerValor(int hora) {
        if (Math.random() < Sensor.CHANCE_DE_FALHA) {
            this.funcionando = false;
            this.ultimaLeitura = Sensor.VALOR_DE_FALHA;
            return this.ultimaLeitura;
        }
        this.funcionando = true;
        double variacao = (Math.random() * 2 - 1) * VARIACAO_MAXIMA;
        this.ultimaLeitura = this.perfilHorario[hora] + variacao;
        this.historico.add(this.ultimaLeitura);
        if (this.historico.size() > Sensor.MAXIMO_HISTORICO) {
            this.historico.remove(0);
        }
        return this.ultimaLeitura;
    }

    @Override
    public boolean verificarFuncionamento() {
        return this.funcionando;
    }

    @Override
    public String getTipo() {
        return "Temperatura do ar";
    }

    @Override
    public String getUnidade() {
        return "°C";
    }

    @Override
    public double getLimiteAlerta() {
        return this.limiteAlerta;
    }

    @Override
    public void setLimiteAlerta(double limite) {
        this.limiteAlerta = limite;
    }

    @Override
    public boolean passouDoLimite(double valor) {
        return valor > this.limiteAlerta;
    }

    @Override
    public ArrayList<Double> getHistorico() {
        return new ArrayList<>(this.historico);
    }

    @Override
    public double getUltimaLeitura() {
        return this.ultimaLeitura;
    }

    @Override
    public String getRecomendacao() {
        return "Abrir pontos de hidratação e resfriamento (UBS, CRAS, escolas), avisar quem cuida de idosos"
                + " e crianças e evitar atividades ao ar livre entre 11h e 16h.";
    }
}
