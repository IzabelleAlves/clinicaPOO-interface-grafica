//Formulário para adicionar/editar exame
package crud.clinica.view;

import crud.clinica.dao.ExameDAO;
import crud.clinica.dao.PacienteDAO;
import crud.clinica.database.IConnection;
import crud.clinica.model.Exame;
import crud.clinica.model.Paciente;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.util.List;

public class ExameFormDialog extends JDialog {
    private static final long serialVersionUID = 1L;

    private JComboBox<Paciente> pacienteComboBox;
    private JComboBox<String> tipoComboBox;
    private JTextField dataField;
    private JTextArea observacoesArea;
    private JButton btnSalvar, btnCancelar;

    private ExameDAO exameDAO;
    private PacienteDAO pacienteDAO;
    private Exame exame;

    public ExameFormDialog(JFrame parent, IConnection dbConnection, Exame exame) {
        super(parent, true); // modal
        this.exame = exame;
        this.exameDAO = new ExameDAO(dbConnection);
        this.pacienteDAO = new PacienteDAO(dbConnection);

        setTitle(exame == null ? "Cadastrar Exame" : "Editar Exame");
        setSize(400, 300);
        setLocationRelativeTo(parent);
        setLayout(new BorderLayout());

        // Painel de formulário
        JPanel formPanel = new JPanel(new GridLayout(4, 2, 5, 5));

        formPanel.add(new JLabel("Paciente:"));
        pacienteComboBox = new JComboBox<>();
        carregarPacientes();
        formPanel.add(pacienteComboBox);

        formPanel.add(new JLabel("Tipo de Exame:"));
        tipoComboBox = new JComboBox<>(new String[]{"Sangue", "Urina", "Raio-X", "Outro"});
        formPanel.add(tipoComboBox);

        formPanel.add(new JLabel("Data (dd/mm/aaaa):"));
        dataField = new JTextField();
        formPanel.add(dataField);

        formPanel.add(new JLabel("Observações:"));
        observacoesArea = new JTextArea(3, 20);
        JScrollPane scrollPane = new JScrollPane(observacoesArea);
        formPanel.add(scrollPane);

        add(formPanel, BorderLayout.CENTER);

        // Painel de botões
        JPanel buttonPanel = new JPanel();
        btnSalvar = new JButton("Salvar");
        btnCancelar = new JButton("Cancelar");
        buttonPanel.add(btnSalvar);
        buttonPanel.add(btnCancelar);

        add(buttonPanel, BorderLayout.SOUTH);

        // Se for edição, preencher os dados
        if (exame != null) {
            pacienteComboBox.setSelectedItem(exame.getPaciente());
            tipoComboBox.setSelectedItem(exame.getTipo());
            dataField.setText(exame.getData());
            observacoesArea.setText(exame.getObservacoes());
        }

        // Ações
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
        Paciente paciente = (Paciente) pacienteComboBox.getSelectedItem();
        String tipo = (String) tipoComboBox.getSelectedItem();
        String data = dataField.getText().trim();
        String observacoes = observacoesArea.getText().trim();

        if (paciente == null || tipo.isEmpty() || data.isEmpty()) {
            JOptionPane.showMessageDialog(this, "Preencha todos os campos obrigatórios.");
            return;
        }

        try {
            if (exame == null) {
                exameDAO.create(new Exame(paciente, tipo, data, observacoes));
            } else {
                exame.setPaciente(paciente);
                exame.setTipo(tipo);
                exame.setData(data);
                exame.setObservacoes(observacoes);
                exameDAO.update(exame);
            }
            dispose();
        } catch (Exception ex) {
            ex.printStackTrace();
            JOptionPane.showMessageDialog(this, "Erro ao salvar exame.", "Erro", JOptionPane.ERROR_MESSAGE);
        }
    }
}
