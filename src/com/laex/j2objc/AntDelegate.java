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

import java.io.File;
import java.io.IOException;
import java.net.URL;

import org.apache.commons.io.FileUtils;
import org.apache.tools.ant.BuildEvent;
import org.apache.tools.ant.DefaultLogger;
import org.apache.tools.ant.Project;
import org.apache.tools.ant.ProjectHelper;
import org.eclipse.core.runtime.FileLocator;
import org.eclipse.ui.console.MessageConsole;
import org.eclipse.ui.console.MessageConsoleStream;

import com.laex.j2objc.util.LogUtil;
import com.laex.j2objc.util.MessageUtil;

/**
 * The Class AntDelegate.
 */
public class AntDelegate {

    /** The source dir. */
    private String sourceDir;
    
    /** The destination dir. */
    private String destinationDir;

    /**
     * The Class EclipeConsoleBuildLogger.
     */
    class EclipeConsoleBuildLogger extends DefaultLogger {

        /** The msg console. */
        private MessageConsole msgConsole;
        
        /** The msg console stream. */
        private MessageConsoleStream msgConsoleStream;

        /**
         * Instantiates a new eclipe console build logger.
         *
         * @param msgConsole the msg console
         */
        public EclipeConsoleBuildLogger(MessageConsole msgConsole) {
            super();

            setMessageOutputLevel(Project.MSG_ERR);
            setErrorPrintStream(System.err);
            setOutputPrintStream(System.out);

            this.msgConsole = msgConsole;
            msgConsoleStream = this.msgConsole.newMessageStream();
            msgConsoleStream.setActivateOnWrite(true);
        }

        /* (non-Javadoc)
         * @see org.apache.tools.ant.DefaultLogger#targetStarted(org.apache.tools.ant.BuildEvent)
         */
        @Override
        public void targetStarted(BuildEvent event) {
            super.targetStarted(event);
            try {
                msgConsoleStream.write("Exporting ObjectiveC Files");
                msgConsoleStream.write(MessageUtil.NEW_LINE_CONSTANT);
                msgConsoleStream.write("Source Directory: " + sourceDir);
                msgConsoleStream.write(MessageUtil.NEW_LINE_CONSTANT);
                msgConsoleStream.write("Destination Directory: " + destinationDir);
                msgConsoleStream.write(MessageUtil.NEW_LINE_CONSTANT);
            } catch (IOException e) {
                LogUtil.logException(e);
            }
        }

        /* (non-Javadoc)
         * @see org.apache.tools.ant.DefaultLogger#targetFinished(org.apache.tools.ant.BuildEvent)
         */
        @Override
        public void targetFinished(BuildEvent event) {
            super.targetFinished(event);
            try {
                msgConsoleStream.write("Export finished.");
                msgConsoleStream.write(MessageUtil.NEW_LINE_CONSTANT);
            } catch (IOException e) {
                LogUtil.logException(e);
            }
        }
    }

    /**
     * Instantiates a new ant delegate.
     *
     * @param sourceDir the source dir
     * @param destinationDir the destination dir
     */
    public AntDelegate(String sourceDir, String destinationDir) {
        this.sourceDir = sourceDir;
        this.destinationDir = destinationDir;
    }

    /**
     * Execute export.
     *
     * @throws IOException Signals that an I/O exception has occurred.
     */
    public void executeExport() throws IOException {
        // Resolve file from the plugin
        URL url = Activator.getDefault().getBundle().getEntry("exportANT.xml");
        url = FileLocator.resolve(url);
        File exportANTFile = FileUtils.toFile(url);

        Project exportObjCFilesProject = new Project();

        exportObjCFilesProject.setUserProperty("ant.file", exportANTFile.getAbsolutePath());
        exportObjCFilesProject.init();

        ProjectHelper helper = ProjectHelper.getProjectHelper();
        exportObjCFilesProject.addReference("ant.projectHelper", helper);

        exportObjCFilesProject.setProperty("EXPORT_DIRECTORY", destinationDir);
        exportObjCFilesProject.setProperty("SOURCE_DIRECTORY", sourceDir);

        MessageConsole console = MessageUtil.findConsole(MessageUtil.J2OBJC_CONSOLE);

        exportObjCFilesProject.addBuildListener(new EclipeConsoleBuildLogger(console));

        helper.parse(exportObjCFilesProject, exportANTFile);

        exportObjCFilesProject.executeTarget(exportObjCFilesProject.getDefaultTarget());
    }

}
