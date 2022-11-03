package com.epam.mjc.nio;

import java.io.File;
import java.io.IOException;
import java.io.RandomAccessFile;
import java.nio.ByteBuffer;
import java.nio.channels.FileChannel;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;


public class FileReader {

    public Profile getDataFromFile(File file) {
        StringBuilder data = new StringBuilder();
        try (RandomAccessFile accessFile = new RandomAccessFile(file,"r");
             FileChannel fileChannel = accessFile.getChannel()) {
            ByteBuffer buffer = ByteBuffer.allocate((int) fileChannel.size());
            fileChannel.read(buffer);
            buffer.flip();
            while (buffer.hasRemaining()) {
                data.append((char) buffer.get());
            }
            buffer.clear();

        } catch (IOException e) {
            throw new RuntimeException(e.getMessage());
        }
        return parseInfo(data.toString());
    }

    private Profile parseInfo (String info){
        List<String> lines =info.lines().collect(Collectors.toList());
        Map<String, String> pairs = new HashMap<>();
        String key;
        String val;
        for(int i=0; i<lines.size(); i++){
            String temp = lines.get(i);
            key = temp.substring(0,temp.indexOf(":"));
            val = temp.substring(temp.indexOf(" ")+1);
            pairs.put(key,val);
        }
        return new Profile(pairs.get("Name"), Integer.parseInt(pairs.get("Age")),pairs.get("Email"),Long.parseLong(pairs.get("Phone")));
    }
}
