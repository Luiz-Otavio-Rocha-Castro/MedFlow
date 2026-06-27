package hospital.repository;

import hospital.model.Leito;
import hospital.model.Paciente;

import java.io.*;
import java.util.ArrayList;
import java.util.List;

/**
 * LeitoRepository — Camada de persistência para o estado dos Leitos.
 *
 * Demonstra MANIPULAÇÃO DE ARQUIVOS:
 *  - Salva e carrega a ocupação dos leitos usando BufferedWriter / BufferedReader.
 *
 * Formato da linha no arquivo (separado por '|'):
 *   numero | ala | pacienteId  (ou "LIVRE" se disponível)
 */
public class LeitoRepository {

    private static final String ARQUIVO = "data/leitos.txt";

    public LeitoRepository() {
        new File("data").mkdirs();
    }

    /**
     * Salva o estado de ocupação de todos os leitos.
     */
    public void salvarOcupacao(List<Leito> leitos) {
        try (BufferedWriter bw = new BufferedWriter(new FileWriter(ARQUIVO, false))) {
            for (Leito l : leitos) {
                String pacId = l.isDisponivel()
                        ? "LIVRE"
                        : String.valueOf(l.getPacienteAlocado().getId());
                bw.write(l.getNumero() + "|" + l.getAla() + "|" + pacId);
                bw.newLine();
            }
        } catch (IOException e) {
            System.err.println("[LeitoRepository] Erro ao salvar: " + e.getMessage());
        }
    }

    /**
     * Carrega os leitos e, para os ocupados, associa o Paciente correto
     * a partir da lista fornecida.
     *
     * @param pacientes Lista de todos os pacientes (para associar ao leito)
     * @return Lista de leitos com ocupação restaurada
     */
    public List<Leito> carregar(List<Paciente> pacientes) {
        List<Leito> leitos = new ArrayList<>();
        File arquivo = new File(ARQUIVO);
        if (!arquivo.exists()) return leitos;

        try (BufferedReader br = new BufferedReader(new FileReader(arquivo))) {
            String linha;
            while ((linha = br.readLine()) != null) {
                linha = linha.trim();
                if (linha.isEmpty()) continue;

                String[] f = linha.split("\\|", -1);
                if (f.length < 3) continue;

                int    numero = Integer.parseInt(f[0].trim());
                String ala    = f[1].trim();
                String pacId  = f[2].trim();

                Leito leito = new Leito(numero, ala);
                if (!"LIVRE".equals(pacId)) {
                    int idPac = Integer.parseInt(pacId);
                    pacientes.stream()
                             .filter(p -> p.getId() == idPac)
                             .findFirst()
                             .ifPresent(leito::alocarPaciente);
                }
                leitos.add(leito);
            }
        } catch (IOException e) {
            System.err.println("[LeitoRepository] Erro ao carregar: " + e.getMessage());
        }
        return leitos;
    }
}
