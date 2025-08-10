-- 1. Insert nutrients
INSERT INTO nutrient (name, unit)
VALUES ('Protein', 'g'),
       ('Fat', 'g'),
       ('Carbohydrates', 'g'),
       ('Calories', 'kcal')
    ON CONFLICT (name) DO NOTHING;

-- 2. Insert ingredients
INSERT INTO ingredient (name, unit)
VALUES ('Chicken Breast', 'grams'),
       ('Olive Oil', 'ml'),
       ('Pasta', 'grams'),
       ('Cheese', 'grams')
    ON CONFLICT (name) DO NOTHING;

-- 3. Insert cuisine
INSERT INTO cuisine (name)
VALUES ('Italian')
    ON CONFLICT (name) DO NOTHING;

-- 4. Insert recipes
INSERT INTO recipe (name, vegetarian, summary, dish_type, url, readyInMinutes)
VALUES ('Grilled Chicken', false, 'Tasty grilled chicken recipe', 'LUNCH', 'http://recipe1.example.com', 30),
       ('Mega Pasta', false, 'Extremely hearty pasta for big appetites', 'LUNCH', 'http://recipe2.example.com', 45);

-- 5. Insert recipe_ingredient
INSERT INTO recipe_ingredient (recipeId, ingredientId, amount)
VALUES ((SELECT id FROM recipe WHERE name = 'Grilled Chicken'),
        (SELECT id FROM ingredient WHERE name = 'Chicken Breast'), 200),
       ((SELECT id FROM recipe WHERE name = 'Grilled Chicken'),
        (SELECT id FROM ingredient WHERE name = 'Olive Oil'), 30),
       ((SELECT id FROM recipe WHERE name = 'Mega Pasta'),
        (SELECT id FROM ingredient WHERE name = 'Pasta'), 500),
       ((SELECT id FROM recipe WHERE name = 'Mega Pasta'),
        (SELECT id FROM ingredient WHERE name = 'Cheese'), 200);

-- 6. Insert recipe_nutrient
INSERT INTO recipe_nutrient (recipeId, nutrientId, amount)
VALUES ((SELECT id FROM recipe WHERE name = 'Grilled Chicken'), (SELECT id FROM nutrient WHERE name = 'Protein'), 50),
       ((SELECT id FROM recipe WHERE name = 'Grilled Chicken'), (SELECT id FROM nutrient WHERE name = 'Fat'), 10),
       ((SELECT id FROM recipe WHERE name = 'Grilled Chicken'), (SELECT id FROM nutrient WHERE name = 'Carbohydrates'), 10),
       ((SELECT id FROM recipe WHERE name = 'Grilled Chicken'), (SELECT id FROM nutrient WHERE name = 'Calories'), 350),

       ((SELECT id FROM recipe WHERE name = 'Mega Pasta'), (SELECT id FROM nutrient WHERE name = 'Protein'), 60),
       ((SELECT id FROM recipe WHERE name = 'Mega Pasta'), (SELECT id FROM nutrient WHERE name = 'Fat'), 80),
       ((SELECT id FROM recipe WHERE name = 'Mega Pasta'), (SELECT id FROM nutrient WHERE name = 'Carbohydrates'), 300),
       ((SELECT id FROM recipe WHERE name = 'Mega Pasta'), (SELECT id FROM nutrient WHERE name = 'Calories'), 2000);

-- 7. Insert recipe_cuisine
INSERT INTO recipe_cuisine (recipeId, cuisineId)
VALUES ((SELECT id FROM recipe WHERE name = 'Grilled Chicken'),
        (SELECT id FROM cuisine WHERE name = 'Italian')),
       ((SELECT id FROM recipe WHERE name = 'Mega Pasta'),
        (SELECT id FROM cuisine WHERE name = 'Italian'));
