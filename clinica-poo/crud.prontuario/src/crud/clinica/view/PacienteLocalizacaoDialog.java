package crud.clinica.view;

import crud.clinica.dao.PacienteDAO;
import crud.clinica.database.IConnection;
import crud.clinica.model.Paciente;

import javax.swing.*;
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.util.List;

public class PacienteLocalizacaoDialog extends JDialog {
    /**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private final PacienteDAO pacienteDAO;
    private final JTextField buscaField;
    private final JRadioButton rbNome;
    private final JRadioButton rbCPF;
    private final JTable tabela;
    private final DefaultTableModel modeloTabela;

    public PacienteLocalizacaoDialog(Window parent, IConnection dbConnection) {
        super(parent, "Localizar Paciente", ModalityType.APPLICATION_MODAL);
        this.pacienteDAO = new PacienteDAO(dbConnection);

        setSize(700, 400);
        setLocationRelativeTo(parent);
        setLayout(new BorderLayout(10, 10));

        // ==== Painel de Pesquisa ====
        JPanel painelPesquisa = new JPanel(new FlowLayout(FlowLayout.LEFT));
        rbNome = new JRadioButton("Nome", true);
        rbCPF = new JRadioButton("CPF");
        ButtonGroup grupo = new ButtonGroup();
        grupo.add(rbNome);
        grupo.add(rbCPF);

        buscaField = new JTextField(30);
        JButton btnPesquisar = new JButton("Pesquisar");
        btnPesquisar.addActionListener(e -> filtrar());

        painelPesquisa.add(rbNome);
        painelPesquisa.add(rbCPF);
        painelPesquisa.add(new JLabel("Buscar:"));
        painelPesquisa.add(buscaField);
        painelPesquisa.add(btnPesquisar);

        add(painelPesquisa, BorderLayout.NORTH);

        // ==== Tabela ====
        modeloTabela = new DefaultTableModel(new Object[]{"ID", "Nome", "CPF", "Data de Nascimento"}, 0) {
            /**
			 * 
			 */
			private static final long serialVersionUID = 1L;

			public boolean isCellEditable(int row, int column) {
                return false;
            }
        };
        tabela = new JTable(modeloTabela);
        add(new JScrollPane(tabela), BorderLayout.CENTER);

        // ==== Botão Sair ====
        JPanel painelInferior = new JPanel(new FlowLayout(FlowLayout.RIGHT));
        JButton btnSair = new JButton("Sair");
        btnSair.addActionListener(e -> dispose());
        painelInferior.add(btnSair);
        add(painelInferior, BorderLayout.SOUTH);

        // ==== Busca dinâmica ====
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
        for (Paciente p : pacientes) {
            modeloTabela.addRow(new Object[]{p.getId(), p.getNome(), p.getCpf(), p.getDataNascimento()});
        }
    }

    private void filtrar() {
        String termo = buscaField.getText().trim();

        modeloTabela.setRowCount(0);

        if (termo.isEmpty()) {
            carregarTodos();
            return;
        }

        if (rbCPF.isSelected() && !termo.matches("\\d{3}\\.\\d{3}\\.\\d{3}-\\d{2}")) {
            return; // CPF com formato inválido
        } 

        List<Paciente> pacientes = pacienteDAO.findAll();
        for (Paciente p : pacientes) {
            if (rbNome.isSelected() && p.getNome().toLowerCase().contains(termo.toLowerCase())) {
                modeloTabela.addRow(new Object[]{p.getId(), p.getNome(), p.getCpf(), p.getDataNascimento()});
            } else if (rbCPF.isSelected() && p.getCpf().equals(termo)) {
                modeloTabela.addRow(new Object[]{p.getId(), p.getNome(), p.getCpf(), p.getDataNascimento()});
            }
        }
    }
}
