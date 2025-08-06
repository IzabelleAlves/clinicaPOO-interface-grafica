package crud.clinica.view;

import java.awt.BorderLayout;
import java.awt.FlowLayout;
import java.awt.Window;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;

import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.JTextField;
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;
import javax.swing.table.DefaultTableModel;

import crud.clinica.dao.ExameDAO;
import crud.clinica.dao.PacienteDAO;
import crud.clinica.model.Exame;

public class ExameLocalizacaoDialog extends JDialog {

    private static final long serialVersionUID = 1L;

    private final ExameDAO exameDAO;
    private final PacienteDAO pacienteDAO;
    private final JTextField buscaField;
    private final JTable tabela;
    private final DefaultTableModel modeloTabela;

    private DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd/MM/yyyy");

    public ExameLocalizacaoDialog(Window parent, ExameDAO exameDAO, PacienteDAO pacienteDAO) {
        super(parent, "Localizar Exame por Descrição", ModalityType.APPLICATION_MODAL);
        this.exameDAO = exameDAO;
        this.pacienteDAO = pacienteDAO;

        setSize(800, 400);
        setLocationRelativeTo(parent);
        setLayout(new BorderLayout(10, 10));

        // Painel de Pesquisa
        JPanel painelPesquisa = new JPanel(new FlowLayout(FlowLayout.LEFT));

        buscaField = new JTextField(30);
        JButton btnPesquisar = new JButton("Pesquisar");
        btnPesquisar.addActionListener(e -> filtrar());

        painelPesquisa.add(new JLabel("Descrição do Exame:"));
        painelPesquisa.add(buscaField);
        painelPesquisa.add(btnPesquisar);

        add(painelPesquisa, BorderLayout.NORTH);

        // Tabela
        modeloTabela = new DefaultTableModel(new Object[]{"ID", "Paciente", "Data", "Descrição"}, 0) {
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

        // Busca dinâmica: atualiza ao digitar
        buscaField.getDocument().addDocumentListener(new DocumentListener() {
            public void insertUpdate(DocumentEvent e) { filtrar(); }
            public void removeUpdate(DocumentEvent e) { filtrar(); }
            public void changedUpdate(DocumentEvent e) { filtrar(); }
        });

        carregarTodos();
    }

    private void carregarTodos() {
        modeloTabela.setRowCount(0);
        List<Exame> exames = exameDAO.findAll();

        for (Exame exame : exames) {
            String dataFormatada = exame.getData_exame() != null ? exame.getData_exame().format(formatter) : "";
            String nomePaciente = exame.getPaciente() != null ? exame.getPaciente().getNome() : "";
            modeloTabela.addRow(new Object[]{
                exame.getId(),
                nomePaciente,
                dataFormatada,
                exame.getDescricao()
            });
        }
    }

    private void filtrar() {
        String termo = buscaField.getText().trim();
        modeloTabela.setRowCount(0);

        if (termo.isEmpty()) {
            carregarTodos();
            return;
        }

        List<Exame> examesEncontrados = exameDAO.findByNomePaciente(termo);

        if (examesEncontrados == null) {
            examesEncontrados = new ArrayList<>();
        }

        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd/MM/yyyy");

        for (Exame exame : examesEncontrados) {
            String dataFormatada = exame.getData_exame() != null
                ? exame.getData_exame().format(formatter)
                : "";
            String nomePaciente = exame.getPaciente() != null ? exame.getPaciente().getNome() : "";
            modeloTabela.addRow(new Object[]{
                exame.getId(),
                nomePaciente,
                dataFormatada,
                exame.getDescricao()
            });
        }
    }

}
