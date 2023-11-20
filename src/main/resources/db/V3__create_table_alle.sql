CREATE TABLE sfs_data_alle
(
    pk                          INT primary key not null,
    aarmnd                      VARCHAR         not null,
    sykm_bydel_navn             VARCHAR         not null,
    sykm_kommune_navn           VARCHAR         not null,
    sykm_fylke_navn             VARCHAR         not null,
    sykm_hovedgruppe_kode       VARCHAR         not null,
    sykm_undergruppe_kode       VARCHAR         not null,
    sykmelder_sammenl_type_kode VARCHAR         not null,
    pasient_kjonn_kode          VARCHAR         not null,
    pasient_alder_gruppe7_besk  VARCHAR         not null,
    hovedgruppe_smp_besk        VARCHAR         not null,
    undergruppe_smp_besk        VARCHAR         not null,
    varighet_gruppe9_besk       VARCHAR         not null,
    naering_gruppe6_besk_lang   VARCHAR         not null,
    antall_sykmeldinger         INT             not null,
    gradert_flagg               INT             not null,
    antall_dager                INT             not null
);