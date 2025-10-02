public class Student extends Account {
    String student_number;
    String course_preference;
    String[] course_selection= {"CHM1311","PHY1124"."GNG1105.GNG1103,MAT1321,MAT1320,MAT1348,"};
    public Student(String username,String password,String phone_Number,String email, String student_number){
        super(username, password, phone_Number, email);
        this.student_number=student_number;
    }
    public static void main(String[] args){
        Student todd = new Student("todd123","todd123","123","todd123@hotmail.com","1234");
        System.out.println(todd.username);
    }


}
