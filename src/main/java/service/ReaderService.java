package service;

import dto.ReaderDTO;
import entity.Reader;
import mapper.ReaderMapper;
import repository.ReaderRepository;

import java.util.List;
import java.util.stream.Collectors;

public class ReaderService {
    private final ReaderRepository repository = new ReaderRepository();

    public ReaderDTO createReader(ReaderDTO dto) {
        Reader reader = ReaderMapper.toReader(dto);
        Reader created = repository.create(reader);
        return ReaderMapper.toReaderDTO(created);
    }

    public ReaderDTO getReaderById(int id) {
        Reader reader = repository.getById(id);
        return ReaderMapper.toReaderDTO(reader);
    }

    public List<ReaderDTO> getAllReaders() {
        List<Reader> readers = repository.getAll();
        return readers.stream().map(ReaderMapper::toReaderDTO).collect(Collectors.toList());
    }

    public boolean updateReader(int id, ReaderDTO dto) {
        Reader reader = ReaderMapper.toReader(dto);
        reader.setReader_id(id);
        return repository.update(reader);
    }

    public boolean deleteReader(int id) {
        return repository.delete(id);
    }
}
