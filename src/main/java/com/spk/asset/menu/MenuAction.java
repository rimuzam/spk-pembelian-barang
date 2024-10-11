package com.spk.menu;

/**
 *
 * @author Ridho Multazam
 */
public class MenuAction {

    protected boolean isCancel() {
        return cancel;
    }

    public void cancel() {
        this.cancel = true;
    }

    private boolean cancel = false;
}
