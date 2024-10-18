CREATE TABLE IF NOT EXISTS nutrient
(
    id   serial PRIMARY KEY,
    name varchar(50),
    unit varchar(20)
);

CREATE TABLE IF NOT EXISTS ingredient
(
    id   serial PRIMARY KEY,
    name varchar(50),
    unit varchar(20)
);
CREATE TABLE IF NOT EXISTS cuisine
(
    id    serial PRIMARY KEY,
    name varchar(50)
);
CREATE TABLE IF NOT EXISTS recipe
(
    id             serial PRIMARY KEY,
    name           varchar(50),
    vegetarian     boolean,
    summary        text,
    readyInMinutes varchar(10)
);

CREATE TABLE IF NOT EXISTS recipe_ingredient
(
    id            serial PRIMARY KEY,
    recipeId     int,
    ingredientId int,
    amount       int
);


CREATE TABLE IF NOT EXISTS recipe_nutrient
(
    id         serial PRIMARY KEY,
    recipeId   int,
    nutrientId int,
    amount     int
);

CREATE TABLE IF NOT EXISTS recipe_ingredient
(
    id        serial PRIMARY KEY,
    recipeId   int,
    ingredientId int,
    amount  double precision
);