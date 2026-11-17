package br.com.territoryinsights.model;

public class DadosTerritorio {

    private static final int TAMANHO_MINIMO_REGIAO = 3;
    private static final int POPULACAO_MAXIMA = 20000000;
    private static final int TAMANHO_MINIMO_CODIGO = 4;
    private static final double TEMPERATURA_MINIMA = 25;
    private static final double TEMPERATURA_MAXIMA = 50;
    private static final double CARGA_MINIMA = 50;
    private static final double CARGA_MAXIMA = 120;
    private static final double CONSUMO_MINIMO = 0.5;
    private static final double CONSUMO_MAXIMO = 10;

    private String regiao;
    private int populacao;
    private int populacaoRisco;
    private String codigoAcesso;
    private double limiteTemperatura;
    private double limiteCarga;
    private double limiteConsumo;

    public DadosTerritorio(String regiao, int populacao, int populacaoRisco, String codigoAcesso) {
        this.setRegiao(regiao);
        this.setPopulacao(populacao);
        this.setPopulacaoRisco(populacaoRisco);
        this.codigoAcesso = codigoAcesso;
        this.limiteTemperatura = 35.0;
        this.limiteCarga = 90.0;
        this.limiteConsumo = 2.3;
    }

    public boolean setRegiao(String regiao) {
        if (regiao == null || regiao.trim().length() < TAMANHO_MINIMO_REGIAO) {
            System.out.println("Erro: o nome da região precisa ter pelo menos " + TAMANHO_MINIMO_REGIAO + " caracteres.");
            return false;
        }
        this.regiao = regiao.trim();
        return true;
    }

    public boolean setPopulacao(int populacao) {
        if (populacao <= 0) {
            System.out.println("Erro: a população precisa ser maior que zero.");
            return false;
        }
        if (populacao > POPULACAO_MAXIMA) {
            System.out.println("Erro: população acima de " + POPULACAO_MAXIMA + ". Confira se digitou certo.");
            return false;
        }
        if (populacao < this.populacaoRisco) {
            System.out.println("Erro: a população não pode ser menor que a população em grupo de risco ("
                    + this.populacaoRisco + ").");
            return false;
        }
        this.populacao = populacao;
        return true;
    }

    public boolean setPopulacaoRisco(int populacaoRisco) {
        if (populacaoRisco < 0) {
            System.out.println("Erro: a população em grupo de risco não pode ser negativa.");
            return false;
        }
        if (populacaoRisco > this.populacao) {
            System.out.println("Erro: a população em grupo de risco não pode ser maior que a população total ("
                    + this.populacao + ").");
            return false;
        }
        this.populacaoRisco = populacaoRisco;
        return true;
    }

    public boolean setLimiteTemperatura(double limite, String codigo) {
        if (!this.codigoCorreto(codigo)) {
            return false;
        }
        if (limite < TEMPERATURA_MINIMA || limite > TEMPERATURA_MAXIMA) {
            System.out.printf("Erro: o limite de temperatura deve ficar entre %.1f e %.1f °C.%n",
                    TEMPERATURA_MINIMA, TEMPERATURA_MAXIMA);
            return false;
        }
        this.limiteTemperatura = limite;
        return true;
    }

    public boolean setLimiteCarga(double limite, String codigo) {
        if (!this.codigoCorreto(codigo)) {
            return false;
        }
        if (limite < CARGA_MINIMA || limite > CARGA_MAXIMA) {
            System.out.printf("Erro: o limite de carga da rede deve ficar entre %.1f e %.1f %%.%n",
                    CARGA_MINIMA, CARGA_MAXIMA);
            return false;
        }
        this.limiteCarga = limite;
        return true;
    }

    public boolean setLimiteConsumo(double limite, String codigo) {
        if (!this.codigoCorreto(codigo)) {
            return false;
        }
        if (limite < CONSUMO_MINIMO || limite > CONSUMO_MAXIMO) {
            System.out.printf("Erro: o limite de consumo deve ficar entre %.1f e %.1f MWh.%n",
                    CONSUMO_MINIMO, CONSUMO_MAXIMO);
            return false;
        }
        this.limiteConsumo = limite;
        return true;
    }

    public boolean alterarCodigo(String codigoAtual, String novoCodigo) {
        if (!this.codigoCorreto(codigoAtual)) {
            return false;
        }
        if (novoCodigo == null || novoCodigo.length() < TAMANHO_MINIMO_CODIGO) {
            System.out.println("Erro: o novo código precisa ter pelo menos " + TAMANHO_MINIMO_CODIGO + " caracteres.");
            return false;
        }
        this.codigoAcesso = novoCodigo;
        return true;
    }

    private boolean codigoCorreto(String codigo) {
        if (!this.codigoAcesso.equals(codigo)) {
            System.out.println("Acesso negado: código incorreto.");
            return false;
        }
        return true;
    }

    public String getRegiao() {
        return this.regiao;
    }

    public int getPopulacao() {
        return this.populacao;
    }

    public int getPopulacaoRisco() {
        return this.populacaoRisco;
    }

    public double getLimiteTemperatura() {
        return this.limiteTemperatura;
    }

    public double getLimiteCarga() {
        return this.limiteCarga;
    }

    public double getLimiteConsumo() {
        return this.limiteConsumo;
    }

    public void exibir() {
        System.out.println("Região: " + this.regiao);
        System.out.println("População: " + this.populacao + " habitantes (" + this.populacaoRisco
                + " em grupo de risco ao calor)");
        System.out.printf("Limites: temperatura %.1f °C | carga da rede %.1f %% | consumo %.2f MWh%n",
                this.limiteTemperatura, this.limiteCarga, this.limiteConsumo);
        System.out.println("Código de acesso: ****");
    }
}
