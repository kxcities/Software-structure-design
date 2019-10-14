package test;

class PlayThread extends Thread {
    byte tempBuffer[] = new byte[320];
 
    public void run() {
        try {
            int cnt;
            hasStop = false;
            // ��ȡ���ݵ���������
            while ((cnt = audioInputStream.read(tempBuffer, 0,
                    tempBuffer.length)) != -1) {
                if (isStop)
                    break;
                if (cnt > 0) {
                    // д�뻺������
                    sourceDataLine.write(tempBuffer, 0, cnt);
                }
            }
            // Block�ȴ���ʱ���ݱ����Ϊ��
            sourceDataLine.drain();
            sourceDataLine.close();
            hasStop = true;
        } catch (Exception e) {
            e.printStackTrace();
            System.exit(0);
        }
    }
}