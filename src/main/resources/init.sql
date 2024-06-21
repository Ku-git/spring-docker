CREATE TABLE license (
     license_id varchar(36) NOT NULL,
     description varchar(50) DEFAULT NULL,
     organization_id varchar(36) DEFAULT NULL,
     product_name varchar(20) DEFAULT NULL,
     license_type varchar(20) DEFAULT NULL,
     PRIMARY KEY (license_id)
);

INSERT INTO license
(license_id, description, organization_id, product_name, license_type)
VALUES('055e972f-e568-47ef-b4aa-ba6d04806ebf', 'add license', 'd898a142-de44-466c-8c88-9ceb2c2429d3', 'license', 'full');
INSERT INTO license
(license_id, description, organization_id, product_name, license_type)
VALUES('4210130b-2141-46f8-b108-5d3313277f47', 'add license', 'd898a142-de44-466c-8c88-9ceb2c2429d3', 'license', 'full');
INSERT INTO license
(license_id, description, organization_id, product_name, license_type)
VALUES('5ab4387d-e7dd-43e8-bf22-e0b873343ee0', 'add license', 'e839ee96-28de-4f67-bb79-870ca89743a0', 'license', 'full');
INSERT INTO license
(license_id, description, organization_id, product_name, license_type)
VALUES('b3573aee-453d-46a3-b4e8-4853f3b172aa', 'add license', 'e839ee96-28de-4f67-bb79-870ca89743a0', 'license', 'full');
INSERT INTO license
(license_id, description, organization_id, product_name, license_type)
VALUES('e84d9dfb-933a-4939-a354-9915a4189c29', 'add license', 'e839ee96-28de-4f67-bb79-870ca89743a0', 'license', 'full');

CREATE TABLE organization (
    organization_id varchar(36) NOT NULL,
    contact_email varchar(50) NOT NULL,
    contact_name varchar(20) NOT NULL,
    contact_phone varchar(20) NOT NULL,
    name varchar(20) NOT NULL,
    PRIMARY KEY (organization_id)
);

INSERT INTO organization
(organization_id, contact_email, contact_name, contact_phone, name)
VALUES ('d898a142-de44-466c-8c88-9ceb2c2429d3', 'test@mail.com', 'test', '132456', 'test');

INSERT INTO organization
(organization_id, contact_email, contact_name, contact_phone, name)
VALUES ('e839ee96-28de-4f67-bb79-870ca89743a0', 'org@mail.com', 'org', '123456', 'org');