package br.com.territoryinsights.model;

public class Alerta {

    private NivelAlerta nivel;
    private Sensor sensorMonitorado;
    private double limiteNoMomento;
    private String estrategia;
    private double valorPrevisto;
    private String horario;
    private int estrategiasComRisco;
    private int estrategiasUsadas;

    public Alerta(NivelAlerta nivel, Sensor sensorMonitorado, String estrategia, double valorPrevisto, String horario,
                  int estrategiasComRisco, int estrategiasUsadas) {
        this.nivel = nivel;
        this.sensorMonitorado = sensorMonitorado;
        this.limiteNoMomento = sensorMonitorado.getLimiteAlerta();
        this.estrategia = estrategia;
        this.valorPrevisto = valorPrevisto;
        this.horario = horario;
        this.estrategiasComRisco = estrategiasComRisco;
        this.estrategiasUsadas = estrategiasUsadas;
    }

    public void exibir(DadosTerritorio dadosTerritorio) {
        String borda;
        if (this.nivel == NivelAlerta.CRITICO) {
            borda = "############################################################";
        } else if (this.nivel == NivelAlerta.ALERTA) {
            borda = "============================================================";
        } else {
            borda = "------------------------------------------------------------";
        }

        String unidade = this.sensorMonitorado.getUnidade();
        double diferenca = this.valorPrevisto - this.limiteNoMomento;
        double percentual = this.valorPrevisto / this.limiteNoMomento * 100;

        System.out.println(borda);
        System.out.println(" " + this.nivel.getNome() + " - " + this.sensorMonitorado.getTipo());
        System.out.println(borda);
        System.out.printf(" Previsão: %.2f %s para %s (estratégia: %s)%n", this.valorPrevisto, unidade, this.horario,
                this.estrategia);
        System.out.printf(" Limite: %.2f %s%n", this.limiteNoMomento, unidade);
        if (diferenca > 0) {
            System.out.printf(" Motivo: a previsão passa do limite em %.2f %s (%.1f%% do limite)%n",
                    diferenca, unidade, percentual);
        } else {
            System.out.printf(" Motivo: a previsão está a %.2f %s do limite (%.1f%% do limite)%n",
                    -diferenca, unidade, percentual);
        }
        System.out.println(" Estratégias que indicam risco: " + this.estrategiasComRisco + " de " + this.estrategiasUsadas);
        if (this.estrategiasComRisco < this.estrategiasUsadas) {
            System.out.println(" As estratégias não concordam, então vale confirmar com as próximas leituras.");
        }

        if (this.nivel == NivelAlerta.CRITICO) {
            System.out.println(" Nível CRÍTICO: agir agora e avisar os responsáveis (Defesa Civil, distribuidora, UBS).");
        } else if (this.nivel == NivelAlerta.ALERTA) {
            System.out.println(" Nível ALERTA: começar hoje as ações preventivas.");
        } else {
            System.out.println(" Nível ATENÇÃO: acompanhar as próximas leituras e se preparar.");
        }

        System.out.println(" Pessoas na região: " + dadosTerritorio.getPopulacao() + " ("
                + dadosTerritorio.getPopulacaoRisco() + " em grupo de risco)");
        System.out.println(" O que fazer: " + this.sensorMonitorado.getRecomendacao());
        System.out.println(" Lembrete: o sistema só sugere, quem decide é a equipe responsável pelo território.");
        System.out.println(borda);
    }

    public String gerarResumo() {
        String unidade = this.sensorMonitorado.getUnidade();
        return String.format("%s - %s: previsto %.2f %s para %s (limite %.2f %s) [%s]",
                this.nivel.getNome(), this.sensorMonitorado.getTipo(), this.valorPrevisto, unidade, this.horario,
                this.limiteNoMomento, unidade, this.estrategia);
    }

    public String gerarMensagemPainel() {
        return "[" + this.nivel.getNome() + "] " + this.sensorMonitorado.getTipo() + " pode passar do limite em "
                + this.horario + ". " + this.sensorMonitorado.getRecomendacao();
    }

    public NivelAlerta getNivel() {
        return this.nivel;
    }

    public Sensor getSensorMonitorado() {
        return this.sensorMonitorado;
    }
}
