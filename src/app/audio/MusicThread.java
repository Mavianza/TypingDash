package app.audio;

import javax.sound.sampled.*;
import java.io.File;


public class MusicThread extends Thread {
    private final String filePath;
    private volatile boolean running = true;
    private volatile boolean paused  = false;

    private Clip clip;
    private FloatControl gain; 

    public MusicThread(String filePath) {
        this.filePath = filePath;
        setName("MusicThread");
        setDaemon(true); // biar ikut mati saat aplikasi keluar
    }

    @Override
    public void run() {
        try {
            File file = new File(filePath);
            if (!file.exists()) {
                System.out.println("[Music] File tidak ditemukan: " + file.getAbsolutePath());
                return;
            }

            AudioInputStream in = AudioSystem.getAudioInputStream(file);
            clip = AudioSystem.getClip();
            clip.open(in);
            clip.loop(Clip.LOOP_CONTINUOUSLY);

            if (clip.isControlSupported(FloatControl.Type.MASTER_GAIN)) {
                gain = (FloatControl) clip.getControl(FloatControl.Type.MASTER_GAIN);
            }

            clip.start();

            // loop ringan untuk menjaga thread hidup dan menangani pause/resume
            while (running) {
                if (paused) {
                    if (clip.isRunning()) clip.stop();
                } else {
                    if (!clip.isRunning()) clip.start();
                }
                try { Thread.sleep(150); } catch (InterruptedException ignored) {}
            }

        } catch (Exception e) {
            System.out.println("[Music] Gagal memutar musik: " + e.getMessage());
        } finally {
            cleanup();
        }
    }

    /** Jeda musik (tidak menutup resource). */
    public void pauseMusic() { paused = true; }

    /** Lanjutkan musik setelah pause. */
    public void resumeMusic() { paused = false; }

    /** Hentikan total dan tutup resource. */
    public void stopMusic() {
        running = false;
        // biar loop segera keluar
        this.interrupt();
    }

    public void setVolume(float dB) {
        try {
            if (gain != null) {
                dB = Math.max(gain.getMinimum(), Math.min(gain.getMaximum(), dB));
                gain.setValue(dB);
            }
        } catch (Exception ignored) {}
    }

    private void cleanup() {
        try {
            if (clip != null) {
                if (clip.isRunning()) clip.stop();
                clip.close();
            }
        } catch (Exception ignored) {}
    }
}
