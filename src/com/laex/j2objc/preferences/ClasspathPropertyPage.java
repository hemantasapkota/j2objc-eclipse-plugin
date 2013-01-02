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
import java.util.Arrays;
import java.util.HashSet;
import java.util.Properties;
import java.util.Set;

import org.eclipse.core.resources.IFile;
import org.eclipse.core.resources.IFolder;
import org.eclipse.core.resources.IProject;
import org.eclipse.core.resources.ResourcesPlugin;
import org.eclipse.core.runtime.CoreException;
import org.eclipse.core.runtime.IPath;
import org.eclipse.core.runtime.Path;
import org.eclipse.jdt.core.IClasspathEntry;
import org.eclipse.jdt.core.IJavaElement;
import org.eclipse.jdt.core.IJavaProject;
import org.eclipse.jdt.core.JavaModelException;
import org.eclipse.jface.viewers.CheckboxTableViewer;
import org.eclipse.jface.viewers.IStructuredContentProvider;
import org.eclipse.jface.viewers.IStructuredSelection;
import org.eclipse.jface.viewers.ITableLabelProvider;
import org.eclipse.jface.viewers.LabelProvider;
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
import org.eclipse.swt.widgets.Table;
import org.eclipse.swt.widgets.TableColumn;
import org.eclipse.ui.ISharedImages;
import org.eclipse.ui.IWorkbenchPropertyPage;
import org.eclipse.ui.dialogs.ContainerSelectionDialog;
import org.eclipse.ui.dialogs.PropertyPage;
import org.eclipse.ui.internal.SharedImages;

import com.laex.j2objc.util.LogUtil;
import com.laex.j2objc.util.MessageUtil;
import com.laex.j2objc.util.PropertiesUtil;

/**
 * The Class ClasspathPropertyPage.
 */
public class ClasspathPropertyPage extends PropertyPage implements IWorkbenchPropertyPage {

    /** The classpath ref. */
    private Set<String> classpathRef = new HashSet<String>();

    /** The btn remove. */
    private Button btnRemove;

    /** The table. */
    private Table table;

    /** The checkbox table viewer. */
    private CheckboxTableViewer checkboxTableViewer;

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
            String elm = (String) element;
            if (elm.endsWith("jar") || elm.endsWith("zip")) {
                return new org.eclipse.jdt.internal.ui.SharedImages().getImage(org.eclipse.jdt.ui.ISharedImages.IMG_OBJS_JAR);
            }

