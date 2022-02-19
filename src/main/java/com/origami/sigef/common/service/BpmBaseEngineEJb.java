/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.origami.sigef.common.service;

import com.origami.sigef.common.service.interfaces.BpmBaseEngine;
import com.origami.sigef.common.service.interfaces.BpmProcessEngine;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.ejb.Stateless;
import javax.inject.Inject;
import org.activiti.bpmn.model.BpmnModel;
import org.activiti.engine.ProcessEngine;
import org.activiti.engine.RepositoryService;
import org.activiti.engine.RuntimeService;
import org.activiti.engine.TaskService;
import org.activiti.engine.history.HistoricActivityInstance;
import org.activiti.engine.history.HistoricIdentityLink;
import org.activiti.engine.history.HistoricProcessInstance;
import org.activiti.engine.history.HistoricTaskInstance;
import org.activiti.engine.repository.ProcessDefinition;
import org.activiti.engine.repository.ProcessDefinitionQuery;
import org.activiti.engine.runtime.ProcessInstance;
import org.activiti.engine.task.Attachment;
import org.activiti.engine.task.IdentityLink;
import org.activiti.engine.task.Task;
import org.activiti.image.ProcessDiagramGenerator;

/**
 *
 * @author User
 */
@Stateless
@javax.enterprise.context.Dependent
public class BpmBaseEngineEJb implements BpmBaseEngine {

    @Inject
    private BpmProcessEngine engine;

    @Override
    public ProcessEngine getProcessEngine() {
        return engine.getProcessEngine();
    }

    @Override
    public String getFormKey(String processId) {
        return engine.getProcessEngine().getFormService().getStartFormData(processId).getFormKey();
    }

    @Override
    public String getProcessKey(String processId) {
        return engine.getProcessEngine().getRepositoryService().createProcessDefinitionQuery()
                .processDefinitionId(processId).singleResult().getKey();
    }

    @Override
    public String getFromKey(Task task) {
        return engine.getProcessEngine().getFormService().getTaskFormKey(task.getProcessDefinitionId(),
                task.getTaskDefinitionKey());
    }

    @Override
    public List<Task> getUsertasksList(String asignee, String keyTarea) {
        TaskService taskService = engine.getProcessEngine().getTaskService();
        ArrayList<Task> acttask = null;
        try {
            if (keyTarea != null) {
                acttask = (ArrayList<Task>) taskService.createTaskQuery().taskAssignee(asignee)
                        .taskDefinitionKey(keyTarea).orderByTaskPriority().desc().list();
            } else {
                acttask = (ArrayList<Task>) taskService.createTaskQuery().taskAssignee(asignee)
                        .orderByTaskPriority().desc().orderByExecutionId().asc().list();
            }
        } catch (Exception e) {
            Logger.getLogger(BpmBaseEngine.class.getName()).log(Level.SEVERE, null, e);
        }
        return acttask;
    }

    @Override
    public List<Task> getCandidateUsertasksList(String asignee) {
        TaskService taskService = engine.getProcessEngine().getTaskService();
        ArrayList<Task> acttask = (ArrayList<Task>) taskService.createTaskQuery().taskCandidateUser(asignee)
                .orderByTaskPriority().desc().list();
        return acttask;
    }

    @Override
    public List<ProcessDefinition> getProcessDesployedList() {
        List<ProcessDefinition> list;
        if (engine.getProcessEngine() == null) {
            list = new ArrayList<>();
            return list;
        }
        list = engine.getProcessEngine().getRepositoryService().createProcessDefinitionQuery()
                .latestVersion().list();
        return list;
    }

    @Override
    public ProcessDefinitionQuery getProcessDesployedLatestVersion() {
        if (engine.getProcessEngine() == null) {
            return null;
        }
        return engine.getProcessEngine().getRepositoryService().createProcessDefinitionQuery()
                .latestVersion();
    }

    @Override
    public List<ProcessDefinition> getAllProcessDesployedList() {
        List<ProcessDefinition> list;
        if (engine.getProcessEngine() == null) {
            list = new ArrayList<>();
            return list;
        }
        return engine.getProcessEngine().getRepositoryService().createProcessDefinitionQuery().list();
    }

