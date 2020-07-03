package altarok.webapp.shorturl.services;

import altarok.webapp.shorturl.models.URL;

public interface URLService {
    URL findById(Long id);
    URL findByKey(String key);
    URL saveURLRandomKey(URL url);
}
