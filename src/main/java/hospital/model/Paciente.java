package hospital.model;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

/**
 * Entidade Paciente.
 *
 * Demonstra:
 *  - SOBRECARGA (Overload): dois construtores — um completo (para carregar do arquivo)
 *    e um sem ID (para novos cadastros, onde o ID é gerado automaticamente).
 *  - VARIÁVEL DE CLASSE (static): proximoId garante o auto-incremento de IDs.
 *  - VARIÁVEL DE INSTÂNCIA: todos os demais atributos.
 */
public class Paciente {

    // ─── Variável de CLASSE (static) — compartilhada por todas as instâncias ───
    private static int proximoId = 1;

    // ─── Variáveis de INSTÂNCIA — exclusivas de cada objeto ──────────────────
    private final int id;
    private String nome;
    private String cpf;
    private int idade;
    private String queixa;
    private CorTriagem corTriagem;
    private StatusPaciente status;
    private final LocalDateTime horaChegada;
    private LocalDateTime horaTriagem;
    private Medico medicoResponsavel;
    private String prescricaoMedica;

    private static final DateTimeFormatter FMT = DateTimeFormatter.ofPattern("dd/MM/yyyy HH:mm");

    // ─── SOBRECARGA — Construtor COMPLETO (usado ao recarregar dados do arquivo) ─
    public Paciente(int id, String nome, String cpf, int idade, String queixa,
                    CorTriagem corTriagem, StatusPaciente status,
                    LocalDateTime horaChegada, LocalDateTime horaTriagem,
                    Medico medicoResponsavel, String prescricaoMedica) {
        this.id           = id;
        this.nome         = nome;
        this.cpf          = cpf;
        this.idade        = idade;
        this.queixa       = queixa;
        this.corTriagem   = corTriagem;
        this.status       = status;
        this.horaChegada  = horaChegada;
        this.horaTriagem  = horaTriagem;
        this.medicoResponsavel = medicoResponsavel;
        this.prescricaoMedica  = prescricaoMedica;
        // Garante que o próximo ID gerado será maior que qualquer ID já existente
        if (id >= proximoId) proximoId = id + 1;
    }

    // ─── SOBRECARGA — Construtor SEM ID (usado para novos cadastros na recepção) ─
    public Paciente(String nome, String cpf, int idade, String queixa) {
        this.id          = proximoId++;   // Incrementa a variável de classe
        this.nome        = nome;
        this.cpf         = cpf;
        this.idade       = idade;
        this.queixa      = queixa;
        this.corTriagem  = null;
        this.status      = StatusPaciente.AGUARDANDO_TRIAGEM;
        this.horaChegada = LocalDateTime.now();
        this.horaTriagem = null;
        this.medicoResponsavel = null;
        this.prescricaoMedica = null;
    }

    // ─── Métodos estáticos para controle do ID ────────────────────────────────
    public static int getProximoId()        { return proximoId; }
    public static void setProximoId(int id) { proximoId = id; }

    // ─── Getters e Setters ────────────────────────────────────────────────────
    public int getId()                              { return id; }
    public String getNome()                         { return nome; }
    public void setNome(String nome)                { this.nome = nome; }
    public String getCpf()                          { return cpf; }
    public void setCpf(String cpf)                  { this.cpf = cpf; }
    public int getIdade()                           { return idade; }
    public void setIdade(int idade)                 { this.idade = idade; }
    public String getQueixa()                       { return queixa; }
    public void setQueixa(String queixa)            { this.queixa = queixa; }
    public CorTriagem getCorTriagem()               { return corTriagem; }
    public void setCorTriagem(CorTriagem cor)        { this.corTriagem = cor; }
    public StatusPaciente getStatus()               { return status; }
    public void setStatus(StatusPaciente status)    { this.status = status; }
    public LocalDateTime getHoraChegada()           { return horaChegada; }
    public LocalDateTime getHoraTriagem()           { return horaTriagem; }
    public void setHoraTriagem(LocalDateTime hora)  { this.horaTriagem = hora; }
    public Medico getMedicoResponsavel()            { return medicoResponsavel; }
    public void setMedicoResponsavel(Medico m)      { this.medicoResponsavel = m; }
    public String getPrescricaoMedica()             { return prescricaoMedica; }
    public void setPrescricaoMedica(String p)       { this.prescricaoMedica = p; }

    public String getHoraChegadaFormatada() {
        return horaChegada != null ? horaChegada.format(FMT) : "—";
    }

    public String getHoraTriagemFormatada() {
        return horaTriagem != null ? horaTriagem.format(FMT) : "—";
    }

    // ─── SOBRESCRITA (Override) ───────────────────────────────────────────────
    @Override
    public String toString() {
        return String.format("[%d] %s | CPF: %s | Queixa: %s | Status: %s",
                id, nome, cpf, queixa, status.getDescricao());
    }
}
