package no.nav.sykmelderstatistikk.database

import io.mockk.every
import io.mockk.mockk
import no.nav.sykmelderstatistikk.models.application.EnvironmentVariables
import org.amshove.kluent.shouldNotBe
import org.junit.jupiter.api.Test

class ExposedDatabaseTest {

    @Test
    fun `Test run flyway`() {
        val environmentVariable =
            mockk<EnvironmentVariables>().apply {
                every { dbUsername } returns "username"
                every { dbPassword } returns "password"
                every { jdbcUrl() } returns TestDb.jdbcUrl
            }
        val exposedDatabase = ExposedDatabase(environmentVariable)
        exposedDatabase shouldNotBe null
    }
}
