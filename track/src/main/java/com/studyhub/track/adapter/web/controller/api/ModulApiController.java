package com.studyhub.track.adapter.web.controller.api;

import com.studyhub.jwt.JWTService;
import com.studyhub.track.adapter.db.modul.ModulDto;
import com.studyhub.track.adapter.db.modul.ModulMapper;
import com.studyhub.track.adapter.mail.KlausurReminderDto;
import com.studyhub.track.adapter.web.*;
import com.studyhub.track.application.service.ModulEventService;
import com.studyhub.track.application.service.ModulService;
import com.studyhub.track.domain.model.modul.Modul;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;
import java.util.UUID;

@RestController
@RequestMapping("/api")
public class ModulApiController {

	@Value("${maxModule}")
	private int MAX_MODULE;

	private final ModulService modulService;
	private final ModulEventService modulEventService;
	private final JWTService jwtService;

	public ModulApiController(ModulService service, ModulEventService modulEventService, JWTService jwtService) {
		this.modulService = service;
		this.modulEventService = modulEventService;
        this.jwtService = jwtService;
    }

	@Api
	@GetMapping("/modules")
	public ResponseEntity<List<Modul>> getAllModules() {
		return ResponseEntity.ok(modulService.findAll());
	}

	@AngularApi
	@PostMapping("/update")
	public ResponseEntity<Void> updateSeconds(@RequestBody ModulUpdateRequest request) {
		try {
			modulService.updateSeconds(UUID.fromString(request.fachId()), request.secondsLearned());
			modulEventService.saveEvent(request, "");
		} catch (Exception e) {
			return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
		}
		return ResponseEntity.status(HttpStatus.OK).build();
	}

	@Api
	@PostMapping("/get-seconds")
	public ResponseEntity<Integer> getSeconds(@RequestBody String fachId) {
		System.out.println("get seconds");
		return ResponseEntity.ok(modulService.getSecondsForId(UUID.fromString(fachId)));
	}

	@Api
	@GetMapping("/module-map")
	public Map<UUID, String> getModuleMap() {
		return modulService.getModuleMap();
	}

	@Api
	@GetMapping("/module-name")
	public String getModulName(String modulFachId) {
		return modulService.findModulNameByFachid(UUID.fromString(modulFachId));
	}

	@Api
	@PostMapping("/data-klausur-reminding")
	public List<KlausurReminderDto> findModuleWithoutKlausurDate(@RequestBody List<String> users) {
		return modulService.findModuleWithoutKlausurDate(users);
	}

	@AngularApi
	@PutMapping("/reset")
	public ResponseEntity<Void> reset(@RequestBody String fachId) {
		try {
			modulService.resetModulTime(UUID.fromString(fachId));
		} catch (Exception e) {
			return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
        return ResponseEntity.status(HttpStatus.OK).build();
	}

	@AngularApi
	@DeleteMapping("/delete")
	public ResponseEntity<Void> deleteModul(@RequestParam String fachId) {
		modulService.deleteModul(UUID.fromString(fachId));
		return ResponseEntity.status(HttpStatus.NO_CONTENT).build();
	}

	@AngularApi
	@GetMapping("get-active-modules")
	public ResponseEntity<List<ModulDto>> getActiveModules(HttpServletRequest request) {
		String username = jwtService.extractUsernameFromHeader(request);
		List<ModulDto> l = modulService.findActiveModuleByUsername(true, username);
		return ResponseEntity.ok(l);
	}

	@AngularApi
	@PostMapping("/new-modul")
	public ResponseEntity<Void> newModule(@RequestBody ModulForm modulForm, HttpServletRequest request) {
		//int semester = authenticationService.getSemesterOfUser(jwtService.extractUsername("token"));
		int semester = 6;
		Modul modul = modulForm.newModulFromFormData(modulForm, semester);
		/**
		 if (Optional.ofNullable(modulForm.stapelCheckbox()).orElse(false)) {
		 CreateNewStapelRequest request = new CreateNewStapelRequest(modul.getFachId().toString(), modulForm.stapelName(), modulForm.beschreibung(), modulForm.lernstufen(), jwtService.extractUsername(token));
		 request.validateForBindingResult(bindingResult);
		 if(bindingResult.hasErrors()) return "add-module";
		 stapelRequestService.sendCreateNewStapelRequest(request);
		 }
		 **/

		if (modulService.modulCanBeCreated(jwtService.extractUsernameFromHeader(request), MAX_MODULE)) {
			modulService.saveNewModul(modul);
			return ResponseEntity.status(HttpStatus.CREATED).build();
		} else {
			return ResponseEntity.status(HttpStatus.BAD_REQUEST).build();
		}
	}

	@AngularApi
	@GetMapping("/get-all-by-username")
	public ResponseEntity<List<ModulDto>> getAllByUsername(HttpServletRequest request) {
		String username = jwtService.extractUsernameFromHeader(request);
		List<ModulDto> l = modulService.findAllByUsername(username).stream().map(ModulMapper::toModulDto).toList();
		return ResponseEntity.ok(l);
	}

	@AngularApi
	@PutMapping("/change-active")
	public ResponseEntity<Void> activate(@RequestBody  String fachId) {
		modulService.changeActivity(UUID.fromString(fachId));
		return ResponseEntity.status(HttpStatus.OK).build();
	}

	@AngularApi
	@PostMapping("/add-time")
	public ResponseEntity<Void> addTime(@RequestBody AddTimeRequest req) {
		try {
			modulService.addTime(UUID.fromString(req.fachId()), req.time());
		} catch (Exception e) {
			return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
		}
		return ResponseEntity.status(HttpStatus.OK).build();
	}

	@AngularApi
	@PostMapping("/add-klausur-date")
	public ResponseEntity<Void> addKlausurDate(@RequestBody KlausurDateRequest req) {
		modulService.addKlausurDate(UUID.fromString(req.fachId()), req.date(), req.time());
		return ResponseEntity.status(HttpStatus.OK).build();
	}
}
