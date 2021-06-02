package telegram.slainte;

public class Test {
    public static void main(String[] args) {
        String controllerDB = "com.mysql.cj.jdbc.Driver";
        String linkDB = "jdbc:mysql://monitoring:3306/telegram?useUnicode=true&characterEncoding=UTF-8";
        String userDB = "telegram";
        String password = "XQ30WWIedlHSxMHs";
//        String[]    nostring={controllerDB,linkDB,userDB,password};
        String[]    nostring={controllerDB+" "+linkDB+" "+userDB+" "+password};

        Main mb=new Main();
        try {
            mb.main(nostring);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
