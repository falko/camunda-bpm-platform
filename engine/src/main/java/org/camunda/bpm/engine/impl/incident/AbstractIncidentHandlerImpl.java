package org.camunda.bpm.engine.impl.incident;

import org.camunda.bpm.engine.runtime.Incident;

/**
 * Abstract base class that allows for more extensible {@link IncidentHandler}
 * implementations.
 *
 * @author Falko Menge
 */
public abstract class AbstractIncidentHandlerImpl implements IncidentHandler {

  /**
   * Handle an incident that arose in the context of an execution.
   * 
   * Default implementation that invokes
   * {@link AbstractIncidentHandlerImpl#createIncident(String, String, String, String, String)}
   */
  public void handleIncident(String processDefinitionId, String activityId, String executionId, String jobId, String message) {
    createIncident(processDefinitionId, activityId, executionId, jobId, message);
  }

  /**
   * Handle an incident that arose in the context of an execution and return
   * the newly created {@link Incident} object.
   */
  public abstract Incident createIncident(String processDefinitionId, String activityId, String executionId, String jobId, String message);

}