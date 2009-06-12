package org.jboss.tools.flow.common.wrapper;

/*
 * Copyright 2005 JBoss Inc
 * 
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 * 
 *      http://www.apache.org/licenses/LICENSE-2.0
 * 
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

/**
 * Event notifying a change in a model element.
 * 
 * @author <a href="mailto:kris_verlaenen@hotmail.com">Kris Verlaenen</a>
 */
public class ModelEvent {
	
	private int changeType;
	private Object changeDiscriminator;
	private Object changedObject;
	private Object oldValue;
	private Object newValue;
    
//    public ModelEvent(int changeType) {
//        this.changeType = changeType;
//    }
    
//    public ModelEvent(int changeType, Object changedObject) {
//    	this.changeType = changeType;
//    	this.changedObject = changedObject;
//    }
    
    public ModelEvent(int changeType, Object changeDiscriminator, Object changedObject, Object oldValue, Object newValue) {
    	this.changeType = changeType;
    	this.changeDiscriminator = changeDiscriminator;
    	this.changedObject = changedObject;
    	this.oldValue = oldValue;
    	this.newValue = newValue;
    }
    
    public int getChangeType() {
        return changeType;
    }
    
    public Object getChangeDiscriminator() {
    	return changeDiscriminator;
    }
    
    public Object getChangedObject() {
    	return changedObject;
    }
    
    public Object getOldValue() {
    	return oldValue;
    }
    
    public Object getNewValue() {
    	return newValue;
    }
    
}
