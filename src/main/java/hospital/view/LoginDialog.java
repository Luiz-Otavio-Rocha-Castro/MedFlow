package hospital.view;

import hospital.controller.HospitalController;
import hospital.model.ProfissionalSaude;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;

public class LoginDialog extends JDialog {

    private final HospitalController controller;
    private ProfissionalSaude profissionalLogado;
    private boolean autenticado = false;
    private JTextField tfLogin;
    private JPasswordField pfSenha;

    public LoginDialog(JFrame parent, HospitalController controller) {
        super(parent, "Login - MedFlow", true);
        this.controller = controller;

        JPanel panel = new JPanel(new GridLayout(3, 2, 10, 10));
        panel.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));

        panel.add(new JLabel("Login:"));
        tfLogin = new JTextField();
        panel.add(tfLogin);

        panel.add(new JLabel("Senha:"));
        pfSenha = new JPasswordField();
        panel.add(pfSenha);

        JButton btnLogin = new JButton("Entrar");
        JButton btnCancelar = new JButton("Cancelar");

        btnLogin.addActionListener(this::tentarLogin);
        btnCancelar.addActionListener(e -> System.exit(0));

        panel.add(btnLogin);
        panel.add(btnCancelar);

        add(panel);
        pack();
        setLocationRelativeTo(parent);

        addWindowListener(new WindowAdapter() {
            @Override
            public void windowClosing(WindowEvent e) {
                System.exit(0);
            }
        });
    }

    private void tentarLogin(ActionEvent e) {
        String login = tfLogin.getText();
        String senha = new String(pfSenha.getPassword());

        try {
            profissionalLogado = controller.autenticar(login, senha);
            autenticado = true;
            dispose();
        } catch (IllegalArgumentException ex) {
            JOptionPane.showMessageDialog(this, ex.getMessage(), "Erro de Autenticação", JOptionPane.ERROR_MESSAGE);
        }
    }

    public ProfissionalSaude getProfissionalLogado() {
        return profissionalLogado;
    }
    
    public boolean isAutenticado() {
        return autenticado;
    }
}
