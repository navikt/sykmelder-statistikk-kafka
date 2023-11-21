package no.nav.sykmelderstatistikk.routes.model

data class ApplicationState(
    var alive: Boolean = true,
    var ready: Boolean = true,
)
