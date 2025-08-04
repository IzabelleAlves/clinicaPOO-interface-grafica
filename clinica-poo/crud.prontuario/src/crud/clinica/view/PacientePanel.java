package crud.clinica.view;

import crud.clinica.dao.PacienteDAO;
import crud.clinica.database.DatabaseConnectionMySQL;
import crud.clinica.database.IConnection;
import crud.clinica.model.Paciente;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.util.List;

public class PacientePanel extends JPanel {
    private static final long serialVersionUID = 1L;

    private DefaultListModel<Paciente> listModel;
    private JList<Paciente> pacienteList;
    private JButton btnCarregar;
    private IConnection dbConnection;

    public PacientePanel() {
        setLayout(new BorderLayout());

        listModel = new DefaultListModel<>();
        pacienteList = new JList<>(listModel);
        JScrollPane scrollPane = new JScrollPane(pacienteList);

        btnCarregar = new JButton("Carregar Pacientes");
        btnCarregar.addActionListener((ActionEvent e) -> carregarPacientes());

        add(scrollPane, BorderLayout.CENTER);
        add(btnCarregar, BorderLayout.SOUTH);

        dbConnection = new DatabaseConnectionMySQL();
    }

    private void carregarPacientes() {
        listModel.clear();
        PacienteDAO pacienteDAO = new PacienteDAO(dbConnection);
        List<Paciente> pacientes = pacienteDAO.findAll();
        for (Paciente p : pacientes) {
            listModel.addElement(p);
        }
    }
}
