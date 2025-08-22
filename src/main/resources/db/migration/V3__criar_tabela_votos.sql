CREATE TABLE VOTOS(
    id BIGINT(20) NOT NULL auto_increment,
    data_voto DATETIME NOT NULL,
    opcao VARCHAR(255) NOT NULL,
    associado_id BIGINT(20) NOT NULL,
    sessao_id BIGINT(20) NOT NULL,
    PRIMARY KEY (id),
    FOREIGN KEY (sessao_id) REFERENCES sessoes(id)
);