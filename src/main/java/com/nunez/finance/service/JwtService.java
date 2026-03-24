package com.nunez.finance.service;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;

import javax.crypto.Mac;
import javax.crypto.spec.SecretKeySpec;
import java.nio.charset.StandardCharsets;
import java.time.Instant;
import java.util.Base64;
import java.util.Map;

@Service
public class JwtService {

    private static final Base64.Encoder URL_ENCODER = Base64.getUrlEncoder().withoutPadding();
    private static final Base64.Decoder URL_DECODER = Base64.getUrlDecoder();

    private final byte[] secret;
    private final long expirationMs;

    public JwtService(
            @Value("${jwt.secret}") String secret,
            @Value("${jwt.expiration-ms:3600000}") long expirationMs
    ) {
        this.secret = secret.getBytes(StandardCharsets.UTF_8);
        this.expirationMs = expirationMs;
    }

    public String generateToken(UserDetails userDetails) {
        try {
            final String headerJson = "{\"alg\":\"HS256\",\"typ\":\"JWT\"}";
            final long exp = Instant.now().toEpochMilli() + this.expirationMs;
            final String payloadJson = "{\"sub\":\"" + escapeJson(userDetails.getUsername()) + "\",\"exp\":" + exp + "}";

            final String header = URL_ENCODER.encodeToString(headerJson.getBytes(StandardCharsets.UTF_8));
            final String payload = URL_ENCODER.encodeToString(payloadJson.getBytes(StandardCharsets.UTF_8));
            final String unsignedToken = header + "." + payload;
            final String signature = URL_ENCODER.encodeToString(this.hmacSha256(unsignedToken));

            return unsignedToken + "." + signature;
        } catch (Exception ex) {
            throw new IllegalStateException("No se pudo generar el token", ex);
        }
    }

    public String extractUsername(String token) {
        final Map<String, String> payload = this.readPayload(token);
        final String sub = payload.get("sub");
        if (sub == null || sub.isBlank()) {
            throw new IllegalArgumentException("Token sin subject");
        }
        return sub;
    }

    public boolean isTokenValid(String token, UserDetails userDetails) {
        try {
            final String username = this.extractUsername(token);
            return username.equals(userDetails.getUsername()) && !this.isTokenExpired(token) && this.hasValidSignature(token);
        } catch (Exception ex) {
            return false;
        }
    }

    private boolean isTokenExpired(String token) {
        final Map<String, String> payload = this.readPayload(token);
        final String expValue = payload.get("exp");
        if (expValue == null) {
            return true;
        }
        try {
            return Long.parseLong(expValue) < Instant.now().toEpochMilli();
        } catch (NumberFormatException ex) {
            return true;
        }
    }

    private Map<String, String> readPayload(String token) {
        final String[] parts = token.split("\\.");
        if (parts.length != 3) {
            throw new IllegalArgumentException("Formato de token invalido");
        }
        final String payloadJson = new String(URL_DECODER.decode(parts[1]), StandardCharsets.UTF_8);
        return Map.of(
                "sub", extractStringField(payloadJson, "sub"),
                "exp", extractNumericField(payloadJson, "exp")
        );
    }

    private String extractStringField(String json, String fieldName) {
        final String key = "\"" + fieldName + "\":\"";
        final int start = json.indexOf(key);
        if (start < 0) {
            return null;
        }
        final int valueStart = start + key.length();
        final int valueEnd = json.indexOf('"', valueStart);
        if (valueEnd < 0) {
            return null;
        }
        return json.substring(valueStart, valueEnd).replace("\\\"", "\"").replace("\\\\", "\\");
    }

    private String extractNumericField(String json, String fieldName) {
        final String key = "\"" + fieldName + "\":";
        final int start = json.indexOf(key);
        if (start < 0) {
            return null;
        }
        int index = start + key.length();
        while (index < json.length() && Character.isWhitespace(json.charAt(index))) {
            index++;
        }
        final StringBuilder value = new StringBuilder();
        while (index < json.length() && Character.isDigit(json.charAt(index))) {
            value.append(json.charAt(index));
            index++;
        }
        return value.isEmpty() ? null : value.toString();
    }

    private boolean hasValidSignature(String token) {
        try {
            final String[] parts = token.split("\\.");
            if (parts.length != 3) {
                return false;
            }
            final String unsignedToken = parts[0] + "." + parts[1];
            final byte[] expectedSignature = this.hmacSha256(unsignedToken);
            final byte[] receivedSignature = URL_DECODER.decode(parts[2]);
            return java.security.MessageDigest.isEqual(expectedSignature, receivedSignature);
        } catch (Exception ex) {
            return false;
        }
    }

    private byte[] hmacSha256(String data) throws Exception {
        final Mac mac = Mac.getInstance("HmacSHA256");
        mac.init(new SecretKeySpec(this.secret, "HmacSHA256"));
        return mac.doFinal(data.getBytes(StandardCharsets.UTF_8));
    }

    private String escapeJson(String value) {
        return value.replace("\\", "\\\\").replace("\"", "\\\"");
    }
}
