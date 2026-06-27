package hospital.model;

/**
 * Classe Leito.
 *
 * Demonstra:
 *  - COMPOSIÇÃO: Leito possui um objeto do tipo Paciente (pacienteAlocado).
 *    Quando o leito está livre, pacienteAlocado é null.
 *    O Paciente "faz parte" do Leito enquanto internado.
 */
public class Leito {

    private final int numero;
    private final String ala;

    /**
     * COMPOSIÇÃO: o Leito contém um Paciente.
     * Se null, o leito está disponível.
     * Se não-null, o leito está ocupado por esse paciente.
     */
    private Paciente pacienteAlocado;

    public Leito(int numero, String ala) {
        this.numero           = numero;
        this.ala              = ala;
        this.pacienteAlocado  = null; // Começa disponível
    }

    // ─── Operações do leito ───────────────────────────────────────────────────
    public void alocarPaciente(Paciente p) {
        this.pacienteAlocado = p;
    }

    public void liberarLeito() {
        this.pacienteAlocado = null;
    }

    public boolean isDisponivel() {
        return pacienteAlocado == null;
    }

    // ─── Getters ──────────────────────────────────────────────────────────────
    public int getNumero()              { return numero; }
    public String getAla()              { return ala; }
    public Paciente getPacienteAlocado(){ return pacienteAlocado; }

    public String getStatusDescricao() {
        return isDisponivel()
                ? "Disponível"
                : "Ocupado — " + pacienteAlocado.getNome() + " (ID: " + pacienteAlocado.getId() + ")";
    }

    // ─── SOBRESCRITA (Override) ───────────────────────────────────────────────
    @Override
    public String toString() {
        return String.format("Leito %02d | Ala: %-15s | %s",
                numero, ala, getStatusDescricao());
    }
}
