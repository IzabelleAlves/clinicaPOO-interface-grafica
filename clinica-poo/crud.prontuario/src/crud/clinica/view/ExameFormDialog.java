package crud.clinica.view;

import crud.clinica.dao.ExameDAO;
import crud.clinica.dao.PacienteDAO;
import crud.clinica.model.Exame;
import crud.clinica.model.Paciente;

import javax.swing.*;
import javax.swing.text.MaskFormatter;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.text.ParseException;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;

public class ExameFormDialog extends JDialog {
    private static final long serialVersionUID = 1L;

    private JComboBox<Paciente> pacienteComboBox;
    private JFormattedTextField dataField;
    private JTextField descricaoField;

    private JButton btnSalvar;
    private JButton btnLimpar;
    private JButton btnSair;

    private ExameDAO exameDAO;
    private PacienteDAO pacienteDAO;
    private Exame exame;

    public ExameFormDialog(Window parent, ExameDAO exameDAO, PacienteDAO pacienteDAO, Exame exame) {
        super(parent, exame == null ? "Cadastrar Exame" : "Editar Exame", ModalityType.APPLICATION_MODAL);
        this.exameDAO = exameDAO;
        this.pacienteDAO = pacienteDAO;
        this.exame = exame;

        setSize(350, 220);
        setLayout(new BorderLayout(10, 10));
        setLocationRelativeTo(parent);
        setResizable(false);

        // === FORM PANEL com GridLayout(3,2) ===
        JPanel formPanel = new JPanel(new GridLayout(3, 2, 5, 5));

        // Campo Paciente
        formPanel.add(new JLabel("Paciente:"));
        pacienteComboBox = new JComboBox<>();
        for (Paciente p : pacienteDAO.findAll()) {
            pacienteComboBox.addItem(p);
        }
        formPanel.add(pacienteComboBox);

        // Campo Data
        formPanel.add(new JLabel("Data (DD/MM/AAAA):"));
        try {
            MaskFormatter dataMask = new MaskFormatter("##/##/####");
            dataMask.setPlaceholderCharacter('_');
            dataField = new JFormattedTextField(dataMask);
        } catch (ParseException e) {
            dataField = new JFormattedTextField();
        }
        formPanel.add(dataField);

        // Campo Descrição
        formPanel.add(new JLabel("Descrição:"));
        descricaoField = new JTextField();
        formPanel.add(descricaoField);

        add(formPanel, BorderLayout.CENTER);

        // === BOTÕES no painel inferior ===
        JPanel buttonPanel = new JPanel();

        btnSalvar = new JButton(exame == null ? "Salvar" : "Atualizar");
        buttonPanel.add(btnSalvar);

        if (exame == null) {
            btnLimpar = new JButton("Limpar");
            buttonPanel.add(btnLimpar);
        }

        btnSair = new JButton("Sair");
        buttonPanel.add(btnSair);

        add(buttonPanel, BorderLayout.SOUTH);

        // === AÇÕES DOS BOTÕES ===
        btnSalvar.addActionListener(this::salvar);

        if (btnLimpar != null) {
            btnLimpar.addActionListener(e -> limparCampos());
        }

        btnSair.addActionListener(e -> dispose());

        // === PRÉ-PREENCHIMENTO se for edição ===
        if (exame != null) {
            pacienteComboBox.setSelectedItem(exame.getPaciente());

            DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd/MM/yyyy");
            dataField.setText(exame.getData_exame().format(formatter));

            descricaoField.setText(exame.getDescricao());
        }
    }

    private void limparCampos() {
        pacienteComboBox.setSelectedIndex(0);
        dataField.setValue(null);
        descricaoField.setText("");
    }

    private void salvar(ActionEvent e) {
        Paciente paciente = (Paciente) pacienteComboBox.getSelectedItem();
        String dataStr = dataField.getText().trim();
        String descricao = descricaoField.getText().trim();

        if (paciente == null || dataStr.isEmpty() || descricao.isEmpty()) {
            JOptionPane.showMessageDialog(this, "Preencha todos os campos.");
            return;
        }

        try {
            DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd/MM/yyyy");
            LocalDate data = LocalDate.parse(dataStr, formatter);

            if (exame == null) {
                Exame novoExame = new Exame(descricao, data);
                novoExame.setPaciente(paciente);
                exameDAO.create(novoExame);
            } else {
                exame.setDescricao(descricao);
                exame.setData_exame(data);
                exame.setPaciente(paciente);
                exameDAO.update(exame);
            }
            JOptionPane.showMessageDialog(this, "Exame salvo com sucesso.");
            dispose();
        } catch (Exception ex) {
            JOptionPane.showMessageDialog(this, "Erro ao salvar exame: " + ex.getMessage());
        }
    }
}
