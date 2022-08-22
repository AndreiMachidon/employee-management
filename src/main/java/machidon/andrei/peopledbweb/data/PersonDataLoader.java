package machidon.andrei.peopledbweb.data;

import machidon.andrei.peopledbweb.biz.model.Person;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;

import java.util.List;

//@Component
public class PersonDataLoader implements ApplicationRunner {
    private PersonRepository personRepository;

    public PersonDataLoader(PersonRepository personRepository) {
        this.personRepository = personRepository;
    }

    @Override
    public void run(ApplicationArguments args) throws Exception {
        if (personRepository.count() == 0) {
            List<Person> people = List.of(
      //              new Person(null, "Jake", "Snake", LocalDate.of(1950, 1, 1), "dummy@sample.com",new BigDecimal("50000")),
      //              new Person(null, "Sarah", "Smith", LocalDate.of(1960, 2, 1), "dummy@sample.com",new BigDecimal("60000")),
      //              new Person(null, "Johnny", "Jackson", LocalDate.of(1970, 3, 1), "dummy@sample.com",new BigDecimal("70000")),
      //              new Person(null, "Bobby", "Kim", LocalDate.of(1980, 4, 1), "dummy@sample.com",new BigDecimal("80000")),
      //              new Person(null, "Raul", "Rodrigo", LocalDate.of(1990, 5, 1), "dummy@sample.com",new BigDecimal("90000")),
      //              new Person(null, "Paul", "Logan", LocalDate.of(1997, 6, 1), "dummy@sample.com",new BigDecimal("95000"))
            );
            personRepository.saveAll(people);
        }
    }
}
