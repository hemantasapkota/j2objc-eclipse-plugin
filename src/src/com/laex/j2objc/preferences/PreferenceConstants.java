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
package com.laex.j2objc.preferences;

/**
 * The Class PreferenceConstants.
 */
public class PreferenceConstants {

    /** The Constant INITIALIZE_FIRST_TIME. */
    public static final String INITIALIZE_FIRST_TIME = "propertyInitFirstTime";

    /** The Constant PATH_TO_COMPILER. */
    public static final String PATH_TO_COMPILER = "compilerPath";

    /** The Constant J2_OBJC_COMPILER. */
    public static final String J2_OBJC_COMPILER = "j2objc";

    /** The Constant OUTPUT_DIR. */
    public static final String OUTPUT_DIR = "-d";

    /** The Constant GENERATE_DEBUGGING_SUPPORT. */
    public static final String GENERATE_DEBUGGING_SUPPORT = "-g";

    /** The Constant CLASSPAPTH. */
    public static final String CLASSPAPTH = "-classpath";

    /** The Constant NO_PACKAGE_DIRECTORIES. */
    public static final String NO_PACKAGE_DIRECTORIES = "--no-package-directories";

    /** The Constant X_LANGUAGE_OBJECTIVE_C. */
    public static final String X_LANGUAGE_OBJECTIVE_C = "-x objective-c";

    /** The Constant X_LANGUAGE_OBJECTIVE_CPP. */
    public static final String X_LANGUAGE_OBJECTIVE_CPP = "-x objective-c++";

    /** The Constant USE_REFERENCE_COUNTING. */
    public static final String USE_REFERENCE_COUNTING = "-use-reference-counting";

    /** The Constant USE_ARC. */
    public static final String USE_ARC = "-use-arc";

    /** The Constant ERROR_TO_WARNING. */
    public static final String ERROR_TO_WARNING = "-Werror";

    /** The Constant QUIET. */
    public static final String QUIET = "--quiet";

    /** The Constant VERBOSE. */
    public static final String VERBOSE = "--verbose";

    /** The Constant PREFIX. */
    public static final String PREFIX = "--prefix";

    /** The Constant PREFIXES. */
    public static final String PREFIXES = "--prefixes";

    /** The Constant PLUGIN_PATH. */
    public static final String PLUGIN_PATH = "-pluginpath";

    /** The Constant PLUGIN_OPTIONS. */
    public static final String PLUGIN_OPTIONS = "-pluginoptions";

    /** The Constant METHOD_MAPPING_FILE. */
    public static final String METHOD_MAPPING_FILE = "--mapping";

    /** The Constant DEAD_CODE_REPORT. */
    public static final String DEAD_CODE_REPORT = "--dead-code-report";

    /** The Constant TIMING_INFO. */
    public static final String TIMING_INFO = "--timing-info";

    /** The Constant BOOTCLASSPATH. */
    public static final String BOOTCLASSPATH = "-Xbootclasspath";

    /* Changes from 0.8.7 */

    /** The Constant BUILD_CLOSURE. */
    public static final String BUILD_CLOSURE = "--build-closure";

    /** The Constant ENCODING. */
    public static final String ENCODING = "-encoding";

    /** The Constant GENERATE_DEPRECATED. */
    public static final String GENERATE_DEPRECATED = "--generate-deprecated";

    /** The Constant STRIP_REFLECTION. */
    public static final String STRIP_REFLECTION = "--strip-reflection";

    /** The Constant STRIP_GWT_INCOMPATIBLE. */
    public static final String STRIP_GWT_INCOMPATIBLE = "--strip-gwt-incompatible";

    /** The Constant SEGMENTED_HEADERS. */
    public static final String SEGMENTED_HEADERS = "--segmented-headers";

    /* End changes from 0.8.7 */
    
    /* Changes from 0.9.6 */
    
    /** The Constant DOC_COMMENTS. */
    public static final String DOC_COMMENTS = "--doc-comments";
    
    /** The Constant EXTRACT_UNSEQUENCED. */
    public static final String EXTRACT_UNSEQUENCED = "--extract-unsequenced";
    
    /* End changes from 0.9.6 */

    /** The Constant HELP. */
    public static final String HELP = "--help";
}
