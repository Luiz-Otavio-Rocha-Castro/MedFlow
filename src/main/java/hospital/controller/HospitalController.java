package hospital.controller;

import hospital.model.*;
import hospital.service.GerenciadorHospital;
import hospital.service.exception.*;

import java.util.List;

/**
 * HospitalController — Camada de Controle.
 *
 * Responsabilidade: intermediar a comunicação entre a camada de visão (View)
 * e a camada de serviço (Service), sem conter nenhuma regra de negócio.
 *
 * A View chama o Controller → O Controller chama o Service →
 * O Service valida e chama o Repository → Os dados são persistidos.
 */
public class HospitalController {

    private final GerenciadorHospital servico;

    public HospitalController() {
        this.servico = new GerenciadorHospital();
    }

    public ProfissionalSaude autenticar(String login, String senha) {
        return servico.autenticar(login, senha);
    }

    // ════════════════════════════════════════════════════════════════════════
    //  PACIENTES
    // ════════════════════════════════════════════════════════════════════════

    public Paciente cadastrarPaciente(String nome, String cpf, int idade, String queixa) {
        return servico.cadastrarPaciente(nome, cpf, idade, queixa);
    }

    public List<Paciente> getTodosOsPacientes() {
        return servico.getTodosOsPacientes();
    }

    public void editarPaciente(int id, String nome, String cpf, int idade, String queixa) throws PacienteNaoEncontradoException {
        servico.editarPaciente(id, nome, cpf, idade, queixa);
    }

    public void removerPaciente(int id) throws PacienteNaoEncontradoException {
        servico.removerPaciente(id);
    }

    public List<Paciente> getFilaTriagem() {
        return servico.getFilaTriagem();
    }

    public List<Paciente> getFilaAtendimento() {
        return servico.getFilaAtendimento();
    }

    public List<Paciente> getPacientesEmAtendimento() {
        return servico.getPacientesEmAtendimento();
    }

    // ════════════════════════════════════════════════════════════════════════
    //  TRIAGEM
    // ════════════════════════════════════════════════════════════════════════

    public void realizarTriagem(int pacienteId, CorTriagem cor)
            throws FilaVaziaException, PacienteNaoEncontradoException {
        servico.realizarTriagem(pacienteId, cor);
    }

    // ════════════════════════════════════════════════════════════════════════
    //  ATENDIMENTO MÉDICO
    // ════════════════════════════════════════════════════════════════════════

    public Paciente chamarProximoPaciente(Medico medicoLogado) throws FilaVaziaException {
        return servico.chamarProximoPaciente(medicoLogado);
    }

    public void salvarAtendimento(int pacienteId, String prescricao) throws PacienteNaoEncontradoException {
        servico.salvarAtendimento(pacienteId, prescricao);
    }

    public Leito internarPaciente(int pacienteId, int leitoNumero)
            throws PacienteNaoEncontradoException, LeitoIndisponivelException {
        return servico.internarPaciente(pacienteId, leitoNumero);
    }

    public void darAlta(int pacienteId) throws PacienteNaoEncontradoException {
        servico.darAlta(pacienteId);
    }

    // ════════════════════════════════════════════════════════════════════════
    //  PROFISSIONAIS
    // ════════════════════════════════════════════════════════════════════════

    public void cadastrarMedico(String nome, String login, String senha,
                                String especialidade, String crm) {
        servico.cadastrarMedico(nome, login, senha, especialidade, crm);
    }

    public void cadastrarEnfermeiro(String nome, String login, String senha, String coren) {
        servico.cadastrarEnfermeiro(nome, login, senha, coren);
    }

    public List<ProfissionalSaude> getProfissionais() {
        return servico.getProfissionais();
    }

    public void editarProfissional(int id, String nome, String login, String senha, String extra, String crm) {
        servico.editarProfissional(id, nome, login, senha, extra, crm);
    }

    public void removerProfissional(int id) {
        servico.removerProfissional(id);
    }

    // ════════════════════════════════════════════════════════════════════════
    //  LEITOS
    // ════════════════════════════════════════════════════════════════════════

    public List<Leito> getLeitos() {
        return servico.getLeitos();
    }

    public List<Leito> getLeitosDisponiveis() {
        return servico.getLeitosDisponiveis();
    }

    // ════════════════════════════════════════════════════════════════════════
    //  PERSISTÊNCIA
    // ════════════════════════════════════════════════════════════════════════

    public void salvarTudo() {
        servico.salvarTudo();
    }
}
