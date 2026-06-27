package hospital.model;

/**
 * Enum que representa as cores do Protocolo de Manchester.
 * Prioridade: VERMELHO (mais urgente) > AMARELO > VERDE (menos urgente).
 */
public enum CorTriagem {

    VERDE("Verde", "Não Urgente — Atendimento em até 2 horas", 0),
    AMARELO("Amarelo", "Urgente — Atendimento em até 30 minutos", 1),
    VERMELHO("Vermelho", "Emergência — Atendimento Imediato", 2);

    private final String nome;
    private final String descricao;
    private final int prioridade; // Quanto maior, mais prioritário

    CorTriagem(String nome, String descricao, int prioridade) {
        this.nome = nome;
        this.descricao = descricao;
        this.prioridade = prioridade;
    }

    public String getNome()      { return nome; }
    public String getDescricao() { return descricao; }
    public int getPrioridade()   { return prioridade; }

    @Override
    public String toString() {
        return nome + " — " + descricao;
    }
}
