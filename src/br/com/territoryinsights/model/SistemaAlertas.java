package br.com.territoryinsights.model;

import java.util.ArrayList;

public class SistemaAlertas {

    private static final double FATOR_ATENCAO = 0.9;
    private static final double FATOR_CRITICO = 1.1;

    private ArrayList<Alerta> historico = new ArrayList<>();

    public NivelAlerta classificar(double valorPrevisto, double limite) {
        if (valorPrevisto >= limite * FATOR_CRITICO) {
            return NivelAlerta.CRITICO;
        }
        if (valorPrevisto > limite) {
            return NivelAlerta.ALERTA;
        }
        if (valorPrevisto >= limite * FATOR_ATENCAO) {
            return NivelAlerta.ATENCAO;
        }
        return NivelAlerta.NORMAL;
    }

    public Alerta avaliar(Sensor sensor, ArrayList<EstrategiaPrevisao> estrategias, String horario) {
        ArrayList<Double> leituras = sensor.getHistorico();
        NivelAlerta piorNivel = NivelAlerta.NORMAL;
        double piorValor = 0;
        String piorEstrategia = "";
        int usadas = 0;
        int comRisco = 0;

        for (EstrategiaPrevisao estrategia : estrategias) {
            if (!estrategia.temDadosSuficientes(leituras)) {
                continue;
            }
            usadas++;
            double previsto = estrategia.prever(leituras);
            NivelAlerta nivel = this.classificar(previsto, sensor.getLimiteAlerta());
            if (nivel != NivelAlerta.NORMAL) {
                comRisco++;
            }
            if (nivel.ordinal() > piorNivel.ordinal()) {
                piorNivel = nivel;
                piorValor = previsto;
                piorEstrategia = estrategia.getNome();
            }
        }

        if (piorNivel == NivelAlerta.NORMAL) {
            return null;
        }
        Alerta alerta = new Alerta(piorNivel, sensor, piorEstrategia, piorValor, horario, comRisco, usadas);
        this.historico.add(alerta);
        return alerta;
    }

    public Alerta buscarMaisGrave(ArrayList<Alerta> alertas) {
        Alerta maisGrave = null;
        for (Alerta alerta : alertas) {
            if (maisGrave == null || alerta.getNivel().ordinal() > maisGrave.getNivel().ordinal()) {
                maisGrave = alerta;
            }
        }
        return maisGrave;
    }

    public int contarPorNivel(NivelAlerta nivel) {
        int quantidade = 0;
        for (Alerta alerta : this.historico) {
            if (alerta.getNivel() == nivel) {
                quantidade++;
            }
        }
        return quantidade;
    }

    public ArrayList<Alerta> getHistorico() {
        return this.historico;
    }
}
