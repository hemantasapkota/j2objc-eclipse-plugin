/*
 * Copyright (c) 2012, 2013 Hemanta Sapkota.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 * Hemanta Sapkota (laex.pearl@gmail.com)
 */
package com.laex.j2objc;

import j2objc_plugin_eclipse.Activator;

import java.util.Map;

import org.eclipse.core.resources.IResource;
import org.eclipse.core.runtime.CoreException;
import org.eclipse.core.runtime.IProgressMonitor;
import org.eclipse.core.runtime.IStatus;
import org.eclipse.core.runtime.Status;
import org.eclipse.core.runtime.jobs.Job;
import org.eclipse.jdt.core.IJavaElement;
import org.eclipse.jdt.core.IJavaProject;
import org.eclipse.jface.action.IAction;
import org.eclipse.jface.viewers.ISelection;
import org.eclipse.jface.viewers.IStructuredSelection;
import org.eclipse.ui.IObjectActionDelegate;
import org.eclipse.ui.IWorkbenchPart;

import com.laex.j2objc.preferences.PreferenceConstants;
import com.laex.j2objc.util.LogUtil;
import com.laex.j2objc.util.PropertiesUtil;

/**
 * The Class ToObjectiveCAction.
 */
public class ToObjectiveCAction implements IObjectActionDelegate {

    /** The target part. */
    IWorkbenchPart targetPart = null;
    
    /** The struc selc. */
    IStructuredSelection strucSelc = null;

    /**
     * Instantiates a new to objective c action.
     */
    public ToObjectiveCAction() {
    }

    /* (non-Javadoc)
     * @see org.eclipse.ui.IActionDelegate#run(org.eclipse.jface.action.IAction)
     */
    @Override
    public void run(IAction action) {
        if (!action.isEnabled()) {
            return;
        }

        //
        final Job job = new Job("J2OBJC Command Line Executor") {
            @Override
            protected IStatus run(IProgressMonitor monitor) {

                try {
                    monitor.beginTask("J2OBJC Compiliation", 1);

                    IJavaElement elm = (IJavaElement) strucSelc.getFirstElement();
                    IJavaProject javaProject = (IJavaProject) elm.getJavaProject();

                    Map<String, String> props = PropertiesUtil.getProjectProperties(javaProject.getResource());
                    props.put(PreferenceConstants.PATH_TO_COMPILER,
                            Activator.getDefault().getPreferenceStore().getString(PreferenceConstants.PATH_TO_COMPILER));

                    ToObjectiveCDelegate del = new ToObjectiveCDelegate(props, monitor);
                    elm.getResource().accept(del);

                    //refresh
                    javaProject.getResource().refreshLocal(IResource.DEPTH_INFINITE, monitor);
                    monitor.worked(1);
                    monitor.done();
                } catch (CoreException ce) {
                    LogUtil.logException(ce);
                    return Status.CANCEL_STATUS;
                }

                return Status.OK_STATUS;
            }
        };
        
        job.setUser(true);
        job.schedule();

    }

    /* (non-Javadoc)
     * @see org.eclipse.ui.IActionDelegate#selectionChanged(org.eclipse.jface.action.IAction, org.eclipse.jface.viewers.ISelection)
     */
    @Override
    public void selectionChanged(IAction action, ISelection selection) {
        this.strucSelc = (IStructuredSelection) selection;
    }

    /* (non-Javadoc)
     * @see org.eclipse.ui.IObjectActionDelegate#setActivePart(org.eclipse.jface.action.IAction, org.eclipse.ui.IWorkbenchPart)
     */
    @Override
    public void setActivePart(IAction action, IWorkbenchPart targetPart) {
        this.targetPart = targetPart;
    }

}
