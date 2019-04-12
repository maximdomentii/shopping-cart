CREATE TABLE "public"."product"
(
   product_id bigint PRIMARY KEY NOT NULL,
   barcode bigint NOT NULL UNIQUE,
   name varchar(255) NOT NULL UNIQUE,
   price_per_um numeric(6,2) NOT NULL,
   available bool NOT NULL
)
;
create index productbarcode_idx on product (barcode);

insert into product values
  (1, 6309095275545, 'Apple', 0.25, true),
  (2, 7981950665189, 'Orange', 0.30, true),
  (3, 5661412180652, 'Banana', 0.15, true),
  (4, 6153569079331, 'Papaya', 0.50, true)
;



CREATE TABLE "public"."discount_rule"
(
   discount_rule_id bigint PRIMARY KEY NOT NULL,
   description varchar(255) NOT NULL,
   number_of_items integer NOT NULL,
   discount_percentage float NOT NULL,
   enable bool NOT NULL
)
;

insert into discount_rule values
  (1, 'three for the price of two', 3, 100, true)
;



CREATE TABLE "public"."product_discount_rule_mapping"
(
  product_id bigint NOT NULL,
  discount_rule_id bigint NOT NULL,
  PRIMARY KEY (product_id, discount_rule_id),
  CONSTRAINT product_discount_rule_mapping_fk1
    FOREIGN KEY (product_id) REFERENCES product (product_id),
  CONSTRAINT product_discount_rule_mapping_fk2
    FOREIGN KEY (discount_rule_id) REFERENCES discount_rule (discount_rule_id)
)
;

insert into product_discount_rule_mapping values
  (4, 1)
;