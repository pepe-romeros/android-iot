package com.example.app.service;

import android.content.Context;

import com.example.app.model.Person;
import com.example.app.repository.PersonRepository;

import java.util.List;

/**
 * This class serves as abstraction between the HttpHandlers and the {@link PersonRepository} DAO.
 * Simplifies the interaction and avoids for web handlers to interact with database directly.
 */
public class PersonService {

    private PersonRepository personRepository;

    /**
     * Main constructor for this class. Receives an instance of the Application's {@link Context} to
     * be able to instantiate the {@link PersonRepository}.
     *
     * @param context the Application's {@link Context} instance.
     */
    public PersonService(Context context) {
        personRepository = new PersonRepository(context);
    }

    /**
     * Will create a new Person in the SQLite database through the {@link PersonRepository#insert(Person)} method.
     *
     * @param firstName the {@link String} value representing a {@link Person}'s first name.
     * @param lastName the {@link String} value representing a {@link Person}'s last name.
     * @return long value corresponding to the new Person's ID on the database
     */
    public long addNewPerson(String firstName, String lastName) {
        Person person = new Person(firstName, lastName);
        return personRepository.insert(person);
    }

    /**
     * Will update an existing {@link Person} in the SQLite database using it's id.
     *
     * @param id the int Id of the Person to be updated
     * @param firstName the new {@link String} value of the {@link Person}'s first name.
     * @param lastName the new {@link String} value of the {@link Person}'s last name.
     * @return boolean true if Person record was updated successfully, false otherwise.
     */
    public boolean updatePerson(int id, String firstName, String lastName) {
        Person person = findPersonById(id);

        if (person != null) {
            person.setFirstName(firstName);
            person.setLastName(lastName);
            return personRepository.update(person);
        } else {
            return false;
        }
    }

    /**
     * Will delete an existing {@link Person} in the SQLite database using it's id.
     *
     * @param id the int Id of the Person to be deleted.
     * @return boolean true if Person record was deleted successfully, false otherwise.
     */
    public boolean deletePerson(int id) {
        Person person = findPersonById(id);
        if (person != null) {
            return personRepository.delete(person);
        } else {
            return false;
        }
    }

    /**
     * Will retrieve all Person records from the SQLite database.
     *
     * @return the {@link List<Person>} list with all the records.
     */
    public List<Person> findAllPeople() {
        return personRepository.findAll();
    }

    /**
     * Will fetch an existing {@link Person} in the SQLite database using it's id.
     *
     * @param id the int ID of the person to be fetched.
     * @return the {@link Person} instance representing the record if found, null otherwise
     */
    public Person findPersonById(int id) {
        return personRepository.findById(id);
    }

    /**
     * Closes and releases the {@link PersonRepository} instance.
     */
    public void cleanUp() {
        if (personRepository != null) {
            personRepository.release();
        }
        personRepository = null;
    }

}
