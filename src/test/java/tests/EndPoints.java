package tests;

public class EndPoints {
    public static final String USERS = "/public/v2/users";
    public static final String DELETE_NON_EXISTENT_END_POINT = "/public/v2/users/10000000";
    public static final String CREATE_USER = "/public/v2/users";

    public static final String UPDATE_USER = "/public/v2/users/6758499";
    public static final String PATCH_USER = "/public/v2/users/6758575";
    public static final String DELETE_USER = "/public/v2/users/6759453";
    public static final String INVALID_ENDPOINT = "/public/v2/use";
}
