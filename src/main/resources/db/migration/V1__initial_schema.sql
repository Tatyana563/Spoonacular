
CREATE TABLE IF NOT EXISTS nutrient
(
    id   serial PRIMARY KEY,
    name varchar(50) UNIQUE not null,
    unit varchar(20)        not null
);
CREATE INDEX IF NOT EXISTS nutrient_name ON nutrient (name ASC);


CREATE TABLE IF NOT EXISTS ingredient
(
    id   serial PRIMARY KEY,
    name varchar(100) UNIQUE not null,
    unit varchar(50)        not null
);

CREATE INDEX IF NOT EXISTS ingredient_name ON ingredient (name ASC);

CREATE TABLE IF NOT EXISTS cuisine
(
    id   serial PRIMARY KEY,
    name varchar(50) UNIQUE not null
);

CREATE TABLE IF NOT EXISTS recipe
(
    id             serial PRIMARY KEY,
    name           text        not null,
    vegetarian     boolean default false not null,
    summary        text,
    url            text UNIQUE           not null,
    readyInMinutes int
);
CREATE INDEX IF NOT EXISTS recipe_name ON recipe (name ASC);
CREATE TABLE IF NOT EXISTS recipe_ingredient
(
    recipeId     int              not null,
    ingredientId int              not null,
    amount       double precision not null,
    CONSTRAINT pk_recipe_ingredient PRIMARY KEY (recipeId, ingredientId),
    CONSTRAINT fk_recipe FOREIGN KEY (recipeId) REFERENCES recipe (id),
    CONSTRAINT fk_ingredient FOREIGN KEY (ingredientId) REFERENCES ingredient (id)
);

CREATE TABLE IF NOT EXISTS recipe_nutrient
(
    recipeId   int              not null,
    nutrientId int              not null,
    amount     double precision not null,
    CONSTRAINT pk_recipe_nutrient PRIMARY KEY (recipeId, nutrientId),
    CONSTRAINT fk_recipe FOREIGN KEY (recipeId) REFERENCES recipe (id),
    CONSTRAINT fk_nutrient FOREIGN KEY (nutrientId) REFERENCES nutrient (id)
);

CREATE TABLE IF NOT EXISTS recipe_cuisine
(
    recipeId  int not null,
    cuisineId int not null,
    CONSTRAINT pk_recipe_cuisineId PRIMARY KEY (recipeId, cuisineId),
    CONSTRAINT fk_recipe FOREIGN KEY (recipeId) REFERENCES recipe (id),
    CONSTRAINT fk_cuisineId FOREIGN KEY (cuisineId) REFERENCES cuisine (id)
);


