package br.uff.ic.gardener.patch.delta;

//~--- JDK imports ------------------------------------------------------------

import java.util.Date;

/**
 *
 * @author Daniel
 */
public class Info {
    private String path = "";
    private Date   date;

    /**
     * @return the path
     */
    public String getPath() {
        return path;
    }

    /**
     * @param path the path to set
     */
    public void setPath(String path) {
        this.path = path;
    }

    /**
     * @return the date
     */
    public Date getDate() {
        return date;
    }

    /**
     * @param date the date to set
     */
    public void setDate(Date date) {
        this.date = date;
    }
}


//~ Formatted by Jindent --- http://www.jindent.com
