CREATE TABLE sfs_sykmelding
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
    pasient_alder        INT     NOT NULL,
    diagnose_hovedgruppe TEXT    NOT NULL,
    diagnose_undergruppe TEXT    NOT NULL,
    naeringsgruppe       TEXT,
    inntektskategori     TEXT,
    gradert              BOOLEAN NOT NULL,
    antall_dager         INT     NOT NULL,
    sykefravar_fom       DATE    NOT NULL,
    fom                  DATE    NOT NULL,
    tom                  DATE    NOT NULL
);

CREATE INDEX idx_sfs_sykmelding_ar ON sfs_sykmelding(ar);
CREATE INDEX idx_sfs_sykmelding_mnd ON sfs_sykmelding(mnd);
CREATE INDEX idx_sfs_sykmelding_sykmelder_fnr ON sfs_sykmelding(sykmelder_fnr);
