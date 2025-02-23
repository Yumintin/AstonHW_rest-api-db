package service;

import dto.ReaderDTO;
import entity.Reader;
import mapper.ReaderMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import repository.ReaderRepository;

import java.util.Arrays;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.argThat;
import static org.mockito.Mockito.*;

class ReaderServiceTest {

    private ReaderRepository repository;
    private ReaderMapper readerMapper;
    private ReaderService readerService;

    @BeforeEach
    void setUp() {
        repository = mock(ReaderRepository.class);
        readerMapper = mock(ReaderMapper.class);
        readerService = new ReaderService(repository, readerMapper);
    }

    @Test
    void testCreateReader() {
        ReaderDTO inputDto = new ReaderDTO();
        inputDto.setName("Test Reader");
        inputDto.setEmail("test@example.com");

        Reader readerEntity = new Reader();
        readerEntity.setName("Test Reader");
        readerEntity.setEmail("test@example.com");

        Reader createdReader = new Reader();
        createdReader.setReaderId(1);
        createdReader.setName("Test Reader");
        createdReader.setEmail("test@example.com");

        ReaderDTO outputDto = new ReaderDTO();
        outputDto.setReaderId(1);
        outputDto.setName("Test Reader");
        outputDto.setEmail("test@example.com");

        when(readerMapper.toEntity(inputDto)).thenReturn(readerEntity);
        when(repository.create(readerEntity)).thenReturn(createdReader);
        when(readerMapper.toDto(createdReader)).thenReturn(outputDto);

        ReaderDTO result = readerService.createReader(inputDto);
        assertNotNull(result);
        assertEquals(1, result.getReaderId());
        assertEquals("Test Reader", result.getName());

        verify(readerMapper).toEntity(inputDto);
        verify(repository).create(readerEntity);
        verify(readerMapper).toDto(createdReader);
    }

    @Test
    void testGetReaderById() {
        int id = 1;
        Reader readerEntity = new Reader();
        readerEntity.setReaderId(id);
        readerEntity.setName("Test Reader");

        ReaderDTO outputDto = new ReaderDTO();
        outputDto.setReaderId(id);
        outputDto.setName("Test Reader");

        when(repository.getById(id)).thenReturn(readerEntity);
        when(readerMapper.toDto(readerEntity)).thenReturn(outputDto);

        ReaderDTO result = readerService.getReaderById(id);
        assertNotNull(result);
        assertEquals(id, result.getReaderId());

        verify(repository).getById(id);
        verify(readerMapper).toDto(readerEntity);
    }

    @Test
    void testGetAllReaders() {
        Reader reader1 = new Reader();
        reader1.setReaderId(1);
        reader1.setName("Reader One");

        Reader reader2 = new Reader();
        reader2.setReaderId(2);
        reader2.setName("Reader Two");

        List<Reader> readers = Arrays.asList(reader1, reader2);

        ReaderDTO dto1 = new ReaderDTO();
        dto1.setReaderId(1);
        dto1.setName("Reader One");

        ReaderDTO dto2 = new ReaderDTO();
        dto2.setReaderId(2);
        dto2.setName("Reader Two");

        when(repository.getAll()).thenReturn(readers);
        when(readerMapper.toDto(reader1)).thenReturn(dto1);
        when(readerMapper.toDto(reader2)).thenReturn(dto2);

        List<ReaderDTO> result = readerService.getAllReaders();
        assertNotNull(result);
        assertEquals(2, result.size());

        verify(repository).getAll();
        verify(readerMapper).toDto(reader1);
        verify(readerMapper).toDto(reader2);
    }

    @Test
    void testUpdateReader() {
        int id = 1;
        ReaderDTO inputDto = new ReaderDTO();
        inputDto.setName("Updated Reader");
        inputDto.setEmail("updated@example.com");

        Reader readerEntity = new Reader();
        readerEntity.setName("Updated Reader");
        readerEntity.setEmail("updated@example.com");

        when(readerMapper.toEntity(inputDto)).thenReturn(readerEntity);
        when(repository.update(argThat(reader -> reader.getReaderId() == id &&
                "Updated Reader".equals(reader.getName()))))
                .thenReturn(true);

        boolean result = readerService.updateReader(id, inputDto);
        assertTrue(result);

        verify(readerMapper).toEntity(inputDto);
        verify(repository).update(argThat(reader -> reader.getReaderId() == id &&
                "Updated Reader".equals(reader.getName())));
    }

    @Test
    void testDeleteReader() {
        int id = 1;
        when(repository.delete(id)).thenReturn(true);
        boolean result = readerService.deleteReader(id);
        assertTrue(result);
        verify(repository).delete(id);
    }
}
