import java.sql.*;
import java.util.Scanner;

public class Main {

    static final String url = "jdbc:mysql://localhost:3306/hotel_db";
    static final String user = "root";
    static final String password = "VidyanshuB.tech"; // change this

    public static void main(String[] args) {

        try (Connection con = DriverManager.getConnection(url, user, password);
             Scanner sc = new Scanner(System.in)) {

            while (true) {
                System.out.println("\n--- HOTEL RESERVATION SYSTEM ---");
                System.out.println("1. Retrieve Reservations");
                System.out.println("2. Allocate Room");
                System.out.println("3. Get Room");
                System.out.println("4. Check Out");
                System.out.println("5. Update Reservation");
                System.out.println("6. Exit");
                System.out.print("Choose option: ");

                int ch = sc.nextInt();
                sc.nextLine();

                switch (ch) {
                    case 1 -> retrieve(con);
                    case 2 -> allocate(con, sc);
                    case 3 -> getRoom(con, sc);
                    case 4 -> checkOut(con, sc);
                    case 5 -> updateReservation(con, sc);
                    case 6 -> {
                        System.out.println("Thank you!");
                        return;
                    }
                    default -> System.out.println("Invalid option");
                }
            }

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    // 1. RETRIEVE
    static void retrieve(Connection con) throws SQLException {
        String sql = "SELECT * FROM reserv";
        Statement st = con.createStatement();
        ResultSet rs = st.executeQuery(sql);

        System.out.println("\nRegNo | Name | Contact | Room | GetIn | CheckOut");
        while (rs.next()) {
            System.out.println(
                    rs.getInt("reg_id") + "      | " +
                            rs.getString("name") + " | " +
                            rs.getString("contact_no") + " | " +
                            rs.getInt("room_no") + " | " +
                            rs.getTimestamp("get_in") + " | " +
                            rs.getTimestamp("check_out"));
        }
    }

    // 2. ALLOCATE
    static void allocate(Connection con, Scanner sc) throws SQLException {
        System.out.print(" Name: ");
        String name = sc.nextLine();

        System.out.print("Contact No: ");
        String contact = sc.nextLine();

        System.out.print("Room No: ");
        int room = sc.nextInt();

        String sql = "INSERT INTO reserv (name, contact_no, room_no) VALUES (?, ?, ?)";
        PreparedStatement ps = con.prepareStatement(sql);
        ps.setString(1, name);
        ps.setString(2, contact);
        ps.setInt(3, room);

        ps.executeUpdate();
        System.out.println("Room allocated successfully ✔");
    }

    // 3. GET ROOM
    static void getRoom(Connection con, Scanner sc) throws SQLException {
        System.out.print("Enter Reservation ID: ");
        int id = sc.nextInt();

        String sql = "SELECT room_no FROM reserv WHERE reg_id = ?";
        PreparedStatement ps = con.prepareStatement(sql);
        ps.setInt(1, id);

        ResultSet rs = ps.executeQuery();
        if (rs.next()) {
            System.out.println("Allocated Room: " + rs.getInt("room_no"));
        } else {
            System.out.println("Reservation not found ❌");
        }
    }

    // 4. CHECK OUT
    static void checkOut(Connection con, Scanner sc) throws SQLException {
        System.out.print("Enter Reservation ID: ");
        int id = sc.nextInt();

        String sql = "UPDATE reserv SET check_out = CURRENT_TIMESTAMP WHERE reg_id = ?";
        PreparedStatement ps = con.prepareStatement(sql);
        ps.setInt(1, id);

        int rows = ps.executeUpdate();
        if (rows > 0) {
            System.out.println("Checked out successfully ✔");
        } else {
            System.out.println("Reservation not found ❌");
        }
    }

    // 5. UPDATE RESERVATION
    static void updateReservation(Connection con, Scanner sc) throws SQLException {
        System.out.print("Enter Reservation ID to update: ");
        int id = sc.nextInt();
        sc.nextLine(); // clear buffer

        System.out.println("What do you want to update?");
        System.out.println("1. Name");
        System.out.println("2. Contact No");
        System.out.println("3. Room No");
        System.out.print("Choose option: ");
        int choice = sc.nextInt();
        sc.nextLine();

        String sql = "";
        switch (choice) {
            case 1 -> {
                System.out.print("Enter new Name: ");
                String name = sc.nextLine();
                sql = "UPDATE reserv SET name = ? WHERE reg_id = ?";
                PreparedStatement ps = con.prepareStatement(sql);
                ps.setString(1, name);
                ps.setInt(2, id);
                executeUpdate(ps);
            }
            case 2 -> {
                System.out.print("Enter new Contact No: ");
                String contact = sc.nextLine();
                sql = "UPDATE reserv SET contact_no = ? WHERE reg_id = ?";
                PreparedStatement ps = con.prepareStatement(sql);
                ps.setString(1, contact);
                ps.setInt(2, id);
                executeUpdate(ps);
            }
            case 3 -> {
                System.out.print("Enter new Room No: ");
                int room = sc.nextInt();
                sql = "UPDATE reserv SET room_no = ? WHERE reg_id = ?";
                PreparedStatement ps = con.prepareStatement(sql);
                ps.setInt(1, room);
                ps.setInt(2, id);
                executeUpdate(ps);
            }
            default -> System.out.println("Invalid update choice ❌");
        }
    }

    // helper method
    static void executeUpdate(PreparedStatement ps) throws SQLException {
        int rows = ps.executeUpdate();
        if (rows > 0) {
            System.out.println("Reservation updated successfully ✔");
        } else {
            System.out.println("Reservation ID not found ❌");
        }
    }
}