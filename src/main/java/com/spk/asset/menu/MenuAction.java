package com.spk.asset.menu;

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
