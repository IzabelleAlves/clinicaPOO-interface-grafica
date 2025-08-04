package crud.clinica.view;

import crud.clinica.dao.PacienteDAO;
import crud.clinica.database.IConnection;
import crud.clinica.exception.CPFJaExisteException;
import crud.clinica.model.Paciente;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;

public class PacienteFormDialog extends JDialog {
    private static final long serialVersionUID = 1L;

    private JTextField nomeField;
    private JTextField cpfField;
    private JButton btnSalvar;
    private JButton btnCancelar;

    private PacienteDAO pacienteDAO;
    private Paciente paciente; // null se for cadastro novo

    public PacienteFormDialog(JFrame parent, IConnection dbConnection, Paciente paciente) {
        super(parent, true); // modal
        this.paciente = paciente;
        this.pacienteDAO = new PacienteDAO(dbConnection);

        setTitle(paciente == null ? "Cadastrar Paciente" : "Editar Paciente");
        setSize(300, 200);
        setLayout(new BorderLayout());
        setLocationRelativeTo(parent);

        JPanel formPanel = new JPanel(new GridLayout(2, 2));
        formPanel.add(new JLabel("Nome:"));
        nomeField = new JTextField();
        formPanel.add(nomeField);
        formPanel.add(new JLabel("CPF:"));
        cpfField = new JTextField();
        formPanel.add(cpfField);

        add(formPanel, BorderLayout.CENTER);

        JPanel buttonPanel = new JPanel();
        btnSalvar = new JButton("Salvar");
        btnCancelar = new JButton("Cancelar");

        buttonPanel.add(btnSalvar);
        buttonPanel.add(btnCancelar);
        add(buttonPanel, BorderLayout.SOUTH);

        // Preencher dados se estiver editando
        if (paciente != null) {
            nomeField.setText(paciente.getNome());
            cpfField.setText(paciente.getCpf());
        }

        btnSalvar.addActionListener((ActionEvent e) -> salvar());
        btnCancelar.addActionListener((ActionEvent e) -> dispose());
    }

    private void salvar() {
        String nome = nomeField.getText().trim();
        String cpf = cpfField.getText().trim();

        if (nome.isEmpty() || cpf.isEmpty()) {
            JOptionPane.showMessageDialog(this, "Preencha todos os campos!");
            return;
        }

        try {
            if (paciente == null) {
                pacienteDAO.create(new Paciente(nome, cpf));  // Use create aqui
            } else {
                paciente.setNome(nome);
                paciente.setCpf(cpf);
                pacienteDAO.update(paciente);  // Use update para editar
            }
            dispose();
        } catch (CPFJaExisteException e) {
            JOptionPane.showMessageDialog(this, e.getMessage(), "CPF já existe", JOptionPane.WARNING_MESSAGE);
        } catch (Exception e) {
            e.printStackTrace();
            JOptionPane.showMessageDialog(this, "Erro ao salvar paciente.", "Erro", JOptionPane.ERROR_MESSAGE);
        }
    }

}
