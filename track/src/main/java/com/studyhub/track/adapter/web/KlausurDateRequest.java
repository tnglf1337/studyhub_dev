package com.studyhub.track.adapter.web;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.format.annotation.DateTimeFormat;

import java.time.LocalDate;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class KlausurDateRequest {
	private String fachId;
	private LocalDate date;
	private String time;
}
