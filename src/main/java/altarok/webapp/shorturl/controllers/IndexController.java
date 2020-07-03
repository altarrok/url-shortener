package altarok.webapp.shorturl.controllers;

import altarok.webapp.shorturl.exceptions.NotFoundExeption;
import altarok.webapp.shorturl.models.URL;
import altarok.webapp.shorturl.services.URLService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

@Slf4j
@Controller
public class IndexController {

    private final URLService urlService;

    public IndexController(URLService urlService) {
        this.urlService = urlService;
    }

    @GetMapping("/")
    public String index(Model model) {
        model.addAttribute("URL", new URL());
        return "index";
    }

    @GetMapping("/show/{id}")
    public String show(@PathVariable String id, Model model) {
        model.addAttribute("URL", urlService.findById(Long.valueOf(id)));
        return "show";
    }

    @PostMapping("/create")
    public String createURL(@Validated @ModelAttribute("URL") URL url, BindingResult bindingResult) {
        if (bindingResult.hasErrors()) {
            bindingResult.getAllErrors().forEach(objectError -> {
                log.error("Validation error: " + objectError);
            });
            return "index";
        }

        URL savedURL = urlService.saveURLRandomKey(url);

        return "redirect:show/" + savedURL.getId();
    }

    @GetMapping("/{key}")
    public String redirect(@PathVariable String key) {
        URL foundURL = urlService.findByKey(key);
        return "redirect:" + foundURL.getUrl();
    }

    @ExceptionHandler({NotFoundExeption.class})
    public String notFoundErrorHandler() {
        return "not-found-error";
    }
}
