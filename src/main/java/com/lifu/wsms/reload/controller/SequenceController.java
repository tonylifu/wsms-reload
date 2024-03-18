package com.lifu.wsms.reload.controller;

import com.lifu.wsms.reload.api.SequenceService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RequiredArgsConstructor
@RestController
@Slf4j
public class SequenceController {
    private final SequenceService sequenceService;
    public static final String SEQUENCE_PATH = "/api/v1/sequence";
    private static final String SEQUENCE_NAME = "students_sequence";

    @GetMapping(SEQUENCE_PATH)
    public ResponseEntity<Long> getNextVal() {
        return sequenceService.getNextSequenceValue(SEQUENCE_NAME)
                .map(ResponseEntity::ok)
                .orElseThrow();
    }
}
