/*
 * JBoss, Home of Professional Open Source
 * Copyright 2005, JBoss Inc., and individual contributors as indicated
 * by the @authors tag. See the copyright.txt in the distribution for a
 * full listing of individual contributors.
 *
 * This is free software; you can redistribute it and/or modify it
 * under the terms of the GNU Lesser General Public License as
 * published by the Free Software Foundation; either version 2.1 of
 * the License, or (at your option) any later version.
 *
 * This software is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the GNU
 * Lesser General Public License for more details.
 *
 * You should have received a copy of the GNU Lesser General Public
 * License along with this software; if not, write to the Free
 * Software Foundation, Inc., 51 Franklin St, Fifth Floor, Boston, MA
 * 02110-1301 USA, or see the FSF site: http://www.fsf.org.
 */
package org.jbpm.gd.jpdl.command;

import org.eclipse.gef.commands.Command;
import org.jbpm.gd.common.model.SemanticElementFactory;
import org.jbpm.gd.jpdl.model.Action;
import org.jbpm.gd.jpdl.model.Timer;
import org.jbpm.gd.jpdl.model.TimerContainer;

public class TimerCreateCommand extends Command {
	
	private TimerContainer timerContainer;
	private Timer timer;
	private SemanticElementFactory factory;
	
	public TimerCreateCommand(SemanticElementFactory factory) {
		this.factory = factory;
	}
	
	public void execute() {
		if (timer == null) {
			timer = (Timer)factory.createById("org.jbpm.gd.jpdl.timer");
			timer.setAction((Action)factory.createById("org.jbpm.gd.jpdl.action"));
		}
		timerContainer.addTimer(timer);
	}
	
	public void undo() {
		timerContainer.removeTimer(timer);
	}
	
	public void setTimerContainer(TimerContainer timerContainer) {
		this.timerContainer = timerContainer;
	}
	
}
