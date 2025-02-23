package service;

import dto.AuthorDTO;
import entity.Author;
import mapper.AuthorMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import repository.AuthorRepository;

import java.util.Arrays;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class AuthorServiceTest {

    private AuthorRepository repository;
    private AuthorMapper mapper;
    private AuthorService authorService;

    @BeforeEach
    void setUp() {
        repository = mock(AuthorRepository.class);
        mapper = mock(AuthorMapper.class);
        authorService = new AuthorService(repository, mapper);
    }

    @Test
    void testCreateAuthor() {
        AuthorDTO inputDto = new AuthorDTO();
        inputDto.setName("Author Name");

        Author authorEntity = new Author();
        authorEntity.setName("Author Name");

        Author createdEntity = new Author();
        createdEntity.setAuthorId(1);
        createdEntity.setName("Author Name");

        AuthorDTO outputDto = new AuthorDTO();
        outputDto.setAuthorId(1);
        outputDto.setName("Author Name");

        when(mapper.toEntity(inputDto)).thenReturn(authorEntity);
        when(repository.create(authorEntity)).thenReturn(createdEntity);
        when(mapper.toDto(createdEntity)).thenReturn(outputDto);

        AuthorDTO result = authorService.createAuthor(inputDto);

        assertNotNull(result);
        assertEquals(1, result.getAuthorId());
        assertEquals("Author Name", result.getName());

        verify(mapper).toEntity(inputDto);
        verify(repository).create(authorEntity);
        verify(mapper).toDto(createdEntity);
    }

    @Test
    void testGetAuthorById() {
        int id = 1;
        Author authorEntity = new Author();
        authorEntity.setAuthorId(id);
        authorEntity.setName("Author Name");

        AuthorDTO authorDto = new AuthorDTO();
        authorDto.setAuthorId(id);
        authorDto.setName("Author Name");

        when(repository.findbyId(id)).thenReturn(authorEntity);
        when(mapper.toDto(authorEntity)).thenReturn(authorDto);

        AuthorDTO result = authorService.getAuthorById(id);

        assertNotNull(result);
        assertEquals(id, result.getAuthorId());
        assertEquals("Author Name", result.getName());

        verify(repository).findbyId(id);
        verify(mapper).toDto(authorEntity);
    }

    @Test
    void testGetAllAuthors() {
        Author author1 = new Author();
        author1.setAuthorId(1);
        author1.setName("Author 1");

        Author author2 = new Author();
        author2.setAuthorId(2);
        author2.setName("Author 2");

        List<Author> authors = Arrays.asList(author1, author2);

        AuthorDTO dto1 = new AuthorDTO();
        dto1.setAuthorId(1);
        dto1.setName("Author 1");

        AuthorDTO dto2 = new AuthorDTO();
        dto2.setAuthorId(2);
        dto2.setName("Author 2");

        when(repository.findAll()).thenReturn(authors);
        when(mapper.toDto(author1)).thenReturn(dto1);
        when(mapper.toDto(author2)).thenReturn(dto2);

        List<AuthorDTO> result = authorService.getAllAuthors();

        assertNotNull(result);
        assertEquals(2, result.size());
        assertEquals("Author 1", result.get(0).getName());
        assertEquals("Author 2", result.get(1).getName());

        verify(repository).findAll();
        verify(mapper).toDto(author1);
        verify(mapper).toDto(author2);
    }

    @Test
    void testUpdateAuthor() {
        int id = 1;
        AuthorDTO inputDto = new AuthorDTO();
        inputDto.setName("Updated Author");

        Author authorEntity = new Author();
        authorEntity.setName("Updated Author");

        Author updatedEntity = new Author();
        updatedEntity.setAuthorId(id);
        updatedEntity.setName("Updated Author");

        when(mapper.toEntity(inputDto)).thenReturn(authorEntity);
        when(repository.update(any(Author.class))).thenReturn(true);

        boolean result = authorService.updateAuthor(id, inputDto);
        assertTrue(result);

        verify(mapper).toEntity(inputDto);
        verify(repository).update(argThat(author -> author.getAuthorId() == id &&
                "Updated Author".equals(author.getName())));
    }

    @Test
    void testDeleteAuthor() {
        int id = 1;
        when(repository.delete(id)).thenReturn(true);
        boolean result = authorService.deleteAuthor(id);
        assertTrue(result);
        verify(repository).delete(id);
    }
}
