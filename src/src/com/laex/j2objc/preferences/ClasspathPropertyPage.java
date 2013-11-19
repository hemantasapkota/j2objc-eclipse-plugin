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

import java.io.IOException;
import java.util.Arrays;
import java.util.HashSet;
import java.util.Properties;
import java.util.Set;

import org.eclipse.core.resources.IFolder;
import org.eclipse.core.resources.IProject;
import org.eclipse.core.resources.IResource;
import org.eclipse.core.resources.ResourcesPlugin;
import org.eclipse.core.runtime.CoreException;
import org.eclipse.core.runtime.IPath;
import org.eclipse.core.runtime.Path;
import org.eclipse.jdt.core.IClasspathEntry;
import org.eclipse.jdt.core.IJavaElement;
import org.eclipse.jdt.core.JavaCore;
import org.eclipse.jdt.core.JavaModelException;
import org.eclipse.jface.viewers.CheckboxTableViewer;
import org.eclipse.jface.viewers.IStructuredContentProvider;
import org.eclipse.jface.viewers.ITableLabelProvider;
import org.eclipse.jface.viewers.LabelProvider;
import org.eclipse.jface.viewers.TableViewerColumn;
import org.eclipse.jface.viewers.Viewer;
import org.eclipse.swt.SWT;
import org.eclipse.swt.events.SelectionAdapter;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.graphics.Image;
import org.eclipse.swt.layout.FillLayout;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Control;
import org.eclipse.swt.widgets.Table;
import org.eclipse.swt.widgets.TableColumn;
import org.eclipse.ui.ISharedImages;
import org.eclipse.ui.IWorkbenchPropertyPage;
import org.eclipse.ui.dialogs.PropertyPage;
import org.eclipse.ui.internal.SharedImages;

import com.laex.j2objc.util.LogUtil;
import com.laex.j2objc.util.PropertiesUtil;

/**
 * The Class ClasspathPropertyPage.
 */
public class ClasspathPropertyPage extends PropertyPage implements IWorkbenchPropertyPage {

    /** The classpath ref. */
    private Set<String> classpathRef = new HashSet<String>();

    /** The table. */
    private Table table;

    /** The checkbox table viewer. */
    private CheckboxTableViewer checkboxTableViewer;

    private Button btnUseAllClasspathLibraries;

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
            if (columnIndex == 0) {
                String elm = (String) element;
                if (elm.endsWith("jar") || elm.endsWith("zip")) {
                    return new org.eclipse.jdt.internal.ui.SharedImages().getImage(org.eclipse.jdt.ui.ISharedImages.IMG_OBJS_JAR);
                } else
                    return new SharedImages().getImage(ISharedImages.IMG_OBJ_FOLDER);
            }

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
            if (columnIndex == 0) {
                Path p = new Path((String) element);
                return p.lastSegment();
            }

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
        setMessage("Classpath Preferences");
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
        container.setLayout(new GridLayout(1, false));

        btnUseAllClasspathLibraries = new Button(container, SWT.CHECK);
        btnUseAllClasspathLibraries.addSelectionListener(new SelectionAdapter() {
            @Override
            public void widgetSelected(SelectionEvent e) {
                useAllClasspathLibs();
            }
        });
        btnUseAllClasspathLibraries.setText("Use all classpath libraries");

        Composite composite = new Composite(container, SWT.NONE);
        composite.setLayout(new FillLayout(SWT.HORIZONTAL));
        GridData gd_composite = new GridData(SWT.FILL, SWT.FILL, true, true, 1, 1);
        gd_composite.widthHint = 358;
        gd_composite.heightHint = 308;
        composite.setLayoutData(gd_composite);

        checkboxTableViewer = CheckboxTableViewer.newCheckList(composite, SWT.BORDER | SWT.FULL_SELECTION);
        table = checkboxTableViewer.getTable();
        table.setHeaderVisible(true);

        TableViewerColumn tableViewerColumn = new TableViewerColumn(checkboxTableViewer, SWT.NONE);
        TableColumn tblclmnLibraryName = tableViewerColumn.getColumn();
        tblclmnLibraryName.setWidth(116);
        tblclmnLibraryName.setText("Library");

        TableColumn tblclmnClasspath = new TableColumn(table, SWT.NONE);
        tblclmnClasspath.setWidth(400);
        tblclmnClasspath.setText("Classpath");

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

                IClasspathEntry entry = JavaCore.getResolvedClasspathEntry((IClasspathEntry) o);
                String path = null;

                // We need to figure out the path for different types of
                // classpath entries.
                // the types of entries are: CPE_LIBRARY, CPE_PROJECT,
                // CPE_SOURCE, CPE_VARIABLE, CPE_CONTAINER
                switch (entry.getEntryKind()) {
                case IClasspathEntry.CPE_LIBRARY:

                    // With CPE Library: some jar files could reside in system,
                    // like JDK jar files
                    // some jar files may reside in workspace, within other
                    // project.
                    // Check if the library resides in workspace project
                    IResource file = ResourcesPlugin.getWorkspace().getRoot().findMember(entry.getPath());
                    // null means, this library does not reside in workspace but
                    // instead in the system
                    if (file == null) {
                        // get the apropriate path
                        path = entry.getPath().makeAbsolute().toOSString();
                    } else {
                        // if resdies in workspace, get the appropriate path
                        if (file.exists()) {
                            path = file.getLocation().makeAbsolute().toOSString();
                        }
                    }

                    break;

                case IClasspathEntry.CPE_PROJECT:
                case IClasspathEntry.CPE_SOURCE:
                    // project and source
                    IResource res = ResourcesPlugin.getWorkspace().getRoot().findMember(entry.getPath());
                    if (res.exists()) {
                        path = res.getFullPath().makeAbsolute().toOSString();
                    }
                    break;
                }

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
                        // if this is a project, then we assume it has a SRC
                        // folder; and therefore append SRC at the end
                        // this is required because j2objc compiler needs the
                        // path till the src folder to compile properly

                        classpathRef.add(refPrj.getLocation().append("src").makeAbsolute().toOSString());
                    }

                } else {
                    // add whatever archive path we get from the results
                    classpathRef.add(path);
                }

            }

            checkboxTableViewer.setInput(classpathRef);
            checkboxTableViewer.refresh();

        } catch (JavaModelException e) {
            LogUtil.logException(e);
        }
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
     * @throws CoreException
     *             the core exception
     * @throws IOException
     *             Signals that an I/O exception has occurred.
     */
    private void loadUserSelectedClasspaths() throws CoreException, IOException {
        IJavaElement javaPrj = (IJavaElement) getElement();
        Properties props = PropertiesUtil.getClasspathEntries(javaPrj.getJavaProject().getProject());

        for (Object s : props.keySet()) {
            if (classpathRef.contains(s)) {
                checkboxTableViewer.setChecked(s, true);
            }
        }

        btnUseAllClasspathLibraries.setSelection(checkboxTableViewer.getCheckedElements().length == classpathRef.size());
    }

    private void useAllClasspathLibs() {
        checkboxTableViewer.setAllChecked(btnUseAllClasspathLibraries.getSelection());
        checkboxTableViewer.refresh();
    }
}
