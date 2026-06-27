package hospital.service;

import hospital.model.*;
import hospital.repository.*;
import hospital.service.exception.*;

import java.time.LocalDateTime;
import java.util.*;
import java.util.stream.Collectors;

/**
 * GerenciadorHospital — Camada de Serviço (regras de negócio).
 *
 * Demonstra:
 *  - AGREGAÇÃO: a classe agrega (usa, mas não "possui") listas de
 *    ProfissionalSaude e Leito. Essas listas existem independentemente.
 *  - COLEÇÕES: uso de List e ArrayList para gerenciar filas e entidades.
 *  - EXCEÇÕES CUSTOMIZADAS: lança FilaVaziaException, LeitoIndisponivelException,
 *    PacienteNaoEncontradoException nas situações de erro de negócio.
 *  - VALIDAÇÕES: toda regra de negócio é validada aqui, antes de chamar o Repository.
 */
public class GerenciadorHospital {

    // ── COLEÇÕES (Collections) — List e ArrayList ─────────────────────────────
    private List<Paciente>          pacientes;      // Todos os pacientes registrados
    private List<ProfissionalSaude> profissionais;  // AGREGAÇÃO — lista de profissionais
    private List<Leito>             leitos;         // AGREGAÇÃO — lista de leitos

    // Controle de ID para profissionais (gerenciado pelo Service)
    private int proximoIdProfissional = 1;

    // ── Repositórios ──────────────────────────────────────────────────────────
    private final PacienteRepository      pacienteRepo;
    private final ProfissionalRepository  profissionalRepo;
    private final LeitoRepository         leitoRepo;

    private static final int TOTAL_LEITOS = 10;

    // ─────────────────────────────────────────────────────────────────────────
    public GerenciadorHospital() {
        this.pacienteRepo     = new PacienteRepository();
        this.profissionalRepo = new ProfissionalRepository();
        this.leitoRepo        = new LeitoRepository();
        this.pacientes        = new ArrayList<>();
        this.profissionais    = new ArrayList<>();
        this.leitos           = new ArrayList<>();
        carregarDados();
    }

    // ════════════════════════════════════════════════════════════════════════
    //  INICIALIZAÇÃO E PERSISTÊNCIA
    // ════════════════════════════════════════════════════════════════════════

    /** Carrega todos os dados dos arquivos .txt ao iniciar o sistema. */
    private void carregarDados() {
        // 1. Profissionais (Carregados PRIMEIRO para vincular os pacientes)
        profissionais = profissionalRepo.carregarTodos();
        profissionais.stream()
                     .mapToInt(ProfissionalSaude::getId)
                     .max()
                     .ifPresent(max -> proximoIdProfissional = max + 1);

        // 2. Pacientes
        pacientes = pacienteRepo.carregarTodos(profissionais);
        pacientes.stream()
                 .mapToInt(Paciente::getId)
                 .max()
                 .ifPresent(max -> Paciente.setProximoId(max + 1));

        // 3. Leitos (associa pacientes já carregados aos leitos ocupados)
        List<Leito> leitosCarregados = leitoRepo.carregar(pacientes);
        leitos = leitosCarregados.isEmpty() ? criarLeitosPadrao() : leitosCarregados;
    }

    /** Cria os leitos padrão na primeira execução do sistema. */
    private List<Leito> criarLeitosPadrao() {
        String[] alas = {"Emergência", "Clínica Geral", "Pediatria", "UTI", "Cardiologia"};
        List<Leito> novos = new ArrayList<>();
        for (int i = 1; i <= TOTAL_LEITOS; i++) {
            novos.add(new Leito(i, alas[(i - 1) % alas.length]));
        }
        leitoRepo.salvarOcupacao(novos);
        return novos;
    }

    /** Persiste todos os dados em disco. */
    public void salvarTudo() {
        pacienteRepo.salvarTodos(pacientes);
        profissionalRepo.salvarTodos(profissionais);
        leitoRepo.salvarOcupacao(leitos);
    }

    // ════════════════════════════════════════════════════════════════════════
    //  GESTÃO DE PACIENTES
    // ════════════════════════════════════════════════════════════════════════

