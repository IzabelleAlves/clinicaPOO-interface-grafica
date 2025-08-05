package crud.clinica.view;

import crud.clinica.dao.PacienteDAO;
import crud.clinica.database.IConnection;
import crud.clinica.model.Paciente;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.util.List;

public class PacienteListDialog extends JDialog {
	private static final long serialVersionUID = 1L;

	private IConnection dbConnection;
	private PacienteDAO pacienteDAO;
	private JTable table;
	private DefaultTableModel tableModel;

	private boolean podeEditar;
	private boolean podeExcluir;

	public PacienteListDialog(Window parent, IConnection dbConnection, boolean podeEditar, boolean podeExcluir) {
		super(parent, "Lista de Pacientes", ModalityType.APPLICATION_MODAL);
		this.dbConnection = dbConnection;
		this.pacienteDAO = new PacienteDAO(dbConnection);

		this.podeEditar = podeEditar;
		this.podeExcluir = podeExcluir;

		setSize(700, 400);
		setLocationRelativeTo(parent);
		setLayout(new BorderLayout());

		// Modelo da tabela com colunas ID, Nome e CPF
		tableModel = new DefaultTableModel(new Object[] { "ID", "Nome", "CPF", "Data de Nascimento" }, 0) {
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

		JButton btnEditar = new JButton("Editar");
		JButton btnExcluir = new JButton("Excluir");
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

		// Carrega pacientes na tabela
		carregarPacientes();
	}

	private void carregarPacientes() {
		List<Paciente> pacientes = pacienteDAO.findAll();
		tableModel.setRowCount(0); // limpa tabela
		for (Paciente p : pacientes) {
//			tableModel.addRow(new Object[] { p.getId(), p.getNome(), p.getCpf(), p.getDataNascimento() });
			String dataFormatada = p.getDataNascimento() != null
				    ? p.getDataNascimento().format(java.time.format.DateTimeFormatter.ofPattern("dd/MM/yyyy"))
				    : "";
				tableModel.addRow(new Object[] { p.getId(), p.getNome(), p.getCpf(), dataFormatada });

		}
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
			} else {
				JOptionPane.showMessageDialog(this, "Paciente não encontrado.");
			}
		}
	}
}
