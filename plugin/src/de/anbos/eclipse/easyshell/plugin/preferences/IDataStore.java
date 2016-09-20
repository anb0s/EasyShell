package de.anbos.eclipse.easyshell.plugin.preferences;

public interface IDataStore {

    IData getPreviousElement(IData data);

    IData getNextElement(IData data);

    IData getLastElement();

    void save();

    void loadDefaults();

    void load();

    void removeAll();

    boolean isMigrated();

    void setMigrated(boolean migrated);

    void renumber();

    void sort();

}