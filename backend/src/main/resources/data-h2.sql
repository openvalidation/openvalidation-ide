/*
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


MERGE INTO schema (schema_id)
    VALUES (CAST('1863f048-6843-4d04-a56c-64a6c8925972' as UUID));

MERGE INTO attribute (attribute_id, attribute_type, name)
    VALUES (CAST('005fb4eb-cbae-421d-a59c-7700d57955d2' as UUID), 4, 'delivery_address');
MERGE INTO attribute (attribute_id, attribute_type, name)
    VALUES (CAST('6883ffa9-d572-4d5a-95e7-67995bee95c0' as UUID), 4, 'billing_address');
MERGE INTO attribute (attribute_id, attribute_type, name, value)
    VALUES (CAST('bcc7cdd2-dcd8-42da-89e4-2ea3e95617b3' as UUID), 2, 'location', 'Dortmund');
MERGE INTO attribute (attribute_id, attribute_type, name, value)
    VALUES (CAST('3d16535a-cf17-4695-beba-4e7f4d424fa7' as UUID), 2, 'location', 'Cologne');
MERGE INTO attribute (attribute_id, attribute_type, name)
    VALUES (CAST('49e14afb-de0b-4234-a983-2984043d2367' as UUID), 3, 'applicants');
MERGE INTO attribute (attribute_id, attribute_type, name)
    VALUES (CAST('932e0e70-79a1-4701-bb68-231f6c5f0ef8' as UUID), 4, '0');
MERGE INTO attribute (attribute_id, attribute_type, name)
    VALUES (CAST('ca2cd126-7982-4233-b4f8-04865d010b68' as UUID), 4, '1');
MERGE INTO attribute (attribute_id, attribute_type, name)
    VALUES (CAST('e6bea99b-6da4-4b56-8108-59e3cacd2ef9' as UUID), 4, '2');
MERGE INTO attribute (attribute_id, attribute_type, name, value)
    VALUES (CAST('1812378e-1fde-46c3-8d26-0decc519de35' as UUID), 2, 'name', 'Peter');
MERGE INTO attribute (attribute_id, attribute_type, name, value)
    VALUES (CAST('94be056d-83b7-4454-9fa9-9597608c58d0' as UUID), 1, 'age', '17');
MERGE INTO attribute (attribute_id, attribute_type, name, value)
    VALUES (CAST('ba5c832b-e891-40c6-a1f1-75e6b4d67a9d' as UUID), 2, 'name', 'Klaus');
MERGE INTO attribute (attribute_id, attribute_type, name, value)
    VALUES (CAST('43bc5e8a-8513-4ffa-8a9a-68a0dabdcab6' as UUID), 1, 'age', '19');
MERGE INTO attribute (attribute_id, attribute_type, name, value)
    VALUES (CAST('4377298e-4d80-49b0-b7c1-44f4a863a2ad' as UUID), 2, 'name', 'Frieda');
MERGE INTO attribute (attribute_id, attribute_type, name, value)
    VALUES (CAST('74acf6d8-8c69-4c79-ac56-6c2bac6e6e7b' as UUID), 1, 'age', '38');

MERGE INTO schema_attributes (schema_schema_id, attributes_attribute_id)
    VALUES (CAST('1863f048-6843-4d04-a56c-64a6c8925972' as UUID),
            CAST('005fb4eb-cbae-421d-a59c-7700d57955d2' as UUID));
MERGE INTO schema_attributes (schema_schema_id, attributes_attribute_id)
    VALUES (CAST('1863f048-6843-4d04-a56c-64a6c8925972' as UUID),
            CAST('6883ffa9-d572-4d5a-95e7-67995bee95c0' as UUID));
MERGE INTO attribute_children (attribute_attribute_id, children_attribute_id)
    VALUES (CAST('005fb4eb-cbae-421d-a59c-7700d57955d2' as UUID),
            CAST('bcc7cdd2-dcd8-42da-89e4-2ea3e95617b3' as UUID));
MERGE INTO attribute_children (schema_schema_id, attributes_attribute_id)
    VALUES (CAST('6883ffa9-d572-4d5a-95e7-67995bee95c0' as UUID),
            CAST('3d16535a-cf17-4695-beba-4e7f4d424fa7' as UUID));
MERGE INTO schema_attributes (schema_schema_id, attributes_attribute_id)
    VALUES (CAST('1863f048-6843-4d04-a56c-64a6c8925972' as UUID),
            CAST('49e14afb-de0b-4234-a983-2984043d2367' as UUID));
MERGE INTO attribute_children (attribute_attribute_id, children_attribute_id)
    VALUES (CAST('49e14afb-de0b-4234-a983-2984043d2367' as UUID),
            CAST('932e0e70-79a1-4701-bb68-231f6c5f0ef8' as UUID));
MERGE INTO attribute_children (attribute_attribute_id, children_attribute_id)
    VALUES (CAST('49e14afb-de0b-4234-a983-2984043d2367' as UUID),
            CAST('ca2cd126-7982-4233-b4f8-04865d010b68' as UUID));
MERGE INTO attribute_children (attribute_attribute_id, children_attribute_id)
    VALUES (CAST('49e14afb-de0b-4234-a983-2984043d2367' as UUID),
            CAST('e6bea99b-6da4-4b56-8108-59e3cacd2ef9' as UUID));
MERGE INTO attribute_children (attribute_attribute_id, children_attribute_id)
    VALUES (CAST('932e0e70-79a1-4701-bb68-231f6c5f0ef8' as UUID),
            CAST('1812378e-1fde-46c3-8d26-0decc519de35' as UUID));
MERGE INTO attribute_children (attribute_attribute_id, children_attribute_id)
    VALUES (CAST('932e0e70-79a1-4701-bb68-231f6c5f0ef8' as UUID),
            CAST('94be056d-83b7-4454-9fa9-9597608c58d0' as UUID));
MERGE INTO attribute_children (attribute_attribute_id, children_attribute_id)
    VALUES (CAST('ca2cd126-7982-4233-b4f8-04865d010b68' as UUID),
            CAST('ba5c832b-e891-40c6-a1f1-75e6b4d67a9d' as UUID));
MERGE INTO attribute_children (attribute_attribute_id, children_attribute_id)
    VALUES (CAST('ca2cd126-7982-4233-b4f8-04865d010b68' as UUID),
            CAST('43bc5e8a-8513-4ffa-8a9a-68a0dabdcab6' as UUID));
MERGE INTO attribute_children (attribute_attribute_id, children_attribute_id)
    VALUES (CAST('e6bea99b-6da4-4b56-8108-59e3cacd2ef9' as UUID),
            CAST('4377298e-4d80-49b0-b7c1-44f4a863a2ad' as UUID));
MERGE INTO attribute_children (attribute_attribute_id, children_attribute_id)
    VALUES (CAST('e6bea99b-6da4-4b56-8108-59e3cacd2ef9' as UUID),
            CAST('74acf6d8-8c69-4c79-ac56-6c2bac6e6e7b' as UUID));

MERGE INTO ruleset (ruleset_Id, created_at, created_by, description, name, rules, schema_schema_id)
    VALUES (CAST('28f4e790-2647-4bdf-94b5-f24aa80587b2' as UUID), now(), 'OpenVALIDATION-IDE Team',
            'Examples for more advanced rules',
            'Advanced ruleset',
            'the delivery_address.location of the customer MUST be Dortmund\n\n' ||
            'COMMENT This is a comment\n\n' ||
            'ALL persons FROM the applicants WITH an age LESS THAN 20 AS juniors\n' ||
            'juniors.age AS age list\n\n' ||
            'SUM OF age list MUST NOT EXCEED the total age of 50\n',
            CAST('1863f048-6843-4d04-a56c-64a6c8925972' as UUID));*/
