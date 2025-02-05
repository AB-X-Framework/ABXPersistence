package org.abx.persistence.client.model;

public enum ProjectRole {
    Owner, Admin, User, CI, Visitor;

    public boolean canChangeOwner() {
        return this == Owner;
    }

    public boolean canDelete() {
        return canChangeOwner();
    }

    public boolean canRename() {
        return canChangeOwner();
    }

    /**
     * You cannot remove owner
     *
     * @param other
     * @return
     */
    public boolean canRemove(ProjectRole other) {
        return this == Owner || (this == Admin && other != Owner);
    }

    public boolean canAddRepo() {
        return this == Owner || this == Admin || this == User;
    }

    public boolean canAddSim() {
        return canAddRepo();
    }

    public boolean canRun() {
        return this == Owner || this == Admin || this == User || this == CI;
    }
}
