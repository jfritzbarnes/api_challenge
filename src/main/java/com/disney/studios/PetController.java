package com.disney.studios;

//import java.util.concurrent.
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;

import org.springframework.beans.factory.annotation.Autowired;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.Resource;

import java.util.*;

@RestController
public class PetController {

  @Autowired
  PetLoader petLoader;

  /**
   * respond to the GET /dogs API.
   * returns an array of all breeds; with the dogs within each
   * breed sorted by votes.
   * Cloning occurs to allow for serialization of the Dogs containing
   * the haveVoted property based on the client making the request
   * @param clientID comes from query param
   */
  @RequestMapping("/dogs")
  public List<Breed> dogs(@RequestParam("clientID") String clientid) {
    List<Breed> clone = new ArrayList<Breed>();
    for(Breed b : petLoader.breeds) {
      Breed bClone = b.clone(clientid);
      Collections.sort(bClone.dogs);
      clone.add(bClone);
    }
    return clone;
  }

  /**
   * respond to the GET /dogs/{breed} API.
   * returns an array of all dogs within the desired breed sorted
   * by votes.
   * Cloning occurs to allow for serialization of the Dogs containing
   * the haveVoted property based on the client making the request
   * @param breed comes from the path variable and is the breed to return
   * @param clientID comes from query param
   */
  @RequestMapping(value = "/dogs/{breed}", method=RequestMethod.GET)
  public List<Dog> breeds(@PathVariable String breed,
                          @RequestParam("clientID") String clientid) {
    Iterator<Breed> i = petLoader.breeds.iterator();
    while(i.hasNext()) {
      Breed b = i.next();
      if(b.breed.equals(breed)) {
        Breed cloned = b.clone(clientid);
        Collections.sort(cloned.dogs);
        return cloned.dogs;
      }
    }

    // breed not found in list of breeds
    Breed breed404 = new Breed(breed);
    return breed404.dogs;
  }

  /**
   * vote for a dog as favorite.
   * Iterates through breeds and dogs until we find a match
   * than calls that dog vote function
   * @param id id of the dog we're voting for
   * @param action up|down vote
   * @param clientID id of the client
   */
  @RequestMapping(value = "/dog/{id}", method=RequestMethod.POST)
  public Dog vote(@PathVariable String id,
                  @RequestParam("action") String action,
                  @RequestParam("clientID") String clientid) {
    Iterator<Breed> breeds = petLoader.breeds.iterator();
    while(breeds.hasNext()) {
      Breed b = breeds.next();
      Iterator<Dog> dogs = b.dogs.iterator();
      while(dogs.hasNext()) {
        Dog d = dogs.next();
        if(d.getId().equals(id)) {
          d.vote(action, clientid);
          return d.clone(clientid);
        }
      }
    }
    return new Dog(id, "not", "found");
  }
}
