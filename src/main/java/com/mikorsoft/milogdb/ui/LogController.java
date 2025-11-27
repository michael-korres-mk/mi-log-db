package com.mikorsoft.milogdb.ui;

import com.mikorsoft.milogdb.domain.LogType;
import com.mikorsoft.milogdb.domain.MiLogColumn;
import com.mikorsoft.milogdb.domain.MiLogFilter;
import com.mikorsoft.milogdb.repository.MiLogRepository;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;
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

		queryUIComponents.put(1, new QueryUIComponent(1L, "1. Find the total logs per type that were created within a specified time range and sort them in a descending order.", List.of(MiLogColumn.LOG_TYPE, COUNT),MiLogFilter.timerange()));
		queryUIComponents.put(2, new QueryUIComponent(2L, "2. Find the total logs per day for a specific action type and time range.", List.of(MiLogColumn.DAY, COUNT),Stream.concat(MiLogFilter.timerange().stream(),Stream.of(MiLogFilter.LOG_TYPE)).toList()));
		queryUIComponents.put(3, new QueryUIComponent(3L, "3. Find the most common log per source IP for a specific day.", List.of(MiLogColumn.DAY, COUNT),List.of(MiLogFilter.DAY)));
		queryUIComponents.put(4, new QueryUIComponent(4L, "4. Find the top-5 Block IDs with regards to total number of actions per day for a specific date range (for types that Block ID is available)", List.of(BLOCK_ID, MiLogColumn.DAY, COUNT),MiLogFilter.timerange()));
		queryUIComponents.put(5, new QueryUIComponent(5L, "5. Find the referrers (if any) that have led to more than one resources.", List.of(REFERRER, COUNT),MiLogFilter.none()));
		queryUIComponents.put(6, new QueryUIComponent(6L, "6. Find the 2nd–most–common resource requested.", List.of(RESOURCE_REQUESTED, COUNT),MiLogFilter.none()));
		// TODO: Only access logs
		queryUIComponents.put(7, new QueryUIComponent(7L, "7. Find the access log (all fields) where the size is less than a specified number.", MiLogColumn.miLogColumns(),List.of(MiLogFilter.SIZE)));
		queryUIComponents.put(8, new QueryUIComponent(8L, "8. Find the blocks that have been replicated the same day that they have also been served.", List.of(BLOCK_ID, MiLogColumn.DAY),MiLogFilter.none()));
		queryUIComponents.put(9, new QueryUIComponent(9L, "9. Find the blocks that have been replicated the same day and hour that they have also been served.", List.of(BLOCK_ID, MiLogColumn.DAY, HOUR),MiLogFilter.none()));
		queryUIComponents.put(10, new QueryUIComponent(10L, "10. Find access logs that specified a particular version of Firefox as their browser.", MiLogColumn.miLogColumns(),MiLogFilter.none()));
		queryUIComponents.put(11, new QueryUIComponent(11L, "11. Find IPs that have issued a particular HTTP method on a particular time range.", List.of(IP, COUNT),Stream.concat(MiLogFilter.timerange().stream(),Stream.of(MiLogFilter.HTTP_METHOD)).toList()));
		queryUIComponents.put(12, new QueryUIComponent(12L, "12. Find IPs that have issued two particular HTTP methods on a particular time range.", List.of(IP, COUNT), Stream.concat(MiLogFilter.timerange().stream(),Stream.of(MiLogFilter.HTTP_METHODS)).toList()));
		queryUIComponents.put(13, new QueryUIComponent(13L, "13. Find IPs that have issued any four distinct HTTP methods on a particular time range.", List.of(IP, COUNT),Stream.concat(MiLogFilter.timerange().stream(),Stream.of(MiLogFilter.HTTP_METHODS)).toList()));
	}

	@GetMapping
	String query() {
		return "logs";
	}

	@GetMapping("/{qid}")
	String query(@PathVariable int qid,
	             @RequestParam(required = false) LocalDate from,
	             @RequestParam(required = false) LocalDate to,
	             @RequestParam(required = false) LogType logType,
	             @RequestParam(required = false) LocalDate day,
	             @RequestParam(required = false) Long size,
	             @RequestParam(required = false) String httpMethod,
	             @RequestParam(required = false) List<String> httpMethods,
	             @RequestParam Map<String, String> params,
	             Model model) {

		LocalDateTime fromT = (from != null)?from.atStartOfDay(): null;
		LocalDateTime toT = (to != null)?to.plusDays(1).atStartOfDay():null;

		List<Map<String, Object>> logs = (switch (qid) {
			case 1 -> miLogRepository.query1(fromT, toT);
			case 2 -> miLogRepository.query2((logType != null)? logType.name():null, fromT, toT);
			case 3 -> miLogRepository.query3(day);
			case 4 -> miLogRepository.query4(fromT, toT);
			case 5 -> miLogRepository.query5();
			case 6 -> miLogRepository.query6();
			case 7 -> miLogRepository.query7(size);
			case 8 -> miLogRepository.query8();
			case 9 -> miLogRepository.query9();
			case 10 -> miLogRepository.query10();
			case 11 -> miLogRepository.query11(fromT, toT, httpMethod);
			case 12 -> miLogRepository.query12(fromT, toT, (httpMethods != null)? httpMethods: new ArrayList<>());
			case 13 -> miLogRepository.query13(fromT, toT);
			default -> throw new IllegalStateException("Unexpected value: " + qid);
		}).stream().map(log -> dtoToMap(log, queryUIComponents.get(qid).columns())).toList();

		model.addAttribute("queryTitle", queryUIComponents.get(qid).title());
		model.addAttribute("filters", queryUIComponents.get(qid).filters());
		model.addAttribute("columns", queryUIComponents.get(qid).columns());
		model.addAttribute("logs", logs);
		model.addAttribute("params", params);

		return "logs";

	}
}
