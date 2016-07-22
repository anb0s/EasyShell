package de.anbos.eclipse.easyshell.plugin.preferences;

public interface IData {

    int getPosition();

    String getId();

    boolean equals(Object object);

    void setPosition(int position);

    void setId(String id);

}