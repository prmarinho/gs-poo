package br.com.territoryinsights.main;

import br.com.territoryinsights.model.Alerta;
import br.com.territoryinsights.model.ComponenteTerritorial;
import br.com.territoryinsights.model.DadosTerritorio;
import br.com.territoryinsights.model.EstacaoDeColeta;
import br.com.territoryinsights.model.EstrategiaPrevisao;
import br.com.territoryinsights.model.NivelAlerta;
import br.com.territoryinsights.model.PainelInformativo;
import br.com.territoryinsights.model.PrevisaoComparacao;
import br.com.territoryinsights.model.PrevisaoMediaMovel;
import br.com.territoryinsights.model.Sensor;
import br.com.territoryinsights.model.SensorCargaRede;
import br.com.territoryinsights.model.SensorConsumoEnergia;
import br.com.territoryinsights.model.SensorTemperatura;
import br.com.territoryinsights.model.SistemaAlertas;

import java.util.ArrayList;
import java.util.Scanner;

public class SistemaMonitoramento {

    private static final int HORAS_DO_DIA = 24;
    private static final int MAXIMO_HORAS_SIMULACAO = 48;
    private static final int QUANTIDADE_ULTIMOS_ALERTAS = 5;

    private static Scanner sc = new Scanner(System.in);

    private static DadosTerritorio dados;
    private static Sensor sensorTemperatura;
    private static Sensor sensorCarga;
    private static Sensor sensorConsumo;
    private static EstacaoDeColeta estacao;
    private static PainelInformativo painel;
    private static ArrayList<ComponenteTerritorial> componentes = new ArrayList<>();
    private static ArrayList<EstrategiaPrevisao> estrategias = new ArrayList<>();
    private static SistemaAlertas sistemaAlertas = new SistemaAlertas();

    public static void main(String[] args) {
        inicializar();

        System.out.println("==================================================");
        System.out.println(" TERRITORY PREDICTIVE INSIGHTS - Calor e Energia");
        System.out.println("==================================================");
        System.out.println("Monitoramento de ondas de calor e da rede elétrica da " + dados.getRegiao() + ".");
        System.out.println("O sistema começa sem leituras e só faz previsões depois de 5 leituras.");
        System.out.println("Dica: use a opção 6 para simular um dia inteiro.");

        int opcao = -1;
        while (opcao != 0) {
            mostrarMenu();
            opcao = lerInt("Escolha uma opção: ");
            switch (opcao) {
                case 1:
                    verificarSensores();
                    break;
                case 2:
                    rodarPrevisoes();
                    break;
                case 3:
                    gerenciarDados();
                    break;
                case 4:
                    simularLeituras();
                    break;
                case 5:
                    exibirStatus();
                    break;
                case 6:
                    simular24Horas();
                    break;
                case 7:
                    gerenciarComponentes();
                    break;
                case 0:
                    System.out.println("Saindo do sistema...");
                    break;
                default:
                    System.out.println("Opção inválida!");
            }
        }
    }

    private static void inicializar() {
        dados = new DadosTerritorio("Região Leste - Setor 3", 18500, 4200, "4321");

        sensorTemperatura = new SensorTemperatura(dados.getLimiteTemperatura());
        sensorCarga = new SensorCargaRede(dados.getLimiteCarga());
        sensorConsumo = new SensorConsumoEnergia(dados.getLimiteConsumo());

        estacao = new EstacaoDeColeta("EST-01", "Estação de Coleta Setor 3", dados.getRegiao(), 6);
        estacao.instalarSensor(sensorTemperatura);
        estacao.instalarSensor(sensorCarga);
        estacao.instalarSensor(sensorConsumo);

        painel = new PainelInformativo("PNL-01", "Painel Informativo da UBS", dados.getRegiao(),
                "entrada da UBS do Setor 3");

        componentes.add(estacao);
        componentes.add(painel);
        estacao.ativar();
        painel.ativar();

        estrategias.add(new PrevisaoMediaMovel(5));
        estrategias.add(new PrevisaoComparacao(3));
    }

