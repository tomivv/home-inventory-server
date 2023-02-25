package com.github.tomivv.homeinventoryserver.item;

import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import java.util.Optional;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.hamcrest.Matchers.is;

import org.junit.jupiter.api.Test;
import org.junit.runner.RunWith;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;

@RunWith(SpringRunner.class)
@WebMvcTest(ItemController.class)
public class ItemControllerTest {
    @Autowired
    private MockMvc mockMvc;
 
    @MockBean
    private ItemRepository itemRepository;
 
    @Test
    public void testCreateItem() throws Exception {
        Item item = new Item();
        item.setName("Test Item");
        item.setComment("This is a test item");
        item.setQuantity(10);
 
        when(itemRepository.save(any(Item.class))).thenReturn(item);
 
        mockMvc.perform(post("/api/v1/item")
                .contentType(MediaType.APPLICATION_JSON)
                .content("{\"name\": \"Test Item\", \"comment\": \"This is a test item\", \"quantity\": 10}"))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.name", is("Test Item")))
                .andExpect(jsonPath("$.comment", is("This is a test item")))
                .andExpect(jsonPath("$.quantity", is(10)));
 
        verify(itemRepository, times(1)).save(any(Item.class));
    }

    @Test
    public void testGetItemById() throws Exception {
        Item item = new Item();
        item.setId("testid");
        item.setName("Test Item");
        item.setComment("Test Comment");
        item.setQuantity(1);

        when(itemRepository.findById(anyString())).thenReturn(Optional.of(item));

        mockMvc.perform(get("/api/v1/item/testid"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id", is(item.getId())))
                .andExpect(jsonPath("$.name", is(item.getName())))
                .andExpect(jsonPath("$.comment", is(item.getComment())))
                .andExpect(jsonPath("$.quantity", is(item.getQuantity())));
    }

    @Test
    public void testGetItemByIdNotFound() throws Exception {
        when(itemRepository.findById(anyString())).thenReturn(Optional.empty());

        mockMvc.perform(get("/api/v1/item/test"))
                .andExpect(status().isNotFound())
                .andExpect(content().string("No item with id: test"));
    }

    @Test
    public void testDeleteItem() throws Exception {
        Item item = new Item();
        item.setId("testid");
        item.setName("Test Item");
        item.setComment("Test Comment");
        item.setQuantity(1);

        when(itemRepository.findById(anyString())).thenReturn(Optional.of(item));

        mockMvc.perform(delete("/api/v1/item/testid"))
                .andExpect(status().isOk());
    }

    @Test
    public void testDeleteItemNotFound() throws Exception {
        when(itemRepository.findById(anyString())).thenReturn(Optional.empty());

        mockMvc.perform(delete("/api/v1/item/test"))
                .andExpect(status().isNotFound())
                .andExpect(content().string("No item with id: test"));
    }

    @Test
    public void testUpdateItem() throws Exception {
        Item item = new Item();
        item.setId("testid");
        item.setName("Test item");
        item.setComment("Test Comment");
        item.setQuantity(1);

        when(itemRepository.findById(anyString())).thenReturn(Optional.of(item));
        when(itemRepository.save(any(Item.class))).thenAnswer(invocation -> invocation.getArgument(0));

        String json = "{\n" +
            "  \"name\": \"Updated Item\",\n" +
            "  \"comment\": \"Updated Comment\",\n" +
            "  \"quantity\": 20\n" +
            "}";

        mockMvc.perform(MockMvcRequestBuilders.put("/api/v1/item/{id}", "testid")
                .content(json)
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(MockMvcResultMatchers.jsonPath("$.id").value("testid"))
                .andExpect(MockMvcResultMatchers.jsonPath("$.name").value("Updated Item"))
                .andExpect(MockMvcResultMatchers.jsonPath("$.comment").value("Updated Comment"))
                .andExpect(MockMvcResultMatchers.jsonPath("$.quantity").value(20));
    }

    @Test
    public void testUpdateItemNotFound() throws Exception {
        when(itemRepository.findById(anyString())).thenReturn(Optional.empty());

        String json = "{\n" +
            "  \"name\": \"Updated Item\",\n" +
            "  \"comment\": \"Updated Comment\",\n" +
            "  \"quantity\": 20\n" +
            "}";

        mockMvc.perform(MockMvcRequestBuilders.put("/api/v1/item/{id}", "testid")
                .content(json)
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(MockMvcResultMatchers.status().isNotFound());
    }
}
