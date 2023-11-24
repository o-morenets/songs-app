package com.example.resourceservice.controller;

import com.example.resourceservice.entity.Resource;
import com.example.resourceservice.exception.Mp3ValidationException;
import com.example.resourceservice.exception.ResourceNotFoundException;
import com.example.resourceservice.service.ResourceService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

@RestController
@RequestMapping("/resources")
@RequiredArgsConstructor
public class ResourceController {

	private final ResourceService resourceService;

	@PostMapping
	public ResponseEntity<String> upload(@RequestParam("file") MultipartFile file) {
		try {
			Resource resource = resourceService.save(file);
			return ResponseEntity.status(HttpStatus.OK).body(String.format("{\"id\":%d}", resource.getId()));
		} catch (Mp3ValidationException e) {
			return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
		} catch (Exception e) {
			return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
		}
	}

	@GetMapping("/{id}")
	public ResponseEntity<Resource> findById(@PathVariable Long id) {
		try {
			Resource resource = resourceService.findById(id);
			return new ResponseEntity<>(resource, HttpStatus.OK);
		} catch (ResourceNotFoundException e) {
			return new ResponseEntity<>(HttpStatus.NOT_FOUND);
		} catch (Exception e) {
			return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
		}
	}

	@DeleteMapping
	public ResponseEntity<List<Long>> deleteAllById(@RequestParam List<Long> id) {
		try {
			List<Long> deletedIds = resourceService.deleteAllById(id);
			return new ResponseEntity<>(deletedIds, HttpStatus.OK);
		} catch (Exception e) {
			return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
		}
	}
}
