package br.com.territoryinsights.model;

public class PainelInformativo extends ComponenteTerritorial {

    private String localInstalacao;
    private String ultimaMensagem = "nenhuma";
    private int totalMensagens = 0;

    public PainelInformativo(String id, String nome, String regiao, String localInstalacao) {
        super(id, nome, regiao);
        this.localInstalacao = localInstalacao;
    }

    public void mostrarMensagem(String mensagem) {
        if (!this.isAtivo()) {
            System.out.println("[Painel desligado] A mensagem NÃO chegou à comunidade. Ative o painel na opção 7.");
            return;
        }
        this.ultimaMensagem = mensagem;
        this.totalMensagens++;
        System.out.println("\n*** PAINEL - " + this.localInstalacao + " ***");
        System.out.println(mensagem);
    }

    @Override
    public void verificarStatus() {
        System.out.println("[" + this.getId() + "] " + this.getNome() + " - " + this.getStatus());
        if (!this.isAtivo()) {
            System.out.println("   Painel desligado: os alertas não chegam à comunidade.");
            return;
        }
        System.out.println("   Local: " + this.localInstalacao);
        System.out.println("   Mensagens exibidas: " + this.totalMensagens);
        System.out.println("   Última mensagem: " + this.ultimaMensagem);
    }
}
