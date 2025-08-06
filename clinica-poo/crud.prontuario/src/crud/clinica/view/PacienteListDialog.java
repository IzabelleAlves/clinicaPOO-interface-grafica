package crud.clinica.view;

import crud.clinica.dao.PacienteDAO;
import crud.clinica.database.IConnection;
import crud.clinica.model.Paciente;

import javax.swing.*;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.awt.event.*;
import java.util.List;
import java.util.stream.Collectors;

public class PacienteListDialog extends JDialog {
    private static final long serialVersionUID = 1L;

    private IConnection dbConnection;
    private PacienteDAO pacienteDAO;
    private JTable table;
    private DefaultTableModel tableModel;

    private boolean podeEditar;
    private boolean podeExcluir;

    private JButton btnEditar;
    private JButton btnExcluir;

    // Novos componentes para pesquisa
    private JTextField txtPesquisa;
    private JRadioButton rbNome;
    private JRadioButton rbCpf;

    public PacienteListDialog(Window parent, IConnection dbConnection, boolean podeEditar, boolean podeExcluir) {
        super(parent, "Lista de Pacientes", ModalityType.APPLICATION_MODAL);
        this.dbConnection = dbConnection;
        this.pacienteDAO = new PacienteDAO(dbConnection);

        this.podeEditar = podeEditar;
        this.podeExcluir = podeExcluir;

        setSize(800, 450);
        setLocationRelativeTo(parent);
        setLayout(new BorderLayout());

        // Painel superior com opções de pesquisa
        JPanel painelPesquisa = new JPanel(new FlowLayout(FlowLayout.LEFT));
        rbNome = new JRadioButton("Nome", true);
        rbCpf = new JRadioButton("CPF");
        ButtonGroup grupoRadio = new ButtonGroup();
        grupoRadio.add(rbNome);
        grupoRadio.add(rbCpf);

        txtPesquisa = new JTextField(20);

        painelPesquisa.add(new JLabel("Pesquisar por:"));
        painelPesquisa.add(rbNome);
        painelPesquisa.add(rbCpf);
        painelPesquisa.add(txtPesquisa);

        add(painelPesquisa, BorderLayout.NORTH);

        // Modelo da tabela com colunas ID, Nome, CPF, Data de Nascimento e Quantidade de Exames
        tableModel = new DefaultTableModel(
            new Object[] { "ID", "Nome", "CPF", "Data de Nascimento", "Qtd. Exames" }, 0
        ) {
            private static final long serialVersionUID = 1L;

            @Override
            public boolean isCellEditable(int row, int column) {
                return false; // Tabela somente leitura
            }
        };

        table = new JTable(tableModel);
        JScrollPane scrollPane = new JScrollPane(table);
        add(scrollPane, BorderLayout.CENTER);

        // Painel dos botões
        JPanel buttonPanel = new JPanel();

        btnEditar = new JButton("Editar");
        btnExcluir = new JButton("Excluir");
        JButton btnFechar = new JButton("Fechar");

        // Adiciona botões conforme permissão
        if (podeEditar) {
            buttonPanel.add(btnEditar);
            btnEditar.addActionListener((ActionEvent e) -> editarPaciente());
        }
        if (podeExcluir) {
            buttonPanel.add(btnExcluir);
            btnExcluir.addActionListener((ActionEvent e) -> excluirPaciente());
        }
        buttonPanel.add(btnFechar);
        btnFechar.addActionListener((ActionEvent e) -> dispose());

        add(buttonPanel, BorderLayout.SOUTH);

        // Desabilitar editar/excluir no início porque nada está selecionado
        btnEditar.setEnabled(false);
        btnExcluir.setEnabled(false);

        // Carrega pacientes na tabela
        carregarPacientes();

        // ListSelectionListener para ativar/desativar botões ao selecionar linha
        table.getSelectionModel().addListSelectionListener(new ListSelectionListener() {
            @Override
            public void valueChanged(ListSelectionEvent e) {
                boolean linhaSelecionada = table.getSelectedRow() != -1;
                btnEditar.setEnabled(linhaSelecionada && podeEditar);
                btnExcluir.setEnabled(linhaSelecionada && podeExcluir);
            }
        });

        // ItemListener para os JRadioButton que muda o filtro de pesquisa
        ItemListener itemListener = new ItemListener() {
            @Override
            public void itemStateChanged(ItemEvent e) {
                // Atualiza a tabela filtrando conforme o rádio selecionado e texto
                filtrarPacientes();
            }
        };
        rbNome.addItemListener(itemListener);
        rbCpf.addItemListener(itemListener);

        // Também atualiza ao digitar no campo texto
        txtPesquisa.addKeyListener(new KeyAdapter() {
            @Override
            public void keyReleased(KeyEvent e) {
                filtrarPacientes();
            }
        });
    }

