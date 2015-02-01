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

import java.util.HashMap;
import java.util.Map;

import org.eclipse.core.resources.IResource;
import org.eclipse.core.runtime.CoreException;
import org.eclipse.swt.SWT;
import org.eclipse.swt.events.SelectionAdapter;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.layout.RowLayout;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Control;
import org.eclipse.swt.widgets.DirectoryDialog;
import org.eclipse.swt.widgets.FileDialog;
import org.eclipse.swt.widgets.Group;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.TabFolder;
import org.eclipse.swt.widgets.TabItem;
import org.eclipse.swt.widgets.Text;
import org.eclipse.ui.IWorkbenchPropertyPage;
import org.eclipse.ui.dialogs.PropertyPage;

import com.laex.j2objc.util.LogUtil;
import com.laex.j2objc.util.ProjectUtil;
import com.laex.j2objc.util.PropertiesUtil;

/**
 * The Class J2ObjCPropertyPage.
 */
public class J2ObjCPropertyPage extends PropertyPage implements IWorkbenchPropertyPage {

    /** The composite_2. */
    private Composite composite_2;

    /** The btn generate debug support. */
    private Button btnGenerateDebugSupport;

    /** The btn no package dir. */
    private Button btnNoPackageDir;

    /** The btn lang objective c. */
    private Button btnLangObjectiveC;

    /** The btn lang objective cpp. */
    private Button btnLangObjectiveCPP;

    /** The btn mem manual ref. */
    private Button btnMemManualRef;

    /** The btn make errors to warnings. */
    private Button btnMakeErrorsToWarnings;

    /** The btn quiet. */
    private Button btnQuiet;

    /** The btn verbose. */
    private Button btnVerbose;

    /** The btn timing info. */
    private Button btnTimingInfo;

    /** The btn mem arc. */
    private Button btnMemARC;

    /** The grp others. */
    private Group grpOthers;

    /** The txt proguard file. */
    private Text txtProguardFile;

    /** The btn browse pro guard file. */
    private Button btnBrowseProGuardFile;

    /** The txt method mapping file. */
    private Text txtMethodMappingFile;

    /** The btn browse method mapping. */
    private Button btnBrowseMethodMapping;

    /** The txt bootpath. */
    private Text txtBootpath;

    /** The btn bootpath browse. */
    private Button btnBootpathBrowse;

    /** The lbl proguard dead code. */
    private Label lblProguardDeadCode;
    
    /** The tbtm new item. */
    private TabItem tbtmNewItem;
    
    /** The tbtm proguard. */
    private TabItem tbtmProguard;
    
    /** The composite. */
    private Composite composite;
    
    /** The btn build closure. */
    private Button btnBuildClosure;
    
    /** The btn generate deprecated. */
    private Button btnGenerateDeprecated;
    
    /** The btn strip reflection. */
    private Button btnStripReflection;
    
    /** The btn gwt incompatible. */
    private Button btnGwtIncompatible;
    
    /** The label. */
    private Label label;
    
    /** The label_1. */
    private Label label_1;

    /** The btn segmented headers. */
    private Button btnSegmentedHeaders;

    /** The btn doc comments. */
    private Button btnDocComments;
    
    /** The btn extract unsequenced. */
    private Button btnExtractUnsequenced;

    /**
     * Instantiates a new j2 obj c property page.
     */
    public J2ObjCPropertyPage() {
        super();
    }

