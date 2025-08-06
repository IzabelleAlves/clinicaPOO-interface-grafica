package crud.clinica.view;

import crud.clinica.dao.ExameDAO;
import crud.clinica.dao.PacienteDAO;
import crud.clinica.database.IConnection;
import crud.clinica.model.Exame;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.time.format.DateTimeFormatter;
import java.util.List;

public class ExameListDialog extends JDialog {
    private static final long serialVersionUID = 1L;

    private IConnection dbConnection;
    private ExameDAO exameDAO;
    private PacienteDAO pacienteDAO;
    private JTable table;
    private DefaultTableModel tableModel;

    private boolean podeEditar;
    private boolean podeExcluir;

    private DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd/MM/yyyy");

    public ExameListDialog(Window parent, IConnection dbConnection, boolean podeEditar, boolean podeExcluir) {
        super(parent, "Lista de Exames", ModalityType.APPLICATION_MODAL);
        this.dbConnection = dbConnection;
        this.exameDAO = new ExameDAO(dbConnection);
        this.pacienteDAO = new PacienteDAO(dbConnection);

        this.podeEditar = podeEditar;
        this.podeExcluir = podeExcluir;

        setSize(800, 400);
        setLocationRelativeTo(parent);
        setLayout(new BorderLayout());

        // Modelo da tabela com colunas: ID, Paciente, Data, Descrição
        tableModel = new DefaultTableModel(
            new Object[]{"ID", "Paciente", "Data", "Descrição"}, 0
        ) {
            private static final long serialVersionUID = 1L;

            @Override
            public boolean isCellEditable(int row, int column) {
                return false; // tabela somente leitura
            }
        };

        table = new JTable(tableModel);
        JScrollPane scrollPane = new JScrollPane(table);
        add(scrollPane, BorderLayout.CENTER);

        // Painel dos botões
        JPanel buttonPanel = new JPanel();

        JButton btnEditar = new JButton("Editar");
        JButton btnExcluir = new JButton("Excluir");
        JButton btnFechar = new JButton("Fechar");

        if (podeEditar) {
            buttonPanel.add(btnEditar);
            btnEditar.addActionListener((ActionEvent e) -> editarExame());
        }
        if (podeExcluir) {
            buttonPanel.add(btnExcluir);
            btnExcluir.addActionListener((ActionEvent e) -> excluirExame());
        }

        buttonPanel.add(btnFechar);
        btnFechar.addActionListener((ActionEvent e) -> dispose());

        add(buttonPanel, BorderLayout.SOUTH);

        carregarExames();
    }

    private void carregarExames() {
        List<Exame> exames = exameDAO.findAll();
        tableModel.setRowCount(0);
        for (Exame exame : exames) {
            String dataFormatada = exame.getData_exame() != null
                ? exame.getData_exame().format(formatter)
                : "";
            String nomePaciente = exame.getPaciente() != null
                ? exame.getPaciente().getNome()
                : "";
            tableModel.addRow(new Object[]{
                exame.getId(),
                nomePaciente,
                dataFormatada,
                exame.getDescricao()
            });
        }
    }

    private void editarExame() {
        int linhaSelecionada = table.getSelectedRow();
        if (linhaSelecionada == -1) {
            JOptionPane.showMessageDialog(this, "Selecione um exame para editar.");
            return;
        }
        Long id = (Long) tableModel.getValueAt(linhaSelecionada, 0);
        Exame selecionado = exameDAO.findById(id);
        if (selecionado == null) {
            JOptionPane.showMessageDialog(this, "Exame não encontrado.");
            return;
        }

        ExameFormDialog form = new ExameFormDialog(this, exameDAO, pacienteDAO, selecionado);
        form.setVisible(true);

        // Atualiza a tabela após a edição
        carregarExames();
    }

    private void excluirExame() {
        int linhaSelecionada = table.getSelectedRow();
        if (linhaSelecionada == -1) {
            JOptionPane.showMessageDialog(this, "Selecione um exame para excluir.");
            return;
        }

        int confirmar = JOptionPane.showConfirmDialog(this, "Deseja realmente excluir este exame?",
                "Confirmar exclusão", JOptionPane.YES_NO_OPTION);

        if (confirmar == JOptionPane.YES_OPTION) {
            Long id = (Long) tableModel.getValueAt(linhaSelecionada, 0);
            Exame selecionado = exameDAO.findById(id);
            if (selecionado != null) {
                exameDAO.delete(selecionado);
                carregarExames();
            } else {
                JOptionPane.showMessageDialog(this, "Exame não encontrado.");
            }
        }
    }
}
