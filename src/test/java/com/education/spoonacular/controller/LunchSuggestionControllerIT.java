package com.education.spoonacular.controller;

import org.flywaydb.core.Flyway;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.DynamicPropertyRegistry;
import org.springframework.test.context.DynamicPropertySource;
import org.springframework.test.web.servlet.MockMvc;
import org.testcontainers.containers.PostgreSQLContainer;
import org.testcontainers.junit.jupiter.Container;
import org.testcontainers.junit.jupiter.Testcontainers;

import java.sql.*;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@Testcontainers
@SpringBootTest
@AutoConfigureMockMvc
public class LunchSuggestionControllerIT {

    @Container
    public static PostgreSQLContainer<?> postgres = new PostgreSQLContainer<>("postgres:15")
            .withDatabaseName("testdb")
            .withUsername("test")
            .withPassword("test");

    @Autowired
    private MockMvc mockMvc;

    @DynamicPropertySource
    static void registerPgProperties(DynamicPropertyRegistry registry) {
        registry.add("spring.datasource.url", postgres::getJdbcUrl);
        registry.add("spring.datasource.username", postgres::getUsername);
        registry.add("spring.datasource.password", postgres::getPassword);
    }

    @BeforeEach
    void migrateDb() {
        Flyway flyway = Flyway.configure()
                .dataSource(postgres.getJdbcUrl(), postgres.getUsername(), postgres.getPassword())
                .locations("classpath:db/test_migration")
                .baselineOnMigrate(true)
                .cleanDisabled(false)
                .load();

        // First clean the DB to avoid checksum mismatch
        flyway.clean();

        flyway.migrate();
    }

    @Test
    public void suggestLunch_returnsOnlySuitableRecipe() throws Exception {
        String requestBody = """
        {
          "characteristicsDto": {
            "gender": "WOMAN",
            "activityLevel": "SEDENTARY",
            "goal": "LOOSE_WEIGHT",
            "height": 170,
            "weight": 70,
            "age": 55
          }
        }
        """;

        mockMvc.perform(post("/api/suggest-lunch")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(requestBody))
                .andExpect(status().isOk())
                .andExpect(content().json(" [{\"recipeId\":1,\"recipeName\":\"Grilled Chicken\"," +
                        "\"dishType\":\"LUNCH\",\"cuisineName\":[\"Italian\"],\"nutrient\":[{\"name\":\"Calories\"," +
                        "\"amount\":350.0,\"unit\":\"kcal\"},{\"name\":\"Carbohydrates\",\"amount\":10.0,\"unit\":\"g\"}," +
                        "{\"name\":\"Fat\",\"amount\":10.0,\"unit\":\"g\"},{\"name\":\"Protein\",\"amount\":50.0,\"unit\":" +
                        "\"g\"}],\"ingredient\":[{\"name\":\"Chicken Breast\",\"amount\":200.0,\"unit\":\"grams\"}," +
                        "{\"name\":\"Olive Oil\",\"amount\":30.0,\"unit\":\"ml\"}]}]"));
    }
}