    @Override
    public long getTaskCounterByProcessKey(String proccessDefKey) {
        TaskService taskService = engine.getProcessEngine().getTaskService();
        return taskService.createTaskQuery().processDefinitionKey(proccessDefKey).count();
    }

    @Override
    public long getTaskCounterByUser(String asignee) {
        TaskService taskService = engine.getProcessEngine().getTaskService();
        return taskService.createTaskQuery().taskAssignee(asignee).count();
    }

    @Override
    public boolean completeTask(String taskId, HashMap<String, Object> parameters) throws Exception {
        boolean flag;
        try {
            TaskService taskService = engine.getProcessEngine().getTaskService();
            taskService.complete(taskId, parameters);
            flag = true;
        } catch (Exception e) {
            flag = false;
            Logger.getLogger(BpmBaseEngine.class.getName()).log(Level.SEVERE, null, e);
        }
        return flag;
    }

    @Override
    public ProcessInstance startProcessByDefinitionKey(String processDefinitionKey, HashMap<String, Object> parameters) throws Exception {
        ProcessInstance p;
        try {
            RuntimeService runtimeService = engine.getProcessEngine().getRuntimeService();
            p = runtimeService.startProcessInstanceByKey(processDefinitionKey, parameters);
        } catch (Exception e) {
            p = null;
            Logger.getLogger(BpmBaseEngine.class.getName()).log(Level.SEVERE, null, e);
        }
        return p;
    }

    @Override
    public Object getvariable(String taskId, String varName) {
        Object o;
        try {
            Task t = engine.getProcessEngine().getTaskService().createTaskQuery().taskId(taskId).singleResult();
            o = engine.getProcessEngine().getRuntimeService().getVariable(t.getProcessInstanceId(), varName);
            if (o == null) {
                o = engine.getProcessEngine().getRuntimeService().getVariable(t.getExecutionId(), varName);
            }
        } catch (Exception e) {
            o = null;
            Logger.getLogger(BpmBaseEngine.class.getName()).log(Level.SEVERE, null, e);
        }
        return o;
    }

    @Override
    public Object getVariableByProcessInstance(String processInstanceId, String varName) {
        Object o;
        try {
            o = engine.getProcessEngine().getRuntimeService().getVariable(processInstanceId, varName);
        } catch (Exception e) {
            o = null;
            Logger.getLogger(BpmBaseEngine.class.getName()).log(Level.SEVERE, null, e);
        }
        return o;
    }

    @Override
    public HashMap getvariables(String instanceId) {
        HashMap<String, Object> o;
        try {
            o = (HashMap<String, Object>) engine.getProcessEngine().getRuntimeService().getVariables(instanceId);
        } catch (Exception e) {
            o = null;
            Logger.getLogger(BpmBaseEngine.class.getName()).log(Level.SEVERE, null, e);
        }
        return o;
    }

    @Override
    public boolean completeTask(String taskId) {
        boolean flag;
        try {
            TaskService taskService = engine.getProcessEngine().getTaskService();
            taskService.complete(taskId);
            flag = true;
        } catch (Exception e) {
            flag = false;
            Logger.getLogger(BpmBaseEngine.class.getName()).log(Level.SEVERE, null, e);
        }
        return flag;
    }

    @Override
    public void setTaskPriority(String taskId, int priority) {
        try {
            TaskService taskService = engine.getProcessEngine().getTaskService();
            taskService.setPriority(taskId, priority);
        } catch (Exception e) {
            Logger.getLogger(BpmBaseEngine.class.getName()).log(Level.SEVERE, null, e);
        }
    }

    @Override
    public List<Attachment> getAttachmentsFiles(String taskId) {
        return engine.getProcessEngine().getTaskService().getTaskAttachments(taskId);
    }

    @Override
    public Task getTaskDataByTaskID(String taskId) {
        TaskService taskService = engine.getProcessEngine().getTaskService();
        return taskService.createTaskQuery().taskId(taskId).singleResult();
    }

