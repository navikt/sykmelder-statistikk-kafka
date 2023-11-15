package no.nav.sykmelderstatistikk.models.application

data class ApplicationState(
    var alive: Boolean = true,
    var ready: Boolean = true,
)