    private void carregarPacientes() {
        List<Paciente> pacientes = pacienteDAO.findAll();
        carregarPacientesNaTabela(pacientes);
    }

    // Atualiza a tabela mostrando a lista passada
    private void carregarPacientesNaTabela(List<Paciente> pacientes) {
        tableModel.setRowCount(0); // limpa tabela
        for (Paciente p : pacientes) {
            String dataFormatada = p.getDataNascimento() != null
                ? p.getDataNascimento().format(java.time.format.DateTimeFormatter.ofPattern("dd/MM/yyyy"))
                : "";

            int qtdExames = p.getQuantidadeExames();

            tableModel.addRow(new Object[] {
                p.getId(),
                p.getNome(),
                p.getCpf(),
                dataFormatada,
                qtdExames
            });
        }
    }

    // Filtra os pacientes conforme o texto e o rádio selecionado
    private void filtrarPacientes() {
        String texto = txtPesquisa.getText().trim().toLowerCase();
        List<Paciente> pacientes = pacienteDAO.findAll();

        List<Paciente> filtrados = pacientes.stream()
            .filter(p -> {
                if (rbNome.isSelected()) {
                    return p.getNome() != null && p.getNome().toLowerCase().contains(texto);
                } else if (rbCpf.isSelected()) {
                    return p.getCpf() != null && p.getCpf().toLowerCase().contains(texto);
                }
                return true;
            })
            .collect(Collectors.toList());

        carregarPacientesNaTabela(filtrados);
    }

    private void editarPaciente() {
        int linhaSelecionada = table.getSelectedRow();
        if (linhaSelecionada == -1) {
            JOptionPane.showMessageDialog(this, "Selecione um paciente para editar.");
            return;
        }
        Long id = (Long) tableModel.getValueAt(linhaSelecionada, 0);
        Paciente selecionado = pacienteDAO.findById(id);
        if (selecionado == null) {
            JOptionPane.showMessageDialog(this, "Paciente não encontrado.");
            return;
        }

        PacienteFormDialog form = new PacienteFormDialog(this, dbConnection, selecionado);
        form.setVisible(true);

        // Recarrega a tabela depois de editar
        carregarPacientes();
        filtrarPacientes(); // para garantir atualização do filtro se ativo
    }

    private void excluirPaciente() {
        int linhaSelecionada = table.getSelectedRow();
        if (linhaSelecionada == -1) {
            JOptionPane.showMessageDialog(this, "Selecione um paciente para excluir.");
            return;
        }
        int confirmar = JOptionPane.showConfirmDialog(this, "Deseja realmente excluir este paciente?",
                "Confirmar exclusão", JOptionPane.YES_NO_OPTION);

        if (confirmar == JOptionPane.YES_OPTION) {
            Long id = (Long) tableModel.getValueAt(linhaSelecionada, 0);
            Paciente selecionado = pacienteDAO.findById(id);
            if (selecionado != null) {
                pacienteDAO.delete(selecionado);
                carregarPacientes();
                filtrarPacientes();
            } else {
                JOptionPane.showMessageDialog(this, "Paciente não encontrado.");
            }
        }
    }
}
