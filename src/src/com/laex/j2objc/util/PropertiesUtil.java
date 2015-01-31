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
package com.laex.j2objc.util;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import java.util.Properties;

import org.eclipse.core.resources.IFile;
import org.eclipse.core.resources.IProject;
import org.eclipse.core.resources.IResource;
import org.eclipse.core.runtime.CoreException;
import org.eclipse.core.runtime.Path;
import org.eclipse.core.runtime.QualifiedName;
import org.eclipse.jdt.core.IJavaProject;

import com.laex.j2objc.preferences.PreferenceConstants;

/**
 * The Class PropertiesUtil.
 */
public class PropertiesUtil {

    /** The Constant OUTPUT_DIRECTORY_KEY. */
    public static final QualifiedName OUTPUT_DIRECTORY_KEY = new QualifiedName("", "OUTPUT_DIRECTORY");
    
    /** The Constant EXCLUDE_FILES_KEY. */
    public static final QualifiedName EXCLUDE_FILES_KEY = new QualifiedName("", "EXCLUDE_FILES");

    /** The Constant DEFAULT_EXCLUDE_PATTERN. */
    public static final String DEFAULT_EXCLUDE_PATTERN = "bin/*";

    /**
     * Checks for property.
     *
     * @param key the key
     * @param prefs the prefs
     * @return true, if successful
     */
    public static boolean hasProperty(String key, Map<String, String> prefs) {
        if (Boolean.parseBoolean(prefs.get(key))) {
            return true;
        }
        return false;
    }

    /**
     * Checks for text property.
     *
     * @param key the key
     * @param prefs the prefs
     * @return true, if successful
     */
    public static boolean hasTextProperty(String key, Map<String, String> prefs) {
        String val = prefs.get(key);
        if (val != null && !val.isEmpty()) {
            return true;
        }
        return false;
    }

    /**
     * Checks if is default properties set.
     *
     * @param prj the prj
     * @return true, if is default properties set
     * @throws CoreException the core exception
     */
    public static boolean isDefaultPropertiesSet(IResource prj) throws CoreException {
        String firstTimer = prj.getPersistentProperty(new QualifiedName("", PreferenceConstants.GENERATE_DEBUGGING_SUPPORT));
        return !(firstTimer == null);
    }

    /**
     * Construct prefix properties file path.
     *
     * @param project the project
     * @return the string
     */
    public static String constructPrefixPropertiesFilePath(IResource project) {
        return new StringBuilder(".").append(project.getName()).append("-prefixes").toString();
    }

    /**
     * Gets the prefix properties file.
     *
     * @param prj the prj
     * @return the prefix properties file
     */
    public static final String getPrefixPropertiesFile(IProject prj) {
        String prefixFile = PropertiesUtil.constructPrefixPropertiesFilePath(prj);
        IFile file = prj.getFile(new Path(prefixFile));
        return file.getLocation().makeAbsolute().toOSString();
    }

    /**
     * Does exist prefix properties file.
     *
     * @param prj the prj
     * @return true, if successful
     */
    public static boolean doesExistPrefixPropertiesFile(IProject prj) {
        String prefixFile = PropertiesUtil.constructPrefixPropertiesFilePath(prj);
        IFile file = prj.getFile(new Path(prefixFile));
        return file.exists();
    }

