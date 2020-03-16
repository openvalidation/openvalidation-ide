INSERT INTO schema (schema_id)
VALUES ('9d203228-678a-11ea-bc55-0242ac130003');

INSERT INTO attribute (attribute_id, attribute_type, name, value)
VALUES ('3e98ccbe-678b-11ea-bc55-0242ac130003', 2, 'name', 'Satoshi');
INSERT INTO attribute (attribute_id, attribute_type, name, value)
VALUES ('97aa6d3d-8274-4093-b24a-35eeebc588e8', 1, 'age', '25');
INSERT INTO attribute (attribute_id, attribute_type, name, value)
VALUES ('30f8734b-cd81-4725-a3d6-8d9ef2755795', 2, 'location', 'Dortmund');

INSERT INTO schema_attributes (schema_schema_id, attributes_attribute_id)
VALUES ('9d203228-678a-11ea-bc55-0242ac130003',
        '3e98ccbe-678b-11ea-bc55-0242ac130003');
INSERT INTO schema_attributes (schema_schema_id, attributes_attribute_id)
VALUES ('9d203228-678a-11ea-bc55-0242ac130003',
        '97aa6d3d-8274-4093-b24a-35eeebc588e8');
INSERT INTO schema_attributes (schema_schema_id, attributes_attribute_id)
VALUES ('9d203228-678a-11ea-bc55-0242ac130003',
        '30f8734b-cd81-4725-a3d6-8d9ef2755795');

INSERT INTO ruleset (ruleset_Id, created_at, created_by, description, name, rules, schema_schema_id)
VALUES ('d0d95814-678b-11ea-bc55-0242ac130003', '2020-03-11 20:20:34.884', 'Validaria', 'Validates the name',
        'Name validation', 'your name HAS to be Validaria',
        '9d203228-678a-11ea-bc55-0242ac130003');