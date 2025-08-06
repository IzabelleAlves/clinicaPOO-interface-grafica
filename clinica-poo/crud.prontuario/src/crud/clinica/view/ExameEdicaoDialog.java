package crud.clinica.view;

import crud.clinica.dao.ExameDAO;
import crud.clinica.dao.PacienteDAO;
import crud.clinica.model.Exame;
import crud.clinica.model.Paciente;

import javax.swing.*;
import java.awt.*;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;

public class ExameEdicaoDialog extends JDialog {
    private JTextField descricaoField;
    private JTextField dataField;
    private JComboBox<Paciente> pacienteComboBox;

    private final ExameDAO exameDAO;
    private final PacienteDAO pacienteDAO;

    public ExameEdicaoDialog(Frame owner, ExameDAO exameDAO, PacienteDAO pacienteDAO) {
        super(owner, "Edição de Exame", ModalityType.APPLICATION_MODAL);
        this.exameDAO = exameDAO;
        this.pacienteDAO = pacienteDAO;

        setLayout(new GridLayout(5, 2, 10, 10));
        setSize(400, 250);
        setLocationRelativeTo(owner);

        add(new JLabel("Descrição:"));
        descricaoField = new JTextField();
        add(descricaoField);

        add(new JLabel("Data (yyyy-MM-dd):"));
        dataField = new JTextField();
        add(dataField);

        add(new JLabel("Paciente:"));
        pacienteComboBox = new JComboBox<>();
        carregarPacientes();
        add(pacienteComboBox);

        JButton salvarBtn = new JButton("Salvar");
        salvarBtn.addActionListener(e -> salvarExame());
        add(salvarBtn);

        JButton cancelarBtn = new JButton("Cancelar");
        cancelarBtn.addActionListener(e -> dispose());
        add(cancelarBtn);
    }

    private void carregarPacientes() {
        for (Paciente paciente : pacienteDAO.findAll()) {
            pacienteComboBox.addItem(paciente);
        }
    }

    private void salvarExame() {
        try {
            String descricao = descricaoField.getText();
            LocalDate data = LocalDate.parse(dataField.getText(), DateTimeFormatter.ISO_LOCAL_DATE);
            Paciente pacienteSelecionado = (Paciente) pacienteComboBox.getSelectedItem();

            if (pacienteSelecionado == null || descricao.isEmpty()) {
                JOptionPane.showMessageDialog(this, "Preencha todos os campos.", "Erro", JOptionPane.ERROR_MESSAGE);
                return;
            }

            Exame novoExame = new Exame(descricao, data);
            novoExame.setPaciente(pacienteSelecionado);

            exameDAO.create(novoExame);

            JOptionPane.showMessageDialog(this, "Exame salvo com sucesso.");
            dispose();
        } catch (Exception e) {
            JOptionPane.showMessageDialog(this, "Erro ao salvar exame: " + e.getMessage(), "Erro", JOptionPane.ERROR_MESSAGE);
        }
    }
}
