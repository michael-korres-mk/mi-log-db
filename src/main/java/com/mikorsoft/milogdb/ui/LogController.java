package com.mikorsoft.milogdb.ui;

import com.mikorsoft.milogdb.repository.MiLogRepository;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

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



		queryUIComponents.put(1L,new QueryUIComponent(1L,"1. Find the total logs per type that were created within a specified time range and sort them in a descending order.", List.of("Log Type", "Count")));
		queryUIComponents.put(2L,new QueryUIComponent(2L,"2. Find the total logs per day for a specific action type and time range.", List.of("Day", "Count")));
		queryUIComponents.put(3L,new QueryUIComponent(3L,"3. Find the most common log per source IP for a specific day.", List.of("IP","Day", "Count")));
		queryUIComponents.put(4L,new QueryUIComponent(4L,"4. Find the top-5 Block IDs with regards to total number of actions per day for a specific date range (for types that Block ID is available)", List.of("Block ID","Day", "Count")));
		queryUIComponents.put(5L,new QueryUIComponent(5L,"5. Find the referrers (if any) that have led to more than one resources.", List.of("Referrer", "Count")));
		queryUIComponents.put(6L,new QueryUIComponent(6L,"6. Find the 2nd–most–common resource requested.", List.of("Resource Requested", "Count")));
		// TODO: Add fields
		queryUIComponents.put(7L,new QueryUIComponent(7L,"7. Find the access log (all fields) where the size is less than a specified number.", List.of()));
		queryUIComponents.put(8L,new QueryUIComponent(8L,"8. Find the blocks that have been replicated the same day that they have also been served.", List.of("Block ID", "Day")));
		queryUIComponents.put(9L,new QueryUIComponent(9L,"9. Find the blocks that have been replicated the same day and hour that they have also been served.", List.of("Block ID", "Day","Hour")));
		// TODO: Add fields
		queryUIComponents.put(10L,new QueryUIComponent(10L,"10. Find access logs that specified a particular version of Firefox as their browser.", List.of()));
		queryUIComponents.put(11L,new QueryUIComponent(11L,"11. Find IPs that have issued a particular HTTP method on a particular time range.", List.of("IP", "Count")));
		queryUIComponents.put(12L,new QueryUIComponent(12L,"12. Find IPs that have issued two particular HTTP methods on a particular time range.", List.of("IP", "Count")));
		queryUIComponents.put(13L,new QueryUIComponent(13L,"13. Find IPs that have issued any four distinct HTTP methods on a particular time range.", List.of("IP", "Count")));
	}

	@GetMapping
	String query( Model model) {

//		List<Map<String, Object>> logs = miLogRepository.query1(from, to);

//		model.addAttribute("logs", "bla");


		return "logs";

	}

	@GetMapping("/{qid}")
	String query(@PathVariable Long qid, Model model) {



//		List<Map<String, Object>> logs = miLogRepository.query1(from, to);

		model.addAttribute("queryTitle", queryUIComponents.get(qid).title());

//		model.addAttribute("logs", "bla");


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
