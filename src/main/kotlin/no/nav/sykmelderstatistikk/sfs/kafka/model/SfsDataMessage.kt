package no.nav.sykmelderstatistikk.sfs.kafka.model

sealed class DataType

data class Metadata(val type: String)

data class SfsDataMessage<T : DataType>(
    val data: T,
    val metadata: Metadata,
)
