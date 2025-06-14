package com.studyhub.kartei.adapter.web.controller.api;

import com.studyhub.kartei.service.application.JWTService;
import com.studyhub.kartei.adapter.web.controller.request.dto.CreateNewStapelRequest;
import com.studyhub.kartei.service.application.StapelDashboardDataResponse;
import com.studyhub.kartei.domain.model.Stapel;
import com.studyhub.kartei.service.application.StapelService;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/kartei/v1")
public class StapelApiController {

	private final StapelService stapelService;
	private final JWTService jwtService;

	public StapelApiController(StapelService stapelService, JWTService jwtService) {
		this.stapelService = stapelService;
		this.jwtService = jwtService;
	}

	@PostMapping("/create-stapel")
	public ResponseEntity<Void> createNewStapel(@RequestBody CreateNewStapelRequest newStapelRequest, HttpServletRequest req) {
		String username = jwtService.extractUsernameFromHeader(req);
		try {
			stapelService.saveSet(newStapelRequest.toNewStapel(username));
		} catch (Exception e) {
			return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
		}
		return ResponseEntity.status(HttpStatus.CREATED).build();
	}

	@GetMapping("/get-faelligeKarten")
	public Map<String, Integer> getFaelligeKarten() {
		Map<String, Integer> responseMap = stapelService.getAnzahlFaelligeKartenForEachStapel(LocalDateTime.now());
		return responseMap;
	}

	@GetMapping("/sets-available")
	public ResponseEntity<Boolean> setsAvailable(HttpServletRequest request) {
		String username = jwtService.extractUsernameFromHeader(request);
		return ResponseEntity.ok(stapelService.areKarteiSetsAvailableByUsername(username));
	}

	@GetMapping("/get-all-stapel-by-username")
	public ResponseEntity<List<StapelDashboardDataResponse>> getSetsByUsername(HttpServletRequest request) {
		String username = jwtService.extractUsernameFromHeader(request);
		System.out.println(username);
		List<Stapel> stapel = stapelService.findByUsername(username);
		List<StapelDashboardDataResponse> res = stapelService.prepareDashboardInfo(stapel);
		return ResponseEntity.ok(res);
	}

	@PostMapping("/get-stapel-with-faellige-karteikarten-by-fachid")
	public ResponseEntity<Stapel> getStapelByFachid(@RequestBody String fachId) {
		return ResponseEntity.ok(stapelService.findByFachIdWithFaelligeKarten(fachId, LocalDateTime.now()));
	}

	@PostMapping("/get-stapel-with-all-karteikarten-by-fachid")
	public ResponseEntity<Stapel> getStapelWithAllKarteikartenByFachid(@RequestBody String fachId) {
		return ResponseEntity.ok(stapelService.findByFachId(fachId));
	}
}