package service;

import dto.AuthorDTO;
import entity.Author;
import mapper.AuthorMapper;
import repository.AuthorRepository;

import java.util.List;
import java.util.stream.Collectors;

public class AuthorService {
    private final AuthorRepository repository;
    private final AuthorMapper authorMapper;
    public AuthorService(AuthorRepository repository, AuthorMapper authorMapper) {
        this.repository = repository;
        this.authorMapper = authorMapper;
    }

    public AuthorDTO createAuthor(AuthorDTO dto) {
        Author author = authorMapper.toEntity(dto);
        Author created = repository.create(author);
        return authorMapper.toDto(created);
    }

    public AuthorDTO getAuthorById(int id) {
        Author author = repository.findbyId(id);
        return authorMapper.toDto(author);
    }

    public List<AuthorDTO> getAllAuthors() {
        List<Author> authors = repository.findAll();
        return authors.stream().map(authorMapper::toDto).collect(Collectors.toList());
    }

    public boolean updateAuthor(int id, AuthorDTO dto) {
        Author author = authorMapper.toEntity(dto);
        author.setAuthorId(id);
        return repository.update(author);
    }

    public boolean deleteAuthor(int id) {
        return repository.delete(id);
    }
}
