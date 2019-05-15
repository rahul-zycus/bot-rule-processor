package com.barclays.card.bot.controller;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.barclays.card.bot.bo.RuleResponse;
import com.barclays.card.bot.service.FileService;

@RestController
@RequestMapping("/file")
public class FileController {

	private FileService fileService;

	public FileController(FileService fileService) {
		super();
		this.fileService = fileService;
	}

	@PostMapping("/exists")
	public ResponseEntity<RuleResponse> validateFilePresence() {

		return ResponseEntity.accepted().body(fileService.fireFileRule());
	}
}