    private static void mostrarMenu() {
        System.out.println("\n================== MENU ==================");
        System.out.println("Região: " + dados.getRegiao() + " | Próxima leitura: " + estacao.getProximoHorario());
        System.out.println("1 - Verificar sensores");
        System.out.println("2 - Rodar previsões e alertas");
        System.out.println("3 - Gerenciar dados do território");
        System.out.println("4 - Simular próximas leituras");
        System.out.println("5 - Exibir status completo");
        System.out.println("6 - Simular 24 horas");
        System.out.println("7 - Ativar/desativar componentes");
        System.out.println("0 - Sair");
    }

    private static void verificarSensores() {
        System.out.println("\n--- VERIFICAR SENSORES ---");
        if (!estacao.coletarLeituras()) {
            System.out.println("A estação está desativada, nenhuma leitura foi feita. Ative na opção 7.");
            return;
        }
        System.out.println("Leitura feita em " + estacao.getUltimaColeta());

        for (Sensor sensor : estacao.getSensoresInstalados()) {
            System.out.println("\n" + sensor.getTipo());
            if (!sensor.verificarFuncionamento()) {
                System.out.println("  FALHA na leitura! O valor foi descartado.");
            } else {
                System.out.printf("  Valor lido: %.2f %s | limite: %.2f %s%n", sensor.getUltimaLeitura(),
                        sensor.getUnidade(), sensor.getLimiteAlerta(), sensor.getUnidade());
                if (sensor.passouDoLimite(sensor.getUltimaLeitura())) {
                    System.out.println("  ACIMA DO LIMITE!");
                } else {
                    System.out.println("  Dentro do limite.");
                }
            }
            System.out.println("  Leituras guardadas: " + sensor.getHistorico().size());
        }
    }

    private static void rodarPrevisoes() {
        System.out.println("\n--- PREVISÕES PARA " + estacao.getProximoHorario() + " ---");
        if (!estacao.isAtivo()) {
            System.out.println("Aviso: a estação está desligada, as previsões usam só leituras antigas.");
        }

        for (Sensor sensor : estacao.getSensoresInstalados()) {
            ArrayList<Double> leituras = sensor.getHistorico();
            System.out.printf("%n%s (%d leituras, limite %.2f %s)%n", sensor.getTipo(), leituras.size(),
                    sensor.getLimiteAlerta(), sensor.getUnidade());

            for (EstrategiaPrevisao estrategia : estrategias) {
                if (estrategia.temDadosSuficientes(leituras)) {
                    double previsto = estrategia.prever(leituras);
                    NivelAlerta nivel = sistemaAlertas.classificar(previsto, sensor.getLimiteAlerta());
                    System.out.printf("  [%s] %.2f %s -> %s%n", estrategia.getNome(), previsto,
                            sensor.getUnidade(), nivel.getNome());
                    System.out.println("     " + estrategia.getExplicacao());
                } else {
                    System.out.println("  [" + estrategia.getNome() + "] sem previsão: tem " + leituras.size()
                            + " leituras e precisa de " + estrategia.getMinimoLeituras() + ".");
                }
            }
        }

        ArrayList<Alerta> alertas = gerarAlertas();
        if (alertas.isEmpty()) {
            if (podePrever()) {
                System.out.println("\nNenhum alerta para " + estacao.getProximoHorario() + ".");
            } else {
                System.out.println("\nSistema em silêncio: ainda não há leituras suficientes para prever.");
            }
            return;
        }

        System.out.println("\n--- ALERTAS ---");
        for (Alerta alerta : alertas) {
            alerta.exibir(dados);
        }
        painel.mostrarMensagem(sistemaAlertas.buscarMaisGrave(alertas).gerarMensagemPainel());
    }

    private static ArrayList<Alerta> gerarAlertas() {
        ArrayList<Alerta> lista = new ArrayList<>();
        for (Sensor sensor : estacao.getSensoresInstalados()) {
            Alerta alerta = sistemaAlertas.avaliar(sensor, estrategias, estacao.getProximoHorario());
            if (alerta != null) {
                lista.add(alerta);
            }
        }
        return lista;
    }

