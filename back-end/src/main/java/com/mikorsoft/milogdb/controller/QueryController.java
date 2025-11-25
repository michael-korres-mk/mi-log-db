package com.mikorsoft.milogdb.controller;

import com.mikorsoft.milogdb.domain.LogType;
import com.mikorsoft.milogdb.domain.MiLog;
import com.mikorsoft.milogdb.model.*;
import com.mikorsoft.milogdb.repository.MiLogRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.time.LocalDate;
import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/query")
public class QueryController {

	private final MiLogRepository miLogRepository;



	@GetMapping("/1")
	ResponseEntity<List<Query1DTO>> query1(
		@RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate from,
		@RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate to
	){

		ZoneId zone = ZoneId.systemDefault();
		ZonedDateTime fromZ = from.atStartOfDay(zone);
		ZonedDateTime toZ   = to.plusDays(1).atStartOfDay(zone);  // end is exclusive

		return ResponseEntity.ok().body(miLogRepository.query1(fromZ,toZ));
	}

	@GetMapping("/2")
	ResponseEntity<List<Query2DTO>> query2(
			@RequestParam LogType logType,
			@RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate from,
			@RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate to
	){

		ZoneId zone = ZoneId.systemDefault();
		ZonedDateTime fromZ = from.atStartOfDay(zone);
		ZonedDateTime toZ   = to.plusDays(1).atStartOfDay(zone);  // end is exclusive

		return ResponseEntity.ok().body(miLogRepository.query2(logType.name(),fromZ,toZ));
	}

	@GetMapping("/3")
	ResponseEntity<List<Query3DTO>> query3(@RequestParam Long day){
		return ResponseEntity.ok().body(miLogRepository.query3(day));
	}

	@GetMapping("/4")
	ResponseEntity<List<Query4DTO>> query4(
			@RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate from,
			@RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate to
	){

		ZoneId zone = ZoneId.systemDefault();
		ZonedDateTime fromZ = from.atStartOfDay(zone);
		ZonedDateTime toZ   = to.plusDays(1).atStartOfDay(zone);  // end is exclusive

		return ResponseEntity.ok().body(miLogRepository.query4(fromZ,toZ));
	}

	@GetMapping("/5")
	ResponseEntity<List<Query5DTO>> query5(){
		return ResponseEntity.ok().body(miLogRepository.query5());
	}

	@GetMapping("/6")
	ResponseEntity<List<Query6DTO>> query6(){
		return ResponseEntity.ok().body(miLogRepository.query6());
	}

	@GetMapping("/7")
	ResponseEntity<List<MiLog>> query7(@RequestParam Long size){
		return ResponseEntity.ok().body(miLogRepository.query7(size));
	}

	@GetMapping("/8")
	ResponseEntity<List<Query8DTO>> query8(){
		return ResponseEntity.ok().body(miLogRepository.query8());
	}

}
