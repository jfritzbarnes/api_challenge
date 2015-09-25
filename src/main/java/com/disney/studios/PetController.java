package com.disney.studios;

import static com.disney.studios.DogVoteHistory.generateId;
//import java.util.concurrent.
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.transaction.annotation.Transactional;

import org.springframework.beans.factory.annotation.Autowired;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.Resource;

import java.util.*;

@RestController
public class PetController {

  @Autowired
  PetLoader petLoader;
    
  @Autowired
  DogVoteCountRepository countRepository;
  
  @Autowired
  DogVoteHistoryRepository historyRepository;

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
   * than calls that dog vote function. Since we are performing crud operation
   * on possible two different entities we put this in a Transaction
   * @param id id of the dog we're voting for
   * @param action up|down vote
   * @param clientID id of the client
   */
  @RequestMapping(value = "/dog/{id}", method=RequestMethod.POST)
  @Transactional
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
          // Ok we found the dog, lets check if the client already has voted
          // we use a composite key here for simplicity
          String voteHistoryId = generateId(id, clientid);
          DogVoteHistory history = historyRepository.findOne(voteHistoryId);
          DogVoteCount dogCount = countRepository.findOne(id);
          if(dogCount == null) {
             // No vote count exists in the database, let's create one
             dogCount = new DogVoteCount(id);
          }
            
          if(history != null) {

              // User has already voted, just return
              if(history.getVoteType().equals("up") && action.equals("up")) {
                  // Client has already voted up for this dog one time, just return
                  return d.clone(clientid);
              } else if(history.getVoteType().equals("down") && action.equals("down")) {
                  // Client has already voted down for this dog one time, just return
                  return d.clone(clientid);
              } else if(history.getVoteType().equals("up") && action.equals("down")) {
                  // Client has voted up before, but down now
                  history.setVoteType("down");
                  dogCount.decrement();
              } else {
                  // Client voted down before, but votes up now
                  history.setVoteType("up");
                  dogCount.increment();
              }
              
          } else {
              
              if(action.equals("down")) {
                  // first time a user votes and its a down vote, just return
                  return d.clone(clientid);
              }
              
              // No vote registerd before, and it is an up vote save it
              history = new DogVoteHistory(voteHistoryId, "up");
              dogCount.increment();
              
          }
          // Save hcount history for this client
          historyRepository.save(history);
          // Save the total count in db
          countRepository.save(dogCount);
          // Set the count on the dog object
          d.setVotes(dogCount.getCount());
            
          return d.clone(clientid);
        
        }
      }
    }
    return new Dog(id, "not", "found");

  }
    
    








}
