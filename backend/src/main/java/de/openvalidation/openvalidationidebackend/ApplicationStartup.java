package de.openvalidation.openvalidationidebackend;

import de.openvalidation.openvalidationidebackend.maintenance.MaintenanceService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.context.event.ApplicationReadyEvent;
import org.springframework.context.ApplicationListener;
import org.springframework.stereotype.Component;

@Component
public class ApplicationStartup implements ApplicationListener<ApplicationReadyEvent> {
  private MaintenanceService maintenanceService;

  @Autowired
  public ApplicationStartup(MaintenanceService maintenanceService) {
    this.maintenanceService = maintenanceService;
  }

  @Override
  public void onApplicationEvent(final ApplicationReadyEvent event) {
    maintenanceService.createInitialData();
  }
}
