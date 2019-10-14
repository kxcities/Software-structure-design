package test;

class PlayThread extends Thread {
    byte tempBuffer[] = new byte[320];
 
    public void run() {
        try {
            int cnt;
            hasStop = false;
            // 读取数据到缓存数据
            while ((cnt = audioInputStream.read(tempBuffer, 0,
                    tempBuffer.length)) != -1) {
                if (isStop)
                    break;
                if (cnt > 0) {
                    // 写入缓存数据
                    sourceDataLine.write(tempBuffer, 0, cnt);
                }
            }
            // Block等待临时数据被输出为空
            sourceDataLine.drain();
            sourceDataLine.close();
            hasStop = true;
        } catch (Exception e) {
            e.printStackTrace();
            System.exit(0);
        }
    }
}