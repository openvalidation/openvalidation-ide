package de.openvalidation.openvalidationidebackend.infrastructure.database;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.web.servlet.MockMvc;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@ExtendWith(SpringExtension.class)
@SpringBootTest
@AutoConfigureMockMvc
public class DatabaseResetIntegrationTest {
  @Autowired
  private MockMvc mockMvc;

  @Test
  public void onResetDatabaseToInitialState_thenStatusSuccessful() throws Exception {
    mockMvc.perform(delete("/reset")
        .contentType(MediaType.APPLICATION_JSON))
        .andExpect(status().is2xxSuccessful());
  }

}
