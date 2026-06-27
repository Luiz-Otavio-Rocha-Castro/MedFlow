package hospital.view.panels;

import hospital.controller.HospitalController;
import hospital.model.CorTriagem;
import hospital.model.Paciente;
import hospital.model.ProfissionalSaude;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;

public class TriagemPanel extends JPanel {
    private final HospitalController controller;
    private final Runnable onUpdate;

    private JTextField tfIdTriagem;
    private JComboBox<String> cbCorTriagem;
    private JButton btnTriar;
    
    private DefaultTableModel modelFilaTriagem;
    private JTable tableFilaTriagem;

    public TriagemPanel(HospitalController controller, ProfissionalSaude logado, Runnable onUpdate) {
        this.controller = controller;
        this.onUpdate = onUpdate;

        setLayout(new BorderLayout(10, 10));
        setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));

        JPanel form = new JPanel(new GridLayout(3, 2, 5, 5));
        form.setBorder(BorderFactory.createTitledBorder("Realizar Triagem"));
        
        form.add(new JLabel("ID Paciente:"));
        tfIdTriagem = new JTextField();
        form.add(tfIdTriagem);
        
        form.add(new JLabel("Cor:"));
        cbCorTriagem = new JComboBox<>(new String[]{"VERMELHO", "AMARELO", "VERDE"});
        form.add(cbCorTriagem);
        
        btnTriar = new JButton("Fazer Triagem");
        form.add(new JLabel("")); // Spacer
        form.add(btnTriar);
        
        btnTriar.addActionListener(e -> {
            try {
                int id = Integer.parseInt(tfIdTriagem.getText());
                CorTriagem cor = CorTriagem.valueOf(cbCorTriagem.getSelectedItem().toString());
                controller.realizarTriagem(id, cor);
                JOptionPane.showMessageDialog(this, "Triagem realizada com sucesso!");
                tfIdTriagem.setText("");
                onUpdate.run();
            } catch (Exception ex) {
                JOptionPane.showMessageDialog(this, "Erro: " + ex.getMessage());
            }
        });

        // Tabela apenas de triagem
        String[] colunas = {"ID", "Nome", "Queixa", "Status"};
        modelFilaTriagem = new DefaultTableModel(colunas, 0);
        tableFilaTriagem = new JTable(modelFilaTriagem);
        
        JPanel panelTabela = new JPanel(new BorderLayout());
        panelTabela.setBorder(BorderFactory.createTitledBorder("Fila de Espera p/ Triagem"));
        panelTabela.add(new JScrollPane(tableFilaTriagem), BorderLayout.CENTER);

        add(form, BorderLayout.NORTH);
        add(panelTabela, BorderLayout.CENTER);
    }

    public void atualizarTabela() {
        modelFilaTriagem.setRowCount(0);
        for (Paciente p : controller.getFilaTriagem()) {
            modelFilaTriagem.addRow(new Object[]{p.getId(), p.getNome(), p.getQueixa(), p.getStatus().getDescricao()});
        }
    }
}
