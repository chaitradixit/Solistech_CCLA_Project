/ *** REF_INSTRUCTION_TYPE ***/ ALTER TABLE REF_INSTRUCTION_TYPEADD SETTLEMENT_OFFSET Number(1,0) DEFAULT 0;/COMMENT ON COLUMN REF_INSTRUCTION_TYPE.SETTLEMENT_OFFSET IS 'Number of working days expected for the transaction to be settled'/UPDATE REF_INSTRUCTION_TYPE SET SETTLEMENT_OFFSET=0;UPDATE REF_INSTRUCTION_TYPE SET SETTLEMENT_OFFSET=2 WHERE INSTRUCTION_TYPE_NAME='BUYCHQ';UPDATE REF_INSTRUCTION_TYPE SET SETTLEMENT_OFFSET=2 WHERE INSTRUCTION_TYPE_NAME='BUYCHQTP';UPDATE REF_INSTRUCTION_TYPE SET SETTLEMENT_OFFSET=2 WHERE INSTRUCTION_TYPE_NAME='BUYU';UPDATE REF_INSTRUCTION_TYPE SET SETTLEMENT_OFFSET=2 WHERE INSTRUCTION_TYPE_NAME='DEPCHQ';UPDATE REF_INSTRUCTION_TYPE SET SETTLEMENT_OFFSET=2 WHERE INSTRUCTION_TYPE_NAME='DEPCHQCL';UPDATE REF_INSTRUCTION_TYPE SET SETTLEMENT_OFFSET=2 WHERE INSTRUCTION_TYPE_NAME='DEPCHQTP';UPDATE REF_INSTRUCTION_TYPE SET SETTLEMENT_OFFSET=2 WHERE INSTRUCTION_TYPE_NAME='DIODEP';  UPDATE REF_INSTRUCTION_TYPE SET SETTLEMENT_OFFSET=2 WHERE INSTRUCTION_TYPE_NAME='DIOWITH';UPDATE REF_INSTRUCTION_TYPE SET SETTLEMENT_OFFSET=2 WHERE INSTRUCTION_TYPE_NAME='ORSALEBACS';UPDATE REF_INSTRUCTION_TYPE SET SETTLEMENT_OFFSET=2 WHERE INSTRUCTION_TYPE_NAME='ORSALECHQ';UPDATE REF_INSTRUCTION_TYPE SET SETTLEMENT_OFFSET=2 WHERE INSTRUCTION_TYPE_NAME='ORWTHBACS';UPDATE REF_INSTRUCTION_TYPE SET SETTLEMENT_OFFSET=2 WHERE INSTRUCTION_TYPE_NAME='ORWTHCHQ';UPDATE REF_INSTRUCTION_TYPE SET SETTLEMENT_OFFSET=2 WHERE INSTRUCTION_TYPE_NAME='SALEBACS';UPDATE REF_INSTRUCTION_TYPE SET SETTLEMENT_OFFSET=2 WHERE INSTRUCTION_TYPE_NAME='WTHBACS';UPDATE REF_INSTRUCTION_TYPE SET SETTLEMENT_OFFSET=2 WHERE INSTRUCTION_TYPE_NAME='WTHCHQ';