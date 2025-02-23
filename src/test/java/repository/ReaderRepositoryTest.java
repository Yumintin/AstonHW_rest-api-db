package repository;

import entity.Reader;
import org.junit.jupiter.api.*;
import java.util.List;

@TestInstance(TestInstance.Lifecycle.PER_CLASS)
public class ReaderRepositoryTest {

    private ReaderRepository readerRepository;

    @BeforeAll
    public void setUp() {
        readerRepository = new ReaderRepository();
    }

    @BeforeEach
    public void clearData() {
    }

    @Test
    public void testCreateAndGetById() {
        Reader reader = new Reader();
        reader.setName("Test Reader");
        reader.setEmail("reader@example.com");

        Reader createdReader = readerRepository.create(reader);
        Assertions.assertNotNull(createdReader, "Созданный объект Reader не должен быть null");
        Assertions.assertNotNull(createdReader.getReaderId(), "ID читателя должен быть установлен");
        Assertions.assertNotNull(createdReader.getRegistrationDate(), "Дата регистрации должна быть установлена");

        Reader foundReader = readerRepository.getById(createdReader.getReaderId());
        Assertions.assertNotNull(foundReader, "Читатель должен быть найден по ID");
        Assertions.assertEquals("Test Reader", foundReader.getName());
        Assertions.assertEquals("reader@example.com", foundReader.getEmail());
    }

    @Test
    public void testUpdate() {
        Reader reader = new Reader();
        reader.setName("Original Reader");
        reader.setEmail("original@example.com");

        Reader createdReader = readerRepository.create(reader);
        createdReader.setName("Updated Reader");
        createdReader.setEmail("updated@example.com");

        boolean updated = readerRepository.update(createdReader);
        Assertions.assertTrue(updated, "Обновление читателя должно пройти успешно");

        Reader updatedReader = readerRepository.getById(createdReader.getReaderId());
        Assertions.assertNotNull(updatedReader, "После обновления читатель должен находиться по ID");
        Assertions.assertEquals("Updated Reader", updatedReader.getName());
        Assertions.assertEquals("updated@example.com", updatedReader.getEmail());
    }

    @Test
    public void testGetAll() {
        Reader reader1 = new Reader();
        reader1.setName("Reader One");
        reader1.setEmail("one@example.com");
        readerRepository.create(reader1);

        Reader reader2 = new Reader();
        reader2.setName("Reader Two");
        reader2.setEmail("two@example.com");
        readerRepository.create(reader2);

        List<Reader> readers = readerRepository.getAll();
        Assertions.assertNotNull(readers, "Список читателей не должен быть null");
        Assertions.assertTrue(readers.size() >= 2, "В базе должно быть минимум 2 читателя");
    }

    @Test
    public void testDelete() {
        Reader reader = new Reader();
        reader.setName("Delete Reader");
        reader.setEmail("delete@example.com");

        Reader createdReader = readerRepository.create(reader);
        boolean deleted = readerRepository.delete(createdReader.getReaderId());
        Assertions.assertTrue(deleted, "Удаление читателя должно пройти успешно");

        Reader foundReader = readerRepository.getById(createdReader.getReaderId());
        Assertions.assertNull(foundReader, "После удаления читатель не должен быть найден");
    }
}
