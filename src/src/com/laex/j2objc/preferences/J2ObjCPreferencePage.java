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

import j2objc_eclipse_plugin.Activator;

import org.eclipse.jface.preference.DirectoryFieldEditor;
import org.eclipse.jface.preference.FieldEditorPreferencePage;
import org.eclipse.swt.events.ModifyEvent;
import org.eclipse.swt.events.ModifyListener;
import org.eclipse.ui.IWorkbench;
import org.eclipse.ui.IWorkbenchPreferencePage;

/**
 * The Class J2ObjCPreferencePage.
 */

public class J2ObjCPreferencePage extends FieldEditorPreferencePage implements IWorkbenchPreferencePage {

    /**
     * Instantiates a new j2 obj c preference page.
     */
    public J2ObjCPreferencePage() {
        super(GRID);
        setMessage("J2ObjC Preferences");
        setPreferenceStore(Activator.getDefault().getPreferenceStore());
        setDescription("");
    }

    /*
     * (non-Javadoc)
     * 
     * @see
     * org.eclipse.jface.preference.FieldEditorPreferencePage#createFieldEditors
     * ()
     */
    public void createFieldEditors() {
        final DirectoryFieldEditor df = new DirectoryFieldEditor(PreferenceConstants.PATH_TO_COMPILER, "&Path to Compiler", getFieldEditorParent());
        df.getTextControl(getFieldEditorParent()).addModifyListener(new ModifyListener() {
            @Override
            public void modifyText(ModifyEvent e) {
                String str = df.getStringValue().trim();
                if (str.contains(" ")) {
                  setMessage("Path to compiler invalid. Contains spaces", ERROR);
                  setValid(false);
                } else {
                    setMessage(null);
                    setValid(true);
                }
            }
        });
        addField(df);
    }

    /*
     * (non-Javadoc)
     * 
     * @see
     * org.eclipse.ui.IWorkbenchPreferencePage#init(org.eclipse.ui.IWorkbench)
     */
    public void init(IWorkbench workbench) {
    }

}