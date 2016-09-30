-- Add new column 'Return Deadline Date'. Indicates when the form instance
-- must be returned by. Can be null.
ALTER TABLE FORM ADD (
  RETURN_DEADLINE_DATE TIMESTAMP(6)
);

-- Add new column 'Calculation Date'. Indicates when the data on the form was
-- calculated in relation to i.e. an RPI surplus draw-down form will be set to
-- the last day of a quarter. Used as a grouping mechanic for form processing
-- and identifying out-dated/superseded forms.
ALTER TABLE FORM ADD (
  CALCULATION_DATE TIMESTAMP(6)
);

COMMENT ON COLUMN FORM.RETURN_DEADLINE_DATE IS 'Indicates when the generated form instance must be returned by. Can be null. If a form is returned AFTER the specified date it is considered invalid';
COMMENT ON COLUMN FORM.CALCULATION_DATE IS 'Indicates when the data on the form was calculated in relation to i.e. an RPI surplus draw-down form will be set to the last day of a quarter. Used as a grouping mechanic for form processing and identifying out-dated/superseded forms.';


