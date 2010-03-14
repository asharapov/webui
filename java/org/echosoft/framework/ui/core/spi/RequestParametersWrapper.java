package org.echosoft.framework.ui.core.spi;

import java.util.Enumeration;

/**
 * Данный адаптер позволяет нам изменять параметры текущего запроса.
 * @author Anton Sharapov
 */
public interface RequestParametersWrapper {

    public Enumeration<String> getParameterNames();

    public String getParameter(String name);

    public String[] getParameterValues(String name);

    public void setParameter(String name, String value);

    public void setParameterValues(String name, String[] values);

    public void removeParameter(String name);

}
