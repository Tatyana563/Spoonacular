CREATE OR REPLACE VIEW basic_recipe_nutrient AS
SELECT  rn.recipeId,rn.nutrientId,rn.amount
FROM recipe_nutrient rn
         JOIN nutrient n ON rn.nutrientid = n.id
WHERE n.name IN ('Carbohydrates', 'Protein', 'Fat', 'Calories');
