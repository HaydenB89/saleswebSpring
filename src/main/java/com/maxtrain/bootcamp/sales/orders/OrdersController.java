package com.maxtrain.bootcamp.sales.orders;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@CrossOrigin
@RestController
@RequestMapping("/api/orders")
public class OrdersController {
	
	@Autowired
	private OrderRepository orderRepo;
	
	@GetMapping
	public ResponseEntity<Iterable<Orders>> getOrders(){
		var orders = orderRepo.findAll();
		return new ResponseEntity<Iterable<Orders>>(orders, HttpStatus.OK);
	}
	
	@GetMapping("{id}")
	public ResponseEntity<Orders> getOrders(@PathVariable int id) {
		var order = orderRepo.findById(id);
		if(order.isEmpty()) {
			return new ResponseEntity<>(HttpStatus.NOT_FOUND);
		}
		return new ResponseEntity<Orders>(order.get(), HttpStatus.OK);
	}
	
	@GetMapping("reviews")
	public ResponseEntity<Iterable<Orders>> getOrdersInReview() {
		var orders = orderRepo.findByStatus("REVIEW");
		return new ResponseEntity<Iterable<Orders>> (orders, HttpStatus.OK);
	}
	
	@GetMapping("rejected")
	public ResponseEntity<Iterable<Orders>> getOrdersInRejected() {
		var orders = orderRepo.findByStatus("REJECTED");
		return new ResponseEntity<Iterable<Orders>> (orders, HttpStatus.OK);
	}
	
	@GetMapping("approved")
	public ResponseEntity<Iterable<Orders>> getOrdersInApproved() {
		var orders = orderRepo.findByStatus("APPROVED");
		return new ResponseEntity<Iterable<Orders>> (orders, HttpStatus.OK);
	}
	
	@PostMapping
	public ResponseEntity<Orders> postOrder(@RequestBody Orders order){
		if(order == null || order.getId() != 0) {
			return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
		}
		order.setStatus("New");
		var ord = orderRepo.save(order);
		return new ResponseEntity<Orders>(ord, HttpStatus.CREATED);
	}
	
	@SuppressWarnings("rawtypes")
	@PutMapping("review/{id}")
	public ResponseEntity reviewOrder(@PathVariable int id, @RequestBody Orders order) {
		var statusValue = (order.getTotal() <= 50) ? "APPROVED" : "REVIEW";
		order.setStatus(statusValue);
		return putOrders(id, order);
	}
	
	@SuppressWarnings("rawtypes")
	@PutMapping("approve/{id}")
	public ResponseEntity approveOrder(@PathVariable int id, @RequestBody Orders order) {
		order.setStatus("APPROVED");
		return putOrders(id, order);
	}
	
	@SuppressWarnings("rawtypes")
	@PutMapping("reject/{id}")
	public ResponseEntity rejectOrder(@PathVariable int id, @RequestBody Orders order) {
		order.setStatus("REJECTED");
		return putOrders(id, order);
	}
	
	@SuppressWarnings("rawtypes")
	@PutMapping("{id}")
	public ResponseEntity putOrders(@PathVariable int id, @RequestBody Orders order) {
		if(order == null || order.getId() == 0) {
			return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
		}
		var ord = orderRepo.findById(order.getId());
		if(ord.isEmpty()) {
			return new ResponseEntity<>(HttpStatus.NOT_FOUND);
		}
		orderRepo.save(order);
		return new ResponseEntity<>(HttpStatus.NO_CONTENT);
	}
	
	@SuppressWarnings("rawtypes")
	@DeleteMapping("{id}")
	public ResponseEntity putOrders(@PathVariable int id) {
		var order = orderRepo.findById(id);
		if(order.isEmpty()) {
			return new ResponseEntity<>(HttpStatus.NOT_FOUND);
		}
		orderRepo.delete(order.get());
		return new ResponseEntity<>(HttpStatus.NO_CONTENT);
	}

}
