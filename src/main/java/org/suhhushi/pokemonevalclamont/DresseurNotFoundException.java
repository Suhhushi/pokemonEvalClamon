package org.suhhushi.pokemonevalclamont;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(HttpStatus.CREATED)
public class DresseurNotFoundException extends RuntimeException {
    public DresseurNotFoundException(Long id) {
        super("Dresseur introuvable avec l'id " + id);
    }
}
