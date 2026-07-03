package repository;

import model.PersonalLibrary;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

/**
 * OWNER: Team 17
 * FEATURE GROUP: Repository in-memory dùng chung
 * PURPOSE: Thành phần nền tảng của phần 1, phục vụ mô phỏng dữ liệu và luồng nghiệp vụ ở các phần sau.
 */
public class PersonalLibraryRepository {
    private List<PersonalLibrary> libraries = new ArrayList<>();

    public void save(PersonalLibrary library) {
        findByUserId(library.getUserId()).ifPresent(libraries::remove);
        libraries.add(library);
    }

    public PersonalLibrary getOrCreate(String userId) {
        Optional<PersonalLibrary> existing = findByUserId(userId);
        if (existing.isPresent()) {
            return existing.get();
        }
        PersonalLibrary library = new PersonalLibrary(userId);
        save(library);
        return library;
    }

    public Optional<PersonalLibrary> findByUserId(String userId) {
        return libraries.stream().filter(library -> library.getUserId().equals(userId)).findFirst();
    }
}
