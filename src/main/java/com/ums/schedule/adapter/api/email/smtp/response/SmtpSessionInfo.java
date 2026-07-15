package com.ums.schedule.adapter.api.email.smtp.response;


import com.ums.schedule.adapter.api.email.dns.DnsQuery;
import com.ums.schedule.common.code.email.SmtpCommandType;
import org.springframework.util.StringUtils;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.Socket;
import java.nio.charset.StandardCharsets;
import java.util.List;
import java.util.Optional;

public record SmtpSessionInfo(
        Socket socket,
        BufferedReader reader,
        OutputStream os
) {
    public static SmtpSessionInfo of(List<DnsQuery> queryList, String ehlo) throws IOException {
        SmtpSessionInfo session = null;
        for (DnsQuery query : queryList) {
            String[] ips = query.bindIp();
            for (String ip : ips) {
                Socket socket = new Socket(ip, 25);
                BufferedReader reader = new BufferedReader(new InputStreamReader(socket.getInputStream()));
                OutputStream os = socket.getOutputStream();

                session = new SmtpSessionInfo(socket, reader, os);
                session.queryHelo(ehlo);
                break;
            }
            if(session != null) break;
        }
        return Optional.ofNullable(session)
                .orElseThrow(() -> new RuntimeException("[600] DNS Query Error."));
    }

    public void close() {
        try {
            if(socket != null) socket.close();
            if(reader != null) reader.close();
            if(os != null) os.close();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    public void queryHelo(String ehlo) throws IOException {
        sendCommand(SmtpCommandType.EHLO.toCommand(ehlo));

        while(true) {
            String readLine = readLine();
            if(!StringUtils.hasText(readLine.trim())) {
                break;
            }
            if(!"250".startsWith(readLine)) {
                throw new RuntimeException();
            }
        }
    }

    public boolean isConnect() {
        try {
            String readLine = readLine();
            if(readLine.startsWith("220")) {
                return true;
            }
            return false;
        } catch (IOException e) {
            e.printStackTrace();
            return false;
        }
    }

    public void sendCommand(SmtpCommandType command, String ehlo) throws IOException {
        sendCommand(command.value()+ehlo);
    }

    public void sendCommand(String command) throws IOException {
        this.os.write(command.getBytes(StandardCharsets.UTF_8));
        this.os.write("\r\n".getBytes(StandardCharsets.UTF_8));
        this.os.flush();
    }

    public void sendCommand(SmtpCommandType command) throws IOException {
        sendCommand(command.description());
    }

    public void sendRset() throws IOException {
        sendCommand("RSET");
        String line = readLine();
        if(!"250".equals(line)) {
            throw new RuntimeException(line);
        }
    }

    public String readLine() throws IOException {
        return reader.readLine();
    }

    public void writeMailFrom(String command) throws IOException {
        sendCommand(command);
    }

    public void writeReceiver(String command) throws IOException {
        sendCommand(command);
    }
}
