package br.com.territoryinsights.model;

import java.util.ArrayList;

public class SensorCargaRede implements Sensor {

    private static final double VARIACAO_MAXIMA = 3.0;

    private double[] perfilHorario = {
            62, 57, 53, 51, 50, 52,
            57, 62, 66, 70, 74, 77,
            80, 83, 86, 87, 88, 90,
            92, 96, 97, 93, 82, 70
    };

    private ArrayList<Double> historico = new ArrayList<>();
    private double limiteAlerta;
    private double ultimaLeitura;
    private boolean funcionando = true;

    public SensorCargaRede(double limiteAlerta) {
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
        return "Carga da rede elétrica";
    }

    @Override
    public String getUnidade() {
        return "%";
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
        return "Avisar a distribuidora para aliviar o transformador, pedir para a comunidade evitar chuveiro"
                + " elétrico e ferro de passar entre 18h e 21h e garantir energia reserva para a UBS.";
    }
}