    /*
     * (non-Javadoc)
     * 
     * @see
     * org.eclipse.jface.preference.PreferencePage#createContents(org.eclipse
     * .swt.widgets.Composite)
     */
    protected Control createContents(Composite parent) {

        TabFolder tabFolder = new TabFolder(parent, SWT.NONE);
        {
            tbtmNewItem = new TabItem(tabFolder, SWT.NONE);
            tbtmNewItem.setText("Basic");
            composite_2 = new Composite(tabFolder, SWT.NONE);
            tbtmNewItem.setControl(composite_2);

            GridLayout gl_composite_2 = new GridLayout();
            composite_2.setLayout(gl_composite_2);

            {
                btnGenerateDebugSupport = new Button(composite_2, SWT.CHECK);
                btnGenerateDebugSupport.setText("Generate debugging support");
            }
            {
                btnNoPackageDir = new Button(composite_2, SWT.CHECK);
                btnNoPackageDir.setText("Generate output files to specified directory, without create package sub-directories");
            }

            {
                Group grpLanguage = new Group(composite_2, SWT.NONE);
                grpLanguage.setLayout(new RowLayout(SWT.HORIZONTAL));
                grpLanguage.setLayoutData(new GridData(SWT.FILL, SWT.CENTER, false, false, 1, 1));
                grpLanguage.setText("Language");
                {
                    btnLangObjectiveC = new Button(grpLanguage, SWT.RADIO);
                    btnLangObjectiveC.setText("Objective C");
                }
                {
                    btnLangObjectiveCPP = new Button(grpLanguage, SWT.RADIO);
                    btnLangObjectiveCPP.setText("Objective C++");
                }
            }
            {
                Group grpMemoryManagement = new Group(composite_2, SWT.NONE);
                grpMemoryManagement.setLayout(new RowLayout(SWT.VERTICAL));
                grpMemoryManagement.setLayoutData(new GridData(SWT.FILL, SWT.FILL, false, false, 1, 1));
                grpMemoryManagement.setText("Memory Management");

                btnMemManualRef = new Button(grpMemoryManagement, SWT.RADIO);
                btnMemManualRef.setText("Generate Objective-C code to support iOS manual reference counting");

                {
                    btnMemARC = new Button(grpMemoryManagement, SWT.RADIO);
                    btnMemARC.setText("Generate Objective-C code to support Automatic Reference Counting (ARC)");
                }
            }
            {
                Group grpWarningsErrorAnd = new Group(composite_2, SWT.NONE);
                grpWarningsErrorAnd.setLayout(new RowLayout(SWT.HORIZONTAL));
                grpWarningsErrorAnd.setLayoutData(new GridData(SWT.FILL, SWT.FILL, false, false, 1, 1));
                grpWarningsErrorAnd.setText("Warnings, Error and Output");
                {
                    btnMakeErrorsToWarnings = new Button(grpWarningsErrorAnd, SWT.CHECK);
                    btnMakeErrorsToWarnings.setText("Make all warnings into errors");
                }
                {
                    btnQuiet = new Button(grpWarningsErrorAnd, SWT.CHECK);
                    btnQuiet.addSelectionListener(new SelectionAdapter() {
                        @Override
                        public void widgetSelected(SelectionEvent e) {
                            btnVerbose.setSelection(false);
                        }
                    });
                    btnQuiet.setText("Do no print status mesasges");
                }
                {
                    btnVerbose = new Button(grpWarningsErrorAnd, SWT.CHECK);
                    btnVerbose.addSelectionListener(new SelectionAdapter() {
                        @Override
                        public void widgetSelected(SelectionEvent e) {
                            btnQuiet.setSelection(false);
                        }
                    });
                    btnVerbose.setText("Verbose");
                }
            }
            {
                grpOthers = new Group(composite_2, SWT.NONE);
                grpOthers.setLayout(new RowLayout(SWT.VERTICAL));
                grpOthers.setLayoutData(new GridData(SWT.FILL, SWT.CENTER, false, false, 1, 1));
                grpOthers.setText("Others");
                {
                    btnBuildClosure = new Button(grpOthers, SWT.CHECK);
                    btnBuildClosure.setText("Translate dependent classes if out-of-date ( build-closure )");
                }
                {
                    btnTimingInfo = new Button(grpOthers, SWT.CHECK);
                    btnTimingInfo.setText("Print time spent in translation steps");
                }
                {
                    btnGenerateDeprecated = new Button(grpOthers, SWT.CHECK);
                    btnGenerateDeprecated.setText("Generate deprecated attributes for deprecated methods, classes and interfaces.");
                }
                {
                    btnStripReflection = new Button(grpOthers, SWT.CHECK);
                    btnStripReflection.setText("Do not generate metadata needed for Java reflection");
                }
                {
                    btnGwtIncompatible = new Button(grpOthers, SWT.CHECK);
                    btnGwtIncompatible
                            .setText("Removes methods that are marked with a GwtIncompatible annotation, unless its value is known to be compatible");
                }
                {
                    btnSegmentedHeaders = new Button(grpOthers, SWT.CHECK);
                    btnSegmentedHeaders.setText("Generates headers with guards around each declared type. Useful for breaking import cycles.");
                }
                {
                    btnDocComments = new Button(grpOthers, SWT.CHECK);
                    btnDocComments.setText("Translate Javadoc comments into Xcode-compatible comments.");
                }
                {
                    btnExtractUnsequenced = new Button(grpOthers, SWT.CHECK);
                    btnExtractUnsequenced.setText("Rewrite expressions that would produce unsequenced modification errors.");
                }
            }
            {
                tbtmProguard = new TabItem(tabFolder, SWT.NONE);
                tbtmProguard.setText("Advanced");
                {
                    composite = new Composite(tabFolder, SWT.NONE);
                    tbtmProguard.setControl(composite);
                    composite.setLayout(new GridLayout(3, false));
                    {
                        lblProguardDeadCode = new Label(composite, SWT.NONE);
                        lblProguardDeadCode.setText("ProGuard Dead Code");
                    }
                    {
                        txtProguardFile = new Text(composite, SWT.BORDER);
                        txtProguardFile.setLayoutData(new GridData(SWT.FILL, SWT.CENTER, true, false, 1, 1));
                    }
                    {
                        btnBrowseProGuardFile = new Button(composite, SWT.NONE);
                        btnBrowseProGuardFile.addSelectionListener(new SelectionAdapter() {
                            @Override
                            public void widgetSelected(SelectionEvent e) {
                                browseProguardFile();
                            }
                        });
                        btnBrowseProGuardFile.setText("Browse");
                    }
                    {
                        label = new Label(composite, SWT.NONE);
                        label.setLayoutData(new GridData(SWT.RIGHT, SWT.CENTER, false, false, 1, 1));
                        label.setText("Bootclasspath");
                    }
                    {
                        txtMethodMappingFile = new Text(composite, SWT.BORDER);
                        txtMethodMappingFile.setLayoutData(new GridData(SWT.FILL, SWT.CENTER, true, false, 1, 1));
                    }
                    {
                        btnBrowseMethodMapping = new Button(composite, SWT.NONE);
                        btnBrowseMethodMapping.addSelectionListener(new SelectionAdapter() {
                            @Override
                            public void widgetSelected(SelectionEvent e) {
                                browseMethodMappingFile();
                            }
                        });
                        btnBrowseMethodMapping.setText("Browse");
                    }
                    {
                        label_1 = new Label(composite, SWT.NONE);
                        label_1.setLayoutData(new GridData(SWT.RIGHT, SWT.CENTER, false, false, 1, 1));
                        label_1.setText("Method Mapping File");
                    }
                    {
                        txtBootpath = new Text(composite, SWT.BORDER);
                        txtBootpath.setLayoutData(new GridData(SWT.FILL, SWT.CENTER, true, false, 1, 1));
                    }
                    {
                        btnBootpathBrowse = new Button(composite, SWT.NONE);
                        btnBootpathBrowse.addSelectionListener(new SelectionAdapter() {
                            @Override
                            public void widgetSelected(SelectionEvent e) {
                                browseBootpath();
                            }
                        });
                        btnBootpathBrowse.setText("Browse");
                    }
                }
            }

            initialize();

            return composite_2;
        }

    }