    @Override
    public ProcessDefinition getProcessDataByDefID(String defId) {
        return engine.getProcessEngine().getRepositoryService().createProcessDefinitionQuery().processDefinitionId(defId).singleResult();
    }

    @Override
    public List<Attachment> getProcessInstanceAttachmentsFiles(String instanceId) {
        return engine.getProcessEngine().getTaskService().getProcessInstanceAttachments(instanceId);
    }

    @Override
    public List<Attachment> getAttachmentsFilesByProcessInstanceIdMain(String processInstanceId) {
        List<Attachment> attachments = new ArrayList<>();
        String idProcessInstance = processInstanceId;
        List<HistoricProcessInstance> instance1;
        do {
            attachments.addAll(engine.getProcessEngine().getTaskService().getProcessInstanceAttachments(idProcessInstance));
            instance1 = engine.getProcessEngine().getHistoryService().createHistoricProcessInstanceQuery().superProcessInstanceId(idProcessInstance).list();
            if (instance1 != null && !instance1.isEmpty()) {
                for (int i = 0; i < instance1.size() - 1; i++) {
                    attachments.addAll(engine.getProcessEngine().getTaskService().getProcessInstanceAttachments(instance1.get(i).getId()));
                }
                idProcessInstance = instance1.get(instance1.size() - 1).getId();
            }
        } while (instance1 != null && !instance1.isEmpty());
        return attachments;
    }

    @Override
    public List<HistoricTaskInstance> getEndedUsertasksList(String asignee) {
        return engine.getProcessEngine().getHistoryService().createHistoricTaskInstanceQuery().taskAssignee(asignee).finished().orderByDeleteReason().desc().list();
    }

    @Override
    public boolean activateProcess(String processInstanceId) {
        boolean flag;
        try {
            RuntimeService runtimeService = engine.getProcessEngine().getRuntimeService();
            runtimeService.activateProcessInstanceById(processInstanceId);
            flag = true;
        } catch (Exception e) {
            flag = false;
            Logger.getLogger(BpmBaseEngine.class.getName()).log(Level.SEVERE, null, e);
        }
        return flag;
    }

    @Override
    public boolean suspendProcess(String processInstanceId) {
        boolean flag;
        try {
            RuntimeService runtimeService = engine.getProcessEngine().getRuntimeService();
            runtimeService.suspendProcessInstanceById(processInstanceId);
            flag = true;
        } catch (Exception e) {
            flag = false;
            Logger.getLogger(BpmBaseEngine.class.getName()).log(Level.SEVERE, null, e);
        }
        return flag;
    }

    @Override
    public Object getvariableByExecutionId(String taskId, String varName) {
        Task t = engine.getProcessEngine().getTaskService().createTaskQuery().taskId(taskId).singleResult();
        Object o = engine.getProcessEngine().getRuntimeService().getVariable(t.getProcessInstanceId(), varName);
        return o;
    }

    @Override
    public boolean setAssigneeTask(String taskId, String assignee) {
        boolean flag;
        try {
            engine.getProcessEngine().getTaskService().setAssignee(taskId, assignee);
            flag = true;
        } catch (Exception e) {
            flag = false;
            Logger.getLogger(BpmBaseEngine.class.getName()).log(Level.SEVERE, null, e);
        }
        return flag;
    }

    @Override
    public boolean setCandidateUser(String taskId, String candidate) {
        boolean flag;
        try {
            engine.getProcessEngine().getTaskService().addCandidateUser(taskId, candidate);
            flag = true;
        } catch (Exception e) {
            flag = false;
            Logger.getLogger(BpmBaseEngine.class.getName()).log(Level.SEVERE, null, e);
        }
        return flag;
    }

    @Override
    public boolean deleteCandidateUser(String taskId, String candidate) {
        boolean flag;
        try {
            engine.getProcessEngine().getTaskService().deleteCandidateUser(taskId, candidate);
            flag = true;
        } catch (Exception e) {
            flag = false;
            Logger.getLogger(BpmBaseEngine.class.getName()).log(Level.SEVERE, null, e);
        }
        return flag;
    }

