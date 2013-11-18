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

import java.io.IOException;
import java.util.Map;
import java.util.Properties;
import java.util.Scanner;

import org.eclipse.core.resources.IProject;
import org.eclipse.core.resources.IResource;
import org.eclipse.core.resources.IResourceVisitor;
import org.eclipse.core.runtime.CoreException;
import org.eclipse.core.runtime.IPath;
import org.eclipse.core.runtime.IProgressMonitor;
import org.eclipse.core.runtime.Path;
import org.eclipse.jdt.core.JavaCore;
import org.eclipse.swt.SWT;
import org.eclipse.swt.widgets.Display;
import org.eclipse.ui.console.MessageConsole;
import org.eclipse.ui.console.MessageConsoleStream;

import com.laex.j2objc.preferences.PreferenceConstants;
import com.laex.j2objc.util.LogUtil;
import com.laex.j2objc.util.MessageUtil;
import com.laex.j2objc.util.PropertiesUtil;

/**
 * The Class ToObjectiveCDelegate.
 */
public class ToObjectiveCDelegate implements IResourceVisitor {

    /** The prefs. */
    private Map<String, String> prefs;

    /** The monitor. */
    private IProgressMonitor monitor;

    /** The display. */
    private Display display;

    /**
     * Instantiates a new to objective c delegate.
     *
     * @param display the display
     * @param prefs the prefs
     * @param monitor the monitor
     */
    public ToObjectiveCDelegate(Display display, Map<String, String> prefs, IProgressMonitor monitor) {
        this.display = display;
        this.prefs = prefs;
        this.monitor = monitor;
    }

    /**
     * Builds the command.
     *
     * @param prefs the prefs
     * @param project the project
     * @param sourcePath the source path
     * @param outputPath the output path
     * @return the string
     * @throws CoreException the core exception
     * @throws IOException Signals that an I/O exception has occurred.
     */
    private String buildCommand(Map<String, String> prefs, IProject project, String sourcePath, String outputPath) throws CoreException, IOException {
        StringBuilder sb = new StringBuilder();

        // Create platform indenpendent path and append the path to the compiler
        IPath pathToCompiler = new Path(prefs.get(PreferenceConstants.PATH_TO_COMPILER)).append(PreferenceConstants.J2_OBJC_COMPILER);
        sb.append(pathToCompiler.toOSString()).append(" ");

        Properties classpathProps = PropertiesUtil.getClasspathEntries(project);
        if (!classpathProps.isEmpty()) {
            sb.append(PreferenceConstants.CLASSPAPTH).append(" ");
            for (Object key : classpathProps.keySet()) {
                sb.append(key).append(":");
            }

            sb.append(" ");
        }

        if (PropertiesUtil.hasProperty(PreferenceConstants.GENERATE_DEBUGGING_SUPPORT, prefs))
            sb.append(PreferenceConstants.GENERATE_DEBUGGING_SUPPORT).append(" ");

        if (PropertiesUtil.hasProperty(PreferenceConstants.NO_PACKAGE_DIRECTORIES, prefs))
            sb.append(PreferenceConstants.NO_PACKAGE_DIRECTORIES).append(" ");

        if (PropertiesUtil.hasProperty(PreferenceConstants.X_LANGUAGE_OBJECTIVE_C, prefs))
            sb.append(PreferenceConstants.X_LANGUAGE_OBJECTIVE_C).append(" ");

        if (PropertiesUtil.hasProperty(PreferenceConstants.X_LANGUAGE_OBJECTIVE_CPP, prefs))
            sb.append(PreferenceConstants.X_LANGUAGE_OBJECTIVE_CPP).append(" ");

        if (PropertiesUtil.hasProperty(PreferenceConstants.USE_REFERENCE_COUNTING, prefs))
            sb.append(PreferenceConstants.USE_REFERENCE_COUNTING).append(" ");

        if (PropertiesUtil.hasProperty(PreferenceConstants.USE_ARC, prefs))
            sb.append(PreferenceConstants.USE_ARC).append(" ");

        if (PropertiesUtil.hasProperty(PreferenceConstants.USE_GC, prefs))
            sb.append(PreferenceConstants.USE_GC).append(" ");

        if (PropertiesUtil.hasProperty(PreferenceConstants.ERROR_TO_WARNING, prefs))
            sb.append(PreferenceConstants.ERROR_TO_WARNING).append(" ");

        if (PropertiesUtil.hasProperty(PreferenceConstants.QUIET, prefs))
            sb.append(PreferenceConstants.QUIET).append(" ");

        if (PropertiesUtil.hasProperty(PreferenceConstants.VERBOSE, prefs))
            sb.append(PreferenceConstants.VERBOSE).append(" ");

        if (PropertiesUtil.hasProperty(PreferenceConstants.NO_INLINE_FIELD_ACCESS, prefs))
            sb.append(PreferenceConstants.NO_INLINE_FIELD_ACCESS).append(" ");

        if (PropertiesUtil.hasProperty(PreferenceConstants.NO_GENERATE_TEST_MAIN, prefs))
            sb.append(PreferenceConstants.NO_GENERATE_TEST_MAIN).append(" ");

        if (PropertiesUtil.hasProperty(PreferenceConstants.IGNORE_MISSING_IMPORTS, prefs))
            sb.append(PreferenceConstants.IGNORE_MISSING_IMPORTS).append(" ");

        if (PropertiesUtil.hasProperty(PreferenceConstants.PRINT_CONVERTED_SOURCES, prefs))
            sb.append(PreferenceConstants.PRINT_CONVERTED_SOURCES).append(" ");

        if (PropertiesUtil.hasProperty(PreferenceConstants.MEM_DEBUG, prefs))
            sb.append(PreferenceConstants.MEM_DEBUG).append(" ");

        if (PropertiesUtil.hasProperty(PreferenceConstants.GENERATE_NATIVE_STUBS, prefs))
            sb.append(PreferenceConstants.GENERATE_NATIVE_STUBS).append(" ");

        if (PropertiesUtil.hasProperty(PreferenceConstants.TIMING_INFO, prefs))
            sb.append(PreferenceConstants.TIMING_INFO).append(" ");

        if (PropertiesUtil.doesExistPrefixPropertiesFile(project))
            sb.append(PreferenceConstants.PREFIXES).append(" ").append(PropertiesUtil.getPrefixPropertiesFile(project)).append(" ");

        if (PropertiesUtil.hasTextProperty(PreferenceConstants.DEAD_CODE_REPORT, prefs))
            sb.append(PreferenceConstants.DEAD_CODE_REPORT).append(" ").append(prefs.get(PreferenceConstants.DEAD_CODE_REPORT)).append(" ");

        if (PropertiesUtil.hasTextProperty(PreferenceConstants.METHOD_MAPPING_FILE, prefs))
            sb.append(PreferenceConstants.METHOD_MAPPING_FILE).append(" ").append(prefs.get(PreferenceConstants.METHOD_MAPPING_FILE)).append(" ");

        if (PropertiesUtil.hasTextProperty(PreferenceConstants.BOOTCLASSPATH, prefs))
            sb.append(PreferenceConstants.BOOTCLASSPATH).append(":").append(prefs.get(PreferenceConstants.BOOTCLASSPATH)).append(" ");

        sb.append(PreferenceConstants.OUTPUT_DIR).append(" ").append(outputPath).append(" ");

        sb.append(sourcePath);

        return sb.toString();
    }

