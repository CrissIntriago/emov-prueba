/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.origami.sigef.common.service.interfaces;

import java.io.InputStream;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import javax.ejb.Local;
import org.activiti.engine.ProcessEngine;
import org.activiti.engine.history.HistoricIdentityLink;
import org.activiti.engine.history.HistoricProcessInstance;
import org.activiti.engine.history.HistoricTaskInstance;
import org.activiti.engine.repository.ProcessDefinition;
import org.activiti.engine.repository.ProcessDefinitionQuery;
import org.activiti.engine.runtime.ProcessInstance;
import org.activiti.engine.task.Attachment;
import org.activiti.engine.task.IdentityLink;
import org.activiti.engine.task.Task;

/**
 *
 * @author CarlosLoorVargas
 */
@Local
public interface BpmBaseEngine {

    /**
     * @author carlosloorvargas description: Get processEngine object
     * @return Object ProcessEngine
     *
     *
     */
    public ProcessEngine getProcessEngine();

    /**
     * @author carlosloorvargas
     * @param processId String description: Get form key value, from the task by
     * process id
     * @return String
     *
     *
     */
    public String getFormKey(String processId);

    /**
     * @author carlosloorvargas
     * @param processId String description: Get procees key by process id
     * @return String
     *
     *
     */
    public String getProcessKey(String processId);

    /**
     * @author carlosloorvargas
     * @param task Task description: Get form key value, from the task by
     * taskData
     * @return String
     *
     *
     */
    public String getFromKey(Task task);

    /**
     * @author carlosloorvargas
     * @param asignee String
     * @param keyTarea String description: Get user tasks by asignee value and
     * by keyTarea
     * @return List Task
     *
     *
     */
    public List<Task> getUsertasksList(String asignee, String keyTarea);

    /**
     * @author Anyelo
     * @param asignee description: Retorna la lista de tareas por usuarios
     * candidatos a completarla
     * @return List Task
     */
    public List<Task> getCandidateUsertasksList(String asignee);

    /**
     * @author carlosloorvargas description: Get ProcessDefinition or process
     * deployed list
     * @return List ProcessDefinition
     *
     *
     */
    public List<ProcessDefinition> getProcessDesployedList();

    public List<ProcessDefinition> getAllProcessDesployedList();

    /**
     * @author carlosloorvargas
     * @param proccessDefKey String description: Get a counter of tasks from the
     * process
     * @return long
     *
     *
     */
    public long getTaskCounterByProcessKey(String proccessDefKey);

    /**
     * @author carlosloorvargas
     * @return long description: Get number of task by user
     * @param asignee String
     *
     */
    public long getTaskCounterByUser(String asignee);

    /**
     * @author carlosloorvargas
     * @return boolean description:
     * @param taskId String
     *
     * @param parameters Hashmap
     * @throws java.lang.Exception Exception
     */
    public boolean completeTask(String taskId, HashMap<String, Object> parameters) throws Exception;

    /**
     * @author carlosloorvargas description:
     * @param processDefinitionKey String
     * @param parameters HashMap
     * @return ProcessInstance
     * @throws java.lang.Exception Exception
     */
    public ProcessInstance startProcessByDefinitionKey(String processDefinitionKey, HashMap<String, Object> parameters) throws Exception;

    /**
     * @author carlosloorvargas description:
     * @param taskId String
     * @param varName String
     * @return Object variable
     */
    public Object getvariable(String taskId, String varName);

    /**
     * @author henryPilco description:
     * @param processInstanceId String
     * @param varName String
     * @return Object variable
     */
    public Object getVariableByProcessInstance(String processInstanceId, String varName);

    /**
     * @author carlosloorvargas description:
     * @param instanceId String
     * @return HashMap values
     */
    public HashMap<String, Object> getvariables(String instanceId);

    /**
     * @author carlosloorvargas
     * @return boolean description:
     * @param taskId String
     *
     */
    public boolean completeTask(String taskId);

    /**
     * @author carlosloorvargas description:
     * @param taskId String
     * @param priority Integer
     */
    public void setTaskPriority(String taskId, int priority);