    @Override
    public ArrayList<Task> getTaskGroup(String candidate) {
        ArrayList<Task> list = null;
        try {
            list = (ArrayList<Task>) engine.getProcessEngine().getTaskService().createTaskQuery().taskCandidateGroup(candidate).list();
        } catch (Exception e) {
            Logger.getLogger(BpmBaseEngine.class.getName()).log(Level.SEVERE, null, e);
        }
        return list;
    }

    @Override
    public void loadSingleProcessByClassPath(String path) {
        try {
            RepositoryService repositoryService = engine.getProcessEngine().getRepositoryService();
            repositoryService.createDeployment().addClasspathResource(path).deploy();
        } catch (Exception e) {
            Logger.getLogger(BpmBaseEngine.class.getName()).log(Level.SEVERE, null, e);
        }
    }

    @Override
    public ProcessDefinition getProcessDefinitionByKey(String key) {
        ProcessDefinition pd;
        try {
            pd = engine.getProcessEngine().getRepositoryService().createProcessDefinitionQuery().processDefinitionKey(key).latestVersion().singleResult();
        } catch (Exception e) {
            pd = null;
            Logger.getLogger(BpmBaseEngine.class.getName()).log(Level.SEVERE, null, e);
        }
        return pd;
    }

    @Override
    public List<HistoricProcessInstance> getProcessInstanceHistoric() {
        List<HistoricProcessInstance> hpi = null;
        try {
            hpi = engine.getProcessEngine().getHistoryService().createHistoricProcessInstanceQuery().orderByProcessInstanceStartTime().desc().list();
        } catch (Exception e) {
            Logger.getLogger(BpmBaseEngine.class.getName()).log(Level.SEVERE, null, e);
        }

        return hpi;
    }

    @Override
    public List<HistoricProcessInstance> getProcessInstanceHistoric(boolean state) {
        List<HistoricProcessInstance> hpi = null;
        try {
            if (state) {
                hpi = engine.getProcessEngine().getHistoryService().createHistoricProcessInstanceQuery().finished().orderByProcessInstanceStartTime().desc().list();
            } else {
                hpi = engine.getProcessEngine().getHistoryService().createHistoricProcessInstanceQuery().unfinished().orderByProcessInstanceStartTime().desc().list();
            }
        } catch (Exception e) {
            Logger.getLogger(BpmBaseEngine.class.getName()).log(Level.SEVERE, null, e);
        }

        return hpi;
    }

    @Override
    public List<HistoricProcessInstance> getProcessInstanceHistoric(int init, int max, boolean state) {
        List<HistoricProcessInstance> hpi = null;
        try {
            if (state) {
                hpi = engine.getProcessEngine().getHistoryService().createHistoricProcessInstanceQuery().finished().orderByProcessInstanceStartTime().desc().listPage(init, max);
            } else {
                hpi = engine.getProcessEngine().getHistoryService().createHistoricProcessInstanceQuery().unfinished().orderByProcessInstanceStartTime().desc().listPage(init, max);
            }
        } catch (Exception e) {
            Logger.getLogger(BpmBaseEngine.class.getName()).log(Level.SEVERE, null, e);
        }

        return hpi;
    }

    @Override
    public List<HistoricProcessInstance> getProcessInstanceHistoricByKey(String key) {
        List<HistoricProcessInstance> hpi = null;
        try {
            hpi = engine.getProcessEngine().getHistoryService().createHistoricProcessInstanceQuery().processDefinitionKey(key).orderByProcessInstanceStartTime().desc().list();
        } catch (Exception e) {
            Logger.getLogger(BpmBaseEngine.class.getName()).log(Level.SEVERE, null, e);
        }
        return hpi;
    }

    @Override
    public ProcessInstance getProcessInstanceById(String processInstanceId) {
        try {
            return engine.getProcessEngine().getRuntimeService().createProcessInstanceQuery().processInstanceId(processInstanceId).singleResult();
        } catch (Exception e) {
            Logger.getLogger(BpmBaseEngine.class.getName()).log(Level.SEVERE, null, e);
        }
        return null;
    }

