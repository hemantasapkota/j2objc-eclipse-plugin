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

import org.eclipse.core.resources.IProject;
import org.eclipse.core.runtime.CoreException;
import org.eclipse.core.runtime.IAdaptable;
import org.eclipse.jdt.core.IJavaProject;
import org.eclipse.jdt.core.JavaCore;

/**
 * The Class ProjectUtil.
 */
public final class ProjectUtil {

    /**
     * Gets the project.
     *
     * @param element the element
     * @return the project
     */
    public static IProject getProject(IAdaptable element) {
        return (IProject) element.getAdapter(IProject.class);
    }

    /**
     * Gets the java project.
     *
     * @param element the element
     * @return the java project
     * @throws CoreException the core exception
     */
    public static IJavaProject getJavaProject(IAdaptable element) throws CoreException {
        IProject project = ProjectUtil.getProject(element);
        if (project.hasNature(JavaCore.NATURE_ID)) {
            return JavaCore.create(project);
        }
        return null;
    }

}
