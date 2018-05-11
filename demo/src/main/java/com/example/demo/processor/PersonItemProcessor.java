package com.example.demo.processor;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.batch.item.ItemProcessor;

import com.example.demo.model.Person;
import com.example.demo.model.User;

public class PersonItemProcessor implements ItemProcessor<Person, Person> {

    private static final Logger log = LoggerFactory.getLogger(PersonItemProcessor.class);
    
    @Override
    public Person process(final Person person) throws Exception {
    	System.out.println("in Processoer");
        //final String name = person.getName().toUpperCase();
        /*User transformedPerson = new User();
        transformedPerson.setFirstName(person.getFirstName());
        transformedPerson.setLastName(person.getLastName());*/
    	
    	
    	Person transformedPerson = new Person();
        transformedPerson.setFirstName(person.getFirstName());
        transformedPerson.setLastName(person.getLastName());
        transformedPerson.setEmail(person.getEmail());
        transformedPerson.setAge(person.getAge());
        log.info("Converting (" + person + ") "+ "Thread name : "+ Thread.currentThread().getName());
        
        return transformedPerson;
    }

   /* @Override
    public Person process(final Person person) throws Exception {
        final String firstName = person.getFirstName().toUpperCase();
        final String lastName = person.getLastName().toUpperCase();

        final Person transformedPerson = new Person(firstName, lastName,person.getEmail(),person.getAge());

        log.info("Converting (" + person + ") into (" + transformedPerson + ")");

        return transformedPerson;
    }*/

}