            return new SharedImages().getImage(ISharedImages.IMG_OBJ_FOLDER);
        }

        /*
         * (non-Javadoc)
         * 
         * @see
         * org.eclipse.jface.viewers.ITableLabelProvider#getColumnText(java.
         * lang.Object, int)
         */
        public String getColumnText(Object element, int columnIndex) {
            return (String) element;

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
            Object[] o = ((Set<String>) inputElement).toArray();
            Arrays.sort(o);
            return o;
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

    /**
     * Instantiates a new classpath property page.
     */
    public ClasspathPropertyPage() {
        setDescription("The selected classpath entires are used at the time of compiling.");
        setMessage("J2OBJC Preferences");
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

        checkboxTableViewer = CheckboxTableViewer.newCheckList(container, SWT.BORDER | SWT.FULL_SELECTION);
        table = checkboxTableViewer.getTable();
        table.setHeaderVisible(true);
        GridData gd_table = new GridData(SWT.FILL, SWT.FILL, true, false, 1, 1);
        gd_table.heightHint = 400;
        table.setLayoutData(gd_table);

        TableViewerColumn tableViewerColumn = new TableViewerColumn(checkboxTableViewer, SWT.NONE);
        TableColumn tblclmnClasspath = tableViewerColumn.getColumn();
        tblclmnClasspath.setWidth(295);
        tblclmnClasspath.setText("Classpath");

        Composite composite = new Composite(container, SWT.NONE);
        composite.setLayoutData(new GridData(SWT.LEFT, SWT.TOP, false, false, 1, 1));
        composite.setLayout(new RowLayout(SWT.VERTICAL));

        Button btnAddClasspath = new Button(composite, SWT.NONE);
        btnAddClasspath.setLayoutData(new RowData(88, SWT.DEFAULT));
        btnAddClasspath.addSelectionListener(new SelectionAdapter() {
            @Override
            public void widgetSelected(SelectionEvent e) {
                addClasspath();
            }
        });
        btnAddClasspath.setText("Add");

        btnRemove = new Button(composite, SWT.NONE);
        btnRemove.addSelectionListener(new SelectionAdapter() {
            @Override
            public void widgetSelected(SelectionEvent e) {
                removeSelection();
            }
        });
        btnRemove.setLayoutData(new RowData(88, SWT.DEFAULT));
        btnRemove.setText("Remove");
        btnRemove.setEnabled(false);

        Button btnRemoveAll = new Button(composite, SWT.NONE);
        btnRemoveAll.addSelectionListener(new SelectionAdapter() {
            @Override
            public void widgetSelected(SelectionEvent e) {
                removeAll();
            }
        });
        btnRemoveAll.setText("Remove All");

        checkboxTableViewer.setContentProvider(new ContentProvider());
        checkboxTableViewer.setLabelProvider(new TableLabelProvider());

        // load
        try {
            loadProjectReferencedClasspaths();
            loadUserSelectedClasspaths();
        } catch (CoreException e1) {
            LogUtil.logException(e1);
        } catch (IOException e1) {
            LogUtil.logException(e1);
        }

        return container;
    }

    /**
     * Load project referenced classpaths.
     */
    private void loadProjectReferencedClasspaths() {
        IJavaElement elm = (IJavaElement) getElement();
        try {
            IClasspathEntry[] refClasspath = elm.getJavaProject().getResolvedClasspath(true);
            for (IClasspathEntry o : refClasspath) {
                IClasspathEntry entry = (IClasspathEntry) o;
                String path = entry.getPath().makeAbsolute().toOSString();

                // some path may be folders. In that case, we have to make sure
                // we store the full absolute path to the folders
                boolean isArchive = path.endsWith("jar") || path.endsWith("zip");
                if (!isArchive) {
                    IPath ipath = new Path(path);
                    // We can only get a folder if the segment count is greater
                    // 2 i.e. if the path has at least two segments
                    if (ipath.segmentCount() >= 2) {
                        IFolder folder = ResourcesPlugin.getWorkspace().getRoot().getFolder(ipath);
                        if (folder.exists()) {
                            classpathRef.add(folder.getLocation().makeAbsolute().toOSString());
                        }
                    }

                    // If the segment count is 1, then it is probably a project
                    if (ipath.segmentCount() == 1) {
                        IProject refPrj = ResourcesPlugin.getWorkspace().getRoot().getProject(path);
                        //if this is a project, then we assume it has a SRC folder; and therefore append SRC at the end
                        //this is required because j2objc compiler needs the path till the src folder to compile properly

                        classpathRef.add(refPrj.getLocation().append("src").makeAbsolute().toOSString());
                    }

                } else {
                    //add whatever archive path we get from the results
                    classpathRef.add(path);
                }

            }

            checkboxTableViewer.setInput(classpathRef);
            checkboxTableViewer.refresh();
        } catch (JavaModelException e) {
            LogUtil.logException(e);
        }
    }

    /**
     * Removes the selection.
     */
    private void removeSelection() {
        IStructuredSelection iss = (IStructuredSelection) checkboxTableViewer.getSelection();
        String res = (String) iss.getFirstElement();
        classpathRef.remove(res);
        checkboxTableViewer.refresh();
    }

    /**
     * Removes the all.
     */
    protected void removeAll() {
        int resp = MessageUtil.messageRemoveItems(getShell());
        if (resp == SWT.OK) {
            classpathRef.clear();
            checkboxTableViewer.refresh();
        }
    }

    /**
     * Adds the classpath.
     */
    protected void addClasspath() {
        ContainerSelectionDialog csd = new ContainerSelectionDialog(getShell(), ResourcesPlugin.getWorkspace().getRoot(), false,
                "Select a src container as classpath");
        int response = csd.open();
        if (response == ContainerSelectionDialog.CANCEL) {
            return;
        }

        for (Object o : csd.getResult()) {
            IPath path = (IPath) o;
            String s = ResourcesPlugin.getWorkspace().getRoot().getFolder(path).getLocation().makeAbsolute().toOSString();
            classpathRef.add(s);
        }

        checkboxTableViewer.refresh();
    }

    /*
     * (non-Javadoc)
     * 
     * @see org.eclipse.jface.preference.PreferencePage#performOk()
     */
    @Override
    public boolean performOk() {
        IJavaElement javaPrj = (IJavaElement) getElement();

        PropertiesUtil.persistClasspathEntries(javaPrj.getJavaProject().getProject(), checkboxTableViewer.getCheckedElements());

        return super.performOk();
    }

    /**
     * Load user selected classpaths.
     *
     * @throws CoreException the core exception
     * @throws IOException Signals that an I/O exception has occurred.
     */
    private void loadUserSelectedClasspaths() throws CoreException, IOException {
        IJavaElement javaPrj = (IJavaElement) getElement();
        Properties props = PropertiesUtil.getClasspathEntries(javaPrj.getJavaProject().getProject());

        for (Object s : props.keySet()) {
            if (classpathRef.contains(s)) {
                checkboxTableViewer.setChecked(s, true);
            }
        }

    }
}
