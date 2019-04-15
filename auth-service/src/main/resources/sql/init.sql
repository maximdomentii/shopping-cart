CREATE TABLE "public"."user_authentication"
(
   user_id bigint PRIMARY KEY NOT NULL,
   enabled bool NOT NULL,
   password varchar(255) NOT NULL,
   username varchar(255) NOT NULL UNIQUE
)
;



CREATE TABLE "public"."user_authorization"
(
   user_role_id bigint PRIMARY KEY NOT NULL,
   user_id bigint NOT NULL,
   role varchar(50)
)
;
ALTER TABLE "public"."user_authorization"
ADD CONSTRAINT fk_e68gtnp8b2802at5qb9vurt5v
FOREIGN KEY (user_id)
REFERENCES "public"."user_authentication"(user_id)
;



insert into user_authentication values
  (1, true, 'cashier', 'cashier')
;

insert into user_authorization values
  (1, 1, 'ROLE_CASHIER')
;
