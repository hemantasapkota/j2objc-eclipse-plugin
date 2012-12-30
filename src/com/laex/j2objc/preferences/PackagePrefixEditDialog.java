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

import org.eclipse.jface.dialogs.Dialog;
import org.eclipse.jface.dialogs.IDialogConstants;
import org.eclipse.swt.SWT;
import org.eclipse.swt.graphics.Point;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Control;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.swt.widgets.Text;

/**
 * The Class PackagePrefixEditDialog.
 */
public class PackagePrefixEditDialog extends Dialog {
    
    /** The txt package. */
    private Text txtPackage;
    
    /** The txt prefix. */
    private Text txtPrefix;
    
    /** The pkg. */
    private String pkg;
    
    /** The prefix. */
    private String prefix;

    /**
     * Instantiates a new package prefix edit dialog.
     *
     * @param parentShell the parent shell
     */
    public PackagePrefixEditDialog(Shell parentShell) {
        super(parentShell);
    }
    
    /**
     * Instantiates a new package prefix edit dialog.
     *
     * @param parentShell the parent shell
     * @param pkg the pkg
     */
    public PackagePrefixEditDialog(Shell parentShell, String pkg) {
       super(parentShell);
       
       this.pkg = pkg;
    }

    /* (non-Javadoc)
     * @see org.eclipse.jface.dialogs.Dialog#createDialogArea(org.eclipse.swt.widgets.Composite)
     */
    @Override
    protected Control createDialogArea(Composite parent) {
        Composite container = (Composite) super.createDialogArea(parent);
        GridLayout gridLayout = (GridLayout) container.getLayout();
        gridLayout.numColumns = 2;
        
        Label lblPrefix = new Label(container, SWT.NONE);
        lblPrefix.setLayoutData(new GridData(SWT.RIGHT, SWT.CENTER, false, false, 1, 1));
        lblPrefix.setText("Package");
        
        txtPackage = new Text(container, SWT.BORDER);
        txtPackage.setEditable(false);
        txtPackage.setLayoutData(new GridData(SWT.FILL, SWT.CENTER, true, false, 1, 1));
        txtPackage.setText(pkg);
        
        Label lblPrefix_1 = new Label(container, SWT.NONE);
        lblPrefix_1.setLayoutData(new GridData(SWT.RIGHT, SWT.CENTER, false, false, 1, 1));
        lblPrefix_1.setText("Prefix");
        
        txtPrefix = new Text(container, SWT.BORDER);
        txtPrefix.setLayoutData(new GridData(SWT.FILL, SWT.CENTER, true, false, 1, 1));
        txtPrefix.setFocus();

        return container;
    }

    /* (non-Javadoc)
     * @see org.eclipse.jface.dialogs.Dialog#createButtonsForButtonBar(org.eclipse.swt.widgets.Composite)
     */
    @Override
    protected void createButtonsForButtonBar(Composite parent) {
        createButton(parent, IDialogConstants.OK_ID, IDialogConstants.OK_LABEL, true);
        createButton(parent, IDialogConstants.CANCEL_ID, IDialogConstants.CANCEL_LABEL, false);
    }
    
    /* (non-Javadoc)
     * @see org.eclipse.jface.dialogs.Dialog#okPressed()
     */
    @Override
    protected void okPressed() {
        this.prefix = txtPrefix.getText().trim();
        super.okPressed();
    }
    
    /**
     * Gets the prefix.
     *
     * @return the prefix
     */
    public String getPrefix() {
        return prefix;
    }

    /* (non-Javadoc)
     * @see org.eclipse.jface.dialogs.Dialog#getInitialSize()
     */
    @Override
    protected Point getInitialSize() {
        return new Point(308, 142);
    }

}
