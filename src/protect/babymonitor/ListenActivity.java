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

import java.io.IOException;
import java.io.InputStream;
import java.net.Socket;
import java.net.UnknownHostException;
import android.app.Activity;
import android.os.Bundle;
import android.util.Log;
import android.widget.TextView;

public class ListenActivity extends Activity
{
    final String TAG = "BabyMonitor";

    String _address;
    int _port;
    String _name;

    Thread _listenThread;
    private void streamAudio(Socket socket) throws IllegalArgumentException, IllegalStateException, IOException
    {
        Log.i(TAG, "Setting up stream");

        InputStream is = socket.getInputStream();
        int read = 0;
        int bufferSize = 1024; // set this to an appropriate value.
        while(socket.isConnected() && read != -1 && Thread.currentThread().isInterrupted() == false)
        {
            byte [] buffer = new byte[bufferSize*2];
            read = is.read(buffer);
        }
        socket.close();
    }

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);

        Bundle b = getIntent().getExtras();
        _address = b.getString("address");
        _port = b.getInt("port");
        _name = b.getString("name");

        setContentView(R.layout.activity_listen);

        _listenThread = new Thread(new Runnable()
        {
            @Override
            public void run()
            {
                try
                {
                    Socket socket = new Socket(_address, _port);
                    streamAudio(socket);
                }
                catch (UnknownHostException e)
                {
                    Log.e(TAG, "Failed to stream audio", e);
                }
                catch (IOException e)
                {
                    Log.e(TAG, "Failed to stream audio", e);
                }

                ListenActivity.this.runOnUiThread(new Runnable()
                {
                    @Override
                    public void run()
                    {
                        final TextView statusText = (TextView) findViewById(R.id.textStatus);
                        statusText.setText("Listening stopped");
                    }
                });
            }
        });

        _listenThread.start();
    }

    @Override
    public void onDestroy()
    {
        _listenThread.interrupt();
        _listenThread = null;

        super.onDestroy();
    }
}
