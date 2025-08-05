package crud.clinica.model;

import java.sql.Timestamp;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public class Paciente {

    private Long id;
    private String nome;
    private String cpf;
    private int quantidadeExames;
    private LocalDateTime dataNascimento;

    private List<Exame> exames = new ArrayList<>();

    public Paciente() {
    }

    public Paciente(String nome, String cpf, LocalDateTime dataNascimento) {
        this.nome = nome;
        this.cpf = cpf;
        this.dataNascimento = dataNascimento;
    }

    public Paciente(Long id, String nome, String cpf, LocalDateTime localDateTime) {
        this.id = id;
        this.nome = nome;
        this.cpf = cpf;
        if (localDateTime != null) {
            this.dataNascimento = localDateTime;
        } else {
            this.dataNascimento = null;
        }
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getNome() {
        return nome;
    }

    public void setNome(String nome) {
        this.nome = nome;
    }

    public String getCpf() {
        return cpf;
    }

    public void setCpf(String cpf) {
        this.cpf = cpf;
    }

    public List<Exame> getExames() {
        return exames;
    }

    public void setExames(List<Exame> exames) {
        this.exames = exames;
    }

    public void setQuantidadeExames(int quantidadeExames) {
        this.quantidadeExames = quantidadeExames;
    }

    public int getQuantidadeExames() {
        return quantidadeExames;
    }

    public LocalDateTime getDataNascimento() {
        return dataNascimento;
    }

    public void setDataNascimento(LocalDateTime dataNascimento) {
        this.dataNascimento = dataNascimento;
    }

    public void setDataNascimento(Timestamp timestamp) {
        if (timestamp != null) {
            this.dataNascimento = timestamp.toLocalDateTime();
        } else {
            this.dataNascimento = null;
        }
    }

    @Override
    public String toString() {
        return "ID: " + id + " - Nome paciente: " + nome + " - CFP: " + cpf + " - Total de exames do paciente: " + quantidadeExames;
    }

    @Override
    public int hashCode() {
        return Objects.hash(cpf);
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj)
            return true;
        if (obj == null)
            return false;
        if (getClass() != obj.getClass())
            return false;
        Paciente other = (Paciente) obj;
        return Objects.equals(cpf, other.cpf);
    }
}
