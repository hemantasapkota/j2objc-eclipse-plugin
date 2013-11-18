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

import j2objc_eclipse_plugin.Activator;

import java.beans.DesignMode;
import java.io.IOException;
import java.util.Map;

import org.apache.commons.lang.StringUtils;
import org.eclipse.core.resources.IResource;
import org.eclipse.core.resources.IResourceVisitor;
import org.eclipse.core.runtime.CoreException;
import org.eclipse.core.runtime.IProgressMonitor;
import org.eclipse.core.runtime.IStatus;
import org.eclipse.core.runtime.Status;
import org.eclipse.core.runtime.jobs.Job;
import org.eclipse.jdt.core.IJavaElement;
import org.eclipse.jdt.core.IJavaProject;
import org.eclipse.jdt.core.JavaCore;
import org.eclipse.jface.action.IAction;
import org.eclipse.jface.viewers.ISelection;
import org.eclipse.jface.viewers.IStructuredSelection;
import org.eclipse.swt.SWT;
import org.eclipse.swt.widgets.Display;
import org.eclipse.ui.IObjectActionDelegate;
import org.eclipse.ui.IWorkbenchPage;
import org.eclipse.ui.IWorkbenchPart;
import org.eclipse.ui.PartInitException;
import org.eclipse.ui.console.IConsoleConstants;
import org.eclipse.ui.console.IConsoleView;
import org.eclipse.ui.console.MessageConsole;
import org.eclipse.ui.console.MessageConsoleStream;

import com.laex.j2objc.preferences.PreferenceConstants;
import com.laex.j2objc.util.LogUtil;
import com.laex.j2objc.util.MessageUtil;
import com.laex.j2objc.util.PropertiesUtil;

/**
 * The Class ToObjectiveCAction.
 */
public class ToObjectiveCAction implements IObjectActionDelegate {

    /** The target part. */
    IWorkbenchPart targetPart = null;

    /** The struc selc. */
    IStructuredSelection strucSelc = null;

    private int totalWork = 0;

    /**
     * Instantiates a new to objective c action.
     */
    public ToObjectiveCAction() {
    }

    /*
     * (non-Javadoc)
     * 
     * @see org.eclipse.ui.IActionDelegate#run(org.eclipse.jface.action.IAction)
     */
    @Override
    public void run(IAction action) {
        if (!action.isEnabled()) {
            return;
        }

        /* Show the console view */
        final MessageConsole console = MessageUtil.findConsole(MessageUtil.J2OBJC_CONSOLE);

        IWorkbenchPage page = targetPart.getSite().getWorkbenchWindow().getActivePage();
        String id = IConsoleConstants.ID_CONSOLE_VIEW;
        IConsoleView view;
        try {
            view = (IConsoleView) page.showView(id);
            view.display(console);
        } catch (PartInitException e1) {
            LogUtil.logException(e1);
        }

        /* Before starting the job, check if the path to J2OBJC has been set up */
        final Display display = targetPart.getSite().getShell().getDisplay();
        final String pathToCompiler = Activator.getDefault().getPreferenceStore().getString(PreferenceConstants.PATH_TO_COMPILER);

        if (StringUtils.isEmpty(pathToCompiler)) {
            try {
                console.clearConsole();
                MessageConsoleStream mst = console.newMessageStream();
                MessageUtil.setConsoleColor(display, mst, SWT.COLOR_RED);
                mst.write("Path to compiler empty. Please set the path to J2OBJC compiler from global preferences.");
            } catch (IOException e) {
                LogUtil.logException(e);
            }
            return;
        }
        
        /*Before starting the job, calculate total no. of files to compile */
        calculateWork();

        // Start the job
        final Job job = new Job("J2OBJC Command Line Executor") {
            @Override
            protected IStatus run(IProgressMonitor monitor) {

                try {
                    monitor.beginTask("J2OBJC Compiliation", totalWork);

                    IJavaElement elm = (IJavaElement) strucSelc.getFirstElement();
                    IJavaProject javaProject = (IJavaProject) elm.getJavaProject();

                    Map<String, String> props = PropertiesUtil.getProjectProperties(javaProject.getResource());
                    props.put(PreferenceConstants.PATH_TO_COMPILER, pathToCompiler);

                    // some initial message plumbing
                    console.clearConsole();

                    ToObjectiveCDelegate delegate = new ToObjectiveCDelegate(display, props, monitor);
                    elm.getResource().accept(delegate);

                    // copy files to some external directory
                    monitor.subTask("Exporting Objective-C Classes");
                    String destinationDir = PropertiesUtil.getOutputDirectory(javaProject);

                    doOutput(console, display, javaProject, destinationDir);
                    monitor.worked(1);

                    // refresh
                    javaProject.getResource().refreshLocal(IResource.DEPTH_INFINITE, monitor);
                    monitor.worked(1);
                    monitor.done();

                } catch (CoreException ce) {
                    LogUtil.logException(ce);
                    return Status.CANCEL_STATUS;
                } catch (IOException e) {
                    LogUtil.logException(e);
                    return Status.CANCEL_STATUS;
                }

                return Status.OK_STATUS;
            }

            private void doOutput(final MessageConsole console, final Display display, IJavaProject javaProject, String destinationDir)
                    throws IOException, CoreException {
                if (StringUtils.isNotEmpty(destinationDir)) {
                    // Ant specific code
                    String sourceDir = javaProject.getResource().getLocation().makeAbsolute().toOSString();

                    AntDelegate antDelegate = new AntDelegate(javaProject);
                    antDelegate.executeExport(display, sourceDir, destinationDir);
                } else {
                    MessageConsoleStream mst = console.newMessageStream();
                    MessageUtil.setConsoleColor(display, mst, SWT.COLOR_BLUE);
                    mst.write("No Output directory specified. Files will not be exported.");
                }
            }
        };

        job.setUser(true);
        job.schedule();

    }

    /*
     * (non-Javadoc)
     * 
     * @see
     * org.eclipse.ui.IActionDelegate#selectionChanged(org.eclipse.jface.action
     * .IAction, org.eclipse.jface.viewers.ISelection)
     */
    @Override
    public void selectionChanged(IAction action, ISelection selection) {
        this.strucSelc = (IStructuredSelection) selection;
    }

    /*
     * (non-Javadoc)
     * 
     * @see
     * org.eclipse.ui.IObjectActionDelegate#setActivePart(org.eclipse.jface.
     * action.IAction, org.eclipse.ui.IWorkbenchPart)
     */
    @Override
    public void setActivePart(IAction action, IWorkbenchPart targetPart) {
        this.targetPart = targetPart;
    }

    private void calculateWork()
    {
        IJavaElement elm = (IJavaElement) strucSelc.getFirstElement();

        try {
            elm.getResource().accept(new IResourceVisitor() {
                @Override
                public boolean visit(IResource resource) throws CoreException {

                    if (!(resource.getType() == IResource.FILE)) {
                        return true;
                    }

                    if (!JavaCore.isJavaLikeFileName(resource.getName())) {
                        return true;
                    }

                    totalWork++;

                    return true;
                }
            });
        } catch (CoreException e) {
            LogUtil.logException(e);
        }
    }

}
