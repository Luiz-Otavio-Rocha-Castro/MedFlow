package hospital.model;

/**
 * Enum que representa os estados possíveis de um paciente
 * durante o fluxo hospitalar.
 */
public enum StatusPaciente {

    AGUARDANDO_TRIAGEM("Aguardando Triagem"),
    AGUARDANDO_ATENDIMENTO("Aguardando Atendimento"),
    EM_ATENDIMENTO("Em Atendimento"),
    INTERNADO("Internado"),
    ALTA("Alta");

    private final String descricao;

    StatusPaciente(String descricao) {
        this.descricao = descricao;
    }

    public String getDescricao() { return descricao; }

    @Override
    public String toString() { return descricao; }
}
