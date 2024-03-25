package school.hei.soratra.endpoint.rest.controller.health;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.net.URL;
import java.time.Duration;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;
import school.hei.soratra.PojaGenerated;
import school.hei.soratra.file.BucketComponent;

@PojaGenerated
@RestController
@AllArgsConstructor
public class SoratraController {

  private final BucketComponent bucketComponent;

  @PutMapping("/soratra/{id}")
  public ResponseEntity<Void> createAndUpdateSoratra(@PathVariable String id, @RequestBody String poeme) {
    try {
      // Créer un fichier temporaire avec le poème en minuscules
      File poemeLowercaseFile = createTempFile(poeme, true);

      // Créer un fichier temporaire avec le poème en majuscules
      File poemeUppercaseFile = createTempFile(poeme, false);

      // Upload des deux fichiers temporaires dans S3
      String lowercaseKey = "soratra/" + id + "/poeme_lowercase.txt";
      bucketComponent.upload(poemeLowercaseFile, lowercaseKey);

      String uppercaseKey = "soratra/" + id + "/poeme_uppercase.txt";
      bucketComponent.upload(poemeUppercaseFile, uppercaseKey);
    } catch (IOException e) {
      // En cas d'échec, retourner un code 500 avec un corps vide
      return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
    }

    // Si tout se passe bien, retourner un code 200 avec un corps vide
    return ResponseEntity.ok().build();
  }

  private File createTempFile(String poeme, boolean toLowercase) throws IOException {
    // Créer un fichier temporaire
    File tempFile = File.createTempFile("poeme", ".txt");

    // Modifier le poème en minuscules si nécessaire
    if (toLowercase) {
      poeme = poeme.toLowerCase();
    } else {
      poeme = poeme.toUpperCase();
    }

    // Écrire la phrase poétique dans le fichier temporaire
    try (FileWriter writer = new FileWriter(tempFile)) {
      writer.write(poeme);
    }

    return tempFile;
  }


  @GetMapping("/soratra/{id}")
  public ResponseEntity<Object> getSoratra(@PathVariable String id) {
    String bucketKey = "soratra/" + id + "/poeme.txt";

    // Récupérer l'URL pré-signée pour le fichier original (minuscule)
    URL originalUrl = bucketComponent.presign(bucketKey, Duration.ofMinutes(10));

    // Récupérer l'URL pré-signée pour le fichier transformé (majuscule)
    URL transformedUrl = bucketComponent.presign(bucketKey + "_transformed.txt", Duration.ofMinutes(10));

    // Création du JSON de réponse
    String responseBody = String.format("{\"original_url\": \"%s\", \"transformed_url\": \"%s\"}", originalUrl, transformedUrl);

    return ResponseEntity.status(HttpStatus.OK).body(responseBody);
  }
}
