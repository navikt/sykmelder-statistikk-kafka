package no.nav.sykmelderstatistikk.database

import no.nav.sykmelderstatistikk.config.EnvironmentVariables
import org.flywaydb.core.Flyway
import org.jetbrains.exposed.sql.Column
import org.jetbrains.exposed.sql.Database
import org.jetbrains.exposed.sql.Table

object TestTable : Table(name = "test_table") {
    val id: Column<String> = text("id")
    val text: Column<String> = text("text")

    override val primaryKey = PrimaryKey(id)
}

class ExposedDatabase(
    private val env: EnvironmentVariables,
) {
    init {
        Flyway.configure()
            .dataSource(env.jdbcUrl(), env.dbUsername, env.dbPassword)
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
