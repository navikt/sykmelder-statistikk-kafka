CREATE TABLE sfs_data_test
(
    pk                          INT primary key not null,
    aarmnd                      VARCHAR         not null,
    sykm_hovedgruppe_kode       VARCHAR         not null,
    sykm_undergruppe_kode       VARCHAR         not null,
    sykmelder_sammenl_type_kode VARCHAR         not null,
    kjonn_kode                  VARCHAR         not null,
    alder                       VARCHAR         not null,
    alder_gruppe5_besk          VARCHAR         not null,
    bydel_nr                    VARCHAR         not null,
    kommune_nr                  VARCHAR         not null,
    fylke_nr                    VARCHAR         not null,
    alder_yrkesaktiv_flagg      INT             not null,
    naering_inntekt_kategori    VARCHAR         not null,
    ikke_arbeidstaker_flagg     INT             not null,
    rangering                   INT             not null,
    pasient_antall              INT             not null,
    arbeid_antall               INT             not null
);
