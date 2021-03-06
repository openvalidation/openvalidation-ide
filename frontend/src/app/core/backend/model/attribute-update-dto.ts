/**
 * OpenAPI definition
 * No description provided (generated by Openapi Generator https://github.com/openapitools/openapi-generator)
 *
 * The version of the OpenAPI document: v0
 * 
 *
 * NOTE: This class is auto generated by OpenAPI Generator (https://openapi-generator.tech).
 * https://openapi-generator.tech
 * Do not edit the class manually.
 */


export interface AttributeUpdateDto { 
    name?: string;
    attributeType: AttributeUpdateDto.AttributeTypeEnum;
    value?: string;
    children?: Array<AttributeUpdateDto>;
}
export namespace AttributeUpdateDto {
    export type AttributeTypeEnum = 'BOOLEAN' | 'NUMBER' | 'TEXT' | 'LIST' | 'OBJECT';
    export const AttributeTypeEnum = {
        BOOLEAN: 'BOOLEAN' as AttributeTypeEnum,
        NUMBER: 'NUMBER' as AttributeTypeEnum,
        TEXT: 'TEXT' as AttributeTypeEnum,
        LIST: 'LIST' as AttributeTypeEnum,
        OBJECT: 'OBJECT' as AttributeTypeEnum
    };
}


