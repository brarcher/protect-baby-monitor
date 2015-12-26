/**
 * This file is part of the Protect Baby Monitor.
 *
 * Protect Baby Monitor is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * Protect Baby Monitor is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with Protect Baby Monitor. If not, see <http://www.gnu.org/licenses/>.
 */
package protect.babymonitor;

import java.util.concurrent.BlockingQueue;

import android.media.AudioTrack;
import android.util.Log;

public class AudioPlayer implements Runnable
{
    final String TAG = "BabyMonitor";

    private final AudioTrack _audioTrack;
    private final BlockingQueue<byte[]> _queue;

    public AudioPlayer(AudioTrack audioTrack, BlockingQueue<byte[]> queue)
    {
        _audioTrack = audioTrack;
        _queue = queue;
    }

    @Override
    public void run()
    {
        Log.i(TAG, "Audio player thread started");

        android.os.Process.setThreadPriority(android.os.Process.THREAD_PRIORITY_URGENT_AUDIO);

        _audioTrack.play();

        try
        {
            while(Thread.currentThread().isInterrupted() == false)
            {
                byte [] data = _queue.take();
                int written = _audioTrack.write(data, 0, data.length);

                if(written != data.length)
                {
                    Log.i(TAG, "Did not write bytes: " + (data.length - written));
                }
            }
        }
        catch (InterruptedException e)
        {

        }

        Log.i(TAG, "Audio player thread stopping");
        _audioTrack.stop();
    }
}
