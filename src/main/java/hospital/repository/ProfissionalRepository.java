package hospital.repository;

import hospital.model.Enfermeiro;
import hospital.model.Medico;
import hospital.model.ProfissionalSaude;

import java.io.*;
import java.util.ArrayList;
import java.util.List;

/**
 * ProfissionalRepository — Camada de persistência para Profissionais de Saúde.
 *
 * Demonstra MANIPULAÇÃO DE ARQUIVOS:
 *  - Salva e carrega dados usando BufferedWriter / BufferedReader em arquivos .txt
 *
 * Formato da linha no arquivo (separado por '|'):
 *   Para Médico:     MEDICO     | id | nome | login | senha | especialidade | crm
 *   Para Enfermeiro: ENFERMEIRO | id | nome | login | senha | coren         | (vazio)
 */
public class ProfissionalRepository {

    private static final String ARQUIVO   = "data/profissionais.txt";
    private static final String SEP       = "|";
    private static final String ESC_SEP   = "\u00A7";

    public ProfissionalRepository() {
        new File("data").mkdirs();
    }

    /**
     * Sobrescreve o arquivo com a lista atual de profissionais.
     */
    public void salvarTodos(List<ProfissionalSaude> lista) {
        try (BufferedWriter bw = new BufferedWriter(new FileWriter(ARQUIVO, false))) {
            for (ProfissionalSaude p : lista) {
                bw.write(serializar(p));
                bw.newLine();
            }
        } catch (IOException e) {
            System.err.println("[ProfissionalRepository] Erro ao salvar: " + e.getMessage());
        }
    }

    /**
     * Lê todos os profissionais do arquivo .txt.
     */
    public List<ProfissionalSaude> carregarTodos() {
        List<ProfissionalSaude> lista = new ArrayList<>();
        File arquivo = new File(ARQUIVO);
        if (!arquivo.exists()) return lista;

        try (BufferedReader br = new BufferedReader(new FileReader(arquivo))) {
            String linha;
            while ((linha = br.readLine()) != null) {
                linha = linha.trim();
                if (!linha.isEmpty()) {
                    ProfissionalSaude p = desserializar(linha);
                    if (p != null) lista.add(p);
                }
            }
        } catch (IOException e) {
            System.err.println("[ProfissionalRepository] Erro ao carregar: " + e.getMessage());
        }
        return lista;
    }

    // ─── Serialização ─────────────────────────────────────────────────────────

    private String serializar(ProfissionalSaude p) {
        // Flag de tipo explícita para evitar dependência do nome do método getTipo()
        String tipo  = (p instanceof Medico) ? "MEDICO" : "ENFERMEIRO";
        String base  = String.join(SEP,
                tipo,
                String.valueOf(p.getId()),
                esc(p.getNome()),
                esc(p.getLogin()),
                esc(p.getSenha()));

        if (p instanceof Medico m) {
            return base + SEP + esc(m.getEspecialidade()) + SEP + esc(m.getCrm());
        } else if (p instanceof Enfermeiro e) {
            return base + SEP + esc(e.getCoren()) + SEP;
        }
        return base;
    }

    private ProfissionalSaude desserializar(String linha) {
        try {
            String[] f = linha.split("\\" + SEP, -1);
            if (f.length < 6) return null;

            String tipo  = f[0].trim();
            int    id    = Integer.parseInt(f[1].trim());
            String nome  = des(f[2]);
            String login = des(f[3]);
            String senha = des(f[4]);

            if ("MEDICO".equals(tipo)) {
                String especialidade = f.length > 5 ? des(f[5]) : "";
                String crm           = f.length > 6 ? des(f[6]) : "";
                return new Medico(id, nome, login, senha, especialidade, crm);
            } else if ("ENFERMEIRO".equals(tipo)) {
                String coren = f.length > 5 ? des(f[5]) : "";
                return new Enfermeiro(id, nome, login, senha, coren);
            }
        } catch (Exception e) {
            System.err.println("[ProfissionalRepository] Linha inválida ignorada: " + linha);
            System.err.println("  Causa: " + e.getMessage());
        }
        return null;
    }

    // ─── Utilitários de escape ────────────────────────────────────────────────
    private String esc(String s) {
        if (s == null) return "";
        return s.replace(SEP, ESC_SEP).replace("\n", " ").replace("\r", " ");
    }

    private String des(String s) {
        if (s == null) return "";
        return s.replace(ESC_SEP, SEP);
    }
}