    @Override
    public ProcessInstance getProcessInstanceByDefId(String processInstanceDefId) {
        try {
            return engine.getProcessEngine().getRuntimeService().createProcessInstanceQuery().processDefinitionId(processInstanceDefId).singleResult();
        } catch (Exception e) {
            Logger.getLogger(BpmBaseEngine.class.getName()).log(Level.SEVERE, null, e);
        }
        return null;
    }

    @Override
    public List<HistoricTaskInstance> getTaskByProcessInstanceId(String processInstanceId) {
        try {
            List<HistoricTaskInstance> allTask = engine.getProcessEngine().getHistoryService().createHistoricTaskInstanceQuery().processInstanceId(processInstanceId).orderByHistoricTaskInstanceEndTime().desc().orderByTaskCreateTime().desc().list();
            ProcessInstance pi = this.getProcessInstanceById(processInstanceId);
            List<HistoricProcessInstance> lista = engine.getProcessEngine().getHistoryService().createHistoricProcessInstanceQuery().superProcessInstanceId(processInstanceId).list();
            if (lista != null) {
                if (allTask == null) {
                    allTask = new ArrayList();
                }
                for (HistoricProcessInstance hpi : lista) {
                    List<HistoricTaskInstance> subList = engine.getProcessEngine().getHistoryService().createHistoricTaskInstanceQuery().processInstanceId(hpi.getId()).orderByHistoricTaskInstanceEndTime().desc().orderByTaskCreateTime().desc().list();
                    allTask.addAll(subList);
                }
            }
            return allTask;
        } catch (Exception e) {
            Logger.getLogger(BpmBaseEngine.class.getName()).log(Level.SEVERE, null, e);
        }
        return null;
    }

    @Override
    public HistoricTaskInstance getLastTaskByProcessInstance(String processInstanceId) {
        try {
            return engine.getProcessEngine().getHistoryService().createHistoricTaskInstanceQuery().processInstanceId(processInstanceId).unfinished().singleResult();
        } catch (Exception e) {
            Logger.getLogger(BpmBaseEngine.class.getName()).log(Level.SEVERE, null, e);
        }
        return null;
    }

    @Override
    public List<HistoricTaskInstance> getTaskByProcessInstanceIdMain(String processInstanceId) {
        List<String> listaIdsProcessInstace = new ArrayList<>();
        String idProcessInstance = processInstanceId;
        List<HistoricProcessInstance> instance1;
        do {
            listaIdsProcessInstace.add(idProcessInstance);
            instance1 = engine.getProcessEngine().getHistoryService().createHistoricProcessInstanceQuery().superProcessInstanceId(idProcessInstance).list();
            if (instance1 != null && !instance1.isEmpty()) {
                for (int i = 0; i < instance1.size() - 1; i++) {
                    listaIdsProcessInstace.add(instance1.get(i).getId());
                }
                idProcessInstance = instance1.get(instance1.size() - 1).getId();
            }
        } while (instance1 != null && !instance1.isEmpty());

        String ids = "";
        for (String listaIdsProcessInstace1 : listaIdsProcessInstace) {
            ids = ids + "'" + listaIdsProcessInstace1 + "',";
        }
        ids = ids.substring(0, ids.length() - 1);
        String sql = "SELECT TH.* FROM act_hi_taskinst as TH WHERE TH.proc_inst_id_ IN (" + ids + ") ORDER BY TH.start_time_ DESC";
        return engine.getProcessEngine().getHistoryService().createNativeHistoricTaskInstanceQuery().sql(sql).list();
    }

    @Override
    public List<HistoricProcessInstance> getProcessInstanceHistoricById(String id) {
        List<HistoricProcessInstance> lhpi = null;
        try {
            lhpi = engine.getProcessEngine().getHistoryService().createHistoricProcessInstanceQuery().processDefinitionId(id).orderByProcessInstanceStartTime().desc().list();
        } catch (Exception e) {
            Logger.getLogger(BpmBaseEngine.class.getName()).log(Level.SEVERE, null, e);
        }
        return lhpi;
    }

