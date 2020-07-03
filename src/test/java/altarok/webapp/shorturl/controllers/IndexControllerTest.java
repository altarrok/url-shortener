package altarok.webapp.shorturl.controllers;

import altarok.webapp.shorturl.models.URL;
import altarok.webapp.shorturl.services.URLService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;


import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;

import static org.junit.jupiter.api.Assertions.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@ExtendWith(MockitoExtension.class)
class IndexControllerTest {

    @Mock
    URLService urlService;

    @InjectMocks
    IndexController controller;

    MockMvc mockMvc;

    @BeforeEach
    void setUp() {
        mockMvc = MockMvcBuilders.standaloneSetup(controller).build();
    }

    @Test
    void index() throws Exception {
        mockMvc.perform(get("/"))
                .andExpect(status().isOk())
                .andExpect(model().attributeExists("URL"))
                .andExpect(view().name("index"));
    }

    @Test
    void createURL() throws Exception {
        URL url = new URL();
        url.setId(1L);
        url.setUrl("http://www.google.com/");

        when(urlService.saveURLRandomKey(url)).thenReturn(url);

        mockMvc.perform(post("/create")
                .contentType(MediaType.APPLICATION_FORM_URLENCODED)
                .param("id", "1")
                .param("url", "http://www.google.com/"))
                .andExpect(status().is3xxRedirection())
                .andExpect(view().name("redirect:show/1"));
    }

    @Test
    void showURL() throws Exception {
        URL url = new URL();
        url.setId(1L);
        url.setKey("abcdef");
        url.setUrl("https://www.google.com/");

        when(urlService.findById(anyLong())).thenReturn(url);

        mockMvc.perform(get("/show/1"))
                .andExpect(status().isOk())
                .andExpect(model().attributeExists("URL"))
                .andExpect(view().name("show"));
        verify(urlService, times(1)).findById(anyLong());
    }

    @Test
    void testRedirect() throws Exception {
        URL url = new URL();
        url.setId(1L);
        url.setKey("abcdef");
        url.setUrl("https://www.google.com/");

        when(urlService.findByKey("abcdef")).thenReturn(url);

        mockMvc.perform(get("/abcdef"))
                .andExpect(status().is3xxRedirection())
                .andExpect(view().name("redirect:https://www.google.com/"));
        verify(urlService, times(1)).findByKey(anyString());
    }
}