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
import android.os.Bundle;
import android.util.Log;

public class DiscoverActivity extends Activity
{
    final String TAG = "BabyMonitor";

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        Log.i(TAG, "Baby monitor start");

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_discover);
    }

    @Override
    protected void onDestroy()
    {
        Log.i(TAG, "Baby monitoring stop");

        super.onDestroy();
    }
}