    /*
     * (non-Javadoc)
     * 
     * @see
     * org.eclipse.core.resources.IResourceVisitor#visit(org.eclipse.core.resources
     * .IResource)
     */
    @Override
    public boolean visit(final IResource resource) throws CoreException {
        // cancel the job
        if (monitor.isCanceled()) {
            onCancelled();
            monitor.done();
            resource.getProject().refreshLocal(IResource.DEPTH_INFINITE, monitor);
            return false;
        }

        if (!(resource.getType() == IResource.FILE)) {
            return true;
        }

        if (!JavaCore.isJavaLikeFileName(resource.getName())) {
            return true;
        }

        String sourcePath = resource.getLocation().makeAbsolute().toOSString();
        // As per the discussion with Tom Ball, the output of compilation is
        // stored in the project's root source folder
        // See
        // https://groups.google.com/forum/?fromgroups=#!topic/j2objc-discuss/lJGzN-pxmkQ
        String outputPath = resource.getProject().getFolder("src").getLocation().makeAbsolute().toOSString();

        try {
            String cmd = buildCommand(prefs, resource.getProject(), sourcePath, outputPath);

            monitor.subTask(resource.getName());

            Process p = Runtime.getRuntime().exec(cmd);

            Scanner scanInput = new Scanner(p.getInputStream());
            Scanner scanErr = new Scanner(p.getErrorStream());

            MessageConsole mc = MessageUtil.findConsole(MessageUtil.J2OBJC_CONSOLE);
            final MessageConsoleStream mst = mc.newMessageStream();

            mst.write(cmd);
            mst.write(MessageUtil.NEW_LINE_CONSTANT);

            while (scanInput.hasNext()) {
                MessageUtil.resetConsoleColor(display, mst);
                mst.write(scanInput.nextLine());
                mst.write(MessageUtil.NEW_LINE_CONSTANT);
            }

            while (scanErr.hasNext()) {
                MessageUtil.setConsoleColor(display, mst, SWT.COLOR_RED);
                mst.write(scanErr.nextLine());
                mst.write(MessageUtil.NEW_LINE_CONSTANT);
            }

            mst.write(MessageUtil.NEW_LINE_CONSTANT);

        } catch (IOException e) {
            LogUtil.logException(e);
        }

        monitor.worked(1);

        return true;
    }

    private void onCancelled() {
        MessageConsole mc = MessageUtil.findConsole(MessageUtil.J2OBJC_CONSOLE);
        MessageConsoleStream mct = mc.newMessageStream();
        MessageUtil.setConsoleColor(display, mct, SWT.COLOR_RED);

        try {
            mct.write("J2OBJC compilation cancelled!!");
        } catch (IOException e) {
            LogUtil.logException(e);
        }
    }
}