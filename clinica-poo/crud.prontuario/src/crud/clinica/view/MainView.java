package crud.clinica.view;

import crud.clinica.database.DatabaseConnectionMySQL;
import crud.clinica.dao.ExameDAO;
import crud.clinica.dao.PacienteDAO;
import crud.clinica.view.ExameEdicaoDialog;

import crud.clinica.database.IConnection;
import crud.clinica.model.Exame;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import crud.clinica.view.ExameListDialog;

public class MainView extends JFrame {
    private static final long serialVersionUID = 1L;

    private IConnection dbConnection;

    public MainView() {
        setTitle("Sistema da Clínica");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setSize(800, 600);
        setLocationRelativeTo(null); // Centraliza a janela
        setLayout(new BorderLayout());

        dbConnection = new DatabaseConnectionMySQL(); // conexão para usar na aplicação

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
                    dbConnection,
                    null
                );
                cadastro.setVisible(true);
                break;

            case "Listar Pacientes":
                abrirPacienteListDialog(false, false);
                break;

            case "Localizar Paciente":
                PacienteLocalizacaoDialog localizar = new PacienteLocalizacaoDialog(this, dbConnection);
                localizar.setVisible(true);
                break;


            case "Editar Paciente":
                abrirPacienteListDialog(true, false); // editar habilitado
                break;

            case "Excluir Paciente":
                abrirPacienteListDialog(false, true); // excluir habilitado
                break;

            case "Novo Exame":
                ExameDAO exameDAO = new ExameDAO(dbConnection);
                PacienteDAO pacienteDAO = new PacienteDAO(dbConnection);
                ExameFormDialog novoExame = new ExameFormDialog(this, exameDAO, pacienteDAO, null);
                novoExame.setVisible(true);
                break;

            case "Editar Exame":
                ExameListDialog listaExames = new ExameListDialog(this, dbConnection, true, false);
                listaExames.setVisible(true);
                break;
                
            case "Localizar Exame":
                ExameDAO exameDAO2 = new ExameDAO(dbConnection);
                PacienteDAO pacienteDAO2 = new PacienteDAO(dbConnection);
                ExameLocalizacaoDialog localizarExameLoc = new ExameLocalizacaoDialog(
                    this,
                    exameDAO2,
                    pacienteDAO2
                );
                localizarExameLoc.setVisible(true);
                break;
                
            case "Listar Exames":
//                ExameDAO exameDAO3 = new ExameDAO(dbConnection);
//                PacienteDAO pacienteDAO3 = new PacienteDAO(dbConnection);
                ExameListDialog listaExames3 = new ExameListDialog(this, dbConnection, false, false);
                listaExames3.setVisible(true);
                break;

                
            case "Excluir Exame":
                ExameListDialog listaExamesExcluir = new ExameListDialog(this, dbConnection, false, true); // só excluir habilitado
                listaExamesExcluir.setVisible(true);
                break;

            default:
                JOptionPane.showMessageDialog(this, "Função ainda não implementada: " + titulo);
        }
    }

    /**
     * Abre o diálogo de lista de pacientes com opção de editar e/ou excluir habilitada.
     * @param podeEditar se o usuário poderá editar pacientes.
     * @param podeExcluir se o usuário poderá excluir pacientes.
     */
    private void abrirPacienteListDialog(boolean podeEditar, boolean podeExcluir) {
        PacienteListDialog dialog = new PacienteListDialog(this, dbConnection, podeEditar, podeExcluir);
        dialog.setVisible(true);
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
