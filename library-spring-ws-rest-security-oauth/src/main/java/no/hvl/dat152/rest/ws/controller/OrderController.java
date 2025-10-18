/**
 * 
 */
package no.hvl.dat152.rest.ws.controller;

import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.linkTo;
import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.methodOn;

import java.time.LocalDate;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Pageable;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.hateoas.Link;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import no.hvl.dat152.rest.ws.exceptions.OrderNotFoundException;
import no.hvl.dat152.rest.ws.model.Order;
import no.hvl.dat152.rest.ws.service.OrderService;

/**
 * @author tdoy
 */
@RestController
@RequestMapping("/elibrary/api/v1")
public class OrderController {

  @Autowired
  private OrderService orderService;

  // TODO - getAllBorrowOrders (@Mappings, URI=/orders, and method) + filter by
  // expiry and paginate
  @GetMapping("/orders")
  @PreAuthorize("hasRole('ADMIN')")
  public ResponseEntity<List<Order>> getAllBorrowOrders(
      @RequestParam(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate expiry,
      Pageable pageable) throws OrderNotFoundException {

    if (expiry == null) {
      List<Order> orders = orderService.findAllOrders(pageable);
      for (Order o : orders) {
        Link link = linkTo(methodOn(OrderController.class).getBorrowOrder(o.getId())).withSelfRel();
        o.add(link);
      }
      return new ResponseEntity<>(orders, HttpStatus.OK);
    } else {
      List<Order> orders = orderService.findByExpiryDate(expiry, pageable);
      for (Order o : orders) {
        Link link = linkTo(methodOn(OrderController.class).getBorrowOrder(o.getId())).withSelfRel();
        o.add(link);
      }
      return new ResponseEntity<>(orders, HttpStatus.OK);
    }
  }

  // TODO - getBorrowOrder (@Mappings, URI=/orders/{id}, and method)
  @GetMapping("orders/{id}")
  @PreAuthorize("hasRole('ADMIN')")
  public ResponseEntity<Order> getBorrowOrder(@PathVariable Long id) throws OrderNotFoundException {
    Order order = orderService.findOrder(id);
    Link link = linkTo(methodOn(OrderController.class).getBorrowOrder(id)).withSelfRel();
    order.add(link);
    return new ResponseEntity<>(order, HttpStatus.OK);
  }

  // TODO - updateOrder (@Mappings, URI=/orders/{id}, and method)
  @PutMapping("orders/{id}")
  @PreAuthorize("hasRole('ADMIN')")
  public ResponseEntity<Order> updateOrder(@PathVariable Long id, @RequestBody Order order)
      throws OrderNotFoundException {
    Order updatedOrder = orderService.updateOrder(order, id);
    return new ResponseEntity<>(updatedOrder, HttpStatus.OK);
  }

  // TODO - deleteBookOrder (@Mappings, URI=/orders/{id}, and method)
  @DeleteMapping("/orders/{id}")
  @PreAuthorize("hasRole('ADMIN')")
  public ResponseEntity<Object> deleteOrder(@PathVariable Long id) throws OrderNotFoundException {
    orderService.deleteOrder(id);
    return new ResponseEntity<>(HttpStatus.OK);
  }

}
