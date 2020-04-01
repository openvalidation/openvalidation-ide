INSERT INTO schema (schema_id)
VALUES ('9d203228-678a-11ea-bc55-0242ac130003')
ON CONFLICT (schema_id) DO NOTHING;

INSERT INTO attribute (attribute_id, attribute_type, name, value)
VALUES ('3e98ccbe-678b-11ea-bc55-0242ac130003', 2, 'name', 'Satoshi')
ON CONFLICT (attribute_id) DO NOTHING;
INSERT INTO attribute (attribute_id, attribute_type, name, value)
VALUES ('97aa6d3d-8274-4093-b24a-35eeebc588e8', 1, 'age', '25')
ON CONFLICT (attribute_id) DO NOTHING;
INSERT INTO attribute (attribute_id, attribute_type, name, value)
VALUES ('30f8734b-cd81-4725-a3d6-8d9ef2755795', 2, 'location', 'Dortmund')
ON CONFLICT (attribute_id) DO NOTHING;

INSERT INTO schema_attributes (schema_schema_id, attributes_attribute_id)
VALUES ('9d203228-678a-11ea-bc55-0242ac130003', '3e98ccbe-678b-11ea-bc55-0242ac130003')
ON CONFLICT (schema_schema_id, attributes_attribute_id) DO NOTHING;
INSERT INTO schema_attributes (schema_schema_id, attributes_attribute_id)
VALUES ('9d203228-678a-11ea-bc55-0242ac130003', '97aa6d3d-8274-4093-b24a-35eeebc588e8')
ON CONFLICT (schema_schema_id, attributes_attribute_id) DO NOTHING;
INSERT INTO schema_attributes (schema_schema_id, attributes_attribute_id)
VALUES ('9d203228-678a-11ea-bc55-0242ac130003', '30f8734b-cd81-4725-a3d6-8d9ef2755795')
ON CONFLICT (schema_schema_id, attributes_attribute_id) DO NOTHING;

INSERT INTO ruleset (ruleset_Id, created_at, created_by, description, name, rules, schema_schema_id)
VALUES ('d0d95814-678b-11ea-bc55-0242ac130003', '2020-03-11 20:20:34.884', 'Validaria', 'Validates the name',
        'Name validation', 'your name HAS to be Validaria', '9d203228-678a-11ea-bc55-0242ac130003')
ON CONFLICT (ruleset_Id) DO NOTHING;


INSERT INTO schema (schema_id)
VALUES ('c4dc0011-f794-4265-8bd5-1e0de59ae19c')
ON CONFLICT (schema_id) DO NOTHING;

INSERT INTO attribute (attribute_id, attribute_type, name, value)
VALUES ('84d170f6-17ec-45ad-9af3-11e07666a216', 2, 'name', 'Sherlock Holmes')
ON CONFLICT (attribute_id) DO NOTHING;
INSERT INTO attribute (attribute_id, attribute_type, name, value)
VALUES ('766c4b27-7ed6-4677-8afd-b1742f0643db', 1, 'age', '60')
ON CONFLICT (attribute_id) DO NOTHING;
INSERT INTO attribute (attribute_id, attribute_type, name, value)
VALUES ('fa9998ed-4ac1-4cfe-939a-7b87021e2a51', 2, 'residence', 'Baker Street 221b')
ON CONFLICT (attribute_id) DO NOTHING;
INSERT INTO attribute (attribute_id, attribute_type, name, value)
VALUES ('182db919-8b67-4f76-a89d-6f66f7394f1f', 0, 'signed', 'false')
ON CONFLICT (attribute_id) DO NOTHING;

INSERT INTO schema_attributes (schema_schema_id, attributes_attribute_id)
VALUES ('c4dc0011-f794-4265-8bd5-1e0de59ae19c',
        '84d170f6-17ec-45ad-9af3-11e07666a216')
ON CONFLICT (schema_schema_id, attributes_attribute_id) DO NOTHING;
INSERT INTO schema_attributes (schema_schema_id, attributes_attribute_id)
VALUES ('c4dc0011-f794-4265-8bd5-1e0de59ae19c',
        '766c4b27-7ed6-4677-8afd-b1742f0643db')
ON CONFLICT (schema_schema_id, attributes_attribute_id) DO NOTHING;
INSERT INTO schema_attributes (schema_schema_id, attributes_attribute_id)
VALUES ('c4dc0011-f794-4265-8bd5-1e0de59ae19c',
        'fa9998ed-4ac1-4cfe-939a-7b87021e2a51')
ON CONFLICT (schema_schema_id, attributes_attribute_id) DO NOTHING;
INSERT INTO schema_attributes (schema_schema_id, attributes_attribute_id)
VALUES ('c4dc0011-f794-4265-8bd5-1e0de59ae19c',
        '182db919-8b67-4f76-a89d-6f66f7394f1f')
ON CONFLICT (schema_schema_id, attributes_attribute_id) DO NOTHING;

INSERT INTO ruleset (ruleset_Id, created_at, created_by, description, name, rules, schema_schema_id)
VALUES ('5f19d628-2e90-4830-85ea-07cf29d3ebc5', now(), 'OpenVALIDATION-IDE Team',
        'Examples for basic rules',
        'Basic ruleset',
'IF the age of the applicant is LESS than 18
THEN you must be at least 18 years old

the contract MUST be signed

IF the age of the applicant is GREATER 18
  AND the name of the applicant IS Mycroft Holmes
  OR his name IS Sherlock Holmes
THEN the applicant is a genius

the age of the applicant is LESS than 18 years old AS underage

IF the applicant is underage
  AND his residence IS NOT Baker Street 221b
THEN You must be at least 18 years old and come from Baker Street 221b

Age - 18 AS professional experience

IF the applicant is underage
  OR his professional experience is LESS than 5 years
THEN You must be at least 18 years old and have work experience of at least 5 years',
        'c4dc0011-f794-4265-8bd5-1e0de59ae19c')
ON CONFLICT (ruleset_Id) DO NOTHING;