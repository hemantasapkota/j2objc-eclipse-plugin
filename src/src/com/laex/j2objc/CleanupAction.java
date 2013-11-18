package com.laex.j2objc;

import java.io.IOException;

import org.eclipse.core.resources.IResource;
import org.eclipse.core.runtime.CoreException;
import org.eclipse.core.runtime.NullProgressMonitor;
import org.eclipse.jdt.core.IJavaElement;
import org.eclipse.jdt.core.IJavaProject;
import org.eclipse.jface.action.IAction;
import org.eclipse.jface.viewers.ISelection;
import org.eclipse.jface.viewers.IStructuredSelection;
import org.eclipse.swt.SWT;
import org.eclipse.swt.widgets.MessageBox;
import org.eclipse.ui.IObjectActionDelegate;
import org.eclipse.ui.IWorkbenchPart;

import com.laex.j2objc.util.LogUtil;

public class CleanupAction implements IObjectActionDelegate {

    private IWorkbenchPart targetPart;
    private Object[] selected;

    public CleanupAction() {
    }

    @Override
    public void run(IAction action) {
        if (!action.isEnabled()) {
            return;
        }

        MessageBox mb = new MessageBox(targetPart.getSite().getShell(), SWT.OK | SWT.CANCEL);
        mb.setMessage("This action will clean up all the internal files generated by J2OBJC Eclipse Plugin. It will not clean up compiled source files. Are you sure you want to proceed ? ");
        int resp = mb.open();

        if (resp == SWT.CANCEL)
            return;

        for (Object o : selected) {
            if (o instanceof IJavaElement) {
                IJavaElement jel = (IJavaElement) o;

                IJavaProject javaProject = jel.getJavaProject();
                AntDelegate antDel = new AntDelegate(javaProject);

                try {
                    antDel.executeCleanup(targetPart.getSite().getShell().getDisplay());
                } catch (IOException e) {
                    LogUtil.logException(e);
                } catch (CoreException e) {
                    LogUtil.logException(e);
                }

                try {
                    javaProject.getResource().refreshLocal(IResource.DEPTH_INFINITE, new NullProgressMonitor());
                } catch (CoreException e) {
                    LogUtil.logException(e);
                }
            }

        }
    }

    @Override
    public void selectionChanged(IAction action, ISelection selection) {
        if (selection.isEmpty())
            return;

        IStructuredSelection sel = (IStructuredSelection) selection;
        selected = sel.toArray();
    }

    @Override
    public void setActivePart(IAction action, IWorkbenchPart targetPart) {
        this.targetPart = targetPart;
    }

}