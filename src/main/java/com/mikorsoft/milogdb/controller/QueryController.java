package com.mikorsoft.milogdb.controller;

import com.mikorsoft.milogdb.repository.MiLogRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@RequestMapping("/query")
public class QueryController {

	private final MiLogRepository miLogRepository;

//	@GetMapping("/1")
//	ResponseEntity<List<Query1DTO>> query1(LocalDateTime from, LocalDateTime to
//	) {
//		return ResponseEntity.ok().body(miLogRepository.query1(from, to));
//	}
//
//	@GetMapping("/2")
//	ResponseEntity<List<Query2DTO>> query2(@RequestParam LogType logType, LocalDateTime from, LocalDateTime to) {
//		return ResponseEntity.ok().body(miLogRepository.query2(logType.name(), from, to));
//	}
//
//	@GetMapping("/3")
//	ResponseEntity<List<Query3DTO>> query3(@RequestParam LocalDate day) {
//		return ResponseEntity.ok().body(miLogRepository.query3(day));
//	}
//
//	@GetMapping("/4")
//	ResponseEntity<List<Query4DTO>> query4(LocalDateTime from, LocalDateTime to) {
//		return ResponseEntity.ok().body(miLogRepository.query4(from, to));
//	}
//
//	@GetMapping("/5")
//	ResponseEntity<List<Query5DTO>> query5() {
//		return ResponseEntity.ok().body(miLogRepository.query5());
//	}
//
//	@GetMapping("/6")
//	ResponseEntity<List<Query6DTO>> query6() {
//		return ResponseEntity.ok().body(miLogRepository.query6());
//	}
//
//	@GetMapping("/7")
//	ResponseEntity<List<MiLog>> query7(@RequestParam Long size) {
//		return ResponseEntity.ok().body(miLogRepository.query7(size));
//	}
//
//	@GetMapping("/8")
//	ResponseEntity<List<Query8DTO>> query8() {
//		return ResponseEntity.ok().body(miLogRepository.query8());
//	}
//
//	@GetMapping("/9")
//	ResponseEntity<List<Query9DTO>> query9() {
//		return ResponseEntity.ok().body(miLogRepository.query9());
//	}
//
//	@GetMapping("/10")
//	ResponseEntity<List<MiLog>> query10() {
//		return ResponseEntity.ok().body(miLogRepository.query10());
//	}
//
//	@GetMapping("/11")
//	ResponseEntity<List<Query111213DTO>> query11(LocalDateTime from, LocalDateTime to, @RequestParam String httpMethod) {
//		return ResponseEntity.ok().body(miLogRepository.query11(from, to, httpMethod));
//	}
//
//	@GetMapping("/12")
//	ResponseEntity<List<Query111213DTO>> query12(LocalDateTime from, LocalDateTime to, @RequestParam List<String> httpMethods) {
//		return ResponseEntity.ok().body(miLogRepository.query12(from, to, httpMethods));
//	}
//
//	@GetMapping("/13")
//	ResponseEntity<List<Query111213DTO>> query13(LocalDateTime from, LocalDateTime to) {
//		return ResponseEntity.ok().body(miLogRepository.query13(from, to));
//	}

}
