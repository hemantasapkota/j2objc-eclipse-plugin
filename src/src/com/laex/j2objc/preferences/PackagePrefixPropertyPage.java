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

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.HashSet;
import java.util.Properties;
import java.util.Set;

import org.eclipse.core.resources.IFile;
import org.eclipse.core.resources.IResource;
import org.eclipse.core.resources.IResourceVisitor;
import org.eclipse.core.runtime.CoreException;
import org.eclipse.jdt.core.ICompilationUnit;
import org.eclipse.jdt.core.IJavaProject;
import org.eclipse.jdt.core.JavaCore;
import org.eclipse.jdt.internal.ui.SharedImages;
import org.eclipse.jdt.ui.ISharedImages;
import org.eclipse.jface.viewers.ISelectionChangedListener;
import org.eclipse.jface.viewers.IStructuredContentProvider;
import org.eclipse.jface.viewers.IStructuredSelection;
import org.eclipse.jface.viewers.ITableLabelProvider;
import org.eclipse.jface.viewers.LabelProvider;
import org.eclipse.jface.viewers.SelectionChangedEvent;
import org.eclipse.jface.viewers.TableViewer;
import org.eclipse.jface.viewers.TableViewerColumn;
import org.eclipse.jface.viewers.Viewer;
import org.eclipse.swt.SWT;
import org.eclipse.swt.events.SelectionAdapter;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.graphics.Image;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.layout.RowData;
import org.eclipse.swt.layout.RowLayout;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Control;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.Table;
import org.eclipse.swt.widgets.TableColumn;
import org.eclipse.ui.dialogs.PropertyPage;

import com.laex.j2objc.util.LogUtil;
import com.laex.j2objc.util.MessageUtil;
import com.laex.j2objc.util.ProjectUtil;
import com.laex.j2objc.util.PropertiesUtil;

/**
 * The Class PackagePrefixPropertyPage.
 */
public class PackagePrefixPropertyPage extends PropertyPage {

    /** The Constant PACKAGE_IMG. */
    public static final Image PACKAGE_IMG = new SharedImages().getImage(ISharedImages.IMG_OBJS_PACKAGE);

    /**
     * The Class TableLabelProvider.
     */
    private class TableLabelProvider extends LabelProvider implements ITableLabelProvider {

        /*
         * (non-Javadoc)
         * 
         * @see
         * org.eclipse.jface.viewers.ITableLabelProvider#getColumnImage(java
         * .lang.Object, int)
         */
        public Image getColumnImage(Object element, int columnIndex) {
            if (columnIndex == 0)
                return PACKAGE_IMG;

            return null;
        }

        /*
         * (non-Javadoc)
         * 
         * @see
         * org.eclipse.jface.viewers.ITableLabelProvider#getColumnText(java.
         * lang.Object, int)
         */
        public String getColumnText(Object element, int columnIndex) {
            if (columnIndex == 0)
                return (String) element;

            if (columnIndex == 1)
                return (String) pkgPrefix.get((String) element);

            return null;
        }
    }

    /**
     * The Class ContentProvider.
     */
    private class ContentProvider implements IStructuredContentProvider {

        /*
         * (non-Javadoc)
         * 
         * @see
         * org.eclipse.jface.viewers.IStructuredContentProvider#getElements(
         * java.lang.Object)
         */
        public Object[] getElements(Object inputElement) {
            return ((Set<String>) inputElement).toArray();
        }

        /*
         * (non-Javadoc)
         * 
         * @see org.eclipse.jface.viewers.IContentProvider#dispose()
         */
        public void dispose() {
        }

        /*
         * (non-Javadoc)
         * 
         * @see
         * org.eclipse.jface.viewers.IContentProvider#inputChanged(org.eclipse
         * .jface.viewers.Viewer, java.lang.Object, java.lang.Object)
         */
        public void inputChanged(Viewer viewer, Object oldInput, Object newInput) {
        }
    }

    /** The table. */
    private Table table;

    /** The table viewer. */
    private TableViewer tableViewer;

    /** The pkg list. */
    private Set<String> pkgList;

    /** The pkg prefix. */
    private Properties pkgPrefix = new Properties();

    /** The btn edit. */
    private Button btnEdit;