    /**
     * Construct default preferences.
     *
     * @return the map
     */
    public static Map<String, String> constructDefaultPreferences() {
        Map<String, String> prefMap = new HashMap<String, String>();

        prefMap.put(PreferenceConstants.INITIALIZE_FIRST_TIME, Boolean.TRUE.toString());

        prefMap.put(PreferenceConstants.GENERATE_DEBUGGING_SUPPORT, Boolean.TRUE.toString());
        prefMap.put(PreferenceConstants.NO_PACKAGE_DIRECTORIES, Boolean.TRUE.toString());

        prefMap.put(PreferenceConstants.X_LANGUAGE_OBJECTIVE_C, Boolean.TRUE.toString());
        prefMap.put(PreferenceConstants.X_LANGUAGE_OBJECTIVE_CPP, Boolean.FALSE.toString());

        // Memory OPtions
        prefMap.put(PreferenceConstants.USE_REFERENCE_COUNTING, Boolean.FALSE.toString());
        prefMap.put(PreferenceConstants.USE_ARC, Boolean.TRUE.toString());

        prefMap.put(PreferenceConstants.ERROR_TO_WARNING, Boolean.FALSE.toString());
        prefMap.put(PreferenceConstants.QUIET, Boolean.FALSE.toString());
        prefMap.put(PreferenceConstants.VERBOSE, Boolean.TRUE.toString());
        prefMap.put(PreferenceConstants.TIMING_INFO, Boolean.FALSE.toString());

        /* changes from 0.8.7 */

        prefMap.put(PreferenceConstants.BUILD_CLOSURE, Boolean.FALSE.toString());
        prefMap.put(PreferenceConstants.GENERATE_DEPRECATED, Boolean.FALSE.toString());
        prefMap.put(PreferenceConstants.STRIP_REFLECTION, Boolean.FALSE.toString());
        prefMap.put(PreferenceConstants.STRIP_GWT_INCOMPATIBLE, Boolean.FALSE.toString());
        prefMap.put(PreferenceConstants.SEGMENTED_HEADERS, Boolean.FALSE.toString());
        
        /* changes from 0.9.6 */
        
        prefMap.put(PreferenceConstants.DOC_COMMENTS, Boolean.TRUE.toString());
        prefMap.put(PreferenceConstants.EXTRACT_UNSEQUENCED, Boolean.FALSE.toString());

        return prefMap;

    }

    /**
     * Gets the project properties.
     *
     * @param prj the prj
     * @return the project properties
     * @throws CoreException the core exception
     */
    public static Map<String, String> getProjectProperties(IResource prj) throws CoreException {

        // are we running the translation for the first time ?, if so return
        // default preferences
        String hasAnyPropertiesBeenSet = prj.getPersistentProperty(new QualifiedName("", PreferenceConstants.INITIALIZE_FIRST_TIME));
        if (hasAnyPropertiesBeenSet == null) {
            return constructDefaultPreferences();
        }

        String generateDebugSupport = prj.getPersistentProperty(qkey(PreferenceConstants.GENERATE_DEBUGGING_SUPPORT));
        String noPackageDirectories = prj.getPersistentProperty(qkey(PreferenceConstants.NO_PACKAGE_DIRECTORIES));
        String xLangugaeObjectiveC = prj.getPersistentProperty(qkey(PreferenceConstants.X_LANGUAGE_OBJECTIVE_C));
        String xLangugaeObjectiveCPP = prj.getPersistentProperty(qkey(PreferenceConstants.X_LANGUAGE_OBJECTIVE_CPP));

        String useRefCounting = prj.getPersistentProperty(qkey(PreferenceConstants.USE_REFERENCE_COUNTING));
        String useARC = prj.getPersistentProperty(qkey(PreferenceConstants.USE_ARC));

        String errorToWarning = prj.getPersistentProperty(qkey(PreferenceConstants.ERROR_TO_WARNING));
        String quiet = prj.getPersistentProperty(qkey(PreferenceConstants.QUIET));
        String verbose = prj.getPersistentProperty(qkey(PreferenceConstants.VERBOSE));
        String timingInfo = prj.getPersistentProperty(qkey(PreferenceConstants.TIMING_INFO));

        String docComments = prj.getPersistentProperty(qkey(PreferenceConstants.DOC_COMMENTS));
        String defaultPropertiesSetInfo = prj.getPersistentProperty(qkey(PreferenceConstants.INITIALIZE_FIRST_TIME));
        String extractUnsequenced = prj.getPersistentProperty(qkey(PreferenceConstants.EXTRACT_UNSEQUENCED));

        String deadCodeReportFile = prj.getPersistentProperty(qkey(PreferenceConstants.DEAD_CODE_REPORT));
        if (deadCodeReportFile == null)
            deadCodeReportFile = "";

        String methodMappingFile = prj.getPersistentProperty(qkey(PreferenceConstants.METHOD_MAPPING_FILE));
        if (methodMappingFile == null)
            methodMappingFile = "";

        String bootclasspath = prj.getPersistentProperty(qkey(PreferenceConstants.BOOTCLASSPATH));
        if (bootclasspath == null)
            bootclasspath = "";

        /* Changes from 0.8.7 */
        String buildClosure = prj.getPersistentProperty(qkey(PreferenceConstants.BUILD_CLOSURE));
        String generateDeprecated = prj.getPersistentProperty(qkey(PreferenceConstants.GENERATE_DEPRECATED));
        String stripReflection = prj.getPersistentProperty(qkey(PreferenceConstants.STRIP_REFLECTION));
        String stripGwtIncompatible = prj.getPersistentProperty(qkey(PreferenceConstants.STRIP_GWT_INCOMPATIBLE));
        String segmentedHeaders = prj.getPersistentProperty(qkey(PreferenceConstants.SEGMENTED_HEADERS));

        Map<String, String> prefs = new HashMap<String, String>();

        prefs.put(PreferenceConstants.GENERATE_DEBUGGING_SUPPORT, generateDebugSupport);
        prefs.put(PreferenceConstants.NO_PACKAGE_DIRECTORIES, noPackageDirectories);
        prefs.put(PreferenceConstants.X_LANGUAGE_OBJECTIVE_C, xLangugaeObjectiveC);
        prefs.put(PreferenceConstants.X_LANGUAGE_OBJECTIVE_CPP, xLangugaeObjectiveCPP);
        prefs.put(PreferenceConstants.USE_REFERENCE_COUNTING, useRefCounting);
        prefs.put(PreferenceConstants.USE_ARC, useARC);
        prefs.put(PreferenceConstants.ERROR_TO_WARNING, errorToWarning);
        prefs.put(PreferenceConstants.QUIET, quiet);
        prefs.put(PreferenceConstants.VERBOSE, verbose);
        prefs.put(PreferenceConstants.TIMING_INFO, timingInfo);
        prefs.put(PreferenceConstants.INITIALIZE_FIRST_TIME, defaultPropertiesSetInfo);

        prefs.put(PreferenceConstants.DEAD_CODE_REPORT, deadCodeReportFile);
        prefs.put(PreferenceConstants.METHOD_MAPPING_FILE, methodMappingFile);
        prefs.put(PreferenceConstants.BOOTCLASSPATH, bootclasspath);

        prefs.put(PreferenceConstants.DOC_COMMENTS, docComments);
        prefs.put(PreferenceConstants.EXTRACT_UNSEQUENCED, extractUnsequenced);

        prefs.put(PreferenceConstants.BUILD_CLOSURE, buildClosure);
        prefs.put(PreferenceConstants.GENERATE_DEPRECATED, generateDeprecated);
        prefs.put(PreferenceConstants.STRIP_REFLECTION, stripReflection);
        prefs.put(PreferenceConstants.STRIP_GWT_INCOMPATIBLE, stripGwtIncompatible);
        prefs.put(PreferenceConstants.SEGMENTED_HEADERS, segmentedHeaders);

        return prefs;
    }