    /**
     * Cadastra um novo paciente na recepção.
     * Usa o construtor SEM ID (Sobrecarga de Construtor de Paciente).
     */
    public Paciente cadastrarPaciente(String nome, String cpf, int idade, String queixa) {
        // ── Validações de negócio ────────────────────────────────────────────
        validarNaoVazio(nome,   "Nome do paciente");
        validarNaoVazio(cpf,    "CPF");
        validarNaoVazio(queixa, "Queixa principal");
        if (idade <= 0 || idade > 130)
            throw new IllegalArgumentException("Idade inválida: " + idade + ". Deve ser entre 1 e 130.");
        String cpfNumeros = cpf.replaceAll("\\D", "");
        if (cpfNumeros.length() != 11)
            throw new IllegalArgumentException("CPF inválido. Informe 11 dígitos numéricos.");

        // Usa o construtor SEM ID — ID é gerado automaticamente pela variável static
        Paciente p = new Paciente(nome.trim(), cpfNumeros, idade, queixa.trim());
        pacientes.add(p);
        pacienteRepo.salvarTodos(pacientes); // Persiste imediatamente
        return p;
    }

    /** Busca um paciente pelo ID. Lança exceção customizada se não encontrar. */
    public Paciente buscarPorId(int id) throws PacienteNaoEncontradoException {
        return pacientes.stream()
                        .filter(p -> p.getId() == id)
                        .findFirst()
                        .orElseThrow(() -> new PacienteNaoEncontradoException(
                                "Paciente com ID " + id + " não encontrado."));
    }

    public List<Paciente> getTodosOsPacientes() {
        return Collections.unmodifiableList(pacientes);
    }

    /**
     * Edita os dados de um paciente existente.
     * Lança PacienteNaoEncontradoException se o ID não existir.
     */
    public void editarPaciente(int id, String nome, String cpf, int idade, String queixa) throws PacienteNaoEncontradoException {
        Paciente p = buscarPorId(id);
        
        validarNaoVazio(nome, "Nome do paciente");
        validarNaoVazio(cpf, "CPF");
        validarNaoVazio(queixa, "Queixa principal");
        if (idade <= 0 || idade > 130) {
            throw new IllegalArgumentException("Idade inválida: " + idade + ". Deve ser entre 1 e 130.");
        }
        String cpfNumeros = cpf.replaceAll("\\D", "");
        if (cpfNumeros.length() != 11) {
            throw new IllegalArgumentException("CPF inválido. Informe 11 dígitos numéricos.");
        }

        p.setNome(nome.trim());
        p.setCpf(cpfNumeros);
        p.setIdade(idade);
        p.setQueixa(queixa.trim());
        
        pacienteRepo.salvarTodos(pacientes); // Atualiza o arquivo
    }

    /**
     * Remove um paciente pelo ID.
     * Utiliza removeIf para remoção segura na coleção.
     * Se o paciente estiver internado, libera o leito primeiro.
     */
    public void removerPaciente(int id) throws PacienteNaoEncontradoException {
        Paciente p = buscarPorId(id);
        
        // Se estiver internado, precisamos desocupar o leito
        if (p.getStatus() == StatusPaciente.INTERNADO) {
            leitos.stream()
                  .filter(l -> !l.isDisponivel() && l.getPacienteAlocado().getId() == id)
                  .findFirst()
                  .ifPresent(Leito::liberarLeito);
            leitoRepo.salvarOcupacao(leitos);
        }
        
        // Remove da coleção
        pacientes.removeIf(pac -> pac.getId() == id);
        
        pacienteRepo.salvarTodos(pacientes); // Atualiza o arquivo
    }

    // ════════════════════════════════════════════════════════════════════════
    //  TRIAGEM (Protocolo de Manchester)
    // ════════════════════════════════════════════════════════════════════════

    /** Retorna a fila de pacientes aguardando triagem (FIFO). */
    public List<Paciente> getFilaTriagem() {
        return pacientes.stream()
                        .filter(p -> p.getStatus() == StatusPaciente.AGUARDANDO_TRIAGEM)
                        .collect(Collectors.toList());
    }

    /**
     * Aplica a classificação de risco (triagem) a um paciente.
     * Lança FilaVaziaException se não houver pacientes aguardando.
     * Lança PacienteNaoEncontradoException se o ID não existir.
     */
    public void realizarTriagem(int pacienteId, CorTriagem cor)
            throws FilaVaziaException, PacienteNaoEncontradoException {
        // ── Exceção customizada ──────────────────────────────────────────────
        if (getFilaTriagem().isEmpty())
            throw new FilaVaziaException("Não há pacientes aguardando triagem no momento.");

        Paciente p = buscarPorId(pacienteId);

        if (p.getStatus() != StatusPaciente.AGUARDANDO_TRIAGEM)
            throw new IllegalStateException(
                    "Paciente não está na fila de triagem. Status atual: "
                    + p.getStatus().getDescricao());

        p.setCorTriagem(cor);
        p.setHoraTriagem(LocalDateTime.now());
        p.setStatus(StatusPaciente.AGUARDANDO_ATENDIMENTO);
        pacienteRepo.salvarTodos(pacientes);
    }

