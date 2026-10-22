package com.ums.schedule.email.adaptor.out.smtp.model;


import com.ums.schedule.common.code.email.SmtpCommandType;
import com.ums.schedule.email.adaptor.out.smtp.SendReader;
import com.ums.schedule.email.adaptor.out.smtp.SendWriter;
import org.springframework.util.StringUtils;

import java.io.*;
import java.nio.charset.StandardCharsets;

public record SmtpSessionInfo(
        SendReader reader,
        SendWriter os
) implements AutoCloseable {
    public static SmtpSessionInfo of(SendReader reader, SendWriter os) throws IOException {
        return new SmtpSessionInfo(reader, os);
    }

    public void close() {
//        try {
//            if(socket != null) socket.close();
            if(reader != null) reader.close();
            if(os != null) os.close();
//        } catch (IOException e) {
//            throw new RuntimeException(e);
//        }
    }

    public void queryHelo(String ehlo) {
        sendCommand(SmtpCommandType.EHLO.toCommand(ehlo));

//        while(true) {
//            String readLine = readLine();
//            if(!StringUtils.hasText(readLine.trim())) {
//                break;
//            }
//            System.out.println("helo = " + readLine);
//            expect(220);
//        }
    }

    public boolean isConnect() {
        String readLine = readLine();
        if(readLine.startsWith("220")) {
            return true;
        }
        return false;
    }

    public void sendCommand(SmtpCommandType command, String ehlo) {
        sendCommand(command.value()+ehlo);
    }

    public void sendCommand(String command)  {
//       try {
           this.os.write(command.getBytes(StandardCharsets.UTF_8));
           this.os.write("\r\n".getBytes(StandardCharsets.UTF_8));
           this.os.flush();
//       } catch (IOException e) {

//       }
    }
    public void sendCommand(SmtpCommandType command) throws IOException {
        sendCommand(command.description());
    }

    public void sendRset() throws IOException {
        sendCommand("RSET");
    }

    public String readLine()  {
//        try {
            String readLine = reader.readLine();
            System.out.println(readLine);
            return readLine;
//        } catch (IOException e) {
//            throw new IllegalArgumentException(e);
//        }
    }

    public void expect(int code) {
        String readLine = readLine();
//        if(!readLine.startsWith(String.valueOf(code))) {
//            throw new IllegalArgumentException("Send Error : " + readLine);
//        }
    }
}
