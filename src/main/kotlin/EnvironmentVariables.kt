package no.nav.syfo

data class EnvironmentVariables(
    val applicationPort: Int = getEnvVar("APPLICATION_PORT", "8080").toInt(),
    val applicationName: String = getEnvVar("NAIS_APP_NAME", "sykmelder-statistikk-kafka"),
    val naisClusterName: String = getEnvVar("NAIS_CLUSTER_NAME", "localhost"),
)

fun getEnvVar(varName: String, defaultValue: String? = null) =
    System.getenv(varName)
        ?: defaultValue ?: throw RuntimeException("Missing required variable \"$varName\"")