    /**
     * Persist properties.
     *
     * @param prj the prj
     * @param prefs the prefs
     * @throws CoreException the core exception
     */
    public static void persistProperties(IResource prj, Map<String, String> prefs) throws CoreException {

        prj.setPersistentProperty(qkey(PreferenceConstants.INITIALIZE_FIRST_TIME), prefs.get(PreferenceConstants.INITIALIZE_FIRST_TIME));

        prj.setPersistentProperty(qkey(PreferenceConstants.GENERATE_DEBUGGING_SUPPORT), prefs.get(PreferenceConstants.GENERATE_DEBUGGING_SUPPORT));
        prj.setPersistentProperty(qkey(PreferenceConstants.NO_PACKAGE_DIRECTORIES), prefs.get(PreferenceConstants.NO_PACKAGE_DIRECTORIES));
        prj.setPersistentProperty(qkey(PreferenceConstants.X_LANGUAGE_OBJECTIVE_C), prefs.get(PreferenceConstants.X_LANGUAGE_OBJECTIVE_C));
        prj.setPersistentProperty(qkey(PreferenceConstants.X_LANGUAGE_OBJECTIVE_CPP), prefs.get(PreferenceConstants.X_LANGUAGE_OBJECTIVE_CPP));

        // Mem
        prj.setPersistentProperty(qkey(PreferenceConstants.USE_REFERENCE_COUNTING), prefs.get(PreferenceConstants.USE_REFERENCE_COUNTING));
        prj.setPersistentProperty(qkey(PreferenceConstants.USE_ARC), prefs.get(PreferenceConstants.USE_ARC));

        // Output
        prj.setPersistentProperty(qkey(PreferenceConstants.ERROR_TO_WARNING), prefs.get(PreferenceConstants.ERROR_TO_WARNING));
        prj.setPersistentProperty(qkey(PreferenceConstants.QUIET), prefs.get(PreferenceConstants.QUIET));
        prj.setPersistentProperty(qkey(PreferenceConstants.VERBOSE), prefs.get(PreferenceConstants.VERBOSE));
        prj.setPersistentProperty(qkey(PreferenceConstants.TIMING_INFO), prefs.get(PreferenceConstants.TIMING_INFO));

        prj.setPersistentProperty(qkey(PreferenceConstants.DEAD_CODE_REPORT), prefs.get(PreferenceConstants.DEAD_CODE_REPORT));
        prj.setPersistentProperty(qkey(PreferenceConstants.METHOD_MAPPING_FILE), prefs.get(PreferenceConstants.METHOD_MAPPING_FILE));
        prj.setPersistentProperty(qkey(PreferenceConstants.BOOTCLASSPATH), prefs.get(PreferenceConstants.BOOTCLASSPATH));

        /* Changes from 0.8.7 */
        prj.setPersistentProperty(qkey(PreferenceConstants.BUILD_CLOSURE), prefs.get(PreferenceConstants.BUILD_CLOSURE));
        prj.setPersistentProperty(qkey(PreferenceConstants.GENERATE_DEPRECATED), prefs.get(PreferenceConstants.GENERATE_DEPRECATED));
        prj.setPersistentProperty(qkey(PreferenceConstants.STRIP_REFLECTION), prefs.get(PreferenceConstants.STRIP_REFLECTION));
        prj.setPersistentProperty(qkey(PreferenceConstants.STRIP_GWT_INCOMPATIBLE), prefs.get(PreferenceConstants.STRIP_GWT_INCOMPATIBLE));
        prj.setPersistentProperty(qkey(PreferenceConstants.SEGMENTED_HEADERS), prefs.get(PreferenceConstants.SEGMENTED_HEADERS));

        /* Changes from 0.9.6 */
        prj.setPersistentProperty(qkey(PreferenceConstants.DOC_COMMENTS), prefs.get(PreferenceConstants.DOC_COMMENTS));
        prj.setPersistentProperty(qkey(PreferenceConstants.EXTRACT_UNSEQUENCED), prefs.get(PreferenceConstants.EXTRACT_UNSEQUENCED));
    }

