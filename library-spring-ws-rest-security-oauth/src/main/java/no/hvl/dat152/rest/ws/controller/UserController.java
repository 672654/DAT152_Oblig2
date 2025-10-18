/**
 * 
 */
package no.hvl.dat152.rest.ws.controller;

import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.linkTo;
import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.methodOn;

import java.util.List;
import java.util.Set;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.hateoas.Link;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import no.hvl.dat152.rest.ws.exceptions.OrderNotFoundException;
import no.hvl.dat152.rest.ws.exceptions.UserNotFoundException;
import no.hvl.dat152.rest.ws.model.Order;
import no.hvl.dat152.rest.ws.model.User;
import no.hvl.dat152.rest.ws.service.UserService;

/**
 * @author tdoy
 */
@RestController
@RequestMapping("/elibrary/api/v1")
public class UserController {

  @Autowired
  private UserService userService;

  @GetMapping("/users")
  @PreAuthorize("hasRole('ADMIN')")
  public ResponseEntity<Object> getUsers() {

    List<User> users = userService.findAllUsers();

    if (users.isEmpty())

      return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    else
      return new ResponseEntity<>(users, HttpStatus.OK);
  }

  @GetMapping(value = "/users/{id}")
  @PreAuthorize("hasRole('ADMIN')")
  public ResponseEntity<Object> getUser(@PathVariable Long id) throws UserNotFoundException, OrderNotFoundException {

    User user = userService.findUser(id);

    return new ResponseEntity<>(user, HttpStatus.OK);

  }

  // TODO - createUser (@Mappings, URI=/users, and method)
  @PostMapping("/users")
  public ResponseEntity<User> createUser(@RequestBody User user) {
    User createdUser = userService.saveUser(user);
    return new ResponseEntity<>(createdUser, HttpStatus.CREATED);
  }

  // TODO - updateUser (@Mappings, URI, and method)
  @PutMapping(value = "users/{id}")
  public ResponseEntity<User> updateUser(@PathVariable Long id, @RequestBody User user) throws UserNotFoundException {

    User updatedUser = userService.updateUser(user, id);
    return new ResponseEntity<>(updatedUser, HttpStatus.OK);
  }

  // TODO - deleteUser (@Mappings, URI, and method)
  @DeleteMapping(value = "users/{id}")
  public ResponseEntity<Void> deleteUser(@PathVariable Long id) throws UserNotFoundException {
    userService.deleteUser(id);
    return new ResponseEntity<>(HttpStatus.OK);
  }

  // TODO - getUserOrders (@Mappings, URI=/users/{id}/orders, and method)
  @GetMapping("users/{userid}/orders")
  public ResponseEntity<Set<Order>> getUserOrders(@PathVariable Long userid) throws UserNotFoundException {

    Set<Order> orders = userService.getUserOrders(userid);
    return new ResponseEntity<>(orders, HttpStatus.OK);
  }

  // TODO - getUserOrder (@Mappings, URI=/users/{uid}/orders/{oid}, and method)
  @GetMapping(value = "/users/{userid}/orders/{oid}")
  public ResponseEntity<Order> getUserOrder(@PathVariable Long userid, @PathVariable Long oid)
      throws UserNotFoundException {

    Order order = userService.getUserOrder(userid, oid);
    return new ResponseEntity<>(order, HttpStatus.OK);
  }

  // TODO - deleteUserOrder (@Mappings, URI, and method)
  @DeleteMapping("users/{userid}/orders/{orderid}")
  public ResponseEntity<Void> deleteUserOrder(@PathVariable Long userid, @PathVariable Long orderid)
      throws UserNotFoundException {

    userService.deleteOrderForUser(userid, orderid);
    return new ResponseEntity<>(HttpStatus.OK);
  }

  // TODO - createUserOrder (@Mappings, URI, and method) + HATEOAS links
  @PostMapping("users/{userid}/orders")
  public ResponseEntity<Set<Order>> createUserOrder(@RequestBody Order order, @PathVariable Long userid)
      throws UserNotFoundException, OrderNotFoundException {

    User user = userService.createOrdersForUser(userid, order);

    Set<Order> orders = user.getOrders();
    for (Order ord : orders) {
      Link link = linkTo(methodOn(UserController.class).getUserOrder(userid, ord.getId())).withSelfRel();
      ord.add(link);
    }

    return new ResponseEntity<>(orders, HttpStatus.CREATED);
  }

}
