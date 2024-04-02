package tests.helper;


public class RequestBodyBuilder {
    public static String createUserRequestBody(String  id, String name, String email, String gender, String status) {
        return "{"
                + "\"id\": \"" + id + "\","
                + "\"name\": \"" + name + "\","
                + "\"email\": \"" + email + "\","
                + "\"gender\": \"" + gender + "\","
                + "\"status\": \"" + status + "\""
                + "}";
    }

    public static Object[][] userData() {
        return new Object[][] {
                {"5665664","asthaa", "asthha@gmail.com", "female", "active"},
                {"845455","sonu", "sdn@gmail.com", "male", "active"},
                {"2366450","naman", "snj@gmail.com", "male", "active"},
                {"6563232","poooja", "sjn@gmail.com", "male", "active"},
                {"2323622","Visra", "sdnj@gmail.com", "male", "active"},
                {"562585","Vika", "visdnsone@gmail.com", "male", "active"},
                {"5898989","Vhra", "hdsu@gmail.com", "male", "active"},
                {"985984","jane", "jane@gmail.com", "male", "active"},

        };
    }

    public static String updateUserName(String updatedName) {
        return "{"
                + "\"name\": \"" + updatedName + "\""
                + "}";
    }



}
