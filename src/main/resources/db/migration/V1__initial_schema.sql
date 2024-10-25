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
    id   serial PRIMARY KEY,
    name varchar(50) UNIQUE
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
    recipeId     int,
    ingredientId int,
    amount       double precision,
    CONSTRAINT pk_recipe_ingredient PRIMARY KEY (recipeId, ingredientId),
    CONSTRAINT fk_recipe FOREIGN KEY (recipeId) REFERENCES recipe (id),
    CONSTRAINT fk_ingredient FOREIGN KEY (ingredientId) REFERENCES ingredient (id)
);

CREATE TABLE IF NOT EXISTS recipe_nutrient
(
    recipeId   int,
    nutrientId int,
    amount     double precision,
    CONSTRAINT pk_recipe_nutrient PRIMARY KEY (recipeId, nutrientId),
    CONSTRAINT fk_recipe FOREIGN KEY (recipeId) REFERENCES recipe (id),
    CONSTRAINT fk_nutrient FOREIGN KEY (nutrientId) REFERENCES nutrient (id)
);

CREATE TABLE IF NOT EXISTS recipe_cuisine
(
   recipeId  int,
   cuisineId int,
    CONSTRAINT pk_recipe_cuisineId PRIMARY KEY (recipeId,cuisineId),
    CONSTRAINT fk_recipe FOREIGN KEY (recipeId) REFERENCES recipe (id),
    CONSTRAINT fk_cuisineId FOREIGN KEY (cuisineId) REFERENCES cuisine (id)
);


