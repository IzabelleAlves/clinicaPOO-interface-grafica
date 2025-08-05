package crud.clinica.view;

import crud.clinica.dao.ExameDAO;
import crud.clinica.dao.PacienteDAO;
import crud.clinica.database.IConnection;
import crud.clinica.model.Exame;
import crud.clinica.model.Paciente;

import javax.swing.*;
import javax.swing.text.MaskFormatter;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.text.ParseException;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.List;

public class ExameFormDialog extends JDialog {
    private static final long serialVersionUID = 1L;

    private JComboBox<Paciente> pacienteComboBox;
    private JComboBox<String> tipoComboBox;
    private JFormattedTextField dataField;
    private JTextArea descricaoArea;
    private JButton btnSalvar, btnCancelar;

    private ExameDAO exameDAO;
    private PacienteDAO pacienteDAO;
    private Exame exame;

    private final DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd/MM/yyyy");

    public ExameFormDialog(JFrame parent, IConnection dbConnection, Exame exame) {
        super(parent, true); // modal
        this.exame = exame;
        this.exameDAO = new ExameDAO(dbConnection);
        this.pacienteDAO = new PacienteDAO(dbConnection);

        setTitle(exame == null ? "Cadastrar Exame" : "Editar Exame");
        setSize(400, 300);
        setLocationRelativeTo(parent);
        setLayout(new BorderLayout());

        JPanel formPanel = new JPanel(new GridLayout(4, 2, 5, 5));

        formPanel.add(new JLabel("Paciente:"));
        pacienteComboBox = new JComboBox<>();
        carregarPacientes();
        formPanel.add(pacienteComboBox);

        formPanel.add(new JLabel("Tipo de Exame:"));
        tipoComboBox = new JComboBox<>(new String[]{"Sangue", "Urina", "Raio-X", "Outro"});
        formPanel.add(tipoComboBox);

        formPanel.add(new JLabel("Data (dd/mm/aaaa):"));
        try {
            MaskFormatter dateMask = new MaskFormatter("##/##/####");
            dateMask.setPlaceholderCharacter('_');
            dataField = new JFormattedTextField(dateMask);
        } catch (ParseException e) {
            e.printStackTrace();
            dataField = new JFormattedTextField(); // fallback
        }
        formPanel.add(dataField);

        formPanel.add(new JLabel("Descrição:"));
        descricaoArea = new JTextArea(3, 20);
        JScrollPane scrollPane = new JScrollPane(descricaoArea);
        formPanel.add(scrollPane);

        add(formPanel, BorderLayout.CENTER);

        JPanel buttonPanel = new JPanel();
        btnSalvar = new JButton("Salvar");
        btnCancelar = new JButton("Cancelar");
        buttonPanel.add(btnSalvar);
        buttonPanel.add(btnCancelar);

        add(buttonPanel, BorderLayout.SOUTH);

        if (exame != null) {
            pacienteComboBox.setSelectedItem(exame.getPaciente());
            tipoComboBox.setSelectedItem(exame.getTipo());
            dataField.setText(exame.getData_exame().format(formatter));
            descricaoArea.setText(exame.getDescricao());
        }

        btnSalvar.addActionListener(this::salvar);
        btnCancelar.addActionListener(e -> dispose());
    }

    private void carregarPacientes() {
        List<Paciente> pacientes = pacienteDAO.findAll();
        for (Paciente p : pacientes) {
            pacienteComboBox.addItem(p);
        }
    }

    private void salvar(ActionEvent e) {
        try {
            Paciente paciente = (Paciente) pacienteComboBox.getSelectedItem();
            String tipo = (String) tipoComboBox.getSelectedItem();
            String dataTexto = dataField.getText().trim();
            String descricao = descricaoArea.getText().trim();

            if (paciente == null || tipo.isEmpty() || dataTexto.contains("_") || dataTexto.isEmpty()) {
                JOptionPane.showMessageDialog(this, "Preencha todos os campos obrigatórios.");
                return;
            }

            LocalDate dataExame;
            try {
                dataExame = LocalDate.parse(dataTexto, formatter);
            } catch (Exception ex) {
                JOptionPane.showMessageDialog(this, "Data inválida! Use o formato dd/MM/aaaa.");
                return;
            }

            if (exame == null) {
                exameDAO.create(new Exame(paciente, tipo, dataExame, descricao));
            } else {
                exame.setPaciente(paciente);
                exame.setTipo(tipo);
                exame.setData_exame(dataExame);
                exame.setDescricao(descricao);
                exameDAO.update(exame);
            }

            dispose();
        } catch (Exception ex) {
            ex.printStackTrace();
            JOptionPane.showMessageDialog(this, "Erro ao salvar exame.", "Erro", JOptionPane.ERROR_MESSAGE);
        }
    }
}
