# networkDevs
Small java program for detecting devices on a local network

I wanted to be notified if a new device got onto my network, and my router didn't do that, and I didn't want to install anything complicated to monitor my network, so I wrote a little java program to just loop through my subnet and see which IPs are reachable beyond known IPs.

It takes 1 arg - the final byte of the starting IP address.  ie, if you have 5 devices that are always connected to your network (router/modem, phone, tablet, streaming device, and smart assistant), they're probably going to keep their IP addresses, so if a new device joins, it'll most likely get an address ending in 6, so you would call the code using "java networkDevs 6" and the program will try to reach all IPs between 192.168.0.6 and 192.168.0.255.

This code was written using Java SE 16.
