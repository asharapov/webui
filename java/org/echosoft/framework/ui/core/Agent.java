package org.echosoft.framework.ui.core;

/**
 * Содержит описание браузера посредством которого пользователи работают с системой.
 * @author Anton Sharapov
 */
public class Agent implements Comparable {

    /**
     * Содержит перечисление поддерживемых библиотекой платформ.
     */
    public static enum Type {
        /**
         * Семейство браузеров основанных на движке Gecko (Netscape 6.0+, Mozilla, Firefox).
         */
        GECKO,

        /**
         * Семейство браузеров основанных на движке Microsoft Internet Explorer.
         */
        IEXPLORER,

        /**
         * Браузер Operaю
         */
        OPERA,

        /**
         * Браузер Chrome
         */
        CHROME,

        /**
         * Браузер Konqueror
         */
        KONQUEROR,

        /**
         * Браузер Links
         */
        LINKS,

        /**
         * Браузер Lynx
         */
        LYNX
    }


    /**
     * Поддерживаемые виды операционных систем.
     */
    public static enum OS {
        WINDOWS,
        LINUX,
        MACOS,
        SOLARIS
    }


    private final Agent.Type type;
    private final Agent.OS os;
    private final int major;
    private final int minor;
    private final String agent;

    public Agent(Agent.Type type, Agent.OS os, int major, int minor, String agent) {
        this.type= type;
        this.os = os;
        this.major = major;
        this.minor = minor;
        this.agent = agent;
    }

    /**
     * Возвращает информацию о движке, лежащем в основе данного браузера.
     * @return информация о движке лежащем в основе данного браузера.
     */
    public Agent.Type getType() {
        return type;
    }

    /**
     * Возвращает информацию об операционной системе пользователя.
     * @return информация об операционной системе пользователя.
     */
    public Agent.OS getOperationSystem() {
        return os;
    }

    /**
     * Returns the major version number of the agent,
     * or 0 if a version number couldn't be identified.
     * @return major version number of the agent.
     */
    public int getMajorVersion() {
        return major;
    }

    /**
     * Returns the minor version number of the agent,
     * or 0 if a version number couldn't be identified.
     * @return minor version number of the agent.
     */
    public int getMinorVersion() {
        return minor;
    }

    /**
     * Retrieves original string with the user agent description, that usually obtains from 'User-Agent' header of the client's request.
     * @return  an original user agent description.
     */
    public String getAgent() {
        return agent;
    }

    @Override
    public int compareTo(final Object obj) {
        final Agent other = (Agent)obj;
        if (type==null) {
            return other.type!=null ? -1 : 0;
        } else
        if (other.type==null) {
            return 1;
        }
        int result = type.compareTo(other.type);
        if (result!=0)
            return result;

        if (major!=other.major) {
            return major>other.major ? 1 : -1;
        } else
        if (minor!=other.minor) {
            return minor>other.minor ? 1 : -1;
        }

        if (os==null) {
            return other.os!=null ? -1 : 0;
        } else
        if (other.os==null) {
            return 1;
        } else
        result = os.compareTo(other.os);
        if (result!=0)
            return result;
        return agent.compareTo(other.agent);
    }

    @Override
    public int hashCode() {
        return (type!=null ? type.hashCode() : 0);
    }

    @Override
    public boolean equals(final Object obj) {
        if (obj==null || !getClass().equals(obj.getClass()))
            return false;
        final Agent other = (Agent)obj;
        return (type!=null ? type.equals(other.type) : other.type==null) &&
               (os!=null ? os.equals(other.os) : other.os==null) &&
                major==other.major && minor==other.minor &&
               (agent!=null ? agent.equals(other.agent) : other.agent==null);
    }

    @Override
    public String toString() {
        return "[Agent{"+type+" rv:"+major+"."+minor+"  "+os+"}]";
    }
}
