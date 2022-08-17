package com.commandus.utilipv6;

import java.net.Inet6Address;
import java.net.InetAddress;
import java.net.NetworkInterface;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class Address {
    public static String getIPv6GlobalAddress() {
        List<String> r = getIPv6GlobalAddresses();
        if (r.isEmpty()) {
            return "";
        }
        return r.get(0);
    }

    public static List<String> getIPv6GlobalAddresses() {
        List<String> r;
        r = new ArrayList<>();
        try {
            List<NetworkInterface> interfaces = Collections.list(NetworkInterface.getNetworkInterfaces());
            for (NetworkInterface netInterface : interfaces) {
                if (netInterface.isLoopback()) {
                    continue;
                }
                List<InetAddress> addressList = Collections.list(netInterface.getInetAddresses());
                for (InetAddress address : addressList) {
                    if (address.isLoopbackAddress()) {
                        continue;
                    }
                    String h = address.getHostAddress();
                    if (address instanceof Inet6Address) {
                        Inet6Address a = (Inet6Address) address;
                        if (a.isSiteLocalAddress() || a.isLinkLocalAddress()) {
                            continue;
                        }
                        int delimiter = h.indexOf('%'); // drop ip6 zone suffix
                        String hh = delimiter < 0 ? h : h.substring(0, delimiter);
                        r.add(hh);
                    }
                }
            }
        } catch (Exception e) {
        }
        return r;
    }
}
