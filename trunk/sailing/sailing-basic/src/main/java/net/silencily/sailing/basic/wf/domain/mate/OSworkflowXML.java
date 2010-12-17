package net.silencily.sailing.basic.wf.domain.mate;

import java.io.FileOutputStream;
import java.io.IOException;
import java.util.List;

import org.jdom.Document;
import org.jdom.Element;
import org.jdom.JDOMException;
import org.jdom.output.Format;
import org.jdom.output.XMLOutputter;

public class OSworkflowXML {
	
	private static Element workflow;

	private static WorkFlowMeta workFlowMeta;

	public OSworkflowXML() {
		workFlowMeta = new WorkFlowMeta();		
	}

	public void BuildXMLDox() throws IOException, JDOMException {
		workflow = new Element("workflow");
		Document Doc = new Document(workflow);
		workflow = Doc.getRootElement();
		initialActions();
		globalActions();
		steps();

		XMLOutputter xmlout = new XMLOutputter(Format.getPrettyFormat());
		xmlout.output(Doc, new FileOutputStream("E:\\osworkflow.xml"));
	}

	public static void main(String[] args) throws Exception {
		OSworkflowXML oSworkflowXML = new OSworkflowXML();
		oSworkflowXML.BuildXMLDox();
	}
	
	public static void steps(){		
		List stepList;
		Element steps = new Element("steps");
		workflow.addContent(steps);
        
		if(workFlowMeta.getSteps() != null){
			stepList = workFlowMeta.getSteps();
			for(int i = 0; i < stepList.size(); i++){
				Step step = (Step) stepList.get(i);
				Element elementStep = new Element("step");
				steps.addContent(elementStep);
				Permission permission;
				if(step.getPermission() != null){					
					permission = step.getPermission();					
					String permissionName = permission.getName();
					Element externalpermissions = new Element("external-permissions");
					elementStep.addContent(externalpermissions);
					Element permissionElements = new Element("permission");
					permissionElements.setAttribute("name",permissionName);
					permissionElements.addContent(permissionElements);
				
				}
			}
		}
	 
	}


	//全局Action
	public static void globalActions() {
		//GlobalActions globalActions = new GlobalActions();
		//List globalActionslist = globalActions.getAction();
		System.out.println("------------globalActions-----------");
		
	}

	public static void initialActions() {

		//添加initialActions
		Element initialActions = new Element("initial-actions");
		workflow.addContent(initialActions);
		System.out.println("------------initialActions-------------");

		//添加action
		Element action = new Element("action");
		action.setAttribute("id", "100");
		action.setAttribute("name", "初始化工作流");
		initialActions.addContent(action);

		//添加pre-functions
		Element preFunctions = new Element("pre-functions");
		action.addContent(preFunctions);

		//添加function
		Element afunction = new Element("function");
		preFunctions.addContent(afunction);
		afunction.setAttribute("type", "class");

		//arg
		Element arg = new Element("arg");
		arg.setAttribute("name", "class.name");
		arg.setText("com.opensymphony.workflow.util.Caller");
		afunction.addContent(arg);

		//results
		Element results = new Element("results");
		action.addContent(results);

		//unconditional-result
		Element unconditionalResult = new Element("unconditional-result");
		unconditionalResult.setAttribute("old-status", "finish");
		unconditionalResult.setAttribute("status", "processing");

		unconditionalResult.setAttribute("owner", "${caller}");
		results.addContent(unconditionalResult);
		//InitialActions initialActions1 = workFlowMeta.getInitialActions();
		//String step = ((Result)(((Action)initialActions1.getAction()).getResults())).getStep();
		unconditionalResult.setAttribute("step", "10");
	}

}
