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

import j2objc_eclipse_plugin.Activator;

import org.eclipse.core.runtime.Status;

/**
 * The Class LogUtil.
 */
public final class LogUtil {

    /**
     * Log exception.
     *
     * @param e the e
     */
    public static void logException(Exception e) {
        Activator.getDefault().getLog().log(new Status(Status.ERROR, Activator.PLUGIN_ID, e.getMessage()));
    }

}
