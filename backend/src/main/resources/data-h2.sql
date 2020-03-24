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
VALUES (CAST('d0d95814-678b-11ea-bc55-0242ac130003' as UUID), '2020-03-11 20:20:34.884', 'Validaria', 'Validates the name',
        'Name validation', 'your name HAS to be Validaria',
        CAST('9d203228-678a-11ea-bc55-0242ac130003' as UUID));