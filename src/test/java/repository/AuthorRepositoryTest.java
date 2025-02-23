package repository;

import entity.Author;
import org.junit.jupiter.api.*;
import java.util.List;

@TestInstance(TestInstance.Lifecycle.PER_CLASS)
public class AuthorRepositoryTest {

    private AuthorRepository authorRepository;

    @BeforeAll
    public void setUp() {
        authorRepository = new AuthorRepository();
    }

    @BeforeEach
    public void clearData() {
    }

    @Test
    public void testCreateAndFindById() {
        // Создаем автора
        Author author = new Author();
        author.setName("Test Author");
        author.setBirthYear(1970);

        // Проверяем, что создание проходит успешно и возвращается автор с присвоенным ID
        Author createdAuthor = authorRepository.create(author);
        Assertions.assertNotNull(createdAuthor, "Созданный автор не должен быть null");
        Assertions.assertNotNull(createdAuthor.getAuthorId(), "У автора должен быть присвоен ID после создания");

        // Находим автора по ID и проверяем его данные
        Author foundAuthor = authorRepository.findbyId(createdAuthor.getAuthorId());
        Assertions.assertNotNull(foundAuthor, "Автор должен быть найден по ID");
        Assertions.assertEquals("Test Author", foundAuthor.getName());
        Assertions.assertEquals(1970, foundAuthor.getBirthYear());
    }

    @Test
    public void testUpdate() {
        // Создаем автора
        Author author = new Author();
        author.setName("Original Name");
        author.setBirthYear(1980);
        Author createdAuthor = authorRepository.create(author);

        // Изменяем данные автора
        createdAuthor.setName("Updated Name");
        createdAuthor.setBirthYear(1990);
        boolean updated = authorRepository.update(createdAuthor);
        Assertions.assertTrue(updated, "Обновление автора должно быть успешным");

        // Проверяем, что данные автора обновились
        Author updatedAuthor = authorRepository.findbyId(createdAuthor.getAuthorId());
        Assertions.assertNotNull(updatedAuthor, "Автор должен быть найден после обновления");
        Assertions.assertEquals("Updated Name", updatedAuthor.getName());
        Assertions.assertEquals(1990, updatedAuthor.getBirthYear());
    }

    @Test
    public void testFindAll() {
        // Создаем нескольких авторов
        Author author1 = new Author();
        author1.setName("Author One");
        author1.setBirthYear(1960);
        authorRepository.create(author1);

        Author author2 = new Author();
        author2.setName("Author Two");
        author2.setBirthYear(1970);
        authorRepository.create(author2);

        // Получаем список всех авторов
        List<Author> authors = authorRepository.findAll();
        Assertions.assertNotNull(authors, "Список авторов не должен быть null");
        // Если тестовая база данных изначально пуста, ожидаем минимум 2 созданных автора
        Assertions.assertTrue(authors.size() >= 2, "Должно быть как минимум 2 автора");
    }

    @Test
    public void testDelete() {
        // Создаем автора для удаления
        Author author = new Author();
        author.setName("Author To Delete");
        author.setBirthYear(1950);
        Author createdAuthor = authorRepository.create(author);

        // Удаляем автора и проверяем результат
        boolean deleted = authorRepository.delete(createdAuthor.getAuthorId());
        Assertions.assertTrue(deleted, "Удаление автора должно быть успешным");

        // Пытаемся найти удаленного автора
        Author foundAuthor = authorRepository.findbyId(createdAuthor.getAuthorId());
        Assertions.assertNull(foundAuthor, "Автор не должен быть найден после удаления");
    }
}
