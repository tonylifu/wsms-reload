package com.lifu.wsms.reload.service;

import com.lifu.wsms.reload.api.SequenceService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
@ActiveProfiles("test")
class SequenceServiceGeneratorTest {
    @Autowired
    private SequenceService sequenceService;

    @Test
    void getNextSequenceValue() {
        var sequenceName = "students_sequence";
        var sequence = sequenceService.getNextSequenceValue(sequenceName);
        assertTrue(sequence.isPresent());
        var sequenceValue = sequence.get();
        assertTrue(sequenceValue.getNextVal() > 0);
        assertTrue(sequenceValue.getCurrentVal() >= 0);
    }
}