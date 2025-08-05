package crud.clinica.dao;

import java.sql.Date;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import crud.clinica.database.IConnection;
import crud.clinica.model.Exame;
import crud.clinica.model.Paciente;

public class ExameDAO implements IEntityDAO<Exame> {

    private IConnection conn;

    public ExameDAO(IConnection connection) {
        this.conn = connection;
    }

    @Override
    public void create(Exame t) {
        try {
            PreparedStatement pstm = conn.getConnection()
                .prepareStatement("INSERT INTO EXAMES (tipo, descricao, data_exame, paciente_id) VALUES (?, ?, ?, ?);");
            pstm.setString(1, t.getTipo());
            pstm.setString(2, t.getDescricao());
            pstm.setDate(3, Date.valueOf(t.getData_exame()));
            pstm.setLong(4, t.getPaciente().getId());
            pstm.execute();
            pstm.close();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    @Override
    public Exame findById(Long id) {
        Exame exame = null;

        try {
            PreparedStatement pstm = conn.getConnection()
                .prepareStatement("SELECT * FROM EXAMES WHERE ID = ?;");
            pstm.setLong(1, id);
            ResultSet rs = pstm.executeQuery();

            if (rs.next()) {
                exame = new Exame();
                exame.setId(rs.getLong("id"));
                exame.setTipo(rs.getString("tipo"));
                exame.setDescricao(rs.getString("descricao"));
                exame.setData_exame(rs.getDate("data_exame").toLocalDate());

                Paciente paciente = new Paciente();
                paciente.setId(rs.getLong("paciente_id"));
                exame.setPaciente(paciente);
            }

            pstm.close();
        } catch (SQLException e) {
            e.printStackTrace();
        }

        return exame;
    }

    @Override
    public void delete(Exame t) {
        try {
            PreparedStatement pstm = conn.getConnection()
                .prepareStatement("DELETE FROM EXAMES WHERE ID = ?;");
            pstm.setLong(1, t.getId());
            pstm.execute();
            pstm.close();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    @Override
    public List<Exame> findAll() {
        List<Exame> exames = new ArrayList<>();

        try {
            PreparedStatement pstm = conn.getConnection()
                .prepareStatement("SELECT * FROM EXAMES;");
            ResultSet rs = pstm.executeQuery();

            while (rs.next()) {
                Exame exame = new Exame();
                exame.setId(rs.getLong("id"));
                exame.setTipo(rs.getString("tipo"));
                exame.setDescricao(rs.getString("descricao"));
                exame.setData_exame(rs.getDate("data_exame").toLocalDate());

                Paciente paciente = new Paciente();
                paciente.setId(rs.getLong("paciente_id"));
                exame.setPaciente(paciente);

                exames.add(exame);
            }

            pstm.close();
        } catch (SQLException e) {
            e.printStackTrace();
        }

        return exames;
    }

    @Override
    public void update(Exame t) {
        try {
            PreparedStatement pstm = conn.getConnection()
                .prepareStatement("UPDATE EXAMES SET tipo = ?, descricao = ?, data_exame = ?, paciente_id = ? WHERE id = ?;");
            pstm.setString(1, t.getTipo());
            pstm.setString(2, t.getDescricao());
            pstm.setDate(3, Date.valueOf(t.getData_exame()));
            pstm.setLong(4, t.getPaciente().getId());
            pstm.setLong(5, t.getId());
            pstm.execute();
            pstm.close();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public List<Exame> findByPacienteId(Long pacienteId) {
        List<Exame> exames = new ArrayList<>();
        try {
            PreparedStatement pstm = conn.getConnection()
                .prepareStatement("SELECT * FROM EXAMES WHERE paciente_id = ?;");
            pstm.setLong(1, pacienteId);
            ResultSet rs = pstm.executeQuery();
            while (rs.next()) {
                Exame exame = new Exame();
                exame.setId(rs.getLong("id"));
                exame.setTipo(rs.getString("tipo"));
                exame.setDescricao(rs.getString("descricao"));
                exame.setData_exame(rs.getDate("data_exame").toLocalDate());

                Paciente paciente = new Paciente();
                paciente.setId(rs.getLong("paciente_id"));
                exame.setPaciente(paciente);

                exames.add(exame);
            }
            pstm.close();
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return exames;
    }

    public int countByPacienteId(Long pacienteId) {
        int total = 0;
        try {
            PreparedStatement pstm = conn.getConnection()
                .prepareStatement("SELECT COUNT(*) AS total FROM EXAMES WHERE paciente_id = ?;");
            pstm.setLong(1, pacienteId);
            ResultSet rs = pstm.executeQuery();

            if (rs.next()) {
                total = rs.getInt("total");
            }

            pstm.close();
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return total;
    }
}
