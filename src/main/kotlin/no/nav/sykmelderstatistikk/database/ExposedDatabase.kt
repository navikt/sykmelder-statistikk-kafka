package no.nav.sykmelderstatistikk.database

import no.nav.sykmelderstatistikk.models.application.EnvironmentVariables
import org.flywaydb.core.Flyway
import org.jetbrains.exposed.sql.Column
import org.jetbrains.exposed.sql.Database
import org.jetbrains.exposed.sql.Table
import org.jetbrains.exposed.sql.selectAll
import org.jetbrains.exposed.sql.transactions.transaction
import org.slf4j.Logger
import org.slf4j.LoggerFactory

object TestTable : Table(name = "test_table") {
    val id: Column<String> = text("id")
    val text: Column<String> = text("text")

    override val primaryKey = PrimaryKey(id)
}

class ExposedDatabase(
    private val env: EnvironmentVariables,
) {
    companion object {
        private val log: Logger = LoggerFactory.getLogger(ExposedDatabase::class.java)
    }

    init {
        Database.connect(
            env.jdbcUrl(),
            driver = "org.postgresql.Driver",
            user = env.dbUsername,
            password = env.dbPassword,
        )

        Flyway.configure().run {
            locations("db")
                .dataSource(env.jdbcUrl(), env.dbUsername, env.dbPassword)
                .load()
                .migrate()
        }
    }
}


fun selectAllTestStuff() = transaction {
    TestTable.selectAll().toList()
}
