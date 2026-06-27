package hospital.view.panels;

import hospital.controller.HospitalController;
import hospital.model.Paciente;
import hospital.model.ProfissionalSaude;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;

public class PacientesPanel extends JPanel {
    private final HospitalController controller;
    private final Runnable onUpdate;

    private JTextField tfNomePac, tfCpfPac, tfIdadePac, tfQueixaPac;
    private DefaultTableModel modelPacientes;
    private JTable tablePacientes;

    public PacientesPanel(HospitalController controller, ProfissionalSaude logado, Runnable onUpdate) {
        this.controller = controller;
        this.onUpdate = onUpdate;

        setLayout(new BorderLayout(10, 10));
        setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));

        JPanel form = new JPanel(new GridLayout(5, 2, 5, 5));
        form.setBorder(BorderFactory.createTitledBorder("Gerenciar Pacientes"));

        form.add(new JLabel("Nome:"));
        tfNomePac = new JTextField();
        form.add(tfNomePac);

        form.add(new JLabel("CPF (somente números):"));
        tfCpfPac = new JTextField();
        form.add(tfCpfPac);

        form.add(new JLabel("Idade:"));
        tfIdadePac = new JTextField();
        form.add(tfIdadePac);

        form.add(new JLabel("Queixa Principal:"));
        tfQueixaPac = new JTextField();
        form.add(tfQueixaPac);

        JPanel botoes = new JPanel(new FlowLayout());
        JButton btnCadastrar = new JButton("Cadastrar");
        JButton btnEditar = new JButton("Editar");
        JButton btnRemover = new JButton("Remover");
        JButton btnLimpar = new JButton("Limpar");

        btnCadastrar.addActionListener(e -> {
            try {
                controller.cadastrarPaciente(tfNomePac.getText(), tfCpfPac.getText(), Integer.parseInt(tfIdadePac.getText()), tfQueixaPac.getText());
                JOptionPane.showMessageDialog(this, "Paciente cadastrado com sucesso!");
                limparFormulario();
                onUpdate.run();
            } catch (Exception ex) {
                JOptionPane.showMessageDialog(this, "Erro: " + ex.getMessage());
            }
        });

        btnEditar.addActionListener(e -> {
            try {
                int row = tablePacientes.getSelectedRow();
                if (row < 0) {
                    JOptionPane.showMessageDialog(this, "Selecione um paciente na tabela para editar.");
                    return;
                }
                int id = Integer.parseInt(modelPacientes.getValueAt(row, 0).toString());
                controller.editarPaciente(id, tfNomePac.getText(), tfCpfPac.getText(), Integer.parseInt(tfIdadePac.getText()), tfQueixaPac.getText());
                JOptionPane.showMessageDialog(this, "Paciente editado com sucesso!");
                onUpdate.run();
            } catch (Exception ex) {
                JOptionPane.showMessageDialog(this, "Erro: " + ex.getMessage());
            }
        });

        btnRemover.addActionListener(e -> {
            try {
                int row = tablePacientes.getSelectedRow();
                if (row < 0) {
                    JOptionPane.showMessageDialog(this, "Selecione um paciente na tabela para remover.");
                    return;
                }
                int id = Integer.parseInt(modelPacientes.getValueAt(row, 0).toString());
                controller.removerPaciente(id);
                JOptionPane.showMessageDialog(this, "Paciente removido com sucesso!");
                limparFormulario();
                onUpdate.run();
            } catch (Exception ex) {
                JOptionPane.showMessageDialog(this, "Erro: " + ex.getMessage());
            }
        });

        btnLimpar.addActionListener(e -> limparFormulario());

        botoes.add(btnCadastrar); 
        botoes.add(btnEditar); 
        botoes.add(btnRemover); 
        botoes.add(btnLimpar);
        form.add(botoes); 
        
        String[] colunas = {"ID", "Nome", "CPF", "Idade", "Queixa", "Status"};
        modelPacientes = new DefaultTableModel(colunas, 0);
        tablePacientes = new JTable(modelPacientes);
        
        tablePacientes.getSelectionModel().addListSelectionListener(e -> {
            int row = tablePacientes.getSelectedRow();
            if(row >= 0) {
                tfNomePac.setText(modelPacientes.getValueAt(row, 1).toString());
                tfCpfPac.setText(modelPacientes.getValueAt(row, 2).toString());
                tfIdadePac.setText(modelPacientes.getValueAt(row, 3).toString());
                tfQueixaPac.setText(modelPacientes.getValueAt(row, 4).toString());
            }
        });

        add(form, BorderLayout.NORTH);
        add(new JScrollPane(tablePacientes), BorderLayout.CENTER);
    }

    private void limparFormulario() {
        tfNomePac.setText(""); tfCpfPac.setText(""); tfIdadePac.setText(""); tfQueixaPac.setText("");
    }

    public void atualizarTabela() {
        modelPacientes.setRowCount(0);
        for (Paciente p : controller.getTodosOsPacientes()) {
            modelPacientes.addRow(new Object[]{p.getId(), p.getNome(), p.getCpf(), p.getIdade(), p.getQueixa(), p.getStatus().getDescricao()});
        }
    }
}