    private static boolean podePrever() {
        for (Sensor sensor : estacao.getSensoresInstalados()) {
            for (EstrategiaPrevisao estrategia : estrategias) {
                if (estrategia.temDadosSuficientes(sensor.getHistorico())) {
                    return true;
                }
            }
        }
        return false;
    }

    private static void gerenciarDados() {
        int opcao = -1;
        while (opcao != 0) {
            System.out.println("\n--- DADOS DO TERRITÓRIO ---");
            System.out.println("1 - Ver dados");
            System.out.println("2 - Alterar região");
            System.out.println("3 - Alterar população");
            System.out.println("4 - Alterar população em grupo de risco");
            System.out.println("5 - Alterar limite de alerta (precisa do código)");
            System.out.println("6 - Alterar código de acesso");
            System.out.println("0 - Voltar");
            opcao = lerInt("Escolha: ");

            switch (opcao) {
                case 1:
                    dados.exibir();
                    break;
                case 2:
                    String regiao = lerTexto("Novo nome da região: ");
                    if (dados.setRegiao(regiao)) {
                        for (ComponenteTerritorial componente : componentes) {
                            componente.setRegiao(dados.getRegiao());
                        }
                        System.out.println("Região alterada.");
                    } else {
                        System.out.println("Valor mantido: " + dados.getRegiao());
                    }
                    break;
                case 3:
                    int populacao = lerInt("Nova população: ");
                    if (dados.setPopulacao(populacao)) {
                        System.out.println("População alterada.");
                    } else {
                        System.out.println("Valor mantido: " + dados.getPopulacao());
                    }
                    break;
                case 4:
                    int risco = lerInt("Nova população em grupo de risco: ");
                    if (dados.setPopulacaoRisco(risco)) {
                        System.out.println("População em grupo de risco alterada.");
                    } else {
                        System.out.println("Valor mantido: " + dados.getPopulacaoRisco());
                    }
                    break;
                case 5:
                    alterarLimite();
                    break;
                case 6:
                    alterarCodigo();
                    break;
                case 0:
                    break;
                default:
                    System.out.println("Opção inválida!");
            }
        }
    }

    private static void alterarLimite() {
        System.out.printf("1 - Temperatura (atual %.1f °C, permitido de 25 a 50)%n", dados.getLimiteTemperatura());
        System.out.printf("2 - Carga da rede (atual %.1f %%, permitido de 50 a 120)%n", dados.getLimiteCarga());
        System.out.printf("3 - Consumo de energia (atual %.2f MWh, permitido de 0,5 a 10)%n", dados.getLimiteConsumo());
        int tipo = lerInt("Qual limite deseja alterar? ");
        if (tipo < 1 || tipo > 3) {
            System.out.println("Opção inválida!");
            return;
        }
        double valor = lerDouble("Novo limite: ");
        String codigo = lerTexto("Código de acesso: ");

        boolean alterou;
        if (tipo == 1) {
            alterou = dados.setLimiteTemperatura(valor, codigo);
        } else if (tipo == 2) {
            alterou = dados.setLimiteCarga(valor, codigo);
        } else {
            alterou = dados.setLimiteConsumo(valor, codigo);
        }

        if (alterou) {
            atualizarLimitesSensores();
            System.out.println("Limite alterado com sucesso.");
        } else {
            System.out.println("Nenhum limite foi alterado.");
        }
    }

    private static void atualizarLimitesSensores() {
        sensorTemperatura.setLimiteAlerta(dados.getLimiteTemperatura());
        sensorCarga.setLimiteAlerta(dados.getLimiteCarga());
        sensorConsumo.setLimiteAlerta(dados.getLimiteConsumo());
    }

    private static void alterarCodigo() {
        String atual = lerTexto("Código atual: ");
        String novo = lerTexto("Novo código: ");
        String confirmacao = lerTexto("Confirme o novo código: ");
        if (!novo.equals(confirmacao)) {
            System.out.println("Os códigos não são iguais. Código mantido.");
            return;
        }
        if (dados.alterarCodigo(atual, novo)) {
            System.out.println("Código alterado com sucesso.");
        } else {
            System.out.println("Código mantido.");
        }
    }

