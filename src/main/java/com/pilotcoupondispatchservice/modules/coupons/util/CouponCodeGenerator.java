package com.pilotcoupondispatchservice.modules.coupons.util;

import com.pilotcoupondispatchservice.exceptions.DataProcessingException;
import com.pilotcoupondispatchservice.modules.coupons.repository.CouponRepository;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Component;

import java.security.SecureRandom;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;

@Component
@AllArgsConstructor
public class CouponCodeGenerator {

    private static final DateTimeFormatter YEAR_MONTH_FORMAT = DateTimeFormatter.ofPattern("yyyyMM");
    private static final String ALPHANUMERIC = "ABCDEFGHIJKLMNOPQRSTUVWXYZ0123456789";
    private static final int RANDOM_SUFFIX_LENGTH = 6;
    private static final int MAX_ATTEMPTS = 20;

    private final CouponRepository couponRepository;
    private final SecureRandom secureRandom = new SecureRandom();

    /**
     * Format: CPN-yyyyMM- plus 6 uppercase alphanumeric characters from SecureRandom.
     * Checked against the repository and regenerated on collision instead of relying on the
     * database unique constraint alone.
     */
    public String generateCouponCode() {
        String prefix = "CPN-" + LocalDate.now().format(YEAR_MONTH_FORMAT) + "-";

        for (int attempt = 0; attempt < MAX_ATTEMPTS; attempt++) {
            String candidate = prefix + randomAlphanumeric(RANDOM_SUFFIX_LENGTH);
            if (!couponRepository.existsByCode(candidate)) {
                return candidate;
            }
        }
        throw new DataProcessingException("Failed to generate a unique coupon code, please retry");
    }

    private String randomAlphanumeric(int length) {
        StringBuilder builder = new StringBuilder(length);
        for (int i = 0; i < length; i++) {
            builder.append(ALPHANUMERIC.charAt(secureRandom.nextInt(ALPHANUMERIC.length())));
        }
        return builder.toString();
    }
}
