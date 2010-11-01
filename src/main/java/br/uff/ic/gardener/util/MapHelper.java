
/*
* To change this template, choose Tools | Templates
* and open the template in the editor.
 */
package br.uff.ic.gardener.util;

//~--- JDK imports ------------------------------------------------------------

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 *
 * @author Daniel
 */
public class MapHelper {
    public static Object getKeyFromValue(Map<?, ?> map, Object value) throws Exception {
        List<Object> list = new ArrayList<Object>();

        for (Object key : map.keySet()) {
            if (map.get(key).equals(value)) {
                list.add(key);
            }
        }

        if (list.size() > 1) {
            throw new Exception("More than one key for value");
        } else if (list.isEmpty()) {
            throw new Exception("No key found for value");
        }

        return list.get(0);
    }
}


//~ Formatted by Jindent --- http://www.jindent.com