    @Override
    public HistoricProcessInstance getHistoricProcessInstanceByInstanceID(String processInstanceId) {
        HistoricProcessInstance lhpi = null;
        try {
            lhpi = engine.getProcessEngine().getHistoryService().createHistoricProcessInstanceQuery().processInstanceId(processInstanceId).singleResult();
        } catch (Exception e) {
            Logger.getLogger(BpmBaseEngine.class.getName()).log(Level.SEVERE, null, e);
        }
        return lhpi;
    }

    @Override
    public void setVariableProcessInstance(String porcessInstanceId, String varName, Object value) {
        try {
            engine.getProcessEngine().getRuntimeService().setVariable(porcessInstanceId, varName, value);
        } catch (Exception e) {
            Logger.getLogger(BpmBaseEngine.class.getName()).log(Level.SEVERE, null, e);
        }
    }

    @Override
    public List<Task> getListTaskActiveByProcessInstance(String porcessInstanceId) {
        try {
            return engine.getProcessEngine().getTaskService().createTaskQuery().processInstanceId(porcessInstanceId).active().list();
        } catch (Exception e) {
            Logger.getLogger(BpmBaseEngine.class.getName()).log(Level.SEVERE, null, e);
        }
        return null;
    }

    @Override
    public List<IdentityLink> identityLinkPorTareaId(String idTarea) {
        return engine.getProcessEngine().getTaskService().getIdentityLinksForTask(idTarea);
    }

    @Override
    public List<String> getListProcessInstanceIdsByProcessInstanceIdMain(String processInstanceId) {
        List<String> listaIdsProcessInstace = new ArrayList<>();
        String idProcessInstance = processInstanceId;
        List<HistoricProcessInstance> instance1;
        do {
            listaIdsProcessInstace.add(idProcessInstance);
            instance1 = engine.getProcessEngine().getHistoryService().createHistoricProcessInstanceQuery().superProcessInstanceId(idProcessInstance).unfinished().list();
            if (instance1 != null && !instance1.isEmpty()) {
                for (int i = 0; i < instance1.size() - 1; i++) {
                    listaIdsProcessInstace.add(instance1.get(i).getId());
                }
                idProcessInstance = instance1.get(instance1.size() - 1).getId();
            }
        } while (instance1 != null && !instance1.isEmpty());
        return listaIdsProcessInstace;
    }

    @Override
    public Map getVar(String taskId) {
        Map<String, Object> o;
        try {
            o = engine.getProcessEngine().getRuntimeService().getVariables(taskId);
//            engine.getProcessEngine().close();
        } catch (Exception e) {
            o = null;
            Logger.getLogger(BpmBaseEngine.class.getName()).log(Level.SEVERE, null, e);
        }
        return o;
    }

    @Override
    public void deleteProcessInstance(String processInstance, String reason) {
        try {
            engine.getProcessEngine().getRuntimeService().deleteProcessInstance(processInstance, reason);
        } catch (Exception e) {
            Logger.getLogger(BpmBaseEngine.class.getName()).log(Level.SEVERE, null, e);
        }
    }

    @Override
    public List<HistoricIdentityLink> HistoricidentityLinkPorTareaId(String idTarea) {
        return engine.getProcessEngine().getHistoryService().getHistoricIdentityLinksForTask(idTarea);
    }

    @Override
    public Task getTaskDataByProcessID(String processId) {
        TaskService taskService = engine.getProcessEngine().getTaskService();
        return taskService.createTaskQuery().processInstanceId(processId).singleResult();
    }

    @Override
    public Object getvariableByExecutionId(String taskId, String executionId, String varName) {
        Task t = engine.getProcessEngine().getTaskService().createTaskQuery().taskId(taskId).singleResult();
        Object o = engine.getProcessEngine().getRuntimeService().getVariable(t.getExecutionId(), varName);
        return o;
    }

    @Override
    public List<HistoricProcessInstance> getProcessInstanceHistoric(String id, boolean state) {
        List<HistoricProcessInstance> hpi = null;
        try {
            if (state) {
                hpi = engine.getProcessEngine().getHistoryService().createHistoricProcessInstanceQuery().processInstanceId(id).finished().orderByProcessInstanceStartTime().desc().list();
            } else {
                hpi = engine.getProcessEngine().getHistoryService().createHistoricProcessInstanceQuery().processInstanceId(id).unfinished().orderByProcessInstanceStartTime().desc().list();
            }
        } catch (Exception e) {
            Logger.getLogger(BpmBaseEngine.class.getName()).log(Level.SEVERE, null, e);
        }

        return hpi;
    }

