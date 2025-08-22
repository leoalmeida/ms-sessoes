CREATE TABLE TOTALIZADORES(
    id BIGINT(20) NOT NULL auto_increment,
    opcao VARCHAR(255) NOT NULL,
    quantidade BIGINT(20) NOT NULL,
    sessao_id BIGINT(20) NOT NULL,
    PRIMARY KEY (id),
    FOREIGN KEY (sessao_id) REFERENCES sessoes(id)
);