package org.vitramu.engine.constant;

import org.vitramu.engine.definition.element.TaskDefinition;

public class DefinitionState {
    public static final TaskDefinition REQUEST_ARRIVED = new TaskDefinition("T1", "REQUEST_ARRIVED");
    public static final TaskDefinition REQUEST_SAVING = new TaskDefinition("T2", "REQUEST_SAVING");
    public static final TaskDefinition CREATE_PARALLEL = new TaskDefinition("T3", "CREATE_PARALLEL");
    public static final TaskDefinition CREATE_BOOKING = new TaskDefinition("T4", "CREATE_BOOKING");
    public static final TaskDefinition CREATE_OSB = new TaskDefinition("T5", "CREATE_OSB");
    public static final TaskDefinition END_OSB = new TaskDefinition("T6", "END_OSB");
    public static final TaskDefinition CREATE_PUD = new TaskDefinition("T7", "CREATE_PUD");
    public static final TaskDefinition END_PUD = new TaskDefinition("T8", "END_PUD");
    public static final TaskDefinition CREATE_FINISH = new TaskDefinition("T9", "CREATE_FINISH");
    public static final TaskDefinition CHOICE_EW = new TaskDefinition("T10", "CHOICE_EW");
    public static final TaskDefinition CREATE_EW = new TaskDefinition("T11", "CREATE_EW");
    public static final TaskDefinition REFRESH_STATUS = new TaskDefinition("T12", "REFRESH_STATUS");
    public static final TaskDefinition END = new TaskDefinition("T13", "END");
}
