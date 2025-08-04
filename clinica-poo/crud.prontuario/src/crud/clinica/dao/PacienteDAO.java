package crud.clinica.dao;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import crud.clinica.database.IConnection;
import crud.clinica.exception.CPFJaExisteException;
import crud.clinica.model.Exame;
import crud.clinica.model.Paciente;


public class PacienteDAO implements IEntityDAO<Paciente>{

	private IConnection conn;
	
	public PacienteDAO(IConnection connection) {
		this.conn = connection;
		this.exameDAO = new ExameDAO(connection);
	}
	
	private ExameDAO exameDAO;

	public void create(Paciente t) throws SQLException, CPFJaExisteException {
		
	    if (findByCPF(t.getCpf()) != null) {
	        throw new CPFJaExisteException("Já existe um paciente com o CPF informado. Digite outro CPF.");
	    }
	    try {
	        PreparedStatement pstm = conn.getConnection()
	            .prepareStatement("INSERT INTO PACIENTES (nome, cpf) VALUES (?, ?);");
	        pstm.setString(1, t.getNome());
	        pstm.setString(2, t.getCpf());
	        pstm.execute();
	        pstm.close();
	    } catch (SQLException e) {
	        e.printStackTrace();
	        throw e; 
	    }
	}


	@Override
	public Paciente findById(Long id) {
	    Paciente p = null;

	    try {
	        PreparedStatement pstm = conn.getConnection()
	                .prepareStatement("SELECT * FROM PACIENTES WHERE ID = ?;");
	        pstm.setLong(1, id);
	        ResultSet rs = pstm.executeQuery();

	        if (rs.next()) {
	            p = new Paciente(rs.getLong("id"), rs.getString("nome"), rs.getString("cpf"));

	            List<Exame> examesDoPaciente = exameDAO.findByPacienteId(p.getId());
	            p.setExames(examesDoPaciente);
	        }

	        pstm.close();
	    } catch (SQLException e) {
	        e.printStackTrace();
	    }

	    return p;
	}


	public Paciente findByCPF(String cpf) {
	    try (PreparedStatement pstm = conn.getConnection().prepareStatement("SELECT * FROM PACIENTES WHERE cpf = ?")) {
	        pstm.setString(1, cpf);
	        try (ResultSet rs = pstm.executeQuery()) {
	            if (rs.next()) {
	                Paciente paciente = new Paciente();
	                paciente.setId(rs.getLong("id"));
	                paciente.setNome(rs.getString("nome"));
	                paciente.setCpf(rs.getString("cpf"));
	                return paciente;
	            }
	        }
	    } catch (SQLException e) {
	        e.printStackTrace();
	    }
	    return null;
	}


	
	@Override
	public void delete(Paciente t) {

		try {
			PreparedStatement pstm = conn.getConnection()
					.prepareStatement("DELETE FROM PACIENTES WHERE ID = ?;");
			pstm.setLong(1, t.getId());
			pstm.execute();
			pstm.close();
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}

	@Override
	public List<Paciente> findAll() {
		 List<Paciente> pacientes = new ArrayList<>();

		    try {
		        PreparedStatement pstm = conn.getConnection()
		                .prepareStatement("SELECT * FROM PACIENTES;");
		        ResultSet rs = pstm.executeQuery();

		        while (rs.next()) {
		            Paciente p = new Paciente(rs.getLong("id"), rs.getString("nome"), rs.getString("cpf"));

		            int totalExames = exameDAO.countByPacienteId(p.getId());

		            p.setQuantidadeExames(totalExames);

		            pacientes.add(p);
		        }

		        pstm.close();
		    } catch (SQLException e) {
		        e.printStackTrace();
		    }

		    if (pacientes.isEmpty()) {
		        System.out.println("Nenhum paciente encontrado.");
		    }

		    return pacientes;
	}



	@Override
	public void update(Paciente t) throws SQLException, CPFJaExisteException {
		Paciente pacienteExistente = findByCPF(t.getCpf());
		
		if (pacienteExistente != null && !pacienteExistente.getId().equals(t.getId())) {
	        throw new CPFJaExisteException("Já existe um paciente com o CPF informado. Digite outro CPF");
	    }
		
	    PreparedStatement pstm = conn.getConnection()
	        .prepareStatement("UPDATE PACIENTES SET NOME = ?, CPF = ? WHERE ID = ?;");
	    pstm.setString(1, t.getNome());
	    pstm.setString(2, t.getCpf());
	    pstm.setLong(3, t.getId());
	    pstm.execute();
	    pstm.close();
	}


	public ExameDAO getExameDAO() {
		return exameDAO;
	}


	public void setExameDAO(ExameDAO exameDAO) {
		this.exameDAO = exameDAO;
	}

}
