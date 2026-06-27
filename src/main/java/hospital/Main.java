package hospital;

import hospital.view.MainFrame;

import javax.swing.*;

/**
 * Main — Ponto de entrada do sistema MedFlow.
 *
 * Inicializa o tema visual e abre a janela principal na thread de eventos do Swing.
 */
public class Main {

    public static void main(String[] args) {
        // Inicia a interface gráfica na EDT (Event Dispatch Thread) do Swing
        SwingUtilities.invokeLater(() -> {
            MainFrame frame = new MainFrame();
            frame.setVisible(true);
        });
    }
}
