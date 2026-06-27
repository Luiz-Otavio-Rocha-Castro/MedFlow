package hospital.repository;

import hospital.model.CorTriagem;
import hospital.model.Paciente;
import hospital.model.StatusPaciente;
import hospital.model.ProfissionalSaude;
import hospital.model.Medico;

import java.io.*;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;

/**
 * PacienteRepository — Camada de persistência para Pacientes.
 *
 * Demonstra MANIPULAÇÃO DE ARQUIVOS:
 *  - Salva e carrega dados usando BufferedWriter / BufferedReader em arquivos .txt
 *  - Garante que os dados não são perdidos ao fechar o programa.
 *
 * Formato da linha no arquivo (separado por '|'):
 *   id | nome | cpf | idade | queixa | corTriagem | status | horaChegada | horaTriagem
 */
public class PacienteRepository {

    private static final String ARQUIVO      = "data/pacientes.txt";
    private static final String SEPARADOR    = "|";
    private static final String ESC_SEP      = "\u00A7"; // § como escape para o separador
    private static final DateTimeFormatter FMT = DateTimeFormatter.ISO_LOCAL_DATE_TIME;

    public PacienteRepository() {
        // Cria a pasta 'data' se ela não existir
        new File("data").mkdirs();
    }

    /**
     * Sobrescreve o arquivo com a lista atual de pacientes.
     */
    public void salvarTodos(List<Paciente> pacientes) {
        try (BufferedWriter bw = new BufferedWriter(new FileWriter(ARQUIVO, false))) {
            for (Paciente p : pacientes) {
                bw.write(serializar(p));
                bw.newLine();
            }
        } catch (IOException e) {
            System.err.println("[PacienteRepository] Erro ao salvar: " + e.getMessage());
        }
    }

    /**
     * Lê todos os pacientes do arquivo .txt.
     */
    public List<Paciente> carregarTodos(List<ProfissionalSaude> profissionais) {
        List<Paciente> lista = new ArrayList<>();
        File arquivo = new File(ARQUIVO);
        if (!arquivo.exists()) return lista;

        try (BufferedReader br = new BufferedReader(new FileReader(arquivo))) {
            String linha;
            while ((linha = br.readLine()) != null) {
                linha = linha.trim();
                if (!linha.isEmpty()) {
                    Paciente p = desserializar(linha, profissionais);
                    if (p != null) lista.add(p);
                }
            }
        } catch (IOException e) {
            System.err.println("[PacienteRepository] Erro ao carregar: " + e.getMessage());
        }
        return lista;
    }

    // ─── Serialização ─────────────────────────────────────────────────────────

    private String serializar(Paciente p) {
        return String.join(SEPARADOR,
                String.valueOf(p.getId()),
                esc(p.getNome()),
                esc(p.getCpf()),
                String.valueOf(p.getIdade()),
                esc(p.getQueixa()),
                p.getCorTriagem()  != null ? p.getCorTriagem().name()  : "NULL",
                p.getStatus().name(),
                p.getHoraChegada() != null ? p.getHoraChegada().format(FMT) : "NULL",
                p.getHoraTriagem() != null ? p.getHoraTriagem().format(FMT) : "NULL",
                p.getMedicoResponsavel() != null ? String.valueOf(p.getMedicoResponsavel().getId()) : "NULL",
                esc(p.getPrescricaoMedica())
        );
    }

    private Paciente desserializar(String linha, List<ProfissionalSaude> profissionais) {
        try {
            String[] f = linha.split("\\" + SEPARADOR, -1);
            if (f.length < 9) return null;

            int            id         = Integer.parseInt(f[0].trim());
            String         nome       = des(f[1]);
            String         cpf        = des(f[2]);
            int            idade      = Integer.parseInt(f[3].trim());
            String         queixa     = des(f[4]);
            CorTriagem     cor        = "NULL".equals(f[5].trim()) ? null : CorTriagem.valueOf(f[5].trim());
            StatusPaciente status     = StatusPaciente.valueOf(f[6].trim());
            LocalDateTime  chegada    = "NULL".equals(f[7].trim()) ? null : LocalDateTime.parse(f[7].trim(), FMT);
            LocalDateTime  triagem    = "NULL".equals(f[8].trim()) ? null : LocalDateTime.parse(f[8].trim(), FMT);

            Medico medico = null;
            if (f.length > 9 && !"NULL".equals(f[9].trim()) && !f[9].trim().isEmpty()) {
                int medicoId = Integer.parseInt(f[9].trim());
                if (profissionais != null) {
                    medico = (Medico) profissionais.stream()
                             .filter(prof -> prof.getId() == medicoId && prof instanceof Medico)
                             .findFirst().orElse(null);
                }
            }
            String prescricao = f.length > 10 ? des(f[10]) : null;

            return new Paciente(id, nome, cpf, idade, queixa, cor, status, chegada, triagem, medico, prescricao);
        } catch (Exception e) {
            System.err.println("[PacienteRepository] Linha inválida ignorada: " + linha);
            System.err.println("  Causa: " + e.getMessage());
            return null;
        }
    }

    // ─── Utilitários de escape ────────────────────────────────────────────────
    private String esc(String s) {
        if (s == null) return "";
        return s.replace(SEPARADOR, ESC_SEP).replace("\n", " ").replace("\r", " ");
    }

    private String des(String s) {
        if (s == null) return "";
        return s.replace(ESC_SEP, SEPARADOR);
    }
}
