package com.mikorsoft.milogdb.ui;

import com.mikorsoft.milogdb.domain.LogType;
import com.mikorsoft.milogdb.domain.MiLogColumn;
import com.mikorsoft.milogdb.repository.MiLogRepository;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Stream;

import static com.mikorsoft.milogdb.domain.MiLogColumn.*;

@Controller
@RequestMapping("/logs")
public class LogController {

	private final MiLogRepository miLogRepository;
	private final Map<Integer, QueryUIComponent> queryUIComponents;


	public LogController(MiLogRepository miLogRepository) {
		this.miLogRepository = miLogRepository;

		this.queryUIComponents = new HashMap<>();

//  1. Find the total logs per type that were created within a specified time range and sort them in a descending order. Please note that individual files may log actions of more than one type.
//  2. Find the total logs per day for a specific action type and time range.
//  3. Find the most common log per source IP for a specific day.
//  4. Find the top-5 Block IDs with regards to total number of actions per day for a specific date range (for types that Block ID is available)
//  5. Find the referrers (if any) that have led to more than one resources.
//  6. Find the 2nd–most–common resource requested.
//  7. Find the access log (all fields) where the size is less than a specified number.
//  8. Find the blocks that have been replicated the same day that they have also been served.
//  9. Find the blocks that have been replicated the same day and hour that they have also been served.
//  10. Find access logs that specified a particular version of Firefox as their browser.
//  11. Find IPs that have issued a particular HTTP method on a particular time range.
//  12. Find IPs that have issued two particular HTTP methods on a particular time range.
//  13. Find IPs that have issued any four distinct HTTP methods on a particular time range.


		queryUIComponents.put(1, new QueryUIComponent(1L, "1. Find the total logs per type that were created within a specified time range and sort them in a descending order.", Stream.of(LOG_TYPE, COUNT).toList()));
		queryUIComponents.put(2, new QueryUIComponent(2L, "2. Find the total logs per day for a specific action type and time range.", Stream.of(DAY, COUNT).toList()));
		queryUIComponents.put(3, new QueryUIComponent(3L, "3. Find the most common log per source IP for a specific day.", Stream.of(DAY, COUNT).toList()));
		queryUIComponents.put(4, new QueryUIComponent(4L, "4. Find the top-5 Block IDs with regards to total number of actions per day for a specific date range (for types that Block ID is available)", Stream.of(BLOCK_ID, DAY, COUNT).toList()));
		queryUIComponents.put(5, new QueryUIComponent(5L, "5. Find the referrers (if any) that have led to more than one resources.", Stream.of(REFERRER, COUNT).toList()));
		queryUIComponents.put(6, new QueryUIComponent(6L, "6. Find the 2nd–most–common resource requested.", Stream.of(RESOURCE_REQUESTED, COUNT).toList()));
		queryUIComponents.put(7, new QueryUIComponent(7L, "7. Find the access log (all fields) where the size is less than a specified number.", MiLogColumn.miLogColumns()));
		queryUIComponents.put(8, new QueryUIComponent(8L, "8. Find the blocks that have been replicated the same day that they have also been served.", Stream.of(BLOCK_ID, DAY).toList()));
		queryUIComponents.put(9, new QueryUIComponent(9L, "9. Find the blocks that have been replicated the same day and hour that they have also been served.", Stream.of(BLOCK_ID, DAY, HOUR).toList()));
		queryUIComponents.put(10, new QueryUIComponent(10L, "10. Find access logs that specified a particular version of Firefox as their browser.", MiLogColumn.miLogColumns()));
		queryUIComponents.put(11, new QueryUIComponent(11L, "11. Find IPs that have issued a particular HTTP method on a particular time range.", Stream.of(IP, COUNT).toList()));
		queryUIComponents.put(12, new QueryUIComponent(12L, "12. Find IPs that have issued two particular HTTP methods on a particular time range.", Stream.of(IP, COUNT).toList()));
		queryUIComponents.put(13, new QueryUIComponent(13L, "13. Find IPs that have issued any four distinct HTTP methods on a particular time range.", Stream.of(IP, COUNT).toList()));
	}

	@GetMapping
	String query() {
		return "logs";
	}

	@GetMapping("/{qid}")
	String query(@PathVariable int qid,
	             @RequestParam(required = false) LocalDateTime from,
	             @RequestParam(required = false) LocalDateTime to,
	             @RequestParam(required = false) LogType logType,
	             @RequestParam(required = false) LocalDate day,
	             @RequestParam(required = false) Long size,
	             @RequestParam(required = false) String httpMethod,
	             @RequestParam(required = false) List<String> httpMethods,
	             Model model) {

		List<Map<String, Object>> logs = (switch (qid) {
			case 1 -> miLogRepository.query1(from, to);
			case 2 -> miLogRepository.query2(logType.name(), from, to);
			case 3 -> miLogRepository.query3(day);
			case 4 -> miLogRepository.query4(from, to);
			case 5 -> miLogRepository.query5();
			case 6 -> miLogRepository.query6();
			case 7 -> miLogRepository.query7(size);
			case 8 -> miLogRepository.query8();
			case 9 -> miLogRepository.query9();
			case 10 -> miLogRepository.query10();
			case 11 -> miLogRepository.query11(from, to, httpMethod);
			case 12 -> miLogRepository.query12(from, to, httpMethods);
			case 13 -> miLogRepository.query13(from, to);
			default -> throw new IllegalStateException("Unexpected value: " + qid);
		}).stream().map(log -> dtoToMap(log, queryUIComponents.get(qid).columns())).toList();

		model.addAttribute("queryTitle", queryUIComponents.get(qid).title());
		model.addAttribute("tableColumns", queryUIComponents.get(qid).columns());
		model.addAttribute("logs", logs);


		return "logs";

	}

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