    /**
     * Browse bootpath.
     */
    protected void browseBootpath() {
        DirectoryDialog dd = new DirectoryDialog(getShell());
        String selectedDir = dd.open();
        if (selectedDir != null) {
            txtBootpath.setText(selectedDir);
        }
    }

    /**
     * Browse method mapping file.
     */
    protected void browseMethodMappingFile() {
        FileDialog fd = new FileDialog(getShell(), SWT.OPEN);
        String selectedFile = fd.open();
        if (selectedFile != null) {
            txtMethodMappingFile.setText(selectedFile);
        }
    }

    /**
     * Browse proguard file.
     */
    protected void browseProguardFile() {
        FileDialog fd = new FileDialog(getShell(), SWT.OPEN);
        String selectedFile = fd.open();
        if (selectedFile != null) {
            txtProguardFile.setText(selectedFile);
        }
    }

    /**
     * Initialize.
     */
    private void initialize() {
        try {

            if (!PropertiesUtil.isDefaultPropertiesSet(getProject())) {
                performDefaults();
            } else {
                populateProperties();
            }

        } catch (CoreException e) {
            e.printStackTrace();
        }
    }

    /**
     * Gets the project.
     *
     * @return the project
     */
    private IResource getProject() {
        return ProjectUtil.getProject(getElement());
    }

