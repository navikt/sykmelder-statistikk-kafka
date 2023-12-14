CREATE TABLE sykmelding_varighet
(
    pk                   INT PRIMARY KEY,
    ar                   INT     NOT NULL,
    mnd                  INT     NOT NULL,
    bydel                TEXT,
    kommune              TEXT    NOT NULL,
    fylke                TEXT    NOT NULL,
    sykmelder_fnr        TEXT    NOT NULL,
    hovedgruppe_kode     TEXT    NOT NULL,
    undergruppe_kode     TEXT    NOT NULL,
    samensatt_kode       TEXT    NOT NULL,
    pasient_kjonn        TEXT    NOT NULL,
    pasient_aldersgruppe TEXT    NOT NULL,
    diagnose_hovedgruppe TEXT    NOT NULL,
    diagnose_undergruppe TEXT    NOT NULL,
    varighetsgruppe      TEXT    NOT NULL,
    naeringsgruppe       TEXT,
    antall_sykmeldinger  INT     NOT NULL,
    gradert              BOOLEAN NOT NULL,
    antall_dager         INT     NOT NULL
);

CREATE INDEX idx_sykmelding_varighet_ar ON sykmelding_varighet(ar);
CREATE INDEX idx_sykmelding_varighet_mnd ON sykmelding_varighet(mnd);
CREATE INDEX idx_sykmelding_varighet_sykmelder_fnr ON sykmelding_varighet(sykmelder_fnr);
