package org.xry.churchmodule.utils.ThreadLocalUtils;


public class UserId {
    private static final ThreadLocal<Integer> USER_ID = new ThreadLocal<>();

    public static void setId(Integer id) {
        USER_ID.set(id);
    }

    public static Integer getId() {
        return USER_ID.get();
    }

    public static void remove() {
        USER_ID.remove();
    }

}
