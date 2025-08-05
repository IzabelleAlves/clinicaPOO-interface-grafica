package crud.clinica.model;

import java.time.LocalDate;
import java.util.Objects;

public class Exame {

    private Long id;
    private String tipo;
    private String descricao;
    private LocalDate data_exame;
    private Paciente paciente;

    public Exame() {
    }

    public Exame(Paciente paciente, String tipo, LocalDate data_exame, String descricao) {
        this.paciente = paciente;
        this.tipo = tipo;
        this.data_exame = data_exame;
        this.descricao = descricao;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getTipo() {
        return tipo;
    }

    public void setTipo(String tipo) {
        this.tipo = tipo;
    }

    public String getDescricao() {
        return descricao;
    }

    public void setDescricao(String descricao) {
        this.descricao = descricao;
    }

    public LocalDate getData_exame() {
        return data_exame;
    }

    public void setData_exame(LocalDate data_exame) {
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
               ", Tipo: " + tipo +
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
        if (this == obj) return true;
        if (obj == null || getClass() != obj.getClass()) return false;
        Exame other = (Exame) obj;
        return Objects.equals(id, other.id);
    }
}
