package org.jboss.tools.flow.jpdl4.model;

import org.eclipse.ui.views.properties.ComboBoxPropertyDescriptor;
import org.eclipse.ui.views.properties.IPropertyDescriptor;
import org.eclipse.ui.views.properties.IPropertySource;
import org.eclipse.ui.views.properties.PropertyDescriptor;
import org.eclipse.ui.views.properties.TextPropertyDescriptor;

public class AssignmentPropertySource implements IPropertySource, Assignment {

	private String assignmentType = NONE;
	private String assignmentExpression = "";
	private String assignmentExpressionLanguage = "";
	
	public static Integer getAssignmentTypesIndex(String assignmentType) {
		Integer result = 0;
		for (String type : ASSIGNMENT_TYPES) {
			if (type.equals(assignmentType)) {
				break;
			}
			result++;
		}
		return result;
	}

	private IPropertyDescriptor[] propertyDescriptors;
	
	private void initializePropertyDescriptors() {
		PropertyDescriptor assignmentTypeDescriptor = 
			new ComboBoxPropertyDescriptor(ASSIGNMENT_TYPE, "Type", ASSIGNMENT_TYPES);
		assignmentTypeDescriptor.setCategory(ASSIGNMENT_LABEL);
		PropertyDescriptor assignmentExpressionDescriptor = 
			new TextPropertyDescriptor(ASSIGNMENT_EXPRESSION, "Expression");
		assignmentExpressionDescriptor.setCategory(ASSIGNMENT_LABEL);
		PropertyDescriptor assignmentExpressionLanguageDescriptor = 
			new TextPropertyDescriptor(ASSIGNMENT_EXPRESSION_LANGUAGE, "Language");
		assignmentExpressionLanguageDescriptor.setCategory(ASSIGNMENT_LABEL);
		propertyDescriptors = new IPropertyDescriptor[] {
				assignmentTypeDescriptor, 
				assignmentExpressionDescriptor, 
				assignmentExpressionLanguageDescriptor
		};
	}

	public Object getEditableValue() {
		return null;
	}

	public IPropertyDescriptor[] getPropertyDescriptors() {
		if (propertyDescriptors == null) {
			initializePropertyDescriptors();
		}
		return propertyDescriptors;
	}
	
	public Object getPropertyValue(Object id) {
		if (ASSIGNMENT_TYPE.equals(id)) {
			return getAssignmentTypesIndex(assignmentType);
		} else if (ASSIGNMENT_EXPRESSION.equals(id)) {
			return assignmentExpression;
		} else if (ASSIGNMENT_EXPRESSION_LANGUAGE.equals(id)) {
			return assignmentExpressionLanguage;
		}
		return null;
	}

	public boolean isPropertySet(Object id) {
		if (ASSIGNMENT_TYPE.equals(id)) {
			return true;
		} else if (ASSIGNMENT_EXPRESSION.equals(id)) {
			return !"".equals(assignmentExpression);
		} else if (ASSIGNMENT_EXPRESSION_LANGUAGE.equals(id)) {
			return !"".equals(assignmentExpressionLanguage);
		}
		return false;
	}

	public void resetPropertyValue(Object id) {
		if (ASSIGNMENT_TYPE.equals(id)) {
			assignmentType = NONE;
		} else if (ASSIGNMENT_EXPRESSION.equals(id)) {
			assignmentExpression = "";
		} else if (ASSIGNMENT_EXPRESSION_LANGUAGE.equals(id)) {
			assignmentExpressionLanguage = "";
		}
	}

	public void setPropertyValue(Object id, Object value) {
		if (ASSIGNMENT_TYPE.equals(id)) {
			assignmentType = ASSIGNMENT_TYPES[(Integer)value];
		} else if (ASSIGNMENT_EXPRESSION.equals(id)) {
			assignmentExpression = (String)value;
		} else if (ASSIGNMENT_EXPRESSION_LANGUAGE.equals(id)) {
			assignmentExpressionLanguage = (String)value;
		}
	}

}
