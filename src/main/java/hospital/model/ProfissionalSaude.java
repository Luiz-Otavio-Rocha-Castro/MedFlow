package hospital.model;

/**
 * Classe ABSTRATA ProfissionalSaude.
 *
 * Demonstra:
 *  - HERANÇA: base para Medico e Enfermeiro.
 *  - CLASSE ABSTRATA: não pode ser instanciada diretamente.
 *  - SOBRESCRITA (Override): toString() e autenticar() são sobrescritos nas subclasses.
 */
public abstract class ProfissionalSaude {

    private final int id;
    private String nome;
    private String login;
    private String senha;

    // Construtor da classe abstrata — acessível apenas pelas subclasses via super()
    public ProfissionalSaude(int id, String nome, String login, String senha) {
        this.id    = id;
        this.nome  = nome;
        this.login = login;
        this.senha = senha;
    }

    // ─── Métodos abstratos — obrigam as subclasses a implementar ─────────────
    public abstract String getTipo();
    public abstract String getInfoAdicional();

    // ─── Método com possibilidade de SOBRESCRITA (Override) ───────────────────
    /**
     * Verifica as credenciais de login do profissional.
     * Pode ser sobrescrito pelas subclasses para regras específicas.
     */
    public boolean autenticar(String loginTentativa, String senhaTentativa) {
        return this.login.equals(loginTentativa) && this.senha.equals(senhaTentativa);
    }

    // ─── Getters ──────────────────────────────────────────────────────────────
    public int getId()      { return id; }
    public String getNome() { return nome; }
    public String getLogin(){ return login; }
    public String getSenha(){ return senha; }
    public void setNome(String nome) { this.nome = nome; }
    public void setLogin(String login) { this.login = login; }
    public void setSenha(String senha) { this.senha = senha; }

    // ─── SOBRESCRITA (Override) ───────────────────────────────────────────────
    @Override
    public String toString() {
        return String.format("[%s] %s | Login: %s | %s",
                getTipo(), nome, login, getInfoAdicional());
    }
}