    /** The btn reset. */
    private Button btnReset;

    /**
     * Instantiates a new package prefix property page.
     */
    public PackagePrefixPropertyPage() {
    }

    /*
     * (non-Javadoc)
     * 
     * @see
     * org.eclipse.jface.preference.PreferencePage#createContents(org.eclipse
     * .swt.widgets.Composite)
     */
    @Override
    public Control createContents(Composite parent) {
        Composite container = new Composite(parent, SWT.NULL);
        container.setLayout(new GridLayout(2, false));

        tableViewer = new TableViewer(container, SWT.BORDER | SWT.FULL_SELECTION);
        tableViewer.addSelectionChangedListener(new ISelectionChangedListener() {
            @Override
            public void selectionChanged(SelectionChangedEvent event) {
                onSelectionChanged(event);
            }
        });
        table = tableViewer.getTable();
        table.setLinesVisible(true);
        table.setHeaderVisible(true);
        table.setLayoutData(new GridData(SWT.FILL, SWT.FILL, true, true, 1, 1));

        TableViewerColumn tableViewerColumn = new TableViewerColumn(tableViewer, SWT.NONE);
        TableColumn tblclmnPackage = tableViewerColumn.getColumn();
        tblclmnPackage.setMoveable(true);
        tblclmnPackage.setWidth(100);
        tblclmnPackage.setText("Package");

        TableViewerColumn tableViewerColumn_1 = new TableViewerColumn(tableViewer, SWT.NONE);
        TableColumn tblclmnPrefix = tableViewerColumn_1.getColumn();
        tblclmnPrefix.setWidth(170);
        tblclmnPrefix.setText("Prefix");
        tableViewer.setLabelProvider(new TableLabelProvider());
        tableViewer.setContentProvider(new ContentProvider());

        Composite composite = new Composite(container, SWT.NONE);
        composite.setLayout(new RowLayout(SWT.VERTICAL));
        composite.setLayoutData(new GridData(SWT.FILL, SWT.FILL, false, false, 1, 1));

        btnEdit = new Button(composite, SWT.NONE);
        btnEdit.setLayoutData(new RowData(76, SWT.DEFAULT));
        btnEdit.addSelectionListener(new SelectionAdapter() {
            @Override
            public void widgetSelected(SelectionEvent e) {
                editPrefix();
            }
        });
        btnEdit.setText("Edit");
        btnEdit.setEnabled(false);

        btnReset = new Button(composite, SWT.NONE);
        btnReset.addSelectionListener(new SelectionAdapter() {
            @Override
            public void widgetSelected(SelectionEvent e) {
                resetSelection();
            }
        });
        btnReset.setLayoutData(new RowData(76, SWT.DEFAULT));
        btnReset.setText("Reset");
        btnReset.setEnabled(false);

        Button btnResetAll = new Button(composite, SWT.NONE);
        btnResetAll.addSelectionListener(new SelectionAdapter() {
            @Override
            public void widgetSelected(SelectionEvent e) {
                resetAll();
            }
        });
        btnResetAll.setText("Reset All");

        Label label = new Label(composite, SWT.SEPARATOR | SWT.HORIZONTAL);
        label.setLayoutData(new RowData(79, 2));

        Button btnSetAllBlank = new Button(composite, SWT.NONE);
        btnSetAllBlank.addSelectionListener(new SelectionAdapter() {
            @Override
            public void widgetSelected(SelectionEvent e) {
                blankAll();
            }
        });
        btnSetAllBlank.setText("Set Blank");

        try {
            loadProperties();
            loadPackages();
            tableViewer.setInput(pkgList);
        } catch (CoreException e) {
            e.printStackTrace();
        }

        return container;
    }

    /**
     * On selection changed.
     *
     * @param event the event
     */
    protected void onSelectionChanged(SelectionChangedEvent event) {
        if (event.getSelection().isEmpty()) {
            btnEdit.setEnabled(false);
            btnReset.setEnabled(false);
        } else {
            btnEdit.setEnabled(true);
            btnReset.setEnabled(true);
        }
    }

