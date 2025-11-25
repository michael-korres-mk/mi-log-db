package com.mikorsoft.milogdb.controller;

import com.mikorsoft.milogdb.domain.LogType;
import com.mikorsoft.milogdb.model.Query1DTO;
import com.mikorsoft.milogdb.model.Query2DTO;
import com.mikorsoft.milogdb.model.Query3DTO;
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

}