    @Override
    public ProcessDefinition getProcessDefinitionById(String id) {
        return engine.getProcessEngine().getRepositoryService().createProcessDefinitionQuery().processDefinitionId(id).singleResult();
    }

    @Override
    public Integer getNumberTasksUser(String asignee, String taskDefKey) {
        TaskService taskService = engine.getProcessEngine().getTaskService();
        Long cantidad = 0L;
        try {
            if (taskDefKey == null) {
                cantidad = taskService.createTaskQuery().taskCandidateOrAssigned(asignee).count();
            } else {
                cantidad = taskService.createTaskQuery().taskDefinitionKey(taskDefKey).taskCandidateOrAssigned(asignee).count();
            }
        } catch (Exception e) {
            Logger.getLogger(BpmBaseEngineEJb.class.getName()).log(Level.SEVERE, null, e);
        }
        return cantidad.intValue();
    }

    @Override
    public List<Task> getAllTasksUser(String asignee, int first, int pageSize) {
        TaskService taskService = engine.getProcessEngine().getTaskService();
        ArrayList<Task> taskAssigne = new ArrayList<>();
        try {
            taskAssigne = (ArrayList<Task>) taskService.createTaskQuery().taskCandidateOrAssigned(asignee).orderByTaskPriority().desc().orderByTaskCreateTime().asc().listPage(first, pageSize);
        } catch (Exception e) {
            Logger.getLogger(BpmBaseEngineEJb.class.getName()).log(Level.SEVERE, null, e);
        }
        return taskAssigne;
    }

    @Override
    public List<Task> getTasksUserByTaskDefKey(String asignee, int first, int pageSize, String taskDefKey) {
        TaskService taskService = engine.getProcessEngine().getTaskService();
        ArrayList<Task> taskAssigne = new ArrayList<>();
        try {
            taskAssigne = (ArrayList<Task>) taskService.createTaskQuery().taskDefinitionKey(taskDefKey)
                    .taskCandidateOrAssigned(asignee).orderByTaskPriority().desc().orderByTaskCreateTime().asc().listPage(first, pageSize);
        } catch (Exception e) {
            Logger.getLogger(BpmBaseEngineEJb.class.getName()).log(Level.SEVERE, null, e);
        }
        return taskAssigne;
    }

    @Override
    public List<Task> getAllTasksUser(String asignee, int limite) {
        TaskService taskService = engine.getProcessEngine().getTaskService();
        List<Task> taskAssigne = new ArrayList<>();
        try {
            taskAssigne = taskService.createNativeTaskQuery().sql("select rt.* from act_ru_task rt "
                    + "left join historico_tramites_view ht on ht.id_proceso = rt.proc_inst_id_ "
                    + "where rt.assignee_ = '" + asignee + "' and ht.blocked is false order by ht.fecha_entrega").list();
            Collection<Task> tks = taskService.createNativeTaskQuery().sql("select rt.* "
                    + "from act_ru_task rt left join historico_tramites_view ht on ht.id_proceso = rt.proc_inst_id_ "
                    + "where rt.assignee_ = '" + asignee + "' and ht.blocked is true order by ht.fecha_entrega limit " + limite).list();
            taskAssigne.addAll(tks);
        } catch (Exception e) {
            Logger.getLogger(BpmBaseEngineEJb.class.getName()).log(Level.SEVERE, null, e);
        }
        return taskAssigne;
    }