    /*
     * (non-Javadoc)
     * 
     * @see org.eclipse.jface.preference.PreferencePage#performDefaults()
     */
    protected void performDefaults() {
        Map<String, String> defaultPrefs = PropertiesUtil.constructDefaultPreferences();

        try {
            PropertiesUtil.persistProperties(getProject(), defaultPrefs);
            updateDefaultPrefsInUI(defaultPrefs);
        } catch (CoreException e) {
            LogUtil.logException(e);
        }

        super.performDefaults();
    }

    /*
     * (non-Javadoc)
     * 
     * @see org.eclipse.jface.preference.PreferencePage#performOk()
     */
    public boolean performOk() {
//        IJavaElement res = (IJavaElement) getElement();
        IResource res = ProjectUtil.getProject(getElement());
        Map<String, String> prefMap = constructPrefMap();

        try {
            PropertiesUtil.persistProperties(res, prefMap);
        } catch (CoreException e) {
            return false;
        }

        return true;
    }

    /**
     * Update default prefs in ui.
     *
     * @param defaultPrefs the default prefs
     */
    private void updateDefaultPrefsInUI(Map<String, String> defaultPrefs) {
        btnGenerateDebugSupport.setSelection(Boolean.parseBoolean(defaultPrefs.get(PreferenceConstants.GENERATE_DEBUGGING_SUPPORT)));
        btnNoPackageDir.setSelection(Boolean.parseBoolean(defaultPrefs.get(PreferenceConstants.NO_PACKAGE_DIRECTORIES)));

        btnLangObjectiveC.setSelection(Boolean.parseBoolean(defaultPrefs.get(PreferenceConstants.X_LANGUAGE_OBJECTIVE_C)));
        btnLangObjectiveCPP.setSelection(Boolean.parseBoolean(defaultPrefs.get(PreferenceConstants.X_LANGUAGE_OBJECTIVE_CPP)));

        btnMemManualRef.setSelection(Boolean.parseBoolean(defaultPrefs.get(PreferenceConstants.USE_REFERENCE_COUNTING)));
        btnMemARC.setSelection(Boolean.parseBoolean(defaultPrefs.get(PreferenceConstants.USE_ARC)));

        btnMakeErrorsToWarnings.setSelection(Boolean.parseBoolean(defaultPrefs.get(PreferenceConstants.ERROR_TO_WARNING)));
        btnQuiet.setSelection(Boolean.parseBoolean(defaultPrefs.get(PreferenceConstants.QUIET)));
        btnVerbose.setSelection(Boolean.parseBoolean(defaultPrefs.get(PreferenceConstants.VERBOSE)));

        btnTimingInfo.setSelection(Boolean.parseBoolean(defaultPrefs.get(PreferenceConstants.TIMING_INFO)));

        /* 0.8.7 changes */
        btnBuildClosure.setSelection(Boolean.parseBoolean(defaultPrefs.get(PreferenceConstants.BUILD_CLOSURE)));
        btnGenerateDeprecated.setSelection(Boolean.parseBoolean(defaultPrefs.get(PreferenceConstants.GENERATE_DEPRECATED)));
        btnStripReflection.setSelection(Boolean.parseBoolean(defaultPrefs.get(PreferenceConstants.STRIP_REFLECTION)));
        btnGwtIncompatible.setSelection(Boolean.parseBoolean(defaultPrefs.get(PreferenceConstants.STRIP_GWT_INCOMPATIBLE)));
        btnSegmentedHeaders.setSelection(Boolean.parseBoolean(defaultPrefs.get(PreferenceConstants.SEGMENTED_HEADERS)));

        /* 0.9.6 changes */
        btnDocComments.setSelection(Boolean.parseBoolean(defaultPrefs.get(PreferenceConstants.DOC_COMMENTS)));
        btnExtractUnsequenced.setSelection(Boolean.parseBoolean(defaultPrefs.get(PreferenceConstants.EXTRACT_UNSEQUENCED)));
    }