    private static void simularLeituras() {
        System.out.println("\n--- SIMULAR PRÓXIMAS LEITURAS ---");
        if (!estacao.isAtivo()) {
            System.out.println("A estação está desativada. Ative na opção 7.");
            return;
        }
        int horas = lerInt("Quantas horas deseja simular (1 a " + MAXIMO_HORAS_SIMULACAO + ")? ");
        if (horas < 1 || horas > MAXIMO_HORAS_SIMULACAO) {
            System.out.println("Quantidade inválida!");
            return;
        }

        imprimirCabecalho();
        for (int i = 0; i < horas; i++) {
            estacao.coletarLeituras();
            imprimirLinha();
        }
        System.out.println("(* = acima do limite | FALHA = leitura descartada)");

        System.out.println("\nVerificação automática para " + estacao.getProximoHorario() + ":");
        ArrayList<Alerta> alertas = gerarAlertas();
        if (alertas.isEmpty()) {
            if (podePrever()) {
                System.out.println("  Nenhum alerta.");
            } else {
                System.out.println("  Sem previsão: ainda há poucas leituras.");
            }
        } else {
            for (Alerta alerta : alertas) {
                System.out.println("  " + alerta.gerarResumo());
            }
            System.out.println("  Use a opção 2 para ver a explicação de cada alerta.");
        }
    }

    private static void exibirStatus() {
        System.out.println("\n--- STATUS COMPLETO ---");

        System.out.println("\n[Território]");
        dados.exibir();

        System.out.println("\n[Componentes]");
        for (ComponenteTerritorial componente : componentes) {
            componente.verificarStatus();
        }

        System.out.println("\n[Sensores]");
        for (Sensor sensor : estacao.getSensoresInstalados()) {
            ArrayList<Double> historico = sensor.getHistorico();
            System.out.printf("%s (limite %.2f %s): ", sensor.getTipo(), sensor.getLimiteAlerta(), sensor.getUnidade());
            if (historico.isEmpty()) {
                System.out.println("sem leituras");
            } else {
                System.out.printf("última leitura válida %.2f %s, %d leituras guardadas%n",
                        historico.get(historico.size() - 1), sensor.getUnidade(), historico.size());
            }
        }

        System.out.println("\n[Estratégias de previsão]");
        for (EstrategiaPrevisao estrategia : estrategias) {
            System.out.println("- " + estrategia.descrever());
        }

        System.out.println("\n[Alertas]");
        ArrayList<Alerta> historico = sistemaAlertas.getHistorico();
        System.out.println("Total: " + historico.size()
                + " | ATENÇÃO: " + sistemaAlertas.contarPorNivel(NivelAlerta.ATENCAO)
                + " | ALERTA: " + sistemaAlertas.contarPorNivel(NivelAlerta.ALERTA)
                + " | CRÍTICO: " + sistemaAlertas.contarPorNivel(NivelAlerta.CRITICO));
        if (historico.isEmpty()) {
            System.out.println("Nenhum alerta até agora.");
        } else {
            System.out.println("Últimos alertas:");
            int inicio = Math.max(0, historico.size() - QUANTIDADE_ULTIMOS_ALERTAS);
            for (int i = inicio; i < historico.size(); i++) {
                System.out.println("  " + historico.get(i).gerarResumo());
            }
        }
    }

