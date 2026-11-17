package br.com.territoryinsights.model;

import java.util.ArrayList;

public class EstacaoDeColeta extends ComponenteTerritorial {

    private static final int HORAS_DO_DIA = 24;

    private ArrayList<Sensor> sensoresInstalados = new ArrayList<>();
    private int dia = 1;
    private int hora;
    private String ultimaColeta = "nenhuma";
    private int totalColetas = 0;
    private int leiturasComFalha = 0;

    public EstacaoDeColeta(String id, String nome, String regiao, int horaInicial) {
        super(id, nome, regiao);
        this.hora = horaInicial;
    }

    public void instalarSensor(Sensor sensor) {
        this.sensoresInstalados.add(sensor);
    }

    public boolean coletarLeituras() {
        if (!this.isAtivo()) {
            return false;
        }
        for (Sensor sensor : this.sensoresInstalados) {
            sensor.lerValor(this.hora);
            if (!sensor.verificarFuncionamento()) {
                this.leiturasComFalha++;
            }
        }
        this.ultimaColeta = this.getProximoHorario();
        this.totalColetas++;
        this.hora++;
        if (this.hora == HORAS_DO_DIA) {
            this.hora = 0;
            this.dia++;
        }
        return true;
    }

    public String getProximoHorario() {
        return String.format("Dia %d %02dh", this.dia, this.hora);
    }

    public String getUltimaColeta() {
        return this.ultimaColeta;
    }

    public ArrayList<Sensor> getSensoresInstalados() {
        return this.sensoresInstalados;
    }

    @Override
    public void verificarStatus() {
        System.out.println("[" + this.getId() + "] " + this.getNome() + " - " + this.getStatus());
        if (!this.isAtivo()) {
            System.out.println("   Estação desligada: nenhuma leitura nova está sendo feita.");
            return;
        }
        int funcionando = 0;
        for (Sensor sensor : this.sensoresInstalados) {
            if (sensor.verificarFuncionamento()) {
                funcionando++;
            }
        }
        System.out.println("   Sensores funcionando: " + funcionando + " de " + this.sensoresInstalados.size());
        System.out.println("   Coletas feitas: " + this.totalColetas + " | leituras com falha: " + this.leiturasComFalha);
        System.out.println("   Última coleta: " + this.ultimaColeta);
    }
}