    /**
     * @author carlosloorvargas description: return attachments files from
     * taskId
     * @param taskId String
     * @return List Attachment
     */
    public List<Attachment> getAttachmentsFiles(String taskId);

    /**
     * @author carlosloorvargas description: return task data from current
     * taskId
     * @param taskId String
     * @return Object Task
     */
    public Task getTaskDataByTaskID(String taskId);

    /**
     * @author carlosloorvargas description: return ProcessDefinition from task
     * by definition id
     * @param defId String
     * @return Object ProcessDefinition
     */
    public ProcessDefinition getProcessDataByDefID(String defId);

    /**
     * @author carlosloorvargas description: return attachments files from
     * instanceId
     * @param instanceId String
     *
     * @return List Attachment
     */
    public List<Attachment> getProcessInstanceAttachmentsFiles(String instanceId);

    /**
     * @author henryPilco description: return attachments files from instanceId
     * and instanceId subprocess
     * @param processInstanceId String
     *
     * @return List Attachment
     */
    public List<Attachment> getAttachmentsFilesByProcessInstanceIdMain(String processInstanceId);

    /**
     * @author carlosloorvargas description: return HistoricTaskInstance task
     * ended by user asignee
     * @param asignee String
     *
     * @return List HistoricTaskInstance
     */
    public List<HistoricTaskInstance> getEndedUsertasksList(String asignee);

    /**
     * @return boolean
     * @author carlosloorvargas description: activate suspend process by procees
     * Instance number
     * @param processInstanceId String
     *
     */
    public boolean activateProcess(String processInstanceId);

    /**
     * @return boolean
     * @author carlosloorvargas description: pause process runtime by procees
     * Instance number
     * @param processInstanceId String
     *
     */
    public boolean suspendProcess(String processInstanceId);

    /**
     * @author carlosloorvargas
     * @param taskId String description: return var value from current execution
     * identifier
     * @param varName String
     * @return Object variable
     */
    public Object getvariableByExecutionId(String taskId, String varName);

    /**
     * @author carlosloorvargas
     * @param taskId String
     * @param assignee String
     * @return boolean
     */
    public boolean setAssigneeTask(String taskId, String assignee);

    public boolean setCandidateUser(String taskId, String candidate);

    public boolean deleteCandidateUser(String taskId, String candidate);

    /**
     * @author carlosloorvargas
     * @param candidate String
     * @return ArrayList Task
     */
    public ArrayList<Task> getTaskGroup(String candidate);

    /**
     * @author carlosloorvargas
     * @param path String description: Load/Deploy a process by class path
     *
     */
    public void loadSingleProcessByClassPath(String path);

    /**
     * @author carlosloorvargas
     * @param key String
     * @return ProcessDefinition Object description: return the
     * ProcessDefinition by process key
     *
     */
    public ProcessDefinition getProcessDefinitionByKey(String key);

    /**
     * @author carlosloorvargas
     * @param id String
     * @return ProcessDefinition Object description: return the
     * ProcessDefinition by process key
     *
     */
    public ProcessDefinition getProcessDefinitionById(String id);

    /**
     * @author carlosloorvargas
     * @return List HistoricProcessInstance description: return the List of
     * ProcessInstance
     *
     */
    public List<HistoricProcessInstance> getProcessInstanceHistoric();

    public List<HistoricProcessInstance> getProcessInstanceHistoric(boolean state);

    /**
     * @author carlosloorvargas
     * @param init int
     * @param max int
     * @param state boolean
     * @return List HistoricProcessInstance description: return the List of
     * ProcessInstance
     *
     */
    public List<HistoricProcessInstance> getProcessInstanceHistoric(int init, int max, boolean state);

    /**
     * @author carlosloorvargas
     * @param id String
     * @param state boolean
     * @return List HistoricProcessInstance description: return the List of
     * ProcessInstance
     *
     */
    public List<HistoricProcessInstance> getProcessInstanceHistoric(String id, boolean state);

