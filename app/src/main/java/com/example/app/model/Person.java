package com.example.app.model;

import com.google.gson.annotations.SerializedName;

/**
 * Represents a person enrolled in the system.
 */
public class Person {
    public static final String SERIALIZED_FIRST_NAME = "first_name";
    public static final String SERIALIZED_LAST_NAME = "last_name";

    @SerializedName("id")
    private int id;
    @SerializedName(SERIALIZED_FIRST_NAME)
    private String firstName;
    @SerializedName(SERIALIZED_LAST_NAME)
    private String lastName;

    public Person(int id, String firstName, String lastName) {
        this.id = id;
        this.firstName = firstName;
        this.lastName = lastName;
    }

    public Person(String firstName, String lastName) {
        this.firstName = firstName;
        this.lastName = lastName;
    }

    public int getId() {
        return id;
    }

    public String getFirstName() {
        return firstName;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public String getLastName() {
        return lastName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    @Override
    public String toString() {
        return "Person{" +
                "firstName='" + firstName + '\'' +
                ", lastName='" + lastName + '\'' +
                '}';
    }
}

