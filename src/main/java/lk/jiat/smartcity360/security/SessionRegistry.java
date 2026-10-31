package lk.jiat.smartcity360.security;

import jakarta.enterprise.context.ApplicationScoped;
import java.time.Instant;
import java.util.Map;
import java.util.Optional;
import java.util.UUID;
import java.util.concurrent.ConcurrentHashMap;

/**
 * Thread-safe in-memory session registry holding active bearer tokens with idle timeout.
 */
@ApplicationScoped
public class SessionRegistry {

    public static final long IDLE_TIMEOUT_SECONDS = 30 * 60; // 30 minutes

    public static class UserSession {
        private final Long userId;
        private final String username;
        private final String role;
        private final String fullName;
        private volatile Instant lastAccess;

        public UserSession(Long userId, String username, String role, String fullName) {
            this.userId = userId;
            this.username = username;
            this.role = role;
            this.fullName = fullName;
            this.lastAccess = Instant.now();
        }

        public Long getUserId() {
            return userId;
        }

        public String getUsername() {
            return username;
        }

        public String getRole() {
            return role;
        }

        public String getFullName() {
            return fullName;
        }

        public Instant getLastAccess() {
            return lastAccess;
        }

        public void touch() {
            this.lastAccess = Instant.now();
        }

        public boolean isExpired() {
            return Instant.now().isAfter(lastAccess.plusSeconds(IDLE_TIMEOUT_SECONDS));
        }
    }

    private final Map<String, UserSession> sessions = new ConcurrentHashMap<>();

    public String createSession(Long userId, String username, String role, String fullName) {
        String token = UUID.randomUUID().toString();
        sessions.put(token, new UserSession(userId, username, role, fullName));
        return token;
    }

    public Optional<UserSession> getSession(String token) {
        if (token == null) {
            return Optional.empty();
        }
        UserSession session = sessions.get(token);
        if (session == null) {
            return Optional.empty();
        }
        if (session.isExpired()) {
            sessions.remove(token);
            return Optional.empty();
        }
        session.touch();
        return Optional.of(session);
    }

    public void removeSession(String token) {
        if (token != null) {
            sessions.remove(token);
        }
    }
}
