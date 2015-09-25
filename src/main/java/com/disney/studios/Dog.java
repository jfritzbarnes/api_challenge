package com.disney.studios;

import java.util.*;
import java.lang.Comparable;

/**
 * Store the information about a particular dog
 * and number of times it has been favorited
 * along with who so we can prevent ballot-stuffing.
 */
public class Dog implements Comparable<Dog> {
  private final String id;
  private final String breed;
  private long votes;
  private Set<String> votedClients;
  private final String url;
  private String forClientid;

  /**
   * Create a dog
   *
   * @param id a unique identifer for the dog
   * @param breed the breed of the dog
   * @param url the url to a picture for this dog
   */
  public Dog(long id, String breed, String url) {
    this.id = String.valueOf(id);
    this.breed = breed;
    this.votes = 0;
    this.votedClients = new HashSet<String>();
    this.url = url;
  }

  /**
   * Create a dog via string
   *
   * @param id a unique identifer for the dog
   * @param breed the breed of the dog
   * @param url the url to a picture for this dog
   */
  public Dog(String id, String breed, String url) {
    this.id = String.valueOf(id);
    this.breed = breed;
    this.votes = 0;
    this.votedClients = new HashSet<String>();
    this.url = url;
  }

  /**
   * clone a dog for sending to a particular clientid
   *
   * @param clientid the client
   */
  public Dog clone(String forClientid) {
    Dog d = new Dog(this.id, this.breed, this.url);
    d.votes = this.votes;
    d.votedClients = this.votedClients;
    d.forClientid = forClientid;
    return d;
  }

  /**
   * like/favorite a dog; a dog can only be liked
   * once. If it is liked more than once additional
   * likes will be ignored.
   * 
   * @param action type of vote up|down (up==favorite)
   * @param clientid client who is casting vote
   */
  public void vote(String action, String clientid) {
    if(action.equals("up")) {
      if(!this.votedClients.contains(clientid)) {
        this.votes = this.votes + 1;
        this.votedClients.add(clientid);
      }
    } else if (action.equals("down")) {
      if(this.votedClients.contains(clientid)) {
        this.votes = this.votes - 1;
        this.votedClients.remove(clientid);
      }
    }
  }

  @Override
  public int compareTo(Dog dog) {
    if(this.votes == dog.votes) return this.id.compareTo(dog.id);
    return (int) (dog.votes - this.votes);
  }

  public String toString() {
    return "Breed: " + this.breed + ", votes: " + this.votes + ", id: " + this.id;
  }

  public String getId() {
    return id;
  }

  public String getBreed() {
    return breed;
  }

  public long getVotes() {
    return votes;
  }

  public String getUrl() {
    return url;
  }

  // dynamically calculate haveVoted property based on
  // whether the user is in the votedClients array
  public Boolean getHaveVoted() {
    return (this.votedClients.contains(forClientid)) ? true : false;
  }
}