    /**
     * @author carlosloorvargas
     * @param key String
     * @return List HistoricProcessInstance description: return the List of
     * ProcessInstance Filter by Key Process
     *
     */
    public List<HistoricProcessInstance> getProcessInstanceHistoricByKey(String key);

    /**
     * @author carlosloorvargas
     * @param processInstanceId String
     * @return ProcessInstance Object description: return a ProcessInstance by
     * id
     *
     */
    public ProcessInstance getProcessInstanceById(String processInstanceId);

    /**
     * @author carlosloorvargas
     * @param processInstanceDefId String
     * @return ProcessInstance Object description: return a ProcessInstance by
     * processInstanceDefId
     *
     */
    public ProcessInstance getProcessInstanceByDefId(String processInstanceDefId);

    /**
     * @author carlosloorvargas
     * @param processInstanceId String
     * @return HistoricTaskInstance List description: return a list of all task
     * by processInstanceId
     *
     */
    public List<HistoricTaskInstance> getTaskByProcessInstanceId(String processInstanceId);

    public HistoricTaskInstance getLastTaskByProcessInstance(String processInstanceId);

    /**
     * @author henrypilco
     * @param processInstanceId String
     * @return HistoricTaskInstance List description: return a list of all task
     * by superProcessInstanceId
     *
     */
    public List<HistoricTaskInstance> getTaskByProcessInstanceIdMain(String processInstanceId);

    /**
     * @author carlosloorvargas
     * @param id String
     * @return List HistoricProcessInstance description: return the List of
     * ProcessInstance
     *
     */
    public List<HistoricProcessInstance> getProcessInstanceHistoricById(String id);

    /**
     * @author carlosloorvargas
     * @param processInstanceId String
     * @return HistoricProcessInstance description: return the List of
     * ProcessInstance
     *
     */
    public HistoricProcessInstance getHistoricProcessInstanceByInstanceID(String processInstanceId);

    /**
     * @author henryPilco
     * @param porcessInstanceId String
     * @param varName String
     * @param value Object description: set varible in ProcessInstance
     *
     */
    public void setVariableProcessInstance(String porcessInstanceId, String varName, Object value);

    /**
     * @author henryPilco
     * @param porcessInstanceId String
     * @return List Task description: listado de tareas activas por Instancia
     * del proceso
     */
    public List<Task> getListTaskActiveByProcessInstance(String porcessInstanceId);

    /**
     * @author henryPilco
     * @param idTarea String
     * @return List IdentityLink description: listado de IdentityLink por Id de
     * la tarea
     */
    public List<IdentityLink> identityLinkPorTareaId(String idTarea);

    /**
     * @author henryPilco
     * @param processInstanceId String
     * @return List String description: listado de porcessInstanceIds asociados
     * a porcessInstanceId principal del proceso
     */
    public List<String> getListProcessInstanceIdsByProcessInstanceIdMain(String processInstanceId);

    public Map<String, Object> getVar(String taskId);

    public void deleteProcessInstance(String processInstance, String reason);

    public List<HistoricIdentityLink> HistoricidentityLinkPorTareaId(String idTarea);

    public Task getTaskDataByProcessID(String processId);

    public Object getvariableByExecutionId(String taskId, String executionId, String varName);

    public Integer getNumberTasksUser(String asignee, String taskDefKey);

    public List<Task> getAllTasksUser(String asignee, int first, int pageSize);

    public List<Task> getTasksUserByTaskDefKey(String asignee, int first, int pageSize, String taskDefKey);

    public List<Task> getAllTasksUser(String asignee, int limite);

    public List<Task> getTasksUserByNameTask(String asignee, int limite, String taskDefKey);

    public List<Task> getTasksUserProcessId(String asignee, String processID);

    public InputStream getProcessDiagram(String processID, String diagramName);

    public ProcessDefinitionQuery getProcessDesployedLatestVersion();

    public InputStream getProcessInstanceDiagram(String procInstId);

    public List<Task> allTaskDataByProcessID(String processId);

}
