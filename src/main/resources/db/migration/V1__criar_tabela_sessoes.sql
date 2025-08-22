CREATE TABLE SESSOES(
    id BIGINT(20) NOT NULL auto_increment,
    data_inicio DATETIME NULL,
    data_fim DATETIME NULL,
    resultado VARCHAR(255) NULL,
    status VARCHAR(255) NOT NULL,
    pauta_id BIGINT(20) NOT NULL,
    PRIMARY KEY (id)
);
