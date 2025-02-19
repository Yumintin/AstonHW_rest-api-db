package service;

import dto.AuthorDTO;
import entity.Author;
import mapper.AuthorMapper;
import repository.AuthorRepository;

import java.util.List;
import java.util.stream.Collectors;

public class AuthorService {
    private final AuthorRepository repository = new AuthorRepository();

    public AuthorDTO createAuthor(AuthorDTO dto) {
        Author author = AuthorMapper.toAuthor(dto);
        Author created = repository.create(author);
        return AuthorMapper.toAuthorDTO(created);
    }

    public AuthorDTO getAuthorById(int id) {
        Author author = repository.findbyId(id);
        return AuthorMapper.toAuthorDTO(author);
    }

    public List<AuthorDTO> getAllAuthors() {
        List<Author> authors = repository.findAll();
        return authors.stream().map(AuthorMapper::toAuthorDTO).collect(Collectors.toList());
    }

    public boolean updateAuthor(int id, AuthorDTO dto) {
        Author author = AuthorMapper.toAuthor(dto);
        author.setAuthor_id(id);
        return repository.update(author);
    }

    public boolean deleteAuthor(int id) {
        return repository.delete(id);
    }
}