    /**
     * Reset all.
     */
    protected void resetAll() {
        int resp = MessageUtil.messageRemoveItems(getShell());

        if (resp == SWT.OK) {
            for (String s : pkgList) {
                pkgPrefix.remove(s);
            }
            tableViewer.refresh();
        }
    }

    /**
     * Blank all.
     */
    protected void blankAll() {
        int resp = MessageUtil.messageSetAllPrefixBlank(getShell());

        if (resp == SWT.OK) {
            for (String key : pkgList) {
                pkgPrefix.put(key, " ");
            }
            tableViewer.refresh();
        }
    }

    /**
     * Gets the selected package.
     *
     * @return the selected package
     */
    private String getSelectedPackage() {
        IStructuredSelection selection = (IStructuredSelection) tableViewer.getSelection();
        return (String) selection.getFirstElement();
    }

    /**
     * Reset selection.
     */
    protected void resetSelection() {
        String selectedPackage = getSelectedPackage();
        pkgPrefix.remove(selectedPackage);
        tableViewer.refresh();
    }

    /**
     * Edits the prefix.
     */
    protected void editPrefix() {
        String selectedPkg = getSelectedPackage();
        String p = pkgPrefix.getProperty(selectedPkg);

        if (p == null)
            p = "";

        PackagePrefixEditDialog pped = new PackagePrefixEditDialog(getShell(), selectedPkg, p);

        if (pped.open() == PackagePrefixEditDialog.OK) {
            String prefix = pped.getPrefix();
            pkgPrefix.put(selectedPkg, prefix);
            tableViewer.refresh();
        }
    }

    /**
     * Load packages.
     *
     * @throws CoreException the core exception
     */
    private void loadPackages() throws CoreException {
        pkgList = new HashSet<String>();
        ProjectUtil.getJavaProject(getElement()).getResource().accept(new IResourceVisitor() {
            @Override
            public boolean visit(IResource resource) throws CoreException {
                if (JavaCore.isJavaLikeFileName(resource.getName())) {
                    ICompilationUnit compU = JavaCore.createCompilationUnitFrom((IFile) resource);

                    // Wrap with exception, in case the project does not have
                    // any pacakgaes declared
                    try {
                        pkgList.add(compU.getPackageDeclarations()[0].getElementName());
                    } catch (ArrayIndexOutOfBoundsException aiobx) {
                        LogUtil.logException(aiobx);
                    }

                }
                return true;
            }
        });
    }

    /**
     * Load properties.
     *
     * @throws CoreException the core exception
     */
    private void loadProperties() throws CoreException {
        IJavaProject javaProject = ProjectUtil.getJavaProject(getElement());

        String propertiesFilePath = PropertiesUtil.constructPrefixPropertiesFilePath(javaProject.getProject());
        IFile propertiesFile = javaProject.getProject().getFile(propertiesFilePath);

        if (propertiesFile.exists()) {

            try {
                pkgPrefix.load(propertiesFile.getContents());
            } catch (IOException e) {
                e.printStackTrace();
            } catch (CoreException e) {
                e.printStackTrace();
            }
        }

    }

    /*
     * (non-Javadoc)
     * 
     * @see org.eclipse.jface.preference.PreferencePage#performOk()
     */
    @Override
    public boolean performOk() {
        IJavaProject javaProject = (IJavaProject) getElement();

        String propertiesFilePath = getPropertiesFileName(javaProject);
        IFile propertiesFile = javaProject.getProject().getFile(propertiesFilePath);

        if (propertiesFile.exists()) {
            try {
                propertiesFile.delete(true, null);
            } catch (CoreException e) {
                LogUtil.logException(e);
            }
        }

        ByteArrayOutputStream baos = new ByteArrayOutputStream();

        try {
            pkgPrefix.store(baos, "");
            propertiesFile.create(new ByteArrayInputStream(baos.toByteArray()), false, null);
        } catch (IOException e) {
            LogUtil.logException(e);
        } catch (CoreException e) {
            LogUtil.logException(e);
        }

        return super.performOk();
    }

    /**
     * Gets the properties file name.
     *
     * @param javaProject the java project
     * @return the properties file name
     */
    private String getPropertiesFileName(IJavaProject javaProject) {
        return new StringBuilder(".").append(javaProject.getElementName()).append("-prefixes").toString();
    }

}