    /**
     * Construct pref map.
     *
     * @return the map
     */
    private Map<String, String> constructPrefMap() {
        Map<String, String> prefMap = new HashMap<String, String>();

        // indicate that this project has its properties set
        prefMap.put(PreferenceConstants.INITIALIZE_FIRST_TIME, Boolean.TRUE.toString());

        prefMap.put(PreferenceConstants.GENERATE_DEBUGGING_SUPPORT, Boolean.toString(btnGenerateDebugSupport.getSelection()));
        prefMap.put(PreferenceConstants.NO_PACKAGE_DIRECTORIES, Boolean.toString(btnNoPackageDir.getSelection()));

        prefMap.put(PreferenceConstants.X_LANGUAGE_OBJECTIVE_C, Boolean.toString(btnLangObjectiveC.getSelection()));
        prefMap.put(PreferenceConstants.X_LANGUAGE_OBJECTIVE_CPP, Boolean.toString(btnLangObjectiveCPP.getSelection()));

        // Memory OPtions
        prefMap.put(PreferenceConstants.USE_REFERENCE_COUNTING, Boolean.toString(btnMemManualRef.getSelection()));
        prefMap.put(PreferenceConstants.USE_ARC, Boolean.toString(btnMemARC.getSelection()));

        prefMap.put(PreferenceConstants.ERROR_TO_WARNING, Boolean.toString(btnMakeErrorsToWarnings.getSelection()));
        prefMap.put(PreferenceConstants.QUIET, Boolean.toString(btnQuiet.getSelection()));
        prefMap.put(PreferenceConstants.VERBOSE, Boolean.toString(btnVerbose.getSelection()));
        prefMap.put(PreferenceConstants.TIMING_INFO, Boolean.toString(btnTimingInfo.getSelection()));

        prefMap.put(PreferenceConstants.DEAD_CODE_REPORT, txtProguardFile.getText());
        prefMap.put(PreferenceConstants.METHOD_MAPPING_FILE, txtMethodMappingFile.getText());
        prefMap.put(PreferenceConstants.BOOTCLASSPATH, txtBootpath.getText());

        /* 0.8.7 */
        prefMap.put(PreferenceConstants.BUILD_CLOSURE, Boolean.toString(btnBuildClosure.getSelection()));
        prefMap.put(PreferenceConstants.GENERATE_DEPRECATED, Boolean.toString(btnGenerateDeprecated.getSelection()));
        prefMap.put(PreferenceConstants.STRIP_REFLECTION, Boolean.toString(btnStripReflection.getSelection()));
        prefMap.put(PreferenceConstants.STRIP_GWT_INCOMPATIBLE, Boolean.toString(btnGwtIncompatible.getSelection()));
        prefMap.put(PreferenceConstants.SEGMENTED_HEADERS, Boolean.toString(btnSegmentedHeaders.getSelection()));
        
        /* 0.9.6 */
        prefMap.put(PreferenceConstants.DOC_COMMENTS, Boolean.toString(btnDocComments.getSelection()));
        prefMap.put(PreferenceConstants.EXTRACT_UNSEQUENCED, Boolean.toString(btnExtractUnsequenced.getSelection()));

        return prefMap;
    }

