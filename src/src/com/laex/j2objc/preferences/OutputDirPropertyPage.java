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

import org.eclipse.core.resources.IResource;
import org.eclipse.core.runtime.CoreException;
import org.eclipse.jdt.core.IJavaProject;
import org.eclipse.swt.SWT;
import org.eclipse.swt.events.SelectionAdapter;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Control;
import org.eclipse.swt.widgets.DirectoryDialog;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.Text;
import org.eclipse.ui.IWorkbenchPropertyPage;
import org.eclipse.ui.dialogs.PropertyPage;

import com.laex.j2objc.util.LogUtil;
import com.laex.j2objc.util.ProjectUtil;
import com.laex.j2objc.util.PropertiesUtil;

import org.eclipse.wb.swt.SWTResourceManager;
import org.eclipse.swt.widgets.Link;

/**
 * The Class OutputDirPropertyPage.
 */
public class OutputDirPropertyPage extends PropertyPage implements IWorkbenchPropertyPage {

    /** The txt output directory. */
    private Text txtOutputDirectory;
    
    /** The txt exclude. */
    private Text txtExclude;

    /**
     * Instantiates a new output dir property page.
     */
    public OutputDirPropertyPage() {
        setTitle("Output Directory");
        setMessage("Output Directory for Generated Sources");
    }

    /*
     * (non-Javadoc)
     * 
     * @see
     * org.eclipse.jface.preference.PreferencePage#createContents(org.eclipse
     * .swt.widgets.Composite)
     */
    @Override
    protected Control createContents(Composite parent) {
        Composite container = new Composite(parent, SWT.NULL);
        container.setLayout(new GridLayout(3, false));

        Label lblDirectory = new Label(container, SWT.NONE);
        lblDirectory.setLayoutData(new GridData(SWT.RIGHT, SWT.CENTER, false, false, 1, 1));
        lblDirectory.setText("Directory");

        txtOutputDirectory = new Text(container, SWT.BORDER);
        txtOutputDirectory.setLayoutData(new GridData(SWT.FILL, SWT.CENTER, true, false, 1, 1));

        Button btnBrowse = new Button(container, SWT.NONE);
        btnBrowse.addSelectionListener(new SelectionAdapter() {
            @Override
            public void widgetSelected(SelectionEvent e) {
                browseOutputDirectory();
            }
        });
        btnBrowse.setText("Browse");

        Label label = new Label(container, SWT.SEPARATOR | SWT.HORIZONTAL);
        GridData gd_label = new GridData(SWT.FILL, SWT.CENTER, false, false, 3, 1);
        gd_label.widthHint = 53;
        label.setLayoutData(gd_label);

        Link lblExclude = new Link(container, SWT.NONE);
        lblExclude.setFont(SWTResourceManager.getFont("Lucida Grande", 11, SWT.BOLD));
        lblExclude.setLayoutData(new GridData(SWT.LEFT, SWT.CENTER, false, false, 3, 1));
        lblExclude.setText("Exclude (Space seperated). Ex: jni/*.h");

        txtExclude = new Text(container, SWT.BORDER);
        GridData gd_txtExclude = new GridData(SWT.FILL, SWT.CENTER, true, false, 3, 1);
        gd_txtExclude.widthHint = 111;
        txtExclude.setLayoutData(gd_txtExclude);

        try {
            loadProperty();
        } catch (CoreException e1) {
            LogUtil.logException(e1);
        }

        return container;
    }

    /**
     * Browse output directory.
     */
    protected void browseOutputDirectory() {
        DirectoryDialog dd = new DirectoryDialog(getShell());
        String selectedDirectory = dd.open();
        if (selectedDirectory != null) {
            txtOutputDirectory.setText(selectedDirectory);
        }
    }

    /*
     * (non-Javadoc)
     * 
     * @see org.eclipse.jface.preference.PreferencePage#performDefaults()
     */
    @Override
    protected void performDefaults() {
        txtOutputDirectory.setText("");
    }

    /*
     * (non-Javadoc)
     * 
     * @see org.eclipse.jface.preference.PreferencePage#performApply()
     */
    @Override
    protected void performApply() {
        performOk();
    }

    /*
     * (non-Javadoc)
     * 
     * @see org.eclipse.jface.preference.PreferencePage#performOk()
     */
    @Override
    public boolean performOk() {
        try {
            IResource res = ProjectUtil.getJavaProject(getElement()).getResource();
            res.setPersistentProperty(PropertiesUtil.OUTPUT_DIRECTORY_KEY, txtOutputDirectory.getText().trim());
            res.setPersistentProperty(PropertiesUtil.EXCLUDE_FILES_KEY, txtExclude.getText().trim());
        } catch (CoreException e) {
            LogUtil.logException(e);
            return false;
        }

        return super.performOk();
    }

    /**
     * Load property.
     *
     * @throws CoreException the core exception
     */
    private void loadProperty() throws CoreException {
        IJavaProject javaProject = ProjectUtil.getJavaProject(getElement());
        String outputDir = PropertiesUtil.getOutputDirectory(javaProject);
        String exclude = PropertiesUtil.getExcludePattern(javaProject);

        if (outputDir != null) {
            txtOutputDirectory.setText(outputDir);
        }

        if (exclude != null) {
            txtExclude.setText(exclude);
        }
    }

}
