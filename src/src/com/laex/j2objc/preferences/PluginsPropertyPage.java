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

import org.eclipse.swt.SWT;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Control;
import org.eclipse.swt.widgets.Group;
import org.eclipse.ui.IWorkbenchPropertyPage;
import org.eclipse.ui.dialogs.PropertyPage;

/**
 * The Class PluginsPropertyPage.
 */
public class PluginsPropertyPage extends PropertyPage implements IWorkbenchPropertyPage {

    /**
     * Instantiates a new plugins property page.
     */
    public PluginsPropertyPage() {
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

        Group grpUnused = new Group(container, SWT.NONE);
        grpUnused.setEnabled(false);
        grpUnused.setLayout(new GridLayout(1, false));
        grpUnused.setLayoutData(new GridData(SWT.FILL, SWT.CENTER, false, false, 1, 1));
        grpUnused.setText("Unused");

        Button button = new Button(grpUnused, SWT.CHECK);
        button.setText("Specify where to find plugin class files");

        Button button_1 = new Button(grpUnused, SWT.CHECK);
        button_1.setText("Comma separated key=value pairs passed to all plugins");

        return container;
    }

}
