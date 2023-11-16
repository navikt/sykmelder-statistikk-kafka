package no.nav.sykmelderstatistikk.database

import java.sql.DriverManager
import org.testcontainers.containers.PostgreSQLContainer
import org.testcontainers.containers.wait.strategy.Wait

private class PostgreSQLContainer14 : PostgreSQLContainer<PostgreSQLContainer14>("postgres:14")

class TestDb {
    companion object {
        private val postgreSQLContainer: PostgreSQLContainer14 =
            PostgreSQLContainer14()
                .withDatabaseName("statistikk")
                .withUsername("username")
                .withPassword("password")
                .waitingFor(Wait.defaultWaitStrategy())

        val jdbcUrl: String

        init {
            postgreSQLContainer.start()
            jdbcUrl = postgreSQLContainer.jdbcUrl
            DriverManager.getConnection(jdbcUrl, "username", "password")
                .createStatement()
                .execute("""CREATE USER "sykmelder-statistikk-next" """)
        }
    }
}
