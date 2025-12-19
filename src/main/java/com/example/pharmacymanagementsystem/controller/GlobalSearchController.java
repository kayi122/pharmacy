package com.example.pharmacymanagementsystem.controller;

import java.util.*;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import com.example.pharmacymanagementsystem.service.*;
import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/api/search")
@RequiredArgsConstructor
public class GlobalSearchController {

    private final MedicineService medicineService;
    private final CompanyService companyService;
    private final SaleService saleService;
    private final UserService userService;

    @GetMapping
    public ResponseEntity<Map<String, Object>> globalSearch(@RequestParam String query) {
        Map<String, Object> results = new HashMap<>();
        
        results.put("medicines", medicineService.searchByNameOrCategory(query));
        results.put("companies", companyService.searchCompanies(query));
        results.put("sales", saleService.searchSales(query));
        results.put("users", userService.searchUsers(query));
        
        return ResponseEntity.ok(results);
    }
}
