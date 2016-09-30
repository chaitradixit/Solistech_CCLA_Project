--- MUST BE RUN IN ORDER BELOW ---

delete from instruction_data_applied ida where ida.applicable_instruction_data_id in (
select aid.applicable_instruction_data_id from ref_instruction_type rit
inner join applicable_instruction_data aid on (rit.instruction_type_id = aid.instruction_type_id)
inner join ref_instruction_data rid on (rid.instruction_data_id = aid.instruction_data_id)
where rid.instruction_data_name = 'SOURCE_BANK_ACCOUNT_ID' and rit.instruction_type_name like '%CHQ%')

delete from applicable_instruction_data  where applicable_instruction_data_id in (
select aid.applicable_instruction_data_id from ref_instruction_type rit
inner join applicable_instruction_data aid on (rit.instruction_type_id = aid.instruction_type_id)
inner join ref_instruction_data rid on (rid.instruction_data_id = aid.instruction_data_id)
where rid.instruction_data_name = 'SOURCE_BANK_ACCOUNT_ID' and rit.instruction_type_name like '%CHQ%')