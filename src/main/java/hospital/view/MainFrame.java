package hospital.view;

import hospital.controller.HospitalController;
import hospital.model.Enfermeiro;
import hospital.model.Medico;
import hospital.model.ProfissionalSaude;
import hospital.view.panels.*;

import javax.swing.*;
import java.awt.*;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;

/**
 * MainFrame — Interface Gráfica Principal.
 */
public class MainFrame extends JFrame {

    private final HospitalController controller;
    private ProfissionalSaude profissionalLogado;

    // Contadores
    private JLabel lblLeitosDisp;
    private JLabel lblFilaEspera;

    // Painéis das Abas
    private PacientesPanel pnlPacientes;
    private ProfissionaisPanel pnlProfissionais;
    private TriagemPanel pnlTriagem;
    private AtendimentoPanel pnlAtendimento;
    private LeitosPanel pnlLeitos;

    public MainFrame() {
        controller = new HospitalController();
        
        // 1. Exibir a tela de Login Modal
        LoginDialog loginDialog = new LoginDialog(this, controller);
        loginDialog.setVisible(true);

        if (!loginDialog.isAutenticado()) {
            System.exit(0);
        }

        profissionalLogado = loginDialog.getProfissionalLogado();

        setTitle("MedFlow - Sistema de Gestão Hospitalar" + 
                (profissionalLogado != null ? " | Logado como: " + profissionalLogado.getNome() : " | MODO ADMIN"));
        setSize(900, 700);
        setDefaultCloseOperation(JFrame.DO_NOTHING_ON_CLOSE);
        setLocationRelativeTo(null);
        setLayout(new BorderLayout());

        // 2. Evento para salvar os dados ao fechar a janela
        addWindowListener(new WindowAdapter() {
            @Override
            public void windowClosing(WindowEvent e) {
                controller.salvarTudo();
                System.exit(0);
            }
        });

        // 3. Montar Painel de Contadores
        JPanel pnlNorte = new JPanel(new FlowLayout(FlowLayout.CENTER, 40, 10));
        lblLeitosDisp = new JLabel("Leitos Disponíveis: 0");
        lblFilaEspera = new JLabel("Pacientes na Fila de Espera: 0");
        lblLeitosDisp.setFont(new Font("Arial", Font.BOLD, 14));
        lblFilaEspera.setFont(new Font("Arial", Font.BOLD, 14));
        lblLeitosDisp.setForeground(new Color(0, 102, 51));
        lblFilaEspera.setForeground(new Color(204, 51, 0));
        pnlNorte.add(lblLeitosDisp);
        pnlNorte.add(lblFilaEspera);
        add(pnlNorte, BorderLayout.NORTH);

        // Callback de atualização passado para cada painel
        Runnable onUpdate = this::atualizarTodasTabelas;

        // 4. Instanciar Painéis
        pnlPacientes = new PacientesPanel(controller, profissionalLogado, onUpdate);
        pnlProfissionais = new ProfissionaisPanel(controller, profissionalLogado, onUpdate);
        pnlTriagem = new TriagemPanel(controller, profissionalLogado, onUpdate);
        pnlAtendimento = new AtendimentoPanel(controller, profissionalLogado, onUpdate);
        pnlLeitos = new LeitosPanel(controller, profissionalLogado, onUpdate);

        // 5. Montar Abas com Bloqueios de Permissão
        JTabbedPane tabbedPane = new JTabbedPane();
        tabbedPane.addTab("Pacientes", pnlPacientes);
        tabbedPane.addTab("Profissionais", pnlProfissionais);
        
        if (profissionalLogado == null || profissionalLogado instanceof Enfermeiro) {
            tabbedPane.addTab("Triagem", pnlTriagem);
        }
        
        if (profissionalLogado == null || profissionalLogado instanceof Medico) {
            tabbedPane.addTab("Atendimento Médico", pnlAtendimento);
            tabbedPane.addTab("Leitos", pnlLeitos); // Apenas médico/admin internam e dão alta
        }

        tabbedPane.addChangeListener(e -> atualizarTodasTabelas());

        add(tabbedPane, BorderLayout.CENTER);
        
        atualizarTodasTabelas();
    }

    private void atualizarTodasTabelas() {
        int leitosDisponiveis = controller.getLeitosDisponiveis().size();
        int pacientesNaFila = controller.getFilaTriagem().size() + controller.getFilaAtendimento().size();
        lblLeitosDisp.setText("Leitos Disponíveis: " + leitosDisponiveis);
        lblFilaEspera.setText("Pacientes na Fila de Espera: " + pacientesNaFila);

        pnlPacientes.atualizarTabela();
        pnlProfissionais.atualizarTabela();
        pnlTriagem.atualizarTabela();
        pnlAtendimento.atualizarTabela();
        pnlLeitos.atualizarTabela();
    }
}
