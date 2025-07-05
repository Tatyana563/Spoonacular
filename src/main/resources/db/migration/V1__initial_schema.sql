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
    unit varchar(50)         not null
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
    name           text                  not null,
    vegetarian     boolean default false not null,
    summary        text,
    dish_type      text,
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

CREATE OR REPLACE VIEW recipe_nutrient_view AS
SELECT DISTINCT r.id AS recipe_id,
                r.name AS recipe_name,
                r.dish_type AS dish_type,
                jsonb_agg(DISTINCT c.name) AS cuisines,
                jsonb_agg(DISTINCT jsonb_build_object('name', n.name, 'amount', rn.amount, 'unit',n.unit)) AS nutrient,
                jsonb_agg(DISTINCT jsonb_build_object('name', i.name, 'amount', ri.amount,'unit',i.unit)) AS ingredient
FROM recipe r
         JOIN recipe_nutrient rn ON r.id = rn.recipeid
         JOIN recipe_ingredient ri ON r.id = ri.recipeid
         JOIN nutrient n ON rn.nutrientid = n.id
         JOIN recipe_cuisine rc ON r.id = rc.recipeid
         JOIN cuisine c ON c.id = rc.cuisineid
         JOIN ingredient i ON ri.ingredientid = i.id
WHERE n.name IN ('Carbohydrates', 'Protein', 'Fat', 'Calories')
GROUP BY r.name, r.id, r.dish_type;


