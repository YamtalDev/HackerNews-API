package com.akamai.MiniHackerNews;

import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;
import org.springframework.http.MediaType;

@SpringBootTest
@AutoConfigureMockMvc
public class AppTests {

    @Autowired
    private MockMvc mockMvc;

    @Test
    public void testGetAllContentEndpoint() throws Exception {
        mockMvc.perform(MockMvcRequestBuilders.get("/api/content"))
            .andExpect(MockMvcResultMatchers.status().isOk())
            .andExpect(MockMvcResultMatchers.jsonPath("$[*].id").exists())
            .andExpect(MockMvcResultMatchers.jsonPath("$[*].title").exists())
            .andExpect(MockMvcResultMatchers.jsonPath("$[*].desc").exists());
    }

    @Test
    public void testCreateContentEndpoint() throws Exception {
        mockMvc.perform(MockMvcRequestBuilders.post("/api/content")
            .contentType(MediaType.APPLICATION_JSON)
            .content("{\"id\":3,\"title\":\"Example Title\",\"desc\":\"Example Description\",\"status\":\"IDEA\",\"contentType\":\"ARTICLE\",\"dateCreated\":\"2023-09-20T10:00:00\",\"dateUpdated\":\"2023-09-20T11:00:00\",\"url\":\"http://example.com/content/1\"}"))
            .andExpect(MockMvcResultMatchers.status().isCreated());
    }

    @Test
    public void testGetContentByIdEndpoint() throws Exception {
        // Replace {id} with an existing content ID
        mockMvc.perform(MockMvcRequestBuilders.get("/api/content/{id}", 1))
            .andExpect(MockMvcResultMatchers.status().isOk())
            .andExpect(MockMvcResultMatchers.jsonPath("$.id").value(1))
            .andExpect(MockMvcResultMatchers.jsonPath("$.title").exists())
            .andExpect(MockMvcResultMatchers.jsonPath("$.desc").exists());
    }
}
