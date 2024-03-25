package school.hei.soratra.conf;

import org.springframework.test.context.DynamicPropertyRegistry;
import school.hei.soratra.PojaGenerated;

@PojaGenerated
public class BucketConf {

  void configureProperties(DynamicPropertyRegistry registry) {
    registry.add("aws.s3.bucket", () -> "dummy-bucket");
  }
}
