package hospital.view.panels;

import hospital.controller.HospitalController;
import hospital.model.Enfermeiro;
import hospital.model.Medico;
import hospital.model.ProfissionalSaude;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;

public class ProfissionaisPanel extends JPanel {
    private final HospitalController controller;
    private final Runnable onUpdate;

    private JTextField tfNomeProf, tfLoginProf, tfSenhaProf, tfExtraProf, tfCrmProf;
    private JComboBox<String> cbTipoProf;
    private DefaultTableModel modelProfissionais;
    private JTable tableProfissionais;

    public ProfissionaisPanel(HospitalController controller, ProfissionalSaude logado, Runnable onUpdate) {
        this.controller = controller;
        this.onUpdate = onUpdate;

        setLayout(new BorderLayout(10, 10));
        setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));

        JPanel form = new JPanel(new GridLayout(7, 2, 5, 5));
        form.setBorder(BorderFactory.createTitledBorder("Gerenciar Profissionais"));

        form.add(new JLabel("Tipo (Somente p/ Cadastro):"));
        cbTipoProf = new JComboBox<>(new String[]{"Médico", "Enfermeiro"});
        form.add(cbTipoProf);

        form.add(new JLabel("Nome:"));
        tfNomeProf = new JTextField();
        form.add(tfNomeProf);

        form.add(new JLabel("Login:"));
        tfLoginProf = new JTextField();
        form.add(tfLoginProf);

        form.add(new JLabel("Senha:"));
        tfSenhaProf = new JTextField();
        form.add(tfSenhaProf);

        form.add(new JLabel("Especialidade ou COREN:"));
        tfExtraProf = new JTextField();
        form.add(tfExtraProf);

        form.add(new JLabel("CRM (Se Médico):"));
        tfCrmProf = new JTextField();
        form.add(tfCrmProf);

        JPanel botoes = new JPanel(new FlowLayout());
        JButton btnCadastrar = new JButton("Cadastrar");
        JButton btnEditar = new JButton("Editar");
        JButton btnRemover = new JButton("Remover");
        JButton btnLimpar = new JButton("Limpar");

        btnCadastrar.addActionListener(e -> {
            try {
                if (cbTipoProf.getSelectedIndex() == 0) {
                    controller.cadastrarMedico(tfNomeProf.getText(), tfLoginProf.getText(), tfSenhaProf.getText(), tfExtraProf.getText(), tfCrmProf.getText());
                } else {
                    controller.cadastrarEnfermeiro(tfNomeProf.getText(), tfLoginProf.getText(), tfSenhaProf.getText(), tfExtraProf.getText());
                }
                JOptionPane.showMessageDialog(this, "Profissional cadastrado com sucesso!");
                limparFormulario();
                onUpdate.run();
            } catch (Exception ex) {
                JOptionPane.showMessageDialog(this, "Erro: " + ex.getMessage());
            }
        });

        btnEditar.addActionListener(e -> {
            try {
                int row = tableProfissionais.getSelectedRow();
                if (row < 0) {
                    JOptionPane.showMessageDialog(this, "Selecione um profissional na tabela para editar.");
                    return;
                }
                int id = Integer.parseInt(modelProfissionais.getValueAt(row, 0).toString());
                controller.editarProfissional(id, tfNomeProf.getText(), tfLoginProf.getText(), tfSenhaProf.getText(), tfExtraProf.getText(), tfCrmProf.getText());
                JOptionPane.showMessageDialog(this, "Profissional editado com sucesso!");
                onUpdate.run();
            } catch (Exception ex) {
                JOptionPane.showMessageDialog(this, "Erro: " + ex.getMessage());
            }
        });

        btnRemover.addActionListener(e -> {
            try {
                int row = tableProfissionais.getSelectedRow();
                if (row < 0) {
                    JOptionPane.showMessageDialog(this, "Selecione um profissional na tabela para remover.");
                    return;
                }
                int id = Integer.parseInt(modelProfissionais.getValueAt(row, 0).toString());
                controller.removerProfissional(id);
                JOptionPane.showMessageDialog(this, "Profissional removido com sucesso!");
                limparFormulario();
                onUpdate.run();
            } catch (Exception ex) {
                JOptionPane.showMessageDialog(this, "Erro: " + ex.getMessage());
            }
        });
        
        btnLimpar.addActionListener(e -> limparFormulario());

        botoes.add(btnCadastrar); botoes.add(btnEditar); botoes.add(btnRemover); botoes.add(btnLimpar);
        form.add(botoes);

        String[] colunas = {"ID", "Tipo", "Nome", "Login", "Extra"};
        modelProfissionais = new DefaultTableModel(colunas, 0);
        tableProfissionais = new JTable(modelProfissionais);
        
        tableProfissionais.getSelectionModel().addListSelectionListener(e -> {
            int row = tableProfissionais.getSelectedRow();
            if(row >= 0) {
                int id = Integer.parseInt(modelProfissionais.getValueAt(row, 0).toString());
                ProfissionalSaude p = controller.getProfissionais().stream().filter(prof -> prof.getId() == id).findFirst().orElse(null);
                if (p != null) {
                    tfNomeProf.setText(p.getNome());
                    tfLoginProf.setText(p.getLogin());
                    tfSenhaProf.setText(p.getSenha());
                    if (p instanceof Medico m) {
                        cbTipoProf.setSelectedIndex(0);
                        tfExtraProf.setText(m.getEspecialidade());
                        tfCrmProf.setText(m.getCrm());
                    } else if (p instanceof Enfermeiro enf) {
                        cbTipoProf.setSelectedIndex(1);
                        tfExtraProf.setText(enf.getCoren());
                        tfCrmProf.setText("");
                    }
                }
            }
        });

        add(form, BorderLayout.NORTH);
        add(new JScrollPane(tableProfissionais), BorderLayout.CENTER);
    }

    private void limparFormulario() {
        tfNomeProf.setText(""); tfLoginProf.setText(""); tfSenhaProf.setText(""); tfExtraProf.setText(""); tfCrmProf.setText("");
    }

    public void atualizarTabela() {
        modelProfissionais.setRowCount(0);
        for (ProfissionalSaude p : controller.getProfissionais()) {
            modelProfissionais.addRow(new Object[]{p.getId(), p.getTipo(), p.getNome(), p.getLogin(), p.getInfoAdicional()});
        }
    }
}
