package crud.clinica.model;

import java.time.LocalDateTime;
import java.util.Objects;

public class Exame {

	private Long id;
	private String descricao;
	private LocalDateTime data_exame;
	private Paciente paciente;
	
	public Exame() {
		super();
	}
	
	public Exame( String descricao, LocalDateTime data_exame) {
		super();
		this.descricao = descricao;
		this.data_exame = data_exame;
	}

	public Exame(Long id, String descricao, LocalDateTime data_exame) {
		super();
		this.id = id;
		this.descricao = descricao;
		this.data_exame = data_exame;
	}

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public String getDescricao() {
		return descricao;
	}

	public void setDescricao(String descricao) {
		this.descricao = descricao;
	}

	public LocalDateTime getData_exame() {
		return data_exame;
	}

	public void setData_exame(LocalDateTime data_exame) {
		this.data_exame = data_exame;
	}
	
	public Paciente getPaciente() {
		return paciente;
	}
	
	public void setPaciente(Paciente paciente) {
		this.paciente = paciente;
	}

	@Override
	public String toString() {
	    return "Exame ID: " + id +
	           ", Descrição: " + descricao +
	           ", Data: " + data_exame +
	           ", Paciente ID: " + (paciente != null ? paciente.getId() : "null");
	}



	@Override
	public int hashCode() {
		return Objects.hash(id);
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		Exame other = (Exame) obj;
		return Objects.equals(id, other.id);
	}
	
	
	
	
}
