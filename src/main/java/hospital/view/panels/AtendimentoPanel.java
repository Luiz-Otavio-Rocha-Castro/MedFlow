package hospital.view.panels;

import hospital.controller.HospitalController;
import hospital.model.Medico;
import hospital.model.Paciente;
import hospital.model.ProfissionalSaude;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;

public class AtendimentoPanel extends JPanel {
    private final HospitalController controller;
    private final ProfissionalSaude profissionalLogado;
    private final Runnable onUpdate;

    private JButton btnChamar;
    private JTextField tfIdAtendimento;
    private JTextField tfPrescricao;
    private JButton btnSalvarAtendimento;
    
    private DefaultTableModel modelFilaAtendimento;
    private JTable tableFilaAtendimento;

    public AtendimentoPanel(HospitalController controller, ProfissionalSaude logado, Runnable onUpdate) {
        this.controller = controller;
        this.profissionalLogado = logado;
        this.onUpdate = onUpdate;

        setLayout(new BorderLayout(10, 10));
        setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));

        JPanel form = new JPanel(new GridLayout(4, 2, 5, 5));
        form.setBorder(BorderFactory.createTitledBorder("Atendimento Médico"));
        
        btnChamar = new JButton("Chamar Próximo");
        form.add(new JLabel("")); // Spacer
        form.add(btnChamar);
        
        form.add(new JLabel("ID Pac. Atendimento:"));
        tfIdAtendimento = new JTextField();
        tfIdAtendimento.setEditable(false);
        form.add(tfIdAtendimento);
        
        form.add(new JLabel("Prescrição:"));
        tfPrescricao = new JTextField();
        form.add(tfPrescricao);
        
        btnSalvarAtendimento = new JButton("Salvar Atendimento");
        form.add(new JLabel("")); // Spacer
        form.add(btnSalvarAtendimento);

        btnChamar.addActionListener(e -> {
            try {
                if (profissionalLogado != null && !(profissionalLogado instanceof Medico)) {
                    throw new IllegalStateException("Apenas médicos podem chamar pacientes.");
                }
                Medico medico = (profissionalLogado instanceof Medico) ? (Medico) profissionalLogado : null;
                Paciente p = controller.chamarProximoPaciente(medico);
                JOptionPane.showMessageDialog(this, "Paciente chamado:\nID: " + p.getId() + "\nNome: " + p.getNome());
                tfIdAtendimento.setText(String.valueOf(p.getId()));
                tfPrescricao.setText("");
                onUpdate.run();
            } catch (Exception ex) {
                JOptionPane.showMessageDialog(this, "Erro: " + ex.getMessage());
            }
        });

        btnSalvarAtendimento.addActionListener(e -> {
            try {
                if (tfIdAtendimento.getText().isEmpty()) {
                    throw new IllegalStateException("Nenhum paciente selecionado. Chame o próximo primeiro.");
                }
                int id = Integer.parseInt(tfIdAtendimento.getText());
                String prescricao = tfPrescricao.getText();
                controller.salvarAtendimento(id, prescricao);
                JOptionPane.showMessageDialog(this, "Atendimento salvo com sucesso!");
                tfIdAtendimento.setText("");
                tfPrescricao.setText("");
                onUpdate.run();
            } catch (Exception ex) {
                JOptionPane.showMessageDialog(this, "Erro: " + ex.getMessage());
            }
        });

        String[] colunas = {"ID", "Nome", "Queixa", "Status", "Classificação", "Resp.", "Prescrição"};
        modelFilaAtendimento = new DefaultTableModel(colunas, 0);
        tableFilaAtendimento = new JTable(modelFilaAtendimento);
        
        JPanel panelTabela = new JPanel(new BorderLayout());
        panelTabela.setBorder(BorderFactory.createTitledBorder("Fila e Em Atendimento"));
        panelTabela.add(new JScrollPane(tableFilaAtendimento), BorderLayout.CENTER);

        add(form, BorderLayout.NORTH);
        add(panelTabela, BorderLayout.CENTER);
    }

    public void atualizarTabela() {
        modelFilaAtendimento.setRowCount(0);
        
        // Pacientes aguardando atendimento
        for (Paciente p : controller.getFilaAtendimento()) {
            String cor = (p.getCorTriagem() != null) ? p.getCorTriagem().getNome() : "";
            modelFilaAtendimento.addRow(new Object[]{p.getId(), p.getNome(), p.getQueixa(), p.getStatus().getDescricao(), cor, "", ""});
        }
        
        // Pacientes já em atendimento
        for (Paciente p : controller.getPacientesEmAtendimento()) {
            String cor = (p.getCorTriagem() != null) ? p.getCorTriagem().getNome() : "";
            String resp = p.getMedicoResponsavel() != null ? p.getMedicoResponsavel().getNome() : "";
            String presc = p.getPrescricaoMedica() != null ? p.getPrescricaoMedica() : "";
            modelFilaAtendimento.addRow(new Object[]{p.getId(), p.getNome(), p.getQueixa(), p.getStatus().getDescricao(), cor, resp, presc});
        }
    }
}
