CREATE OR REPLACE FUNCTION normalize_text(text)
RETURNS text AS $$
BEGIN
RETURN unaccent(lower($1));
END;
$$ LANGUAGE plpgsql IMMUTABLE;