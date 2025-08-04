package crud.clinica.view;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;

import crud.clinica.view.PacienteFormDialog;
import crud.clinica.database.DatabaseConnectionMySQL;

public class MainView extends JFrame {
    private static final long serialVersionUID = 1L;

    public MainView() {
        setTitle("Sistema da Clínica");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setSize(800, 600);
        setLocationRelativeTo(null); // Centraliza a janela
        setLayout(new BorderLayout());

        // ---------------------
        // JMenuBar
        // ---------------------
        JMenuBar menuBar = new JMenuBar();

        // Menu Pacientes
        JMenu menuPacientes = new JMenu("Pacientes");

        JMenuItem itemNovoPaciente = new JMenuItem("Novo");
        itemNovoPaciente.addActionListener(e -> abrirJanela("Novo Paciente"));

        JMenuItem itemEditarPaciente = new JMenuItem("Editar");
        itemEditarPaciente.addActionListener(e -> abrirJanela("Editar Paciente"));

        JMenuItem itemLocalizarPaciente = new JMenuItem("Localizar");
        itemLocalizarPaciente.addActionListener(e -> abrirJanela("Localizar Paciente"));

        JMenuItem itemExcluirPaciente = new JMenuItem("Excluir");
        itemExcluirPaciente.addActionListener(e -> abrirJanela("Excluir Paciente"));

        JMenuItem itemListarPaciente = new JMenuItem("Listar");
        itemListarPaciente.addActionListener(e -> abrirJanela("Listar Pacientes"));

        menuPacientes.add(itemNovoPaciente);
        menuPacientes.add(itemEditarPaciente);
        menuPacientes.add(itemLocalizarPaciente);
        menuPacientes.add(itemListarPaciente);
        menuPacientes.add(itemExcluirPaciente);

        // Menu Exames
        JMenu menuExames = new JMenu("Exames");

        JMenuItem itemNovoExame = new JMenuItem("Novo");
        itemNovoExame.addActionListener(e -> abrirJanela("Novo Exame"));

        JMenuItem itemEditarExame = new JMenuItem("Editar");
        itemEditarExame.addActionListener(e -> abrirJanela("Editar Exame"));

        JMenuItem itemLocalizarExame = new JMenuItem("Localizar");
        itemLocalizarExame.addActionListener(e -> abrirJanela("Localizar Exame"));

        JMenuItem itemExcluirExame = new JMenuItem("Excluir");
        itemExcluirExame.addActionListener(e -> abrirJanela("Excluir Exame"));

        JMenuItem itemListarExame = new JMenuItem("Listar");
        itemListarExame.addActionListener(e -> abrirJanela("Listar Exames"));

        menuExames.add(itemNovoExame);
        menuExames.add(itemEditarExame);
        menuExames.add(itemLocalizarExame);
        menuExames.add(itemListarExame);
        menuExames.add(itemExcluirExame);

        // Menu Sair
        JMenu menuSair = new JMenu("Sair");
        JMenuItem itemSair = new JMenuItem("Sair");
        itemSair.addActionListener(this::confirmarSaida);
        menuSair.add(itemSair);

        // Adiciona todos ao menu bar
        menuBar.add(menuPacientes);
        menuBar.add(menuExames);
        menuBar.add(menuSair);

        setJMenuBar(menuBar);

        // ---------------------
        // Painel Central
        // ---------------------
        JPanel painelCentral = new JPanel(new BorderLayout());
        JLabel labelBemVindo = new JLabel("Bem-vindo ao Sistema da Clínica", SwingConstants.CENTER);
        labelBemVindo.setFont(new Font("Arial", Font.BOLD, 24));
        painelCentral.add(labelBemVindo, BorderLayout.CENTER);

        add(painelCentral, BorderLayout.CENTER);
    }

    private void abrirJanela(String titulo) {
        switch (titulo) {
            case "Novo Paciente":
                PacienteFormDialog cadastro = new PacienteFormDialog(
                    this,
                    new DatabaseConnectionMySQL(),
                    null // criação de novo paciente
                );
                cadastro.setVisible(true);
                break;

            case "Listar Pacientes":
                JDialog listarPacientesDialog = new JDialog(this, "Lista de Pacientes", true);
                listarPacientesDialog.setSize(600, 400);
                listarPacientesDialog.setLocationRelativeTo(this);
                listarPacientesDialog.setLayout(new BorderLayout());
                listarPacientesDialog.add(new PacientePanel(), BorderLayout.CENTER);
                listarPacientesDialog.setVisible(true);
                break;

            case "Listar Exames":
                JDialog listarExamesDialog = new JDialog(this, "Lista de Exames", true);
                listarExamesDialog.setSize(600, 400);
                listarExamesDialog.setLocationRelativeTo(this);
                listarExamesDialog.setLayout(new BorderLayout());
                listarExamesDialog.add(new ExamePanel(), BorderLayout.CENTER);
                listarExamesDialog.setVisible(true);
                break;

            case "Editar Paciente":
                JOptionPane.showMessageDialog(this, "Janela de edição ainda não implementada");
                break;

            case "Localizar Paciente":
                JOptionPane.showMessageDialog(this, "Janela de localização ainda não implementada");
                break;

            case "Excluir Paciente":
                JOptionPane.showMessageDialog(this, "Janela de exclusão ainda não implementada");
                break;

            case "Novo Exame":
            case "Editar Exame":
            case "Localizar Exame":
            case "Excluir Exame":
                JOptionPane.showMessageDialog(this, "Funcionalidade de exames ainda não implementada");
                break;

            default:
                JOptionPane.showMessageDialog(this, "Função ainda não implementada: " + titulo);
        }
    }

    private void confirmarSaida(ActionEvent e) {
        int opcao = JOptionPane.showConfirmDialog(
            this,
            "Deseja realmente sair?",
            "Confirmação de Saída",
            JOptionPane.YES_NO_OPTION
        );

        if (opcao == JOptionPane.YES_OPTION) {
            System.exit(0);
        }
    }
}
