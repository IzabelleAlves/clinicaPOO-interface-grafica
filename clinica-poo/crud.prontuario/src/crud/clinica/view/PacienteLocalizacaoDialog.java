package crud.clinica.view;

import crud.clinica.dao.PacienteDAO;
import crud.clinica.database.IConnection;
import crud.clinica.model.Paciente;

import javax.swing.*;
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;

public class PacienteLocalizacaoDialog extends JDialog {

    private static final long serialVersionUID = 1L;
    private final PacienteDAO pacienteDAO;
    private final JTextField buscaField;
    private final JTable tabela;
    private final DefaultTableModel modeloTabela;

    public PacienteLocalizacaoDialog(Window parent, IConnection dbConnection) {
        super(parent, "Localizar Paciente por CPF", ModalityType.APPLICATION_MODAL);
        this.pacienteDAO = new PacienteDAO(dbConnection);

        setSize(700, 400);
        setLocationRelativeTo(parent);
        setLayout(new BorderLayout(10, 10));

        // Painel de Pesquisa
        JPanel painelPesquisa = new JPanel(new FlowLayout(FlowLayout.LEFT));

        buscaField = new JTextField(30);
        JButton btnPesquisar = new JButton("Pesquisar");
        btnPesquisar.addActionListener(e -> filtrar());

        painelPesquisa.add(new JLabel("CPF do Paciente:"));
        painelPesquisa.add(buscaField);
        painelPesquisa.add(btnPesquisar);

        add(painelPesquisa, BorderLayout.NORTH);

        // Tabela
        modeloTabela = new DefaultTableModel(new Object[]{"ID", "Nome", "CPF", "Data de Nascimento"}, 0) {
            private static final long serialVersionUID = 1L;

            @Override
            public boolean isCellEditable(int row, int column) {
                return false;
            }
        };
        tabela = new JTable(modeloTabela);
        add(new JScrollPane(tabela), BorderLayout.CENTER);

        // Botão Sair
        JPanel painelInferior = new JPanel(new FlowLayout(FlowLayout.RIGHT));
        JButton btnSair = new JButton("Sair");
        btnSair.addActionListener(e -> dispose());
        painelInferior.add(btnSair);
        add(painelInferior, BorderLayout.SOUTH);

        // Busca dinâmica
        buscaField.getDocument().addDocumentListener(new DocumentListener() {
            public void insertUpdate(DocumentEvent e) { filtrar(); }
            public void removeUpdate(DocumentEvent e) { filtrar(); }
            public void changedUpdate(DocumentEvent e) { filtrar(); }
        });

        carregarTodos();
    }

    private void carregarTodos() {
        modeloTabela.setRowCount(0);
        List<Paciente> pacientes = pacienteDAO.findAll();
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd/MM/yyyy");

        for (Paciente p : pacientes) {
            String dataFormatada = p.getDataNascimento() != null ? p.getDataNascimento().format(formatter) : "";
            modeloTabela.addRow(new Object[]{p.getId(), p.getNome(), p.getCpf(), dataFormatada});
        }
    }

//    private void filtrar() {
//        String termoCpf = buscaField.getText().trim();
//        modeloTabela.setRowCount(0);
//
//        if (termoCpf.isEmpty()) {
//            carregarTodos();
//            return;
//        }
//
//        List<Paciente> pacientesEncontrados = (List<Paciente>) pacienteDAO.findByCPF(termoCpf);
//
//        if (pacientesEncontrados == null) {
//            // Caso o DAO retorne null, inicializa lista vazia para evitar NPE
//            pacientesEncontrados = new ArrayList<>();
//        }
//
//        for (Paciente p : pacientesEncontrados) {
//            modeloTabela.addRow(new Object[]{p.getId(), p.getNome(), p.getCpf(), p.getDataNascimento()});
//        }
//    }
    
    private void filtrar() {
        String termoCpf = buscaField.getText().trim();
        modeloTabela.setRowCount(0);

        if (termoCpf.isEmpty()) {
            carregarTodos();
            return;
        }

        List<Paciente> pacientesEncontrados = pacienteDAO.findByCPF(termoCpf);

        if (pacientesEncontrados == null) {
            pacientesEncontrados = new ArrayList<>();
        }

        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd/MM/yyyy");

        for (Paciente p : pacientesEncontrados) {
            String dataFormatada = p.getDataNascimento() != null
                ? p.getDataNascimento().format(formatter)
                : "";
            modeloTabela.addRow(new Object[]{p.getId(), p.getNome(), p.getCpf(), dataFormatada});
        }
    }


}