    // ════════════════════════════════════════════════════════════════════════
    //  FILA DE ATENDIMENTO MÉDICO (Ordenada por Prioridade de Manchester)
    // ════════════════════════════════════════════════════════════════════════

    /**
     * Retorna a fila de atendimento ordenada pela prioridade do Manchester:
     *  1º VERMELHO (Emergência), 2º AMARELO (Urgente), 3º VERDE (Não Urgente).
     *  Em caso de empate na cor, ordena por hora de triagem (FIFO).
     */
    public List<Paciente> getFilaAtendimento() {
        return pacientes.stream()
                        .filter(p -> p.getStatus() == StatusPaciente.AGUARDANDO_ATENDIMENTO)
                        .sorted(Comparator
                                .comparingInt((Paciente p) -> p.getCorTriagem().getPrioridade())
                                .reversed()
                                .thenComparing(Paciente::getHoraTriagem))
                        .collect(Collectors.toList());
    }

    /**
     * Chama o próximo paciente da fila (maior prioridade) para atendimento.
     * Lança FilaVaziaException se a fila estiver vazia.
     */
    public Paciente chamarProximoPaciente(Medico medicoLogado) throws FilaVaziaException {
        List<Paciente> fila = getFilaAtendimento();
        // ── Exceção customizada ──────────────────────────────────────────────
        if (fila.isEmpty())
            throw new FilaVaziaException("Não há pacientes aguardando atendimento médico.");

        Paciente proximo = fila.get(0); // Já ordenado por prioridade
        proximo.setStatus(StatusPaciente.EM_ATENDIMENTO);
        proximo.setMedicoResponsavel(medicoLogado);
        pacienteRepo.salvarTodos(pacientes);
        return proximo;
    }

    /**
     * Salva o atendimento médico atual (prescrição) no paciente.
     */
    public void salvarAtendimento(int pacienteId, String prescricao) throws PacienteNaoEncontradoException {
        Paciente p = buscarPorId(pacienteId);
        
        if (p.getStatus() != StatusPaciente.EM_ATENDIMENTO)
            throw new IllegalStateException("Paciente não está em atendimento.");

        p.setPrescricaoMedica(prescricao);
        pacienteRepo.salvarTodos(pacientes);
    }

    // ════════════════════════════════════════════════════════════════════════
    //  ATENDIMENTO MÉDICO — INTERNAR / ALTA
    // ════════════════════════════════════════════════════════════════════════

    /**
     * Interna um paciente em um leito específico.
     * Lança PacienteNaoEncontradoException ou LeitoIndisponivelException se necessário.
     */
    public Leito internarPaciente(int pacienteId, int leitoNumero)
            throws PacienteNaoEncontradoException, LeitoIndisponivelException {
        Paciente p = buscarPorId(pacienteId);

        if (p.getStatus() != StatusPaciente.EM_ATENDIMENTO)
            throw new IllegalStateException(
                    "Paciente deve estar 'Em Atendimento' para ser internado. "
                    + "Status atual: " + p.getStatus().getDescricao());

        // ── Exceção customizada ──────────────────────────────────────────────
        Leito leito = leitos.stream()
                            .filter(l -> l.getNumero() == leitoNumero)
                            .findFirst()
                            .orElseThrow(() -> new LeitoIndisponivelException(
                                    "Leito " + leitoNumero + " não existe."));

        if (!leito.isDisponivel())
            throw new LeitoIndisponivelException(
                    "Leito " + leitoNumero + " está ocupado por: "
                    + leito.getPacienteAlocado().getNome() + ".");

        leito.alocarPaciente(p); // COMPOSIÇÃO: associa o paciente ao leito
        p.setStatus(StatusPaciente.INTERNADO);
        salvarTudo();
        return leito;
    }

    /**
     * Concede alta a um paciente (em atendimento ou internado).
     * Se estiver internado, libera o leito automaticamente.
     */
    public void darAlta(int pacienteId) throws PacienteNaoEncontradoException {
        Paciente p = buscarPorId(pacienteId);

        if (p.getStatus() != StatusPaciente.EM_ATENDIMENTO
                && p.getStatus() != StatusPaciente.INTERNADO)
            throw new IllegalStateException(
                    "Não é possível dar alta. Status atual: " + p.getStatus().getDescricao());

        // Libera o leito se o paciente estava internado (COMPOSIÇÃO — Leito.liberarLeito())
        leitos.stream()
              .filter(l -> !l.isDisponivel() && l.getPacienteAlocado().getId() == pacienteId)
              .findFirst()
              .ifPresent(Leito::liberarLeito);

        p.setStatus(StatusPaciente.ALTA);
        salvarTudo();
    }

