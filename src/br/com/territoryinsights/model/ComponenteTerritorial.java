package br.com.territoryinsights.model;

public abstract class ComponenteTerritorial {

    private static final String STATUS_ATIVO = "ATIVO";
    private static final String STATUS_INATIVO = "INATIVO";

    private String id;
    private String nome;
    private String regiao;
    private String status;

    public ComponenteTerritorial(String id, String nome, String regiao) {
        this.id = id;
        this.nome = nome;
        this.regiao = regiao;
        this.status = STATUS_INATIVO;
    }

    public void ativar() {
        this.status = STATUS_ATIVO;
    }

    public void desativar() {
        this.status = STATUS_INATIVO;
    }

    public boolean isAtivo() {
        return this.status.equals(STATUS_ATIVO);
    }

    /**
     * Mostra o diagnóstico do componente no console.
     * É abstrato porque cada equipamento verifica coisas diferentes:
     * a estação confere os sensores e o painel confere as mensagens.
     */
    public abstract void verificarStatus();

    public String getId() {
        return this.id;
    }

    public String getNome() {
        return this.nome;
    }

    public String getRegiao() {
        return this.regiao;
    }

    public void setRegiao(String regiao) {
        this.regiao = regiao;
    }

    public String getStatus() {
        return this.status;
    }
}
