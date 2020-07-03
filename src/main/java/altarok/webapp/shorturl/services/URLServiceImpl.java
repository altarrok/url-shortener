package altarok.webapp.shorturl.services;

import altarok.webapp.shorturl.exceptions.NotFoundExeption;
import com.aventrix.jnanoid.jnanoid.NanoIdUtils;
import lombok.extern.slf4j.Slf4j;
import altarok.webapp.shorturl.models.URL;
import org.springframework.stereotype.Service;
import altarok.webapp.shorturl.repositories.URLRepository;

import javax.swing.text.html.Option;
import javax.transaction.Transactional;
import java.security.SecureRandom;
import java.util.Optional;

@Slf4j
@Service
public class URLServiceImpl implements URLService {

    private final URLRepository urlRepository;
    private final SecureRandom random = new SecureRandom();
    private final char[] alphabet = "_-0123456789abcdefghijklmnopqrstuvwxyzABCDEFGHIJKLMNOPQRSTUVWXYZ".toCharArray();

    public URLServiceImpl(URLRepository urlRepository) {
        this.urlRepository = urlRepository;
    }

    @Override
    public URL findById(Long id) {
        Optional<URL> urlOptional = urlRepository.findById(id);

        if (!urlOptional.isPresent()) {
            throw new NotFoundExeption();
        }

        return urlOptional.get();
    }

    @Override
    public URL findByKey(String key) {
        Optional<URL> urlOptional = urlRepository.findByKey(key);

        if (!urlOptional.isPresent()) {
            throw new NotFoundExeption();
        }

        return urlOptional.get();
    }

    @Override
    @Transactional
    public URL saveURLRandomKey(URL url) {
        int size = 6;
        String key = NanoIdUtils.randomNanoId(random, alphabet, size);

        // check if the key is taken
        int maxTrials = 100;
        int i = 0;
        while (urlRepository.findByKey(key).isPresent() && i++ < maxTrials) {
            key = NanoIdUtils.randomNanoId(random, alphabet, size);
        }

        url.setKey(key);
        URL savedURL =  urlRepository.save(url);
        log.debug("Saved a URL: " + url.getUrl() + " with key: " + url.getKey() + " ID: " + url.getId());
        return savedURL;
    }
}