    /**
     * Gets the output directory.
     *
     * @param javaProject the java project
     * @return the output directory
     * @throws CoreException the core exception
     */
    public static String getOutputDirectory(IJavaProject javaProject) throws CoreException {
        return javaProject.getResource().getPersistentProperty(OUTPUT_DIRECTORY_KEY);
    }

    /**
     * Gets the exclude pattern.
     *
     * @param javaProject the java project
     * @return the exclude pattern
     * @throws CoreException the core exception
     */
    public static String getExcludePattern(IJavaProject javaProject) throws CoreException {
        return javaProject.getResource().getPersistentProperty(EXCLUDE_FILES_KEY);
    }

    /**
     * Qkey.
     *
     * @param val the val
     * @return the qualified name
     */
    private static QualifiedName qkey(String val) {
        return new QualifiedName("", val);
    }

    /**
     * Gets the classpath filename.
     *
     * @param project the project
     * @return the classpath filename
     */
    private static String getClasspathFilename(IProject project) {
        return new StringBuilder(".").append(project.getName()).append("-classpath").toString();
    }

    /**
     * Gets the classpath entries.
     *
     * @param project the project
     * @return the classpath entries
     * @throws IOException Signals that an I/O exception has occurred.
     * @throws CoreException the core exception
     */
    public static Properties getClasspathEntries(IProject project) throws IOException, CoreException {
        String fileName = getClasspathFilename(project);
        IFile propertiesFile = project.getFile(fileName);

        Properties props = new Properties();

        if (propertiesFile.exists()) {
            props.load(propertiesFile.getContents());
        }

        return props;
    }

    /**
     * Persist classpath entries.
     *
     * @param prj the prj
     * @param elements the elements
     */
    public static void persistClasspathEntries(IProject prj, Object[] elements) {
        String filename = getClasspathFilename(prj);
        IFile classpathFile = prj.getFile(filename);

        if (classpathFile.exists()) {
            try {
                classpathFile.delete(true, null);
            } catch (CoreException e) {
                LogUtil.logException(e);
            }
        }

        Properties props = new Properties();
        for (Object o : elements) {
            props.put(o, "");
        }

        try {
            ByteArrayOutputStream baos = new ByteArrayOutputStream();
            props.store(baos, "");
            classpathFile.create(new ByteArrayInputStream(baos.toByteArray()), false, null);
        } catch (CoreException e) {
            LogUtil.logException(e);
        } catch (IOException e) {
            LogUtil.logException(e);
        }

    }
}
