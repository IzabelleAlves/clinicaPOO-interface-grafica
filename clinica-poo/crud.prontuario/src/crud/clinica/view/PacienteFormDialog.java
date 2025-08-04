package crud.clinica.view;

import crud.clinica.dao.PacienteDAO;
import crud.clinica.database.IConnection;
import crud.clinica.exception.CPFJaExisteException;
import crud.clinica.model.Paciente;

import javax.swing.*;
import javax.swing.text.MaskFormatter;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

public class PacienteFormDialog extends JDialog {
    private static final long serialVersionUID = 1L;

    private JTextField nomeField;
    private JFormattedTextField cpfField;
    private JFormattedTextField dataNascimentoField;
    private JButton btnSalvar;
    private JButton btnLimpar;
    private JButton btnSair;

    private PacienteDAO pacienteDAO;
    private Paciente paciente; // null se cadastro novo

    public PacienteFormDialog(Window parent, IConnection dbConnection, Paciente paciente) {
        super(parent, "Cadastrar Paciente", ModalityType.APPLICATION_MODAL);
        this.pacienteDAO = new PacienteDAO(dbConnection);
        this.paciente = paciente;

        setSize(350, 220);
        setLayout(new BorderLayout(10, 10));
        setLocationRelativeTo(parent);
        setResizable(false);

        JPanel formPanel = new JPanel(new GridLayout(3, 2, 5, 5));
        formPanel.add(new JLabel("Nome:"));
        nomeField = new JTextField();
        formPanel.add(nomeField);

        formPanel.add(new JLabel("CPF:"));
        try {
            MaskFormatter cpfMask = new MaskFormatter("###.###.###-##");
            cpfMask.setPlaceholderCharacter('_');
            cpfField = new JFormattedTextField(cpfMask);
        } catch (ParseException e) {
            cpfField = new JFormattedTextField();
        }
        formPanel.add(cpfField);

        formPanel.add(new JLabel("Data de Nascimento:"));
        try {
            MaskFormatter dataMask = new MaskFormatter("##/##/####");
            dataMask.setPlaceholderCharacter('_');
            dataNascimentoField = new JFormattedTextField(dataMask);
        } catch (ParseException e) {
            dataNascimentoField = new JFormattedTextField();
        }
        formPanel.add(dataNascimentoField);

        add(formPanel, BorderLayout.CENTER);

        JPanel buttonPanel = new JPanel();
        btnSalvar = new JButton("Salvar");
        btnLimpar = new JButton("Limpar");
        btnSair = new JButton("Sair");
        buttonPanel.add(btnSalvar);
        buttonPanel.add(btnLimpar);
        buttonPanel.add(btnSair);

        add(buttonPanel, BorderLayout.SOUTH);

        btnSalvar.addActionListener(this::salvar);
        btnLimpar.addActionListener(e -> limparCampos());
        btnSair.addActionListener(e -> dispose());
        
        if (paciente != null) {
            nomeField.setText(paciente.getNome());
            cpfField.setText(paciente.getCpf());
            dataNascimentoField.setText(paciente.getDataNascimento());
        }
    }

    private void limparCampos() {
        nomeField.setText("");
        cpfField.setValue(null);
        dataNascimentoField.setValue(null);
    }

    private void salvar(ActionEvent e) {
        String nome = nomeField.getText().trim();
        String cpf = cpfField.getText().trim();
        String dataNascimento = dataNascimentoField.getText().trim();

        if (paciente == null) {
            // Valida campos obrigatórios no cadastro novo
            if (nome.isEmpty() || cpf.isEmpty() || dataNascimento.isEmpty() || dataNascimento.contains("_")) {
                JOptionPane.showMessageDialog(this, "Preencha todos os campos obrigatórios.");
                return;
            }

            // Valida data obrigatoriamente
            if (!validarData(dataNascimento)) {
                JOptionPane.showMessageDialog(this, "Data de Nascimento inválida. Use formato DD/MM/AAAA.");
                return;
            }

            try {
                pacienteDAO.create(new Paciente(nome, cpf, dataNascimento));
                dispose();
            } catch (CPFJaExisteException ex) {
                JOptionPane.showMessageDialog(this, ex.getMessage(), "CPF já existe", JOptionPane.WARNING_MESSAGE);
            } catch (Exception ex) {
                ex.printStackTrace();
                JOptionPane.showMessageDialog(this, "Erro ao salvar paciente.", "Erro", JOptionPane.ERROR_MESSAGE);
            }
        } else {
            // Edição: só atualiza os campos que foram preenchidos, sem exigir obrigatoriedade
            if (!nome.isEmpty()) {
                paciente.setNome(nome);
            }
            if (!cpf.isEmpty()) {
                paciente.setCpf(cpf);
            }
            if (!dataNascimento.isEmpty() && !dataNascimento.contains("_")) {
                if (!validarData(dataNascimento)) {
                    JOptionPane.showMessageDialog(this, "Data de Nascimento inválida. Use formato DD/MM/AAAA.");
                    return;
                }
                paciente.setDataNascimento(dataNascimento);
            }
            try {
                pacienteDAO.update(paciente);
                dispose();
            } catch (CPFJaExisteException ex) {
                JOptionPane.showMessageDialog(this, ex.getMessage(), "CPF já existe", JOptionPane.WARNING_MESSAGE);
            } catch (Exception ex) {
                ex.printStackTrace();
                JOptionPane.showMessageDialog(this, "Erro ao atualizar paciente.", "Erro", JOptionPane.ERROR_MESSAGE);
            }
        }
    }

    private boolean validarData(String data) {
        try {
            SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy");
            sdf.setLenient(false);
            Date d = sdf.parse(data);
            return true;
        } catch (Exception e) {
            return false;
        }
    }
}
