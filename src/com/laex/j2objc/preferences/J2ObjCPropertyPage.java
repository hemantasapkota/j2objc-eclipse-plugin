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
import org.eclipse.jdt.core.IJavaElement;
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
import org.eclipse.swt.widgets.Text;
import org.eclipse.ui.IWorkbenchPropertyPage;
import org.eclipse.ui.dialogs.PropertyPage;

import com.laex.j2objc.util.LogUtil;
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
    
    /** The btn mem gc. */
    private Button btnMemGC;
    
    /** The btn make errors to warnings. */
    private Button btnMakeErrorsToWarnings;
    
    /** The btn quiet. */
    private Button btnQuiet;
    
    /** The btn verbose. */
    private Button btnVerbose;
    
    /** The btn no inline field access. */
    private Button btnNoInlineFieldAccess;
    
    /** The btn no generate test main. */
    private Button btnNoGenerateTestMain;
    
    /** The btn print converted sources. */
    private Button btnPrintConvertedSources;
    
    /** The btn timing info. */
    private Button btnTimingInfo;
    
    /** The btn mem arc. */
    private Button btnMemARC;
    
    /** The grp others. */
    private Group grpOthers;
    
    /** The btn ignore missing imports. */
    private Button btnIgnoreMissingImports;
    
    /** The grp proguard dead code. */
    private Group grpProguardDeadCode;
    
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
    
    /** The lbl method mapping file. */
    private Label lblMethodMappingFile;
    
    /** The lbl bootclasspath. */
    private Label lblBootclasspath;

    /**
     * Instantiates a new j2 obj c property page.
     */
    public J2ObjCPropertyPage() {
        super();
    }

    /* (non-Javadoc)
     * @see org.eclipse.jface.preference.PreferencePage#createContents(org.eclipse.swt.widgets.Composite)
     */
    protected Control createContents(Composite parent) {
        composite_2 = new Composite(parent, SWT.NONE);
        GridLayout gl_composite_2 = new GridLayout();
        composite_2.setLayout(gl_composite_2);
        GridData data = new GridData(GridData.FILL);
        data.grabExcessHorizontalSpace = true;
        composite_2.setLayoutData(data);

        {
            btnGenerateDebugSupport = new Button(composite_2, SWT.CHECK);
            btnGenerateDebugSupport.setText("Generate debugging support");
        }
        {
            btnNoPackageDir = new Button(composite_2, SWT.CHECK);
            btnNoPackageDir.setText("Generate output files to specified directory, without create package sub-directories");
        }
        Label separator = new Label(parent, SWT.SEPARATOR | SWT.HORIZONTAL);
        GridData gridData = new GridData();
        gridData.horizontalAlignment = GridData.FILL;
        gridData.grabExcessHorizontalSpace = true;
        separator.setLayoutData(gridData);

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

            btnMemGC = new Button(grpMemoryManagement, SWT.RADIO);
            btnMemGC.setText("Generate Objective-C code to support garbage collection (requires libjre_emul.a rebuild)");
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
                btnMakeErrorsToWarnings.setText("Make all errors into warnings");
            }
            {
                btnQuiet = new Button(grpWarningsErrorAnd, SWT.CHECK);
                btnQuiet.setText("Do no print status mesasges");
            }
            {
                btnVerbose = new Button(grpWarningsErrorAnd, SWT.CHECK);
                btnVerbose.setText("Verbose");
            }
        }
        {
            grpOthers = new Group(composite_2, SWT.NONE);
            grpOthers.setLayout(new RowLayout(SWT.VERTICAL));
            grpOthers.setLayoutData(new GridData(SWT.FILL, SWT.CENTER, false, false, 1, 1));
            grpOthers.setText("Others");
            {
                btnNoInlineFieldAccess = new Button(grpOthers, SWT.CHECK);
                btnNoInlineFieldAccess.setText("Turn off in-lining of generated field accessors.");
            }
            {
                btnNoGenerateTestMain = new Button(grpOthers, SWT.CHECK);
                btnNoGenerateTestMain.setText("Turn off automatically generated main method for JUnit tests");
            }
            {
                btnIgnoreMissingImports = new Button(grpOthers, SWT.CHECK);
                btnIgnoreMissingImports.setText("Continue translation if an imported class is not found on the class or source paths");
            }
            {
                btnPrintConvertedSources = new Button(grpOthers, SWT.CHECK);
                btnPrintConvertedSources.setText("Print input source files after initial conversion");
            }
            {
                btnTimingInfo = new Button(grpOthers, SWT.CHECK);
                btnTimingInfo.setText("Print time spent in translation steps");
            }
        }
        {
            grpProguardDeadCode = new Group(composite_2, SWT.NONE);
            grpProguardDeadCode.setLayout(new GridLayout(3, false));
            grpProguardDeadCode.setLayoutData(new GridData(SWT.FILL, SWT.FILL, false, false, 1, 1));
            {
                lblProguardDeadCode = new Label(grpProguardDeadCode, SWT.NONE);
                lblProguardDeadCode.setLayoutData(new GridData(SWT.RIGHT, SWT.CENTER, false, false, 1, 1));
                lblProguardDeadCode.setText("ProGuard Dead Code");
            }
            {
                txtProguardFile = new Text(grpProguardDeadCode, SWT.BORDER);
                txtProguardFile.setLayoutData(new GridData(SWT.FILL, SWT.CENTER, true, false, 1, 1));
            }
            {
                btnBrowseProGuardFile = new Button(grpProguardDeadCode, SWT.NONE);
                btnBrowseProGuardFile.addSelectionListener(new SelectionAdapter() {
                    @Override
                    public void widgetSelected(SelectionEvent e) {
                        browseProguardFile();
                    }
                });
                btnBrowseProGuardFile.setText("Browse");
            }
            {
                lblMethodMappingFile = new Label(grpProguardDeadCode, SWT.NONE);
                lblMethodMappingFile.setLayoutData(new GridData(SWT.RIGHT, SWT.CENTER, false, false, 1, 1));
                lblMethodMappingFile.setText("Method Mapping File");
            }
            {
                txtMethodMappingFile = new Text(grpProguardDeadCode, SWT.BORDER);
                txtMethodMappingFile.setLayoutData(new GridData(SWT.FILL, SWT.CENTER, false, false, 1, 1));
            }
            {
                btnBrowseMethodMapping = new Button(grpProguardDeadCode, SWT.NONE);
                btnBrowseMethodMapping.addSelectionListener(new SelectionAdapter() {
                    @Override
                    public void widgetSelected(SelectionEvent e) {
                        browseMethodMappingFile();
                    }
                });
                btnBrowseMethodMapping.setText("Browse");
            }
            {
                lblBootclasspath = new Label(grpProguardDeadCode, SWT.NONE);
                lblBootclasspath.setLayoutData(new GridData(SWT.RIGHT, SWT.CENTER, false, false, 1, 1));
                lblBootclasspath.setText("Bootclasspath");
            }
            {
                txtBootpath = new Text(grpProguardDeadCode, SWT.BORDER);
                txtBootpath.setLayoutData(new GridData(SWT.FILL, SWT.CENTER, false, false, 1, 1));
            }
            {
                btnBootpathBrowse = new Button(grpProguardDeadCode, SWT.NONE);
                btnBootpathBrowse.addSelectionListener(new SelectionAdapter() {
                    @Override
                    public void widgetSelected(SelectionEvent e) {
                        browseBootpath();
                    }
                });
                btnBootpathBrowse.setText("Browse");
            }
        }

        // Load/Init
        initialize();

        return composite_2;
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
        return ((IJavaElement) getElement()).getResource();
    }

    /* (non-Javadoc)
     * @see org.eclipse.jface.preference.PreferencePage#performDefaults()
     */
    protected void performDefaults() {
        Map<String, String> defaultPrefs = constructDefaultPrefsMap();

        try {
            PropertiesUtil.persistProperties(getProject(), defaultPrefs);
            updateDefaultPrefsInUI(defaultPrefs);
        } catch (CoreException e) {
            LogUtil.logException(e);
        }

        super.performDefaults();
    }

    /* (non-Javadoc)
     * @see org.eclipse.jface.preference.PreferencePage#performOk()
     */
    public boolean performOk() {
        IJavaElement res = (IJavaElement) getElement();
        Map<String, String> prefMap = constructPrefMap();

        try {
            PropertiesUtil.persistProperties(res.getResource(), prefMap);
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
        btnMemGC.setSelection(Boolean.parseBoolean(defaultPrefs.get(PreferenceConstants.USE_GC)));

        btnMakeErrorsToWarnings.setSelection(Boolean.parseBoolean(defaultPrefs.get(PreferenceConstants.ERROR_TO_WARNING)));
        btnQuiet.setSelection(Boolean.parseBoolean(defaultPrefs.get(PreferenceConstants.QUIET)));
        btnVerbose.setSelection(Boolean.parseBoolean(defaultPrefs.get(PreferenceConstants.VERBOSE)));

        btnNoInlineFieldAccess.setSelection(Boolean.parseBoolean(defaultPrefs.get(PreferenceConstants.NO_INLINE_FIELD_ACCESS)));
        btnNoGenerateTestMain.setSelection(Boolean.parseBoolean(defaultPrefs.get(PreferenceConstants.NO_GENERATE_TEST_MAIN)));
        btnIgnoreMissingImports.setSelection(Boolean.parseBoolean(defaultPrefs.get(PreferenceConstants.IGNORE_MISSING_IMPORTS)));
        btnPrintConvertedSources.setSelection(Boolean.parseBoolean(defaultPrefs.get(PreferenceConstants.PRINT_CONVERTED_SOURCES)));
        btnTimingInfo.setSelection(Boolean.parseBoolean(defaultPrefs.get(PreferenceConstants.TIMING_INFO)));

    }

    /**
     * Construct default prefs map.
     *
     * @return the map
     */
    private Map<String, String> constructDefaultPrefsMap() {
        Map<String, String> prefMap = new HashMap<String, String>();

        prefMap.put(PreferenceConstants.INITIALIZE_FIRST_TIME, Boolean.TRUE.toString());

        prefMap.put(PreferenceConstants.GENERATE_DEBUGGING_SUPPORT, Boolean.TRUE.toString());
        prefMap.put(PreferenceConstants.NO_PACKAGE_DIRECTORIES, Boolean.TRUE.toString());

        prefMap.put(PreferenceConstants.X_LANGUAGE_OBJECTIVE_C, Boolean.TRUE.toString());
        prefMap.put(PreferenceConstants.X_LANGUAGE_OBJECTIVE_CPP, Boolean.FALSE.toString());

        // Memory OPtions
        prefMap.put(PreferenceConstants.USE_REFERENCE_COUNTING, Boolean.FALSE.toString());
        prefMap.put(PreferenceConstants.USE_ARC, Boolean.TRUE.toString());
        prefMap.put(PreferenceConstants.USE_GC, Boolean.FALSE.toString());

        prefMap.put(PreferenceConstants.ERROR_TO_WARNING, Boolean.FALSE.toString());
        prefMap.put(PreferenceConstants.QUIET, Boolean.FALSE.toString());
        prefMap.put(PreferenceConstants.VERBOSE, Boolean.TRUE.toString());

        prefMap.put(PreferenceConstants.NO_INLINE_FIELD_ACCESS, Boolean.FALSE.toString());
        prefMap.put(PreferenceConstants.NO_GENERATE_TEST_MAIN, Boolean.FALSE.toString());
        prefMap.put(PreferenceConstants.IGNORE_MISSING_IMPORTS, Boolean.TRUE.toString());
        prefMap.put(PreferenceConstants.PRINT_CONVERTED_SOURCES, Boolean.FALSE.toString());
        prefMap.put(PreferenceConstants.TIMING_INFO, Boolean.FALSE.toString());

        return prefMap;

    }

    /**
     * Construct pref map.
     *
     * @return the map
     */
    private Map<String, String> constructPrefMap() {
        Map<String, String> prefMap = new HashMap<String, String>();

        prefMap.put(PreferenceConstants.GENERATE_DEBUGGING_SUPPORT, Boolean.toString(btnGenerateDebugSupport.getSelection()));
        prefMap.put(PreferenceConstants.NO_PACKAGE_DIRECTORIES, Boolean.toString(btnNoPackageDir.getSelection()));

        prefMap.put(PreferenceConstants.X_LANGUAGE_OBJECTIVE_C, Boolean.toString(btnLangObjectiveC.getSelection()));
        prefMap.put(PreferenceConstants.X_LANGUAGE_OBJECTIVE_CPP, Boolean.toString(btnLangObjectiveCPP.getSelection()));

        // Memory OPtions
        prefMap.put(PreferenceConstants.USE_REFERENCE_COUNTING, Boolean.toString(btnMemManualRef.getSelection()));
        prefMap.put(PreferenceConstants.USE_ARC, Boolean.toString(btnMemARC.getSelection()));
        prefMap.put(PreferenceConstants.USE_GC, Boolean.toString(btnMemGC.getSelection()));

        prefMap.put(PreferenceConstants.ERROR_TO_WARNING, Boolean.toString(btnMakeErrorsToWarnings.getSelection()));
        prefMap.put(PreferenceConstants.QUIET, Boolean.toString(btnQuiet.getSelection()));
        prefMap.put(PreferenceConstants.VERBOSE, Boolean.toString(btnVerbose.getSelection()));

        prefMap.put(PreferenceConstants.NO_INLINE_FIELD_ACCESS, Boolean.toString(btnNoInlineFieldAccess.getSelection()));
        prefMap.put(PreferenceConstants.NO_GENERATE_TEST_MAIN, Boolean.toString(btnNoGenerateTestMain.getSelection()));
        prefMap.put(PreferenceConstants.IGNORE_MISSING_IMPORTS, Boolean.toString(btnIgnoreMissingImports.getSelection()));
        prefMap.put(PreferenceConstants.PRINT_CONVERTED_SOURCES, Boolean.toString(btnPrintConvertedSources.getSelection()));
        prefMap.put(PreferenceConstants.TIMING_INFO, Boolean.toString(btnTimingInfo.getSelection()));

        prefMap.put(PreferenceConstants.DEAD_CODE_REPORT, txtProguardFile.getText());
        prefMap.put(PreferenceConstants.METHOD_MAPPING_FILE, txtMethodMappingFile.getText());
        prefMap.put(PreferenceConstants.BOOTCLASSPATH, txtBootpath.getText());

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
        btnMemGC.setSelection(Boolean.parseBoolean(prefs.get(PreferenceConstants.USE_GC)));

        btnMakeErrorsToWarnings.setSelection(Boolean.parseBoolean(prefs.get(PreferenceConstants.ERROR_TO_WARNING)));
        btnQuiet.setSelection(Boolean.parseBoolean(prefs.get(PreferenceConstants.QUIET)));
        btnVerbose.setSelection(Boolean.parseBoolean(prefs.get(PreferenceConstants.VERBOSE)));

        btnNoInlineFieldAccess.setSelection(Boolean.parseBoolean(prefs.get(PreferenceConstants.NO_INLINE_FIELD_ACCESS)));
        btnNoGenerateTestMain.setSelection(Boolean.parseBoolean(prefs.get(PreferenceConstants.NO_GENERATE_TEST_MAIN)));
        btnIgnoreMissingImports.setSelection(Boolean.parseBoolean(prefs.get(PreferenceConstants.IGNORE_MISSING_IMPORTS)));
        btnPrintConvertedSources.setSelection(Boolean.parseBoolean(prefs.get(PreferenceConstants.PRINT_CONVERTED_SOURCES)));
        btnTimingInfo.setSelection(Boolean.parseBoolean(prefs.get(PreferenceConstants.TIMING_INFO)));

        txtProguardFile.setText(prefs.get(PreferenceConstants.DEAD_CODE_REPORT));
        txtMethodMappingFile.setText(prefs.get(PreferenceConstants.METHOD_MAPPING_FILE));
        txtBootpath.setText(prefs.get(PreferenceConstants.BOOTCLASSPATH));
    }
}