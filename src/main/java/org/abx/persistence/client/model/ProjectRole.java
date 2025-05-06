package org.abx.persistence.client.model;

public enum ProjectRole {
    Owner, Admin, User, CI, Visitor;

    public boolean canChangeOwner() {
        return this == Owner;
    }


    public boolean canRename() {
        return canChangeOwner();
    }

    /**
     * You cannot remove owner
     *
     * @param other The use to be removed
     * @return True if the user can be removed
     */
    /*public boolean canRemove(ProjectRole other) {
        return this == Owner || (this == Admin && other != Owner);
    }*/

    public boolean canEditRepos() {
        return this == Owner || this == Admin || this == User;
    }

    public boolean canAddSim() {
        return canEditRepos();
    }

    /*public boolean canRun() {
        return this == Owner || this == Admin || this == User || this == CI;
    }*/

    public boolean canAddUser() {
        return this == Owner || this == Admin;
    }

    public boolean canSeeEnrollments() {
        return this == Owner || this == Admin|| this == User;
    }
}
