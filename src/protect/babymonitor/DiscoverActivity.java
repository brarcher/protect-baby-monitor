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

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.net.nsd.NsdManager;
import android.net.nsd.NsdServiceInfo;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.TableLayout;
import android.widget.TableRow;
import android.widget.TextView;

public class DiscoverActivity extends Activity
{
    final String TAG = "BabyMonitor";

    NsdManager _nsdManager;

    NsdManager.DiscoveryListener _discoveryListener;

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        Log.i(TAG, "Baby monitor start");

        _nsdManager = (NsdManager)this.getSystemService(Context.NSD_SERVICE);

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_discover);

        startServiceDiscovery("_babymonitor._tcp.");
    }

    @Override
    protected void onDestroy()
    {
        Log.i(TAG, "Baby monitoring stop");

        if(_discoveryListener != null)
        {
            Log.i(TAG, "Unregistering monitoring service");

            _nsdManager.stopServiceDiscovery(_discoveryListener);
            _discoveryListener = null;
        }

        super.onDestroy();
    }

    public void startServiceDiscovery(final String serviceType)
    {
        final NsdManager nsdManager = (NsdManager)this.getSystemService(Context.NSD_SERVICE);

        // Instantiate a new DiscoveryListener
        _discoveryListener = new NsdManager.DiscoveryListener()
        {
            //  Called as soon as service discovery begins.
            @Override
            public void onDiscoveryStarted(String regType)
            {
                Log.d(TAG, "Service discovery started");
            }

            @Override
            public void onServiceFound(NsdServiceInfo service) {
                // A service was found!  Do something with it.
                Log.d(TAG, "Service discovery success: " + service);

                if (!service.getServiceType().equals(serviceType))
                {
                    // Service type is the string containing the protocol and
                    // transport layer for this service.
                    Log.d(TAG, "Unknown Service Type: " + service.getServiceType());
                }
                else if (service.getServiceName().contains("ProtectBabyMonitor"))
                {
                    NsdManager.ResolveListener resolver = new NsdManager.ResolveListener()
                    {
                        @Override
                        public void onResolveFailed(NsdServiceInfo serviceInfo, int errorCode)
                        {
                            // Called when the resolve fails.  Use the error code to debug.
                            Log.e(TAG, "Resolve failed" + errorCode);
                        }

                        @Override
                        public void onServiceResolved(final NsdServiceInfo serviceInfo)
                        {
                            Log.e(TAG, "Resolve Succeeded. " + serviceInfo);

                            DiscoverActivity.this.runOnUiThread(new Runnable()
                            {
                                @Override
                                public void run()
                                {
                                    final TableLayout serviceTable = (TableLayout) findViewById(R.id.ServiceTable);
                                    final TableRow row = new TableRow(DiscoverActivity.this.getApplicationContext());
                                    serviceTable.addView(row);

                                    final TextView serviceText = new TextView(DiscoverActivity.this.getApplicationContext());
                                    serviceText.setText(serviceInfo.getServiceName());
                                    row.addView(serviceText);
                                    serviceText.setTextSize(20);

                                    serviceText.setOnClickListener(new View.OnClickListener()
                                    {
                                        @Override
                                        public void onClick(View v)
                                        {
                                            Intent i = new Intent(getApplicationContext(), ListenActivity.class);
                                            Bundle b = new Bundle();
                                            b.putString("address", serviceInfo.getHost().getHostAddress());
                                            b.putInt("port", serviceInfo.getPort());
                                            b.putString("name", serviceInfo.getServiceName());
                                            i.putExtras(b);
                                            startActivity(i);
                                        }
                                    });
                                }
                            });
                        }
                    };

                    _nsdManager.resolveService(service, resolver);
                }
                else
                {
                    Log.d(TAG, "Unknown Service name: " + service.getServiceName());
                }
            }

            @Override
            public void onServiceLost(NsdServiceInfo service)
            {
                // When the network service is no longer available.
                // Internal bookkeeping code goes here.
                Log.e(TAG, "service lost" + service);
            }

            @Override
            public void onDiscoveryStopped(String serviceType)
            {
                Log.i(TAG, "Discovery stopped: " + serviceType);
            }

            @Override
            public void onStartDiscoveryFailed(String serviceType, int errorCode)
            {
                Log.e(TAG, "Discovery failed: Error code:" + errorCode);
                nsdManager.stopServiceDiscovery(this);
            }

            @Override
            public void onStopDiscoveryFailed(String serviceType, int errorCode)
            {
                Log.e(TAG, "Discovery failed: Error code:" + errorCode);
                nsdManager.stopServiceDiscovery(this);
            }
        };

        nsdManager.discoverServices(
                serviceType, NsdManager.PROTOCOL_DNS_SD, _discoveryListener);
    }
}
