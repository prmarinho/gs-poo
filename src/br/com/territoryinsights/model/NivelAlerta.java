package br.com.territoryinsights.model;

public enum NivelAlerta {

    NORMAL("NORMAL"),
    ATENCAO("ATENÇÃO"),
    ALERTA("ALERTA"),
    CRITICO("CRÍTICO");

    private String nome;

    NivelAlerta(String nome) {
        this.nome = nome;
    }

    public String getNome() {
        return this.nome;
    }
}
