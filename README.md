# networkDevs
Small java program. written using Java SE 16, for detecting devices on a local network

I wanted to be notified if a new device got onto my network, and my router didn't do that, and I didn't want to install anything complicated to monitor my network, so I wrote a little java program to just loop through my subnet and see which IPs are reachable beyond known IPs.

It takes 2 args - the final byte of the starting IP address (1-255) and a comma-separated list of email addresses to notify.  ie, if you have 5 devices that are always connected to your network (router/modem, phone, tablet, streaming device, and smart assistant), they're probably going to keep their IP addresses, so if a new device joins, it'll most likely get an address ending in 6, so you would call the code using "java networkDevs 6 email@address.com" and the program will try to reach all IPs between 192.168.0.6 and 192.168.0.255.  If you don't want to be notified via email, you still have to include the email address (for now), and it has to be valid according to RFC822 syntax rules (so you can't enter, for example, invalid@email@com), but any simple name (with no "@domain" part) will be accepted - an error will be displayed when the program tries to send an email to that adress, but the program will continue running without filling up your inbox.


