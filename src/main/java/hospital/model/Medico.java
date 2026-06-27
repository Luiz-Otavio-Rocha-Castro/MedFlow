package hospital.model;

/**
 * Classe Medico — estende ProfissionalSaude (HERANÇA).
 *
 * Demonstra:
 *  - HERANÇA: extends ProfissionalSaude.
 *  - SOBRESCRITA (Override): toString(), getTipo(), getInfoAdicional().
 *  - Atributo exclusivo da subclasse: especialidade e crm.
 */
public class Medico extends ProfissionalSaude {

    private String especialidade;
    private String crm;

    public Medico(int id, String nome, String login, String senha,
                  String especialidade, String crm) {
        super(id, nome, login, senha); // Chama construtor da classe-mãe
        this.especialidade = especialidade;
        this.crm           = crm;
    }

    // ─── Getters e Setters exclusivos da subclasse ────────────────────────────
    public String getEspecialidade()              { return especialidade; }
    public void setEspecialidade(String esp)      { this.especialidade = esp; }
    public String getCrm()                        { return crm; }
    public void setCrm(String crm)                { this.crm = crm; }

    // ─── SOBRESCRITA (Override) dos métodos abstratos ─────────────────────────
    @Override
    public String getTipo() { return "Médico"; }

    @Override
    public String getInfoAdicional() {
        return "Especialidade: " + especialidade + " | CRM: " + crm;
    }

    // ─── SOBRESCRITA (Override) do toString herdado ───────────────────────────
    @Override
    public String toString() {
        return String.format("[Médico] Dr(a). %s | %s | CRM: %s | Login: %s",
                getNome(), especialidade, crm, getLogin());
    }
}
