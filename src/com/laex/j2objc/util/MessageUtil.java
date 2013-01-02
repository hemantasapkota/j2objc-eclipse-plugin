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
package com.laex.j2objc.util;

import org.eclipse.swt.SWT;
import org.eclipse.swt.widgets.MessageBox;
import org.eclipse.swt.widgets.Shell;

// TODO: Auto-generated Javadoc
/**
 * The Class MessageUtil.
 */
public final class MessageUtil {

    /**
     * Message remove items.
     *
     * @param shell the shell
     * @return the int
     */
    public static int messageRemoveItems(Shell shell) {
        MessageBox mb = new MessageBox(shell, SWT.OK | SWT.CANCEL);
        mb.setText("Confirmation");
        mb.setMessage("Are you sure you want to reset/remove all the items ?");
        return mb.open();
    }

}
