package dto;

import lombok.Data;

import java.util.Objects;

@Data
public class ReaderDTO {
    private Integer readerId;
    private String name;

    @Override
    public boolean equals(Object o) {
        if (o == null || getClass() != o.getClass()) return false;
        ReaderDTO readerDTO = (ReaderDTO) o;
        return Objects.equals(readerId, readerDTO.readerId) && Objects.equals(name, readerDTO.name) && Objects.equals(email, readerDTO.email) && Objects.equals(registrationDate, readerDTO.registrationDate);
    }

    @Override
    public int hashCode() {
        return Objects.hash(readerId, name, email, registrationDate);
    }

    private String email;
    private String registrationDate;
}