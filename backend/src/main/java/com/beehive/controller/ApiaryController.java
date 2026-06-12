package com.beehive.controller;

import com.beehive.entity.Apiary;
import com.beehive.service.ApiaryService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/api/apiaries")
@RequiredArgsConstructor
public class ApiaryController {

    private final ApiaryService apiaryService;

    @GetMapping
    public ResponseEntity<List<Apiary>> findAll() {
        return ResponseEntity.ok(apiaryService.findAll());
    }

    @GetMapping("/{id}")
    public ResponseEntity<Apiary> findById(@PathVariable Long id) {
        return ResponseEntity.ok(apiaryService.findById(id));
    }

    @PostMapping
    public ResponseEntity<Apiary> create(@RequestBody Apiary apiary) {
        return ResponseEntity.ok(apiaryService.create(apiary));
    }

    @PutMapping("/{id}")
    public ResponseEntity<Apiary> update(@PathVariable Long id, @RequestBody Apiary apiary) {
        return ResponseEntity.ok(apiaryService.update(id, apiary));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable Long id) {
        apiaryService.delete(id);
        return ResponseEntity.noContent().build();
    }
}
