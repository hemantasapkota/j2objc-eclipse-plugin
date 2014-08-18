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

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.StringWriter;
import java.net.URL;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.transform.OutputKeys;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerConfigurationException;
import javax.xml.transform.TransformerException;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;
import javax.xml.xpath.XPath;
import javax.xml.xpath.XPathConstants;
import javax.xml.xpath.XPathExpression;
import javax.xml.xpath.XPathExpressionException;
import javax.xml.xpath.XPathFactory;

import org.apache.commons.lang.StringUtils;
import org.apache.tools.ant.BuildEvent;
import org.apache.tools.ant.DefaultLogger;
import org.apache.tools.ant.Project;
import org.apache.tools.ant.ProjectHelper;
import org.apache.tools.ant.Target;
import org.eclipse.core.resources.IFile;
import org.eclipse.core.runtime.CoreException;
import org.eclipse.jdt.core.IJavaProject;
import org.eclipse.swt.SWT;
import org.eclipse.swt.widgets.Display;
import org.eclipse.ui.console.MessageConsole;
import org.eclipse.ui.console.MessageConsoleStream;
import org.w3c.dom.Document;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xml.sax.SAXException;

import com.laex.j2objc.util.LogUtil;
import com.laex.j2objc.util.MessageUtil;
import com.laex.j2objc.util.PropertiesUtil;

/**
 * The Class AntDelegate.
 */
public class AntDelegate {

    /** The java project. */
    private IJavaProject javaProject;

    /** The source dir. */
    private String sourceDir;

    /** The destination dir. */
    private String destinationDir;

    /** The exclude files. */
    private String excludeFiles;

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
         * @param display
         *            the display
         * @param msgConsole
         *            the msg console
         */
        public EclipeConsoleBuildLogger(Display display, MessageConsole msgConsole) {
            super();

            setMessageOutputLevel(Project.MSG_ERR);
            setErrorPrintStream(System.err);
            setOutputPrintStream(System.out);

            this.msgConsole = msgConsole;
            msgConsoleStream = this.msgConsole.newMessageStream();
            msgConsoleStream.setActivateOnWrite(true);

            MessageUtil.setConsoleColor(display, msgConsoleStream, SWT.COLOR_BLUE);
        }

        /*
         * (non-Javadoc)
         * 
         * @see
         * org.apache.tools.ant.DefaultLogger#targetStarted(org.apache.tools
         * .ant.BuildEvent)
         */
        @Override
        public void targetStarted(BuildEvent event) {
            super.targetStarted(event);
            try {
                Target target = event.getTarget();

                if (isExportTarget(target)) {
                    msgConsoleStream.write("Exporting ObjectiveC Files");
                    msgConsoleStream.write(MessageUtil.NEW_LINE_CONSTANT);
                    msgConsoleStream.write("Source Directory: " + sourceDir);
                    msgConsoleStream.write(MessageUtil.NEW_LINE_CONSTANT);
                    msgConsoleStream.write("Destination Directory: " + destinationDir);
                    msgConsoleStream.write(MessageUtil.NEW_LINE_CONSTANT);
                }

                if (isTargetCleanup(target)) {
                    msgConsoleStream.write("Cleans up internally generated files (<<project_name>>-classpath and <<project_name>>-prefix).");
                    msgConsoleStream.write(MessageUtil.NEW_LINE_CONSTANT);
                    msgConsoleStream.write("Does not clean J2OBJC generated source files.");
                    msgConsoleStream.write(MessageUtil.NEW_LINE_CONSTANT);
                    msgConsoleStream.write("Cleaning up project: " + javaProject.getElementName());
                    msgConsoleStream.write(MessageUtil.NEW_LINE_CONSTANT);
                }

            } catch (IOException e) {
                LogUtil.logException(e);
            }
        }

        /**
         * Checks if is target cleanup.
         * 
         * @param target
         *            the target
         * @return true, if is target cleanup
         */
        private boolean isTargetCleanup(Target target) {
            return target.getName().equals("CLEANUP");
        }

