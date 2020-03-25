MERGE INTO schema (schema_id)
    VALUES (CAST('9d203228-678a-11ea-bc55-0242ac130003' as UUID));

MERGE INTO attribute (attribute_id, attribute_type, name, value)
    VALUES (CAST('3e98ccbe-678b-11ea-bc55-0242ac130003' as UUID), 2, 'name', 'Satoshi');
MERGE INTO attribute (attribute_id, attribute_type, name, value)
    VALUES (CAST('97aa6d3d-8274-4093-b24a-35eeebc588e8' as UUID), 1, 'age', '25');
MERGE INTO attribute (attribute_id, attribute_type, name, value)
    VALUES (CAST('30f8734b-cd81-4725-a3d6-8d9ef2755795' as UUID), 2, 'location', 'Dortmund');

MERGE INTO schema_attributes (schema_schema_id, attributes_attribute_id)
    VALUES (CAST('9d203228-678a-11ea-bc55-0242ac130003' as UUID),
            CAST('3e98ccbe-678b-11ea-bc55-0242ac130003' as UUID));
MERGE INTO schema_attributes (schema_schema_id, attributes_attribute_id)
    VALUES (CAST('9d203228-678a-11ea-bc55-0242ac130003' as UUID),
            CAST('97aa6d3d-8274-4093-b24a-35eeebc588e8' as UUID));
MERGE INTO schema_attributes (schema_schema_id, attributes_attribute_id)
    VALUES (CAST('9d203228-678a-11ea-bc55-0242ac130003' as UUID),
            CAST('30f8734b-cd81-4725-a3d6-8d9ef2755795' as UUID));

MERGE INTO ruleset (ruleset_Id, created_at, created_by, description, name, rules, schema_schema_id)
    VALUES (CAST('d0d95814-678b-11ea-bc55-0242ac130003' as UUID), now(), 'Validaria', 'Validates the name',
            'Name validation', 'your name HAS to be Validaria',
            CAST('9d203228-678a-11ea-bc55-0242ac130003' as UUID));


MERGE INTO schema (schema_id)
    VALUES (CAST('c4dc0011-f794-4265-8bd5-1e0de59ae19c' as UUID));

MERGE INTO attribute (attribute_id, attribute_type, name, value)
    VALUES (CAST('84d170f6-17ec-45ad-9af3-11e07666a216' as UUID), 2, 'name', 'Sherlock Holmes');
MERGE INTO attribute (attribute_id, attribute_type, name, value)
    VALUES (CAST('766c4b27-7ed6-4677-8afd-b1742f0643db' as UUID), 1, 'age', '60');
MERGE INTO attribute (attribute_id, attribute_type, name, value)
    VALUES (CAST('fa9998ed-4ac1-4cfe-939a-7b87021e2a51' as UUID), 2, 'residence', 'Baker Street 221b');
MERGE INTO attribute (attribute_id, attribute_type, name, value)
    VALUES (CAST('182db919-8b67-4f76-a89d-6f66f7394f1f' as UUID), 0, 'signed', 'false');

MERGE INTO schema_attributes (schema_schema_id, attributes_attribute_id)
    VALUES (CAST('c4dc0011-f794-4265-8bd5-1e0de59ae19c' as UUID),
            CAST('84d170f6-17ec-45ad-9af3-11e07666a216' as UUID));
MERGE INTO schema_attributes (schema_schema_id, attributes_attribute_id)
    VALUES (CAST('c4dc0011-f794-4265-8bd5-1e0de59ae19c' as UUID),
            CAST('766c4b27-7ed6-4677-8afd-b1742f0643db' as UUID));
MERGE INTO schema_attributes (schema_schema_id, attributes_attribute_id)
    VALUES (CAST('c4dc0011-f794-4265-8bd5-1e0de59ae19c' as UUID),
            CAST('fa9998ed-4ac1-4cfe-939a-7b87021e2a51' as UUID));
MERGE INTO schema_attributes (schema_schema_id, attributes_attribute_id)
    VALUES (CAST('c4dc0011-f794-4265-8bd5-1e0de59ae19c' as UUID),
            CAST('182db919-8b67-4f76-a89d-6f66f7394f1f' as UUID));

MERGE INTO ruleset (ruleset_Id, created_at, created_by, description, name, rules, schema_schema_id)
    VALUES (CAST('5f19d628-2e90-4830-85ea-07cf29d3ebc5' as UUID), now(), 'OpenVALIDATION-IDE Team',
            'Examples for basic rules',
            'Basic ruleset',
            'IF the age of the applicant is LESS than 18\n' ||
            'THEN you must be at least 18 years old\n\n' ||
            'the contract MUST be signed\n\n' ||
            'IF  the age of the applicant is GREATER 18\n' ||
            '  AND  the name of the applicant IS Mycroft Holmes.\n' ||
            '  OR his name IS Sherlock Holmes.\n' ||
            'THEN  the applicant is a genius\n\n\n' ||
            '  the age of the applicant is LESS than 18 years old\n' ||
            'AS underage\n\n' ||
            '  Age - 18\n' ||
            'AS professional experience\n\n' ||
            'IF the applicant is underage\n' ||
            '  AND his residence IS NOT Baker Street 221b\n' ||
            'THEN You must be at least 18 years old and come from Baker Street 221b.\n' ||
            'IF the applicant is underage\n' ||
            '  OR his professional experience is LESS than 5 years\n' ||
            'THEN You must be at least 18 years old and have work experience\n' ||
            '  of at least 5 years\n',
            CAST('c4dc0011-f794-4265-8bd5-1e0de59ae19c' as UUID));