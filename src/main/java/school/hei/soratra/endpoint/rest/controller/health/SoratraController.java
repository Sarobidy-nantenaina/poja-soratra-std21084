package school.hei.soratra.endpoint.rest.controller.health;

import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;
import school.hei.soratra.PojaGenerated;

@PojaGenerated
@RestController
@AllArgsConstructor
public class SoratraController {

  @PutMapping("/soratra/{id}")
  public ResponseEntity<Void> updateSoratra(@PathVariable String id, @RequestBody String poeme) {
    System.out.println("ID: " + id);
    System.out.println("Poème: " + poeme);

    if (traitementKO()) {
      return ResponseEntity.ok().build();
    } else {
      return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
    }
  }

  private boolean traitementKO() {
    return false;
  }

}