        /**
         * Checks if is export target.
         * 
         * @param target
         *            the target
         * @return true, if is export target
         */
        private boolean isExportTarget(Target target) {
            return target.getName().equals("Export-ObjectiveC-Files");
        }

        /*
         * (non-Javadoc)
         * 
         * @see
         * org.apache.tools.ant.DefaultLogger#targetFinished(org.apache.tools
         * .ant.BuildEvent)
         */
        @Override
        public void targetFinished(BuildEvent event) {
            super.targetFinished(event);
            try {
                Target target = event.getTarget();
                if (isExportTarget(target)) {
                    msgConsoleStream.write("Export finished.");
                    msgConsoleStream.write(MessageUtil.NEW_LINE_CONSTANT);
                }
                if (isTargetCleanup(target)) {
                    msgConsoleStream.write("Cleanup finished");
                    msgConsoleStream.write(MessageUtil.NEW_LINE_CONSTANT);
                }
            } catch (IOException e) {
                LogUtil.logException(e);
            }
        }

    }

    /**
     * Instantiates a new ant delegate.
     * 
     * @param javaProject
     *            the java project
     */
    public AntDelegate(IJavaProject javaProject) {
        this.javaProject = javaProject;
    }

    /**
     * Make exclude pattern.
     * 
     * @return the string
     * @throws CoreException
     *             the core exception
     */
    private String makeExcludePattern() throws CoreException {
        return new StringBuilder(PropertiesUtil.DEFAULT_EXCLUDE_PATTERN).append(" ").append(PropertiesUtil.getExcludePattern(javaProject)).toString();
    }

    /**
     * Append exclude pattern to xml.
     * 
     * @param path
     *            the path
     * @param pats
     *            the pats
     * @throws ParserConfigurationException
     *             the parser configuration exception
     * @throws SAXException
     *             the SAX exception
     * @throws IOException
     *             Signals that an I/O exception has occurred.
     * @throws XPathExpressionException
     *             the x path expression exception
     * @throws CoreException
     *             the core exception
     * @throws TransformerException
     *             the transformer exception
     */
    private void appendExcludePatternToXML(IFile path, String[] pats) throws ParserConfigurationException, SAXException, IOException,
            XPathExpressionException, CoreException, TransformerException {
        DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
        DocumentBuilder builder = factory.newDocumentBuilder();
        Document dom = builder.parse(path.getContents());

        XPathFactory xpathfactory = XPathFactory.newInstance();
        XPath xpath = xpathfactory.newXPath();
        XPathExpression expr = xpath.compile("project/target/move/fileset");

        Node node = (Node) expr.evaluate(dom, XPathConstants.NODE);
        NodeList children = node.getChildNodes();

        // don't why the last node in the xml should be indexed by length - 2
        Node excludeCopy = children.item(children.getLength() - 2).cloneNode(true);
        for (String pattern : pats) {
            if (StringUtils.isNotEmpty(pattern.trim())) {
                Node newnode = excludeCopy.cloneNode(true);
                newnode.getAttributes().getNamedItem("name").setNodeValue(pattern);
                node.appendChild(newnode);
            }
        }

        // Setup transformers
        TransformerFactory tf = TransformerFactory.newInstance();
        Transformer transformer = tf.newTransformer();
        transformer.setOutputProperty(OutputKeys.INDENT, "yes");
        StringWriter sw = new StringWriter();
        transformer.transform(new DOMSource(dom), new StreamResult(sw));
        String output = sw.getBuffer().toString();

        // save the ouput
        ByteArrayInputStream bis = new ByteArrayInputStream(output.getBytes("utf-8"));
        path.setContents(bis, 0, null);
    }

    /**
     * Execute export.
     * 
     * @param display
     *            the display
     * @param sourceDir
     *            the source dir
     * @param destinationDir
     *            the destination dir
     * @throws IOException
     *             Signals that an I/O exception has occurred.
     * @throws CoreException
     *             the core exception
     */
    public void executeExport(Display display, String sourceDir, String destinationDir) throws IOException, CoreException {
        // Resolve file from the plugin
        this.sourceDir = sourceDir;
        this.destinationDir = destinationDir;
        this.excludeFiles = makeExcludePattern();

        URL url = new URL("platform:/plugin/j2objc-eclipse-plugin/exportANT.xml");
        InputStream is = url.openConnection().getInputStream();

        IFile tmpFile = javaProject.getProject().getFile(".exportANT.xml");
        if (!tmpFile.exists()) {
            tmpFile.create(is, false, null);
        }

        // Append exclude patterns to the xml file by loading the dom and
        // manipulating nodes
        String[] pats = this.excludeFiles.split(" ");
        if (pats.length > 0) {
            try {
                appendExcludePatternToXML(tmpFile, pats);
            } catch (ParserConfigurationException e) {
                LogUtil.logException(e);
            } catch (SAXException e) {
                LogUtil.logException(e);
            } catch (XPathExpressionException e) {
                LogUtil.logException(e);
            } catch (TransformerConfigurationException e) {
                LogUtil.logException(e);
            } catch (TransformerException e) {
                LogUtil.logException(e);
            }
        }

        // Do the ANT stuff
        Project exportObjCFilesProject = new Project();

        String tmpAntFile = tmpFile.getLocation().makeAbsolute().toOSString();
        exportObjCFilesProject.setUserProperty("ant.file", tmpAntFile);
        exportObjCFilesProject.init();

        ProjectHelper helper = ProjectHelper.getProjectHelper();
        exportObjCFilesProject.addReference("ant.projectHelper", helper);

        exportObjCFilesProject.setProperty("EXPORT_DIRECTORY", destinationDir);
        exportObjCFilesProject.setProperty("SOURCE_DIRECTORY", sourceDir);

        MessageConsole console = MessageUtil.findConsole(MessageUtil.J2OBJC_CONSOLE);

        exportObjCFilesProject.addBuildListener(new EclipeConsoleBuildLogger(display, console));

        helper.parse(exportObjCFilesProject, tmpFile.getLocation().toFile());

        exportObjCFilesProject.executeTarget(exportObjCFilesProject.getDefaultTarget());

        tmpFile.delete(true, null);
    }

    /**
     * Execute cleanup.
     * 
     * @param display
     *            the display
     * @throws IOException
     *             Signals that an I/O exception has occurred.
     * @throws CoreException
     *             the core exception
     */
    public void executeCleanup(Display display) throws IOException, CoreException {
        URL url = new URL("platform:/plugin/j2objc-eclipse-plugin/exportANT.xml");
        InputStream is = url.openConnection().getInputStream();

        IFile tmpFile = javaProject.getProject().getFile(".exportANT.xml");

        if (tmpFile.exists()) {
            tmpFile.delete(true, null);
        }

        tmpFile.create(is, false, null);

        String tmpAntFile = tmpFile.getLocation().makeAbsolute().toOSString();

        Project cleanupProject = new Project();

        cleanupProject.setUserProperty("ant.file", tmpAntFile);
        cleanupProject.init();

        ProjectHelper helper = ProjectHelper.getProjectHelper();
        cleanupProject.addReference("ant.projectHelper", helper);

        cleanupProject.setProperty("PROJECT_NAME", javaProject.getElementName());

        MessageConsole console = MessageUtil.findConsole(MessageUtil.J2OBJC_CONSOLE);
        console.clearConsole();

        cleanupProject.addBuildListener(new EclipeConsoleBuildLogger(display, console));

        helper.parse(cleanupProject, tmpFile.getLocation().toFile());

        Target target = (Target) cleanupProject.getTargets().get("CLEANUP");
        cleanupProject.executeTarget(target.getName());

        tmpFile.delete(true, null);
    }
}