    /** Retorna pacientes com status EM_ATENDIMENTO. */
    public List<Paciente> getPacientesEmAtendimento() {
        return pacientes.stream()
                        .filter(p -> p.getStatus() == StatusPaciente.EM_ATENDIMENTO)
                        .collect(Collectors.toList());
    }

    // ════════════════════════════════════════════════════════════════════════
    //  GESTÃO DE PROFISSIONAIS DE SAÚDE
    // ════════════════════════════════════════════════════════════════════════

    /** Cadastra um novo médico no sistema. */
    public void cadastrarMedico(String nome, String login, String senha,
                                String especialidade, String crm) {
        validarCredenciais(nome, login, senha);
        validarNaoVazio(especialidade, "Especialidade");
        validarNaoVazio(crm, "CRM");
        Medico m = new Medico(proximoIdProfissional++,
                nome.trim(), login.trim(), senha, especialidade.trim(), crm.trim());
        profissionais.add(m);
        profissionalRepo.salvarTodos(profissionais);
    }

    /** Cadastra um novo enfermeiro no sistema. */
    public void cadastrarEnfermeiro(String nome, String login, String senha, String coren) {
        validarCredenciais(nome, login, senha);
        validarNaoVazio(coren, "COREN");
        Enfermeiro e = new Enfermeiro(proximoIdProfissional++,
                nome.trim(), login.trim(), senha, coren.trim());
        profissionais.add(e);
        profissionalRepo.salvarTodos(profissionais);
    }

    public List<ProfissionalSaude> getProfissionais() {
        return Collections.unmodifiableList(profissionais);
    }

    /**
     * Edita os dados de um profissional de saúde.
     */
    public void editarProfissional(int id, String nome, String login, String senha, String extra, String crm) {
        ProfissionalSaude p = profissionais.stream()
                .filter(prof -> prof.getId() == id)
                .findFirst()
                .orElseThrow(() -> new IllegalArgumentException("Profissional com ID " + id + " não encontrado."));
        
        validarNaoVazio(nome, "Nome");
        validarNaoVazio(login, "Login");
        validarNaoVazio(senha, "Senha");
        validarNaoVazio(extra, "Dado extra (Especialidade ou COREN)");
        
        p.setNome(nome.trim());
        p.setLogin(login.trim());
        p.setSenha(senha);
        
        if (p instanceof Medico m) {
            m.setEspecialidade(extra.trim());
            if (crm != null && !crm.trim().isEmpty()) {
                m.setCrm(crm.trim());
            }
        } else if (p instanceof Enfermeiro e) {
            e.setCoren(extra.trim());
        }
        
        profissionalRepo.salvarTodos(profissionais); // Atualiza o arquivo
    }

    /**
     * Remove um profissional pelo ID usando removeIf.
     */
    public void removerProfissional(int id) {
        boolean removed = profissionais.removeIf(p -> p.getId() == id);
        if (!removed) {
            throw new IllegalArgumentException("Profissional com ID " + id + " não encontrado.");
        }
        profissionalRepo.salvarTodos(profissionais); // Atualiza o arquivo
    }

    // ════════════════════════════════════════════════════════════════════════
    //  GESTÃO DE LEITOS
    // ════════════════════════════════════════════════════════════════════════

    public List<Leito> getLeitos() {
        return Collections.unmodifiableList(leitos);
    }

    public List<Leito> getLeitosDisponiveis() {
        return leitos.stream()
                     .filter(Leito::isDisponivel)
                     .collect(Collectors.toList());
    }

    // ════════════════════════════════════════════════════════════════════════
    //  VALIDAÇÕES PRIVADAS
    // ════════════════════════════════════════════════════════════════════════

    private void validarNaoVazio(String valor, String campo) {
        if (valor == null || valor.trim().isEmpty())
            throw new IllegalArgumentException(campo + " não pode ser vazio.");
    }

    private void validarCredenciais(String nome, String login, String senha) {
        validarNaoVazio(nome,  "Nome");
        validarNaoVazio(login, "Login");
        validarNaoVazio(senha, "Senha");
        boolean loginEmUso = profissionais.stream()
                .anyMatch(p -> p.getLogin().equalsIgnoreCase(login.trim()));
        if (loginEmUso)
            throw new IllegalArgumentException("O login '" + login + "' já está em uso por outro profissional.");
    }

    /**
     * Autentica um profissional de saúde. Retorna null se for admin/admin.
     */
    public ProfissionalSaude autenticar(String login, String senha) {
        if ("admin".equals(login) && "admin".equals(senha)) {
            return null;
        }
        return profissionais.stream()
                .filter(p -> p.getLogin().equals(login) && p.getSenha().equals(senha))
                .findFirst()
                .orElseThrow(() -> new IllegalArgumentException("Login ou senha incorretos."));
    }
}
