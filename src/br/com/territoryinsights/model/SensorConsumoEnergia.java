package br.com.territoryinsights.model;

import java.util.ArrayList;

public class SensorConsumoEnergia implements Sensor {

    private static final double VARIACAO_MAXIMA = 0.08;

    private double[] perfilHorario = {
            1.35, 1.20, 1.10, 1.05, 1.03, 1.08,
            1.22, 1.38, 1.48, 1.58, 1.68, 1.78,
            1.88, 1.97, 2.06, 2.12, 2.15, 2.22,
            2.35, 2.48, 2.50, 2.32, 1.98, 1.62
    };

    private ArrayList<Double> historico = new ArrayList<>();
    private double limiteAlerta;
    private double ultimaLeitura;
    private boolean funcionando = true;

    public SensorConsumoEnergia(double limiteAlerta) {
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
        return "Consumo de energia";
    }

    @Override
    public String getUnidade() {
        return "MWh";
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
        return "Divulgar campanha de economia (ar-condicionado em 23 °C, desligar aparelhos da tomada)"
                + " e combinar com comércio e serviços públicos para reduzir o consumo no horário de pico.";
    }
}
