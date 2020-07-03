package altarok.webapp.shorturl.services;

import altarok.webapp.shorturl.models.URL;
import altarok.webapp.shorturl.repositories.URLRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class URLServiceImplTest {

    URLServiceImpl urlService;

    @Mock
    URLRepository urlRepository;

    @BeforeEach
    void setUp() {
        urlService = new URLServiceImpl(urlRepository);
    }

    @Test
    void testFindById() {
        URL url = new URL();
        url.setId(1L);

        when(urlRepository.findById(1L)).thenReturn(Optional.of(url));

        URL returned = urlService.findById(1L);

        assertNotNull(returned);
        assertEquals(1, returned.getId());
        verify(urlRepository, times(1)).findById(anyLong());
    }

    @Test
    void testFindByKey() {
        URL url = new URL();
        url.setKey("ABCD");

        when(urlRepository.findByKey(anyString())).thenReturn(Optional.of(url));

        URL returned = urlService.findByKey("ABCD");

        assertNotNull(returned);
        assertEquals("ABCD", returned.getKey());
        verify(urlRepository, times(1)).findByKey(anyString());
    }

    @Test
    void saveURLRandomKey() {
        URL url = new URL();
        url.setId(1L);
        url.setUrl("https://www.google.com");

        when(urlRepository.save(any(URL.class))).thenReturn(url);

        URL returned = urlService.saveURLRandomKey(url);

        assertNotNull(returned);
        assertNotNull(returned.getKey());
        assertEquals(1, returned.getId());
        verify(urlRepository).findByKey(anyString());
        verify(urlRepository, times(1)).save(any(URL.class));
    }
}