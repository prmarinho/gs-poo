package br.com.territoryinsights.model;

import java.util.ArrayList;

/**
 * Contrato que todo sensor do território precisa cumprir.
 * Quem implementa deve simular a leitura de cada hora, guardar só as leituras
 * válidas no histórico, descartar as leituras com falha e saber dizer se um
 * valor passou do limite de alerta.
 */
public interface Sensor {

    double CHANCE_DE_FALHA = 0.02;
    double VALOR_DE_FALHA = -1;
    int MAXIMO_HISTORICO = 48;

    /** Faz a leitura da hora informada (0 a 23) e devolve o valor lido. */
    double lerValor(int hora);

    /** Diz se a última leitura funcionou (true) ou falhou (false). */
    boolean verificarFuncionamento();

    /** O que o sensor mede, por exemplo "Temperatura do ar". */
    String getTipo();

    /** Unidade de medida, por exemplo "°C". */
    String getUnidade();

    /** Valor a partir do qual a leitura é considerada risco. */
    double getLimiteAlerta();

    /** Troca o limite de alerta. O menu só chama depois que o código de acesso foi conferido. */
    void setLimiteAlerta(double limite);

    /** Diz se o valor informado está acima do limite de alerta. */
    boolean passouDoLimite(double valor);

    /** Cópia das últimas leituras válidas, da mais antiga para a mais recente. */
    ArrayList<Double> getHistorico();

    /** Valor da última leitura, ou VALOR_DE_FALHA se o sensor falhou. */
    double getUltimaLeitura();

    /** O que a comunidade pode fazer hoje quando este sensor indica risco. */
    String getRecomendacao();
}
