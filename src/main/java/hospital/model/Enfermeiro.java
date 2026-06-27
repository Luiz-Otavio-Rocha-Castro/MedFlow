package hospital.model;

/**
 * Classe Enfermeiro — estende ProfissionalSaude (HERANÇA).
 *
 * Demonstra:
 *  - HERANÇA: extends ProfissionalSaude.
 *  - SOBRESCRITA (Override): toString(), getTipo(), getInfoAdicional().
 *  - Atributo exclusivo da subclasse: coren.
 */
public class Enfermeiro extends ProfissionalSaude {

    private String coren;

    public Enfermeiro(int id, String nome, String login, String senha, String coren) {
        super(id, nome, login, senha); // Chama construtor da classe-mãe
        this.coren = coren;
    }

    // ─── Getter e Setter exclusivos da subclasse ──────────────────────────────
    public String getCoren()           { return coren; }
    public void setCoren(String coren) { this.coren = coren; }

    // ─── SOBRESCRITA (Override) dos métodos abstratos ─────────────────────────
    @Override
    public String getTipo() { return "Enfermeiro(a)"; }

    @Override
    public String getInfoAdicional() { return "COREN: " + coren; }

    // ─── SOBRESCRITA (Override) do toString herdado ───────────────────────────
    @Override
    public String toString() {
        return String.format("[Enfermeiro(a)] %s | COREN: %s | Login: %s",
                getNome(), coren, getLogin());
    }
}
