export * from './attributes.service';
import { AttributesService } from './attributes.service';
export * from './rulesets.service';
import { RulesetsService } from './rulesets.service';
export * from './schema.service';
import { SchemaService } from './schema.service';
export const APIS = [AttributesService, RulesetsService, SchemaService];
