package machidon.andrei.peopledbweb.web.controller;


import lombok.extern.log4j.Log4j2;
import machidon.andrei.peopledbweb.biz.model.Person;
import machidon.andrei.peopledbweb.biz.service.PersonService;
import machidon.andrei.peopledbweb.data.FileStorageRepository;
import machidon.andrei.peopledbweb.data.PersonRepository;
import machidon.andrei.peopledbweb.exception.StorageException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.Resource;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.Errors;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.validation.Valid;
import java.io.IOException;
import java.util.List;
import java.util.Optional;

import static java.lang.String.format;

@Controller
@RequestMapping("/people")
@Log4j2
public class PeopleController {

    private PersonRepository personRepository;
    private FileStorageRepository fileStorageRepository;
    private PersonService personService;

    @Autowired
    public PeopleController(PersonRepository personRepository, FileStorageRepository fileStorageRepository, PersonService personService) {
        this.personRepository = personRepository;
        this.fileStorageRepository = fileStorageRepository;
        this.personService = personService;
    }

    @ModelAttribute("people")
    public Iterable<Person> getPeople( @PageableDefault(size = 10) Pageable page){
        return personService.findAll(page);
    }

    @ModelAttribute("person")
    public Person getPerson(){
        return new Person();
    }

    @GetMapping
    public String showPeoplePage(){
        return "people";
    }

    @GetMapping("/images/{resource}")
    public ResponseEntity<Resource> getResource(@PathVariable String resource) {
        String dispo = """
                 attachment; filename="%s"
                """;
        return ResponseEntity
                .ok()
                .header(HttpHeaders.CONTENT_DISPOSITION, format(dispo, resource))
                .body(fileStorageRepository.findByName(resource));


    }

    @PostMapping
    public String savePerson(Model model, @Valid Person person, Errors errors, @RequestParam("photoFilename") MultipartFile photoFile){
        if(!errors.hasErrors()) {
            try {
                personService.save(person, photoFile.getInputStream());
                return "redirect:people";
            }catch (StorageException | IOException e){
                model.addAttribute("errorMsg", "System can not accept photo files at this time");
            }
                return "people";
            }
            return "people";
    }

    @PostMapping(params = "action=delete")
    public String deletePeople( @RequestParam Optional<List<Long>> selections){
        if(selections.isPresent()){
            System.out.println(selections);
            //personRepository.deleteAllById(selections.get());
            personService.deleteAllById(selections.get());
        }

        return "redirect:people";
    }

    @PostMapping(params = "action=update")
    public String updatePeople(@RequestParam Optional<List<Long>> selections, Model model){
        System.out.println(selections);
        if(selections.isPresent()){
            Person personForUpdate = personRepository.findById(selections.get().get(0)).get();
            model.addAttribute("person", personForUpdate);

        }

        return "people";
    }

    @PostMapping(params = "action=import")
    public String importCSV(@RequestParam MultipartFile csvFile){
        log.info("File name: " +csvFile.getOriginalFilename());
        log.info("File size: " + csvFile.getSize());
        try {
            personService.importCSV(csvFile.getInputStream());
        } catch (IOException e) {
            e.printStackTrace();
        }
        return "redirect:people";
    }
}
