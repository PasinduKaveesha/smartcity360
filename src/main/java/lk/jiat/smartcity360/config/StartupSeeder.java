package lk.jiat.smartcity360.config;

import jakarta.annotation.PostConstruct;
import jakarta.ejb.Singleton;
import jakarta.ejb.Startup;
import jakarta.inject.Inject;
import lk.jiat.smartcity360.persistence.dao.AppUserDao;
import lk.jiat.smartcity360.persistence.entity.AppUserEntity;
import lk.jiat.smartcity360.security.PasswordHasher;
import java.util.logging.Logger;

/**
 * Startup singleton EJB initializing default administrative and demo accounts.
 */
@Singleton
@Startup
public class StartupSeeder {

    private static final Logger LOGGER = Logger.getLogger(StartupSeeder.class.getName());

    @Inject
    private AppUserDao appUserDao;

    @PostConstruct
    public void init() {
        if (appUserDao.count() == 0) {
            LOGGER.info("Seeding default application users into app_user table...");
            seedUser("admin", "Admin@123", "ADMIN", "System Administrator");
            seedUser("staff", "Staff@123", "STAFF", "Municipal Staff Officer");
            seedUser("resident", "Resident@123", "RESIDENT", "City Resident Demo");
            LOGGER.info("Default application users seeded successfully.");
        }
    }

    private void seedUser(String username, String password, String role, String fullName) {
        String hash = PasswordHasher.hash(password);
        AppUserEntity user = new AppUserEntity(username, hash, role, fullName);
        appUserDao.save(user);
    }
}
