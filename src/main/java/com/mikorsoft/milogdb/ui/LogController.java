package com.mikorsoft.milogdb.ui;

import com.mikorsoft.milogdb.model.QueryDTO;
import com.mikorsoft.milogdb.repository.MiLogRepository;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;

import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Stream;

import static com.mikorsoft.milogdb.domain.MiLogColumns.*;

@Controller
@RequestMapping("/logs")
public class LogController {

	private final MiLogRepository miLogRepository;
	private final Map<Long,QueryUIComponent> queryUIComponents;


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



		queryUIComponents.put(1L,new QueryUIComponent(1L,"1. Find the total logs per type that were created within a specified time range and sort them in a descending order.", Stream.of(LOG_TYPE, COUNT).toList()));
		queryUIComponents.put(2L,new QueryUIComponent(2L,"2. Find the total logs per day for a specific action type and time range.", Stream.of(DAY, COUNT).toList()));
		queryUIComponents.put(3L,new QueryUIComponent(3L,"3. Find the most common log per source IP for a specific day.", Stream.of(DAY, COUNT).toList()));
		queryUIComponents.put(4L,new QueryUIComponent(4L,"4. Find the top-5 Block IDs with regards to total number of actions per day for a specific date range (for types that Block ID is available)", Stream.of(BLOCK_ID, DAY, COUNT).toList()));
		queryUIComponents.put(5L,new QueryUIComponent(5L,"5. Find the referrers (if any) that have led to more than one resources.", Stream.of(REFERRER, COUNT).toList()));
		queryUIComponents.put(6L,new QueryUIComponent(6L,"6. Find the 2nd–most–common resource requested.", Stream.of(RESOURCE_REQUESTED, COUNT).toList()));
		// TODO: Add fields
		queryUIComponents.put(7L,new QueryUIComponent(7L,"7. Find the access log (all fields) where the size is less than a specified number.", List.of()));
		queryUIComponents.put(8L,new QueryUIComponent(8L,"8. Find the blocks that have been replicated the same day that they have also been served.", Stream.of(BLOCK_ID,DAY).toList()));
		queryUIComponents.put(9L,new QueryUIComponent(9L,"9. Find the blocks that have been replicated the same day and hour that they have also been served.", Stream.of(BLOCK_ID, DAY, HOUR).toList()));
		// TODO: Add fields
		queryUIComponents.put(10L,new QueryUIComponent(10L,"10. Find access logs that specified a particular version of Firefox as their browser.", List.of()));
		queryUIComponents.put(11L,new QueryUIComponent(11L,"11. Find IPs that have issued a particular HTTP method on a particular time range.", Stream.of(IP, COUNT).toList()));
		queryUIComponents.put(12L,new QueryUIComponent(12L,"12. Find IPs that have issued two particular HTTP methods on a particular time range.", Stream.of(IP, COUNT).toList()));
		queryUIComponents.put(13L,new QueryUIComponent(13L,"13. Find IPs that have issued any four distinct HTTP methods on a particular time range.", Stream.of(IP, COUNT).toList()));
	}

	@GetMapping
	String query() {
		return "logs";
	}

	@GetMapping("/{qid}")
	String query(@PathVariable Long qid, Model model) {

		List<Map<String, Object>> logs = miLogRepository.query1(LocalDateTime.of(2002,11,1,0,0),LocalDateTime.of(2022,11,1,0,0)).stream().map((QueryDTO log) -> dtoToMap(log,queryUIComponents.get(qid).columns())).toList();

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