    /**
     * Populate properties.
     *
     * @throws CoreException the core exception
     */
    private void populateProperties() throws CoreException {
        Map<String, String> prefs = PropertiesUtil.getProjectProperties(getProject());

        btnGenerateDebugSupport.setSelection(Boolean.parseBoolean(prefs.get(PreferenceConstants.GENERATE_DEBUGGING_SUPPORT)));
        btnNoPackageDir.setSelection(Boolean.parseBoolean(prefs.get(PreferenceConstants.NO_PACKAGE_DIRECTORIES)));

        btnLangObjectiveC.setSelection(Boolean.parseBoolean(prefs.get(PreferenceConstants.X_LANGUAGE_OBJECTIVE_C)));
        btnLangObjectiveCPP.setSelection(Boolean.parseBoolean(prefs.get(PreferenceConstants.X_LANGUAGE_OBJECTIVE_CPP)));

        btnMemManualRef.setSelection(Boolean.parseBoolean(prefs.get(PreferenceConstants.USE_REFERENCE_COUNTING)));
        btnMemARC.setSelection(Boolean.parseBoolean(prefs.get(PreferenceConstants.USE_ARC)));

        btnMakeErrorsToWarnings.setSelection(Boolean.parseBoolean(prefs.get(PreferenceConstants.ERROR_TO_WARNING)));
        btnQuiet.setSelection(Boolean.parseBoolean(prefs.get(PreferenceConstants.QUIET)));
        btnVerbose.setSelection(Boolean.parseBoolean(prefs.get(PreferenceConstants.VERBOSE)));
        btnTimingInfo.setSelection(Boolean.parseBoolean(prefs.get(PreferenceConstants.TIMING_INFO)));

        txtProguardFile.setText(prefs.get(PreferenceConstants.DEAD_CODE_REPORT));
        txtMethodMappingFile.setText(prefs.get(PreferenceConstants.METHOD_MAPPING_FILE));
        txtBootpath.setText(prefs.get(PreferenceConstants.BOOTCLASSPATH));

        btnBuildClosure.setSelection(Boolean.parseBoolean(prefs.get(PreferenceConstants.BUILD_CLOSURE)));
        btnGenerateDeprecated.setSelection(Boolean.parseBoolean(prefs.get(PreferenceConstants.GENERATE_DEPRECATED)));
        btnStripReflection.setSelection(Boolean.parseBoolean(prefs.get(PreferenceConstants.STRIP_REFLECTION)));
        btnGwtIncompatible.setSelection(Boolean.parseBoolean(prefs.get(PreferenceConstants.STRIP_GWT_INCOMPATIBLE)));
        btnSegmentedHeaders.setSelection(Boolean.parseBoolean(prefs.get(PreferenceConstants.SEGMENTED_HEADERS)));

        btnDocComments.setSelection(Boolean.parseBoolean(prefs.get(PreferenceConstants.DOC_COMMENTS)));
        btnExtractUnsequenced.setSelection(Boolean.parseBoolean(prefs.get(PreferenceConstants.EXTRACT_UNSEQUENCED)));
    }
}