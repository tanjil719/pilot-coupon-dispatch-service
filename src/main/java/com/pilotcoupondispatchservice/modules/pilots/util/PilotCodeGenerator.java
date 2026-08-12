package com.pilotcoupondispatchservice.modules.pilots.util;

import com.pilotcoupondispatchservice.exceptions.DataProcessingException;
import com.pilotcoupondispatchservice.modules.pilots.repository.PilotRepository;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Component;

@Component
@AllArgsConstructor
public class PilotCodeGenerator {

    private static final String PREFIX = "PLT-";
    private static final int SEQUENCE_LENGTH = 6;
    private static final int MAX_ATTEMPTS = 20;

    private final PilotRepository pilotRepository;

    /**
     * Format: PLT- plus a zero padded six digit sequence, e.g. PLT-000007. The starting sequence
     * is the current pilot count plus one, then checked against the repository and incremented on
     * collision inside a bounded retry loop.
     */
    public String generatePilotCode() {
        long sequence = pilotRepository.count() + 1;

        for (int attempt = 0; attempt < MAX_ATTEMPTS; attempt++) {
            String candidate = PREFIX + String.format("%0" + SEQUENCE_LENGTH + "d", sequence);
            if (!pilotRepository.existsByPilotCode(candidate)) {
                return candidate;
            }
            sequence++;
        }
        throw new DataProcessingException("Failed to generate a unique pilot code, please retry");
    }
}
