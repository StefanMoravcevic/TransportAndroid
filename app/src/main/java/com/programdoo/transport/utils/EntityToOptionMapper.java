package com.programdoo.transport.utils;

/**
 *
 * @param <T> tip entity-ja koji se mapira
 *           <p>
 *              definise kako se entity mapira u select option. <br>
 *              mora da definise kako se dobija id i kako se dobija description. <br>
 *              searchData se automatski vraca kao description. ako treba da se filtrira po
 *              "skrivenim" podacima, override-uje se.
 *           </p>
 */
public abstract class EntityToOptionMapper<T> {
    abstract public int getId(T item);
    abstract public String getDescription(T item);

    /**
     *
     * @param item tip entity-ja koji se mapira
     * @return string po kom se pretrazuje
     * <p>
     *     neophodno je da se vraca lowercase bez razmaka!
     * </p>
     */
    public String getSearchData(T item) {
        return getDescription(item).toLowerCase().replace(" ", "");
    }
}
