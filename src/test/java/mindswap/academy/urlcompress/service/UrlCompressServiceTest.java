package mindswap.academy.urlcompress.service;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertSame;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.Mockito.any;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.util.ArrayList;
import java.util.List;

import mindswap.academy.urlcompress.Persistence.model.UrlCompress;
import mindswap.academy.urlcompress.Persistence.repository.UrlCompressRepository;
import mindswap.academy.urlcompress.exception.UrlCompressNotValidException;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit.jupiter.SpringExtension;

@ContextConfiguration(classes = {UrlCompressService.class})
@ExtendWith(SpringExtension.class)
class UrlCompressServiceTest {
    @MockBean
    private UrlCompressRepository urlCompressRepository;

    @Autowired
    private UrlCompressService urlCompressService;

    /**
     * Method under test: {@link UrlCompressService#getAllUrl()}
     */
    @Test
    void testGetAllUrl() {
        ArrayList<UrlCompress> urlCompressList = new ArrayList<>();
        when(this.urlCompressRepository.findAll()).thenReturn(urlCompressList);
        List<UrlCompress> actualAllUrl = this.urlCompressService.getAllUrl();
        assertSame(urlCompressList, actualAllUrl);
        assertTrue(actualAllUrl.isEmpty());
        verify(this.urlCompressRepository).findAll();
    }

    /**
     * Method under test: {@link UrlCompressService#getAllUrl()}
     */
    @Test
    void testGetAllUrl2() {
        when(this.urlCompressRepository.findAll())
                .thenThrow(new UrlCompressNotValidException("https://example.org/example"));
        assertThrows(UrlCompressNotValidException.class, () -> this.urlCompressService.getAllUrl());
        verify(this.urlCompressRepository).findAll();
    }

    /**
     * Method under test: {@link UrlCompressService#getFullUrl(String)}
     */
    @Test
    void testGetFullUrl() {
        when(this.urlCompressRepository.findByShortUrl((String) any())).thenReturn("https://example.org/example");
        assertEquals("https://example.org/example", this.urlCompressService.getFullUrl("https://example.org/example"));
        verify(this.urlCompressRepository).findByShortUrl((String) any());
    }

    /**
     * Method under test: {@link UrlCompressService#getFullUrl(String)}
     */
    @Test
    void testGetFullUrl2() {
        when(this.urlCompressRepository.findByShortUrl((String) any()))
                .thenThrow(new UrlCompressNotValidException("https://example.org/example"));
        assertThrows(UrlCompressNotValidException.class,
                () -> this.urlCompressService.getFullUrl("https://example.org/example"));
        verify(this.urlCompressRepository).findByShortUrl((String) any());
    }

    /**
     * Method under test: {@link UrlCompressService#createShortUrl(String)}
     */
    @Test
    void testCreateShortUrl() {
        UrlCompress urlCompress = new UrlCompress();
        urlCompress.setId(1);
        urlCompress.setLongUrl("https://example.org/example");
        urlCompress.setShortUrl("https://example.org/example");
        when(this.urlCompressRepository.save((UrlCompress) any())).thenReturn(urlCompress);
        assertSame(urlCompress, this.urlCompressService.createShortUrl("https://example.org/example"));
        verify(this.urlCompressRepository).save((UrlCompress) any());
    }

    /**
     * Method under test: {@link UrlCompressService#createShortUrl(String)}
     */
    @Test
    void testCreateShortUrl2() {
        UrlCompress urlCompress = new UrlCompress();
        urlCompress.setId(1);
        urlCompress.setLongUrl("https://example.org/example");
        urlCompress.setShortUrl("https://example.org/example");
        when(this.urlCompressRepository.save((UrlCompress) any())).thenReturn(urlCompress);
        assertThrows(UrlCompressNotValidException.class, () -> this.urlCompressService.createShortUrl("UU"));
    }



    /**
     * Method under test: {@link UrlCompressService#createShortUrl(String)}
     */
    @Test
    void testCreateShortUrl3() {
        when(this.urlCompressRepository.save((UrlCompress) any()))
                .thenThrow(new UrlCompressNotValidException("https://example.org/example"));
        assertThrows(UrlCompressNotValidException.class,
                () -> this.urlCompressService.createShortUrl("https://example.org/example"));
        verify(this.urlCompressRepository).save((UrlCompress) any());
    }


    /**
     * Method under test: {@link UrlCompressService#validateUrl(String)}
     */
    @Test
    void testValidateUrl() {
        assertTrue(this.urlCompressService.validateUrl("https://example.org/example"));
        assertFalse(this.urlCompressService.validateUrl("UU"));
        assertFalse(this.urlCompressService.validateUrl("[::FFFF:999.999.999.999]:9U"));
        assertFalse(this.urlCompressService.validateUrl("https://example.org/examplehttps://example.org/example"));
        assertFalse(this.urlCompressService.validateUrl("UUhttps://example.org/example"));
    }

    /**
     * Method under test: {@link UrlCompressService#getShortUrl(String)}
     */
    @Test
    void testGetShortUrl() {
        assertEquals("cc70df7e", this.urlCompressService.getShortUrl("https://example.org/example"));
    }
}
