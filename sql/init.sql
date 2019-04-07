CREATE TABLE "public"."product"
(
   id bigint PRIMARY KEY NOT NULL,
   barcode bigint NOT NULL UNIQUE,
   name varchar(255) NOT NULL UNIQUE,
   price_per_um numeric(6,2) NOT NULL,
   available bool NOT NULL
)
;

insert into product values
  (1, 6309095275545, 'Apple', 0.25, true),
  (2, 7981950665189, 'Orange', 0.30, true),
  (3, 5661412180652, 'Banana', 0.15, true),
  (4, 6153569079331, 'Papaya', 0.50, true)
