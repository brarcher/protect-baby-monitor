# protect-baby-monitor
An Open Source Baby Monitor for Android

[![Build Status](https://travis-ci.org/brarcher/protect-baby-monitor.svg?branch=master)](https://travis-ci.org/brarcher/protect-baby-monitor)

_Protect Baby Monitor_ allows two Android devices to act as a baby monitor. The first device,
left in the room with the baby, will advertise itself on the network and stream audio
to a connected client. The second device, with the parent, will connect to the monitoring
device and receive an audio stream.

Currently _Protect Baby Monitor_ builds against Android SDK 17. It may work for
lower SDK versions, but this has not been tested.

The current version of _Protect Baby Monitor_ is rudimentary at best. It is capable
of successfully advertising itself on the network, allows clients to connect,
and streams audio. Room for improvement includes:

1. Robust usage of the AudioTrack API
2. Handle dropped packets gracefully

At the time this project was started there was no obvious open source solution for a
baby monitor for Android. There are both free and paid options available for Android,
including:

- [Baby Monitor Wifi](https://play.google.com/store/apps/details?id=com.bluechillie.babyphone)
- [Dormi](https://play.google.com/store/apps/details?id=com.sleekbit.dormi)
- [Baby Monitor](https://play.google.com/store/apps/details?id=dk.mvainformatics.android.babymonitor)

If there is any interest in improving this open source baby monitor, kindly submit a pull request with
proposed changed.


# Thanks

App icon originals from [WPZOOM](http://www.wpzoom.com/wpzoom/new-freebie-wpzoom-developer-icon-set-154-free-icons)
and formatted using [Android Asset Studio](https://romannurik.github.io/AndroidAssetStudio/index.html).

Audio file originals from [freesound](https://freesound.org).
