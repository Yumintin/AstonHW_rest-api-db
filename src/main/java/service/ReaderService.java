package service;

import dto.ReaderDTO;
import entity.Reader;
import mapper.Mapper;
import mapper.ReaderMapper;
import repository.ReaderRepository;

import java.util.List;
import java.util.stream.Collectors;

public class ReaderService {
    private final ReaderRepository repository;
    private final ReaderMapper readerMapper;

    public ReaderService(ReaderRepository repository, ReaderMapper readerMapper) {
        this.repository=repository;
        this.readerMapper=readerMapper;
    }
    public ReaderDTO createReader(ReaderDTO dto) {
        Reader reader = readerMapper.toEntity(dto);
        Reader created = repository.create(reader);
        return readerMapper.toDto(created);
    }

    public ReaderDTO getReaderById(int id) {
        Reader reader = repository.getById(id);
        return readerMapper.toDto(reader);
    }

    public List<ReaderDTO> getAllReaders() {
        List<Reader> readers = repository.getAll();
        return readers.stream().map(readerMapper::toDto).collect(Collectors.toList());
    }

    public boolean updateReader(int id, ReaderDTO dto) {
        Reader reader = readerMapper.toEntity(dto);
        reader.setReaderId(id);
        return repository.update(reader);
    }

    public boolean deleteReader(int id) {
        return repository.delete(id);
    }
}
