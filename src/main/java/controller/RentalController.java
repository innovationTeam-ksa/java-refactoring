package controller;

import entity.Customer;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import response.RentalResponse;
import service.RentalInfoService;

@RestController
@RequestMapping("/api/rental")
public class RentalController {

    @Autowired
    private RentalInfoService rentalInfoService;

    @PostMapping("/statement")
    public ResponseEntity<RentalResponse> generateRentalStatement(@RequestBody Customer customer) {
        RentalResponse response = rentalInfoService.generateRentalStatement(customer);
        return ResponseEntity.ok(response);
    }

}