    private static void simular24Horas() {
        System.out.println("\n--- SIMULAÇÃO DE 24 HORAS ---");
        if (!estacao.isAtivo()) {
            System.out.println("A estação está desativada. Ative na opção 7.");
            return;
        }
        System.out.println("A cada hora a estação faz uma leitura, o sistema prevê a hora seguinte e verifica os alertas.\n");

        imprimirCabecalho();
        ArrayList<Alerta> alertasDoDia = new ArrayList<>();
        for (int i = 0; i < HORAS_DO_DIA; i++) {
            estacao.coletarLeituras();
            imprimirLinha();
            if (!podePrever()) {
                System.out.println("     (sem previsão: menos de 5 leituras)");
            }
            for (Alerta alerta : gerarAlertas()) {
                System.out.println("     -> " + alerta.gerarResumo());
                alertasDoDia.add(alerta);
            }
        }
        System.out.println("(* = acima do limite | FALHA = leitura descartada)");

        int qtdAtencao = 0;
        int qtdAlerta = 0;
        int qtdCritico = 0;
        for (Alerta alerta : alertasDoDia) {
            if (alerta.getNivel() == NivelAlerta.ATENCAO) {
                qtdAtencao++;
            } else if (alerta.getNivel() == NivelAlerta.ALERTA) {
                qtdAlerta++;
            } else if (alerta.getNivel() == NivelAlerta.CRITICO) {
                qtdCritico++;
            }
        }
        System.out.println("\nResumo do dia: " + qtdAtencao + " ATENÇÃO | " + qtdAlerta + " ALERTA | "
                + qtdCritico + " CRÍTICO");

        if (alertasDoDia.isEmpty()) {
            System.out.println("Nenhum alerta no período.");
            return;
        }
        Alerta maisGrave = sistemaAlertas.buscarMaisGrave(alertasDoDia);
        System.out.println("\nAlerta mais grave do dia:");
        maisGrave.exibir(dados);
        painel.mostrarMensagem(maisGrave.gerarMensagemPainel());
    }

    private static void gerenciarComponentes() {
        System.out.println("\n--- COMPONENTES ---");
        for (int i = 0; i < componentes.size(); i++) {
            ComponenteTerritorial componente = componentes.get(i);
            System.out.println((i + 1) + " - " + componente.getNome() + " (" + componente.getStatus() + ")");
        }
        int escolha = lerInt("Qual componente deseja ativar/desativar? (0 para voltar) ");
        if (escolha == 0) {
            return;
        }
        if (escolha < 1 || escolha > componentes.size()) {
            System.out.println("Opção inválida!");
            return;
        }
        ComponenteTerritorial componente = componentes.get(escolha - 1);
        if (componente.isAtivo()) {
            componente.desativar();
        } else {
            componente.ativar();
        }
        System.out.println(componente.getNome() + " agora está " + componente.getStatus() + ".");
        componente.verificarStatus();
    }

    private static void imprimirCabecalho() {
        System.out.printf("%-12s | %-10s | %-10s | %-10s%n", "Horário", "Temp (°C)", "Carga (%)", "Consumo");
        System.out.println("-------------+------------+------------+------------");
    }

    private static void imprimirLinha() {
        System.out.printf("%-12s | %-10s | %-10s | %-10s%n", estacao.getUltimaColeta(),
                formatarValorTabela(sensorTemperatura), formatarValorTabela(sensorCarga),
                formatarValorTabela(sensorConsumo));
    }

    private static String formatarValorTabela(Sensor sensor) {
        if (!sensor.verificarFuncionamento()) {
            return "FALHA";
        }
        String texto = String.format("%.2f", sensor.getUltimaLeitura());
        if (sensor.passouDoLimite(sensor.getUltimaLeitura())) {
            texto += " *";
        }
        return texto;
    }

    private static int lerInt(String mensagem) {
        while (true) {
            System.out.print(mensagem);
            try {
                return Integer.parseInt(sc.nextLine().trim());
            } catch (NumberFormatException e) {
                System.out.println("Digite um número inteiro.");
            }
        }
    }

    private static double lerDouble(String mensagem) {
        while (true) {
            System.out.print(mensagem);
            try {
                double valor = Double.parseDouble(sc.nextLine().trim().replace(",", "."));
                if (Double.isNaN(valor) || Double.isInfinite(valor)) {
                    System.out.println("Digite um número válido.");
                } else {
                    return valor;
                }
            } catch (NumberFormatException e) {
                System.out.println("Digite um número válido (ex: 35,5).");
            }
        }
    }

    private static String lerTexto(String mensagem) {
        System.out.print(mensagem);
        return sc.nextLine().trim();
    }
}
