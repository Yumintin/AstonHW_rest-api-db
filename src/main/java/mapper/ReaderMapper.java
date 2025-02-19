package mapper;

import dto.ReaderDTO;
import entity.Reader;

public class ReaderMapper {
    public static ReaderDTO toReaderDTO(Reader reader) {
        if (reader == null) return null;
        ReaderDTO readerDTO = new ReaderDTO();
        readerDTO.setReader_id(reader.getReader_id());
        readerDTO.setName(reader.getName());
        readerDTO.setEmail(reader.getEmail());
        readerDTO.setRegistration_date(reader.getRegistration_date());
        return readerDTO;
    }
    public static Reader toReader(ReaderDTO readerDTO) {
        if (readerDTO == null) return null;
        Reader reader = new Reader();
        reader.setReader_id(readerDTO.getReader_id());
        reader.setName(readerDTO.getName());
        reader.setEmail(readerDTO.getEmail());
        reader.setRegistration_date(readerDTO.getRegistration_date());
        return reader;
    }
}
