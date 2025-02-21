package mapper;

import dto.ReaderDTO;
import entity.Reader;

public class ReaderMapper implements Mapper<Reader, ReaderDTO> {
    @Override
    public ReaderDTO toDto(Reader reader) {
        if (reader == null) {
            return null;
        }
        ReaderDTO dto = new ReaderDTO();
        dto.setReaderId(reader.getReaderId());
        dto.setName(reader.getName());
        dto.setEmail(reader.getEmail());
        dto.setRegistrationDate(reader.getRegistrationDate());
        return dto;
    }

    @Override
    public Reader toEntity(ReaderDTO dto) {
        if (dto == null) {
            return null;
        }
        Reader reader = new Reader();
        reader.setReaderId(dto.getReaderId());
        reader.setName(dto.getName());
        reader.setEmail(dto.getEmail());
        reader.setRegistrationDate(dto.getRegistrationDate());
        return reader;
    }
}
