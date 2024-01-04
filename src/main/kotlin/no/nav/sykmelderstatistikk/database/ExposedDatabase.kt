package no.nav.sykmelderstatistikk.database

import no.nav.sykmelderstatistikk.config.EnvironmentVariables
import org.flywaydb.core.Flyway
import org.jetbrains.exposed.sql.Database

class ExposedDatabase(
    private val env: EnvironmentVariables,
) {
    init {
        Flyway.configure()
            .dataSource(env.jdbcUrl(), env.dbUsername, env.dbPassword)
            .validateMigrationNaming(true)
            .load()
            .migrate()

        Database.connect(
            env.jdbcUrl(),
            driver = "org.postgresql.Driver",
            user = env.dbUsername,
            password = env.dbPassword,
        )
    }
}
