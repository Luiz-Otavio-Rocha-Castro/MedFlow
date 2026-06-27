package hospital.view.panels;

import hospital.controller.HospitalController;
import hospital.model.Leito;
import hospital.model.ProfissionalSaude;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;

public class LeitosPanel extends JPanel {
    private final HospitalController controller;
    private final Runnable onUpdate;

    private JTextField tfIdPacInternar, tfNumLeito;
    private JButton btnInternar;
    private JButton btnAlta;
    
    private DefaultTableModel modelLeitos;
    private JTable tableLeitos;

    public LeitosPanel(HospitalController controller, ProfissionalSaude logado, Runnable onUpdate) {
        this.controller = controller;
        this.onUpdate = onUpdate;

        setLayout(new BorderLayout(10, 10));
        setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));

        JPanel form = new JPanel(new FlowLayout(FlowLayout.LEFT));
        form.setBorder(BorderFactory.createTitledBorder("Internar ou Dar Alta"));
        
        form.add(new JLabel("ID Paciente:"));
        tfIdPacInternar = new JTextField(5);
        form.add(tfIdPacInternar);
        
        form.add(new JLabel("Número do Leito:"));
        tfNumLeito = new JTextField(5);
        form.add(tfNumLeito);

        btnInternar = new JButton("Internar");
        btnAlta = new JButton("Dar Alta");

        btnInternar.addActionListener(e -> {
            try {
                int idPac = Integer.parseInt(tfIdPacInternar.getText());
                int numLeito = Integer.parseInt(tfNumLeito.getText());
                controller.internarPaciente(idPac, numLeito);
                JOptionPane.showMessageDialog(this, "Paciente internado no leito " + numLeito);
                tfIdPacInternar.setText("");
                tfNumLeito.setText("");
                onUpdate.run();
            } catch (Exception ex) {
                JOptionPane.showMessageDialog(this, "Erro: " + ex.getMessage());
            }
        });

        btnAlta.addActionListener(e -> {
            try {
                int idPac = Integer.parseInt(tfIdPacInternar.getText());
                controller.darAlta(idPac);
                JOptionPane.showMessageDialog(this, "Alta concedida com sucesso!");
                tfIdPacInternar.setText("");
                onUpdate.run();
            } catch (Exception ex) {
                JOptionPane.showMessageDialog(this, "Erro: " + ex.getMessage());
            }
        });

        form.add(btnInternar);
        form.add(btnAlta);

        String[] colunas = {"Número Leito", "Ala", "Status"};
        modelLeitos = new DefaultTableModel(colunas, 0);
        tableLeitos = new JTable(modelLeitos);

        add(form, BorderLayout.NORTH);
        add(new JScrollPane(tableLeitos), BorderLayout.CENTER);
    }

    public void atualizarTabela() {
        modelLeitos.setRowCount(0);
        for (Leito l : controller.getLeitos()) {
            modelLeitos.addRow(new Object[]{l.getNumero(), l.getAla(), l.getStatusDescricao()});
        }
    }
}
