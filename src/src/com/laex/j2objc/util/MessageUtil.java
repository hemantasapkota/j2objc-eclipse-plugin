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
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.MessageBox;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.ui.console.ConsolePlugin;
import org.eclipse.ui.console.IConsole;
import org.eclipse.ui.console.IConsoleManager;
import org.eclipse.ui.console.MessageConsole;
import org.eclipse.ui.console.MessageConsoleStream;

/**
 * The Class MessageUtil.
 */
public final class MessageUtil {

    /** The Constant J2OBJC_CONSOLE. */
    public static final String J2OBJC_CONSOLE = "J2OBJC Console";
    
    /** The Constant NEW_LINE_CONSTANT. */
    public static final String NEW_LINE_CONSTANT ="\r\n";

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

    public static int messageSetAllPrefixBlank(Shell shell) {
        MessageBox mb = new MessageBox(shell, SWT.OK | SWT.CANCEL);
        mb.setText("Confirmation");
        mb.setMessage("This action will set all the package prefixes to blank. Do you want to continue ?");
        return mb.open();
    }
    
    /**
     * Sets the console color.
     *
     * @param display the display
     * @param mst the mst
     * @param color the color
     */
    public static void setConsoleColor(final Display display, final MessageConsoleStream mst, final int color) {
        display.asyncExec(new Runnable() {
            @Override
            public void run() {
                mst.setColor(display.getSystemColor(color));
            }
        });
    }
    
    /**
     * Reset console color.
     *
     * @param display the display
     * @param mst the mst
     */
    public static void resetConsoleColor(final Display display, final MessageConsoleStream mst) {
        display.asyncExec(new Runnable() {
            @Override
            public void run() {
                mst.setColor(null);
            }
        });
    }

    /**
     * Find console.
     *
     * @param name the name
     * @return the message console
     */
    public static MessageConsole findConsole(String name) {
        ConsolePlugin plugin = ConsolePlugin.getDefault();
        IConsoleManager conMan = plugin.getConsoleManager();
        IConsole[] existing = conMan.getConsoles();
        for (int i = 0; i < existing.length; i++)
            if (name.equals(existing[i].getName()))
                return (MessageConsole) existing[i];
        // no console found, so create a new one
        MessageConsole myConsole = new MessageConsole(name, null);
        conMan.addConsoles(new IConsole[] { myConsole });
        return myConsole;
    }

}
