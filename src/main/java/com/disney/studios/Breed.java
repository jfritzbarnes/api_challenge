package com.disney.studios;

import java.util.*;

/**
 * Breed object that represents a set of Dogs
 */
public class Breed {
  public String breed;
  public List<Dog> dogs;

  public Breed(String breed) {
    this.breed = breed;
    this.dogs = new ArrayList<Dog>();
  }

  /**
   * clone a breed for sending to a particular client
   */
  public Breed clone(String forClientid) {
    Breed b = new Breed(this.breed);
    for(Dog d : this.dogs) {
      b.dogs.add(d.clone(forClientid));
    }
    return b;
  }
}