    @Override
    public List<Task> getTasksUserByNameTask(String asignee, int limite, String taskDefKey) {
        TaskService taskService = engine.getProcessEngine().getTaskService();
        List<Task> taskAssigne = new ArrayList<>();
        try {
            taskAssigne = taskService.createNativeTaskQuery().sql("select rt.* from act_ru_task rt "
                    + "left join historico_tramites_view ht on ht.id_proceso = rt.proc_inst_id_ "
                    + "where rt.task_def_key_ = '" + taskDefKey + "' and rt.assignee_ = '" + asignee
                    + "' and ht.blocked is false order by ht.fecha_entrega").list();
            Collection<Task> tks = taskService.createNativeTaskQuery().sql("select rt.* "
                    + "from act_ru_task rt left join historico_tramites_view ht on ht.id_proceso = rt.proc_inst_id_ "
                    + "where rt.task_def_key_ = '" + taskDefKey + "' and rt.assignee_ = '" + asignee
                    + "' and ht.blocked is true order by ht.fecha_entrega limit " + limite).list();
            taskAssigne.addAll(tks);
        } catch (Exception e) {
            Logger.getLogger(BpmBaseEngineEJb.class.getName()).log(Level.SEVERE, null, e);
        }
        return taskAssigne;
    }

    @Override
    public List<Task> getTasksUserProcessId(String asignee, String processID) {
        TaskService taskService = engine.getProcessEngine().getTaskService();
        ArrayList<Task> taskAssigne = new ArrayList<>();
        try {
            taskAssigne = (ArrayList<Task>) taskService.createTaskQuery().taskCandidateOrAssigned(asignee).processInstanceId(processID).list();
        } catch (Exception e) {
            Logger.getLogger(BpmBaseEngineEJb.class.getName()).log(Level.SEVERE, null, e);
        }
        return taskAssigne;
    }

    @Override
    public InputStream getProcessDiagram(String definitionID, String diagramName) {
        return engine.getProcessEngine().getRepositoryService().getResourceAsStream(definitionID, diagramName);
    }

    @Override
    public InputStream getProcessInstanceDiagram(String procInstId) {
        ProcessInstance instance = this.getProcessInstanceById(procInstId);
        if (instance != null) {
            BpmnModel bpmnModel = this.getProcessEngine().getRepositoryService().getBpmnModel(instance.getProcessDefinitionId());
            List<String> activeActivityIds = this.getProcessEngine().getRuntimeService().getActiveActivityIds(procInstId);
            ProcessDiagramGenerator diagramGenerator = this.getProcessEngine().getProcessEngineConfiguration().getProcessDiagramGenerator();
            return diagramGenerator.generateDiagram(bpmnModel, "png", activeActivityIds);
        } else {
            HistoricProcessInstance historicInstance = engine.getProcessEngine().getHistoryService().createHistoricProcessInstanceQuery().processInstanceId(procInstId).singleResult();
            BpmnModel bpmnModel = this.getProcessEngine().getRepositoryService().getBpmnModel(historicInstance.getProcessDefinitionId());
            List<String> activeActivityIds = new ArrayList<String>();
            List<HistoricActivityInstance> list = this.getProcessEngine().getHistoryService().createNativeHistoricActivityInstanceQuery()
                    .sql("SELECT * FROM " + this.getProcessEngine().getManagementService().getTableName(HistoricActivityInstance.class) + " x WHERE x.proc_inst_id_= #{procInstId} ")
                    .parameter("procInstId", procInstId).list();
            if (list != null) {
                list.forEach((hai) -> {
                    activeActivityIds.add(hai.getActivityId());
                });
            }
            ProcessDiagramGenerator diagramGenerator = this.getProcessEngine().getProcessEngineConfiguration().getProcessDiagramGenerator();
            return diagramGenerator.generateDiagram(bpmnModel, "png", activeActivityIds);
        }
    }

    @Override
    public List<Task> allTaskDataByProcessID(String processId) {
        TaskService taskService = engine.getProcessEngine().getTaskService();
        ArrayList<Task> taskAssigne = new ArrayList<>();
        try {
            taskAssigne = (ArrayList<Task>) taskService.createTaskQuery().processInstanceId(processId).orderByTaskPriority().desc().orderByTaskCreateTime().asc().active().list();
        } catch (Exception e) {
            Logger.getLogger(BpmBaseEngineEJb.class.getName()).log(Level.SEVERE, null, e);
        }
        return taskAssigne;
    }

}
