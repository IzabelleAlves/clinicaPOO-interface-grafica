package crud.clinica.view;

import crud.clinica.dao.ExameDAO;
import crud.clinica.database.DatabaseConnectionMySQL;
import crud.clinica.database.IConnection;
import crud.clinica.model.Exame;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.util.List;

public class ExamePanel extends JPanel {
    private static final long serialVersionUID = 1L;

    private DefaultListModel<Exame> listModel;
    private JList<Exame> exameList;
    private JButton btnCarregar;
    private IConnection dbConnection;  // conexão com o banco

    public ExamePanel() {
        setLayout(new BorderLayout());

        listModel = new DefaultListModel<>();
        exameList = new JList<>(listModel);
        JScrollPane scrollPane = new JScrollPane(exameList);

        btnCarregar = new JButton("Carregar Exames");
        btnCarregar.addActionListener((ActionEvent e) -> carregarExames());

        add(scrollPane, BorderLayout.CENTER);
        add(btnCarregar, BorderLayout.SOUTH);

        // Cria a conexão com o banco no construtor
        dbConnection = new DatabaseConnectionMySQL();
    }

    private void carregarExames() {
        listModel.clear();
        ExameDAO exameDAO = new ExameDAO(dbConnection);
        List<Exame> exames = exameDAO.findAll();
        for (Exame e : exames) {
            listModel.addElement(e);
        }
    }
}